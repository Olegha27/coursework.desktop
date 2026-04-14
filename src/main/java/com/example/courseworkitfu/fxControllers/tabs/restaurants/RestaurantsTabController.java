package com.example.courseworkitfu.fxControllers.tabs.restaurants;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.*;
import com.example.courseworkitfu.session.Session;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RestaurantsTabController implements Initializable {

    public ListView<RestaurantCardRow> restaurantList;
    public TilePane dishTilePane;
    public ListView<CartItemRow> cartList;
    public Label selectedRestaurantLabel;
    public Label cartTotalLabel;
    public TextArea specialInstructionsArea;

    private CustomOperations customOperations;
    private Restaurant selectedRestaurant;
    private final Map<Integer, CartItemRow> cart = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);
        initInteractions();
        loadRestaurantsView(null);
    }

    private void initInteractions() {
        if (restaurantList != null) {
            restaurantList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) {
                    selectedRestaurant = null;

                    if (selectedRestaurantLabel != null) {
                        selectedRestaurantLabel.setText("Select restaurant");
                    }

                    if (dishTilePane != null) {
                        dishTilePane.getChildren().clear();
                    }
                    return;
                }

                selectedRestaurant = null;

                for (Restaurant restaurant : customOperations.getAllRecords(Restaurant.class)) {
                    if (restaurant.getId() == newVal.getId()) {
                        selectedRestaurant = restaurant;
                        break;
                    }
                }

                if (selectedRestaurantLabel != null) {
                    selectedRestaurantLabel.setText("Menu | " + newVal.getTitle());
                }

                loadRestaurantDishes();
            });
        }
    }

    public void loadRestaurantsView(Event event) {
        if (restaurantList == null) {
            return;
        }

        restaurantList.getItems().clear();

        for (Restaurant restaurant : customOperations.getAllRecords(Restaurant.class)) {
            restaurantList.getItems().add(new RestaurantCardRow(
                    restaurant.getId(),
                    restaurant.getUsername(),
                    restaurant.getDescription() == null ? "" : restaurant.getDescription(),
                    restaurant.getAddress() == null ? "" : restaurant.getAddress(),
                    restaurant.getCuisineType() == null ? "" : restaurant.getCuisineType(),
                    String.valueOf(restaurant.getRating()),
                    ""
            ));
        }

        refreshCart();
    }

    private void loadRestaurantDishes() {
        if (dishTilePane == null) {
            return;
        }

        dishTilePane.getChildren().clear();

        if (selectedRestaurant == null) {
            return;
        }

        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            if (dish.getRestaurant() != null
                    && dish.getRestaurant().getId() == selectedRestaurant.getId()
                    && dish.isAvailable()) {

                DishCardRow row = new DishCardRow(
                        dish.getId(),
                        dish.getTitle(),
                        dish.getCategory() == null ? "" : dish.getCategory(),
                        dish.getDescription() == null ? "" : dish.getDescription(),
                        dish.getPrice(),
                        dish.getWeight(),
                        dish.getCalories(),
                        dish.isAvailable(),
                        dish.getImageUrl() == null ? "" : dish.getImageUrl()
                );

                dishTilePane.getChildren().add(createDishCard(row));
            }
        }
    }

    private VBox createDishCard(DishCardRow item) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(110);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-color: lightgray;");

        String imageUrl = item.getImageUrl();

        try {
            if (imageUrl != null && !imageUrl.isBlank()) {
                Image image;

                if (imageUrl.startsWith("/")) {
                    var stream = HelloApplication.class.getResourceAsStream(imageUrl);
                    if (stream != null) {
                        image = new Image(stream);
                        imageView.setImage(image);
                    }
                } else {
                    image = new Image(imageUrl, false);
                    if (!image.isError()) {
                        imageView.setImage(image);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Label title = new Label(item.getTitle());
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        title.setWrapText(true);
        title.setMaxWidth(150);

        Label price = new Label("$" + String.format("%.2f", item.getPrice()));
        price.setStyle("-fx-font-size: 13px;");

        Button addButton = new Button("Add to cart");
        addButton.setPrefWidth(140);
        addButton.setOnAction(e -> addToCart(item));

        VBox card = new VBox(4, imageView, title, price, addButton);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(170);
        card.setMinWidth(170);
        card.setMaxWidth(170);
        card.setPrefHeight(220);
        card.setMinHeight(205);
        card.setMaxHeight(205);
        card.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #d0d0d0;
            -fx-border-width: 1;
            -fx-border-radius: 14;
            -fx-background-radius: 14;
        """);

        return card;
    }

    private void addToCart(DishCardRow dish) {
        CartItemRow existing = cart.get(dish.getId());

        if (existing == null) {
            cart.put(dish.getId(), new CartItemRow(
                    dish.getId(),
                    dish.getTitle(),
                    dish.getPrice(),
                    1
            ));
        } else {
            existing.setQuantity(existing.getQuantity() + 1);
        }

        refreshCart();
    }

    private void refreshCart() {
        if (cartList != null) {
            cartList.getItems().clear();
            cartList.getItems().addAll(cart.values());
        }

        double total = 0.0;
        for (CartItemRow item : cart.values()) {
            total += item.getTotal();
        }

        if (cartTotalLabel != null) {
            cartTotalLabel.setText("Total: $" + String.format("%.2f", total));
        }
    }

    public void clearCart() {
        cart.clear();
        refreshCart();
    }

    public void placeOrder() {
        if (!(Session.getCurrentUser() instanceof Client client)) {
            alert("Access denied", "Only client can place an order.");
            return;
        }

        if (selectedRestaurant == null) {
            alert("Restaurant", "Select a restaurant first.");
            return;
        }

        if (cart.isEmpty()) {
            alert("Cart", "Cart is empty.");
            return;
        }

        FoodOrder order = new FoodOrder();
        order.setBuyer(client);
        order.setRestaurant(selectedRestaurant);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setSpecialInstructions(
                specialInstructionsArea == null ? "" : specialInstructionsArea.getText()
        );

        double total = 0.0;
        ArrayList<Dish> items = new ArrayList<>();

        for (CartItemRow item : cart.values()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                for (Dish dish : customOperations.getAllRecords(Dish.class)) {
                    if (dish.getId() == item.getDishId()) {
                        items.add(dish);
                        total += dish.getPrice();
                        break;
                    }
                }
            }
        }

        order.setItems(items);
        order.setTotalPrice(total);
        order.setDeliveryFee(2.99);
        order.setEstimatedDeliveryMin(30);
        order.setPaymentMethod("CARD");

        customOperations.create(order);

        createNotification(client, "Your order #" + order.getId() + " was created.");
        createNotification(selectedRestaurant,
                "New order #" + order.getId() + " from " + client.getUsername());

        clearCart();

        if (specialInstructionsArea != null) {
            specialInstructionsArea.clear();
        }

        alert("Success", "Order created.");
    }

    private void createNotification(User user, String message) {
        if (user == null) {
            return;
        }

        Notification notification = new Notification(message, user);
        customOperations.create(notification);
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}