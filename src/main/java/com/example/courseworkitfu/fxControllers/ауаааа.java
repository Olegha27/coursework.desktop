package com.example.courseworkitfu.fxControllers;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.fxControllers.dishes.CreateDishForm;
import com.example.courseworkitfu.fxControllers.users.CreateUserForm;
import com.example.courseworkitfu.fxControllers.users.EditUserForm;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.*;
import com.example.courseworkitfu.services.CartService;
import com.example.courseworkitfu.services.OrderService;
import com.example.courseworkitfu.services.RestaurantService;
import com.example.courseworkitfu.session.Session;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ауаааа implements Initializable {

    private boolean isDesktopAccessAllowed(User user) {
        return user != null && (user.isAdmin() || user instanceof Restaurant);
    }

    private Timeline messageRefreshTimeline;
    private FoodOrder currentOrder;

    public Label titleLabel;

    public TabPane mainTabPane;

    public Tab restaurantsTab;

    public ListView<RestaurantCardRow> restaurantList;
    public TilePane dishTilePane;
    public ListView<CartItemRow> cartList;
    public Label selectedRestaurantLabel;
    public Label cartTotalLabel;
    public TextArea specialInstructionsArea;

    public Tab userTab;
    public Tab ordersTab;
    public Tab dishTab;
    public Tab reviewsTab;
    public Tab statisticsTab;
    public Tab notificationsTab;

    public TextField userSearchField;
    public TextField orderSearchField;
    public TextField reviewSearchField;

    public TableView<UserTableParameters> userTable;
    public TableColumn<UserTableParameters, Number> idColumn;
    public TableColumn<UserTableParameters, String> usernameColumn;
    public TableColumn<UserTableParameters, String> emailColumn;
    public TableColumn<UserTableParameters, String> phoneColumn;
    public TableColumn<UserTableParameters, String> dateCreatedColumn;
    public TableColumn<UserTableParameters, String> roleColumn;
    public TableColumn<UserTableParameters, String> activeColumn;
    public ListView<User> userList;

    public TableView<OrderTableRow> orderTable;
    public TableColumn<OrderTableRow, Number> orderIdColumn;
    public TableColumn<OrderTableRow, String> buyerColumn;
    public TableColumn<OrderTableRow, String> restaurantColumn;
    public TableColumn<OrderTableRow, String> driverColumn;
    public TableColumn<OrderTableRow, String> statusColumn;
    public TableColumn<OrderTableRow, Number> priceColumn;

    public Button refreshOrdersButton;
    public Button takeOrderButton;
    public Button completeOrderButton;
    public Button confirmOrderButton;
    public Button prepareOrderButton;
    public Button readyForPickupButton;
    public Button cancelOrderButton;

    public TableView<Dish> dishTable;
    public TableColumn<Dish, Number> dishIdColumn;
    public TableColumn<Dish, Number> dishCaloriesColumn;
    public TableColumn<Dish, String> dishCategoryColumn;
    public TableColumn<Dish, String> dishDescriptionColumn;
    public TableColumn<Dish, String> dishImageUrlColumn;
    public TableColumn<Dish, Boolean> dishAvailableColumn;
    public TableColumn<Dish, Number> dishPreparationColumn;
    public TableColumn<Dish, Number> dishPriceColumn;
    public TableColumn<Dish, String> dishTitleColumn;
    public TableColumn<Dish, Number> dishWeightColumn;
    public TableColumn<Dish, Number> dishRestaurantColumn;

    public ListView<Notification> notificationList;

    public TableView<ReviewTableRow> reviewTable;
    public TableColumn<ReviewTableRow, Number> reviewIdColumn;
    public TableColumn<ReviewTableRow, String> reviewTypeColumn;
    public TableColumn<ReviewTableRow, String> reviewerNameColumn;
    public TableColumn<ReviewTableRow, String> restaurantNameColumn;
    public TableColumn<ReviewTableRow, String> driverNameColumn;
    public TableColumn<ReviewTableRow, String> clientNameColumn;
    public TableColumn<ReviewTableRow, Number> reviewRatingColumn;
    public TableColumn<ReviewTableRow, String> reviewTitleColumn;
    public TableColumn<ReviewTableRow, String> reviewTextColumn;
    public TableColumn<ReviewTableRow, String> reviewDateColumn;

    public Label averageRatingLabel;
    public Label reviewsCountLabel;

    private CustomOperations customOperations;
    private RestaurantService restaurantService;
    private CartService cartService;
    private OrderService orderService;

    public ListView<Message> orderMessagesList;
    public TextArea newMessageArea;
    public Button sendMessageButton;

    private void startMessageAutoRefresh() {
        stopMessageAutoRefresh();

        messageRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> refreshMessages())
        );
        messageRefreshTimeline.setCycleCount(Animation.INDEFINITE);
        messageRefreshTimeline.play();
    }

    private void stopMessageAutoRefresh() {
        if (messageRefreshTimeline != null) {
            messageRefreshTimeline.stop();
            messageRefreshTimeline = null;
        }
    }

    private void refreshMessages() {
        if (currentOrder == null || orderMessagesList == null) {
            return;
        }

        CustomOperations operations = new CustomOperations(HelloApplication.emf);
        List<Message> messages = operations.getMessagesByOrderId(currentOrder.getId());

        orderMessagesList.getItems().setAll(messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);
        restaurantService = new RestaurantService(customOperations);
        cartService = new CartService();
        orderService = new OrderService(customOperations);

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (titleLabel != null) {
            if (currentUser != null) {
                titleLabel.setText("Hungry! | " + currentUser.getUsername());
            } else {
                titleLabel.setText("Hungry!");
            }
        }

        if (orderTable != null) {
            orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) {
                    currentOrder = null;
                    if (orderMessagesList != null) {
                        orderMessagesList.getItems().clear();
                    }
                    stopMessageAutoRefresh();
                    return;
                }

                currentOrder = null;
                for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
                    if (order.getId() == newVal.getId()) {
                        currentOrder = order;
                        break;
                    }
                }

                loadMessagesForSelectedOrder();
                startMessageAutoRefresh();
            });
        }

        initUserTable();
        initOrderTable();
        initDishTable();
        initReviewTable();
        initInteractions();

        applyRoleVisibility();
        applyOrderButtonVisibility();
        loadData();

        if (userList != null) {
            userList.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, newUser) -> {
                if (newUser != null) {
                    syncTableSelection(newUser);
                }
            });

            userList.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && Session.getCurrentUser() != null
                        && Session.getCurrentUser().isAdmin()) {

                    User selected = userList.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        openUserForEdit(selected);
                    }
                }
            });
        }

        if (userTable != null) {
            userTable.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && Session.getCurrentUser() != null
                        && Session.getCurrentUser().isAdmin()) {

                    UserTableParameters selectedRow = userTable.getSelectionModel().getSelectedItem();
                    if (selectedRow != null) {
                        for (User user : customOperations.getAllRecords(User.class)) {
                            if (user.getId() == selectedRow.getId()) {
                                openUserForEdit(user);
                                break;
                            }
                        }
                    }
                }
            });
        }

        if (dishTable != null) {
            dishTable.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Dish selectedDish = dishTable.getSelectionModel().getSelectedItem();
                    if (selectedDish != null) {
                        openDishForEdit(selectedDish);
                    }
                }
            });
        }

        if (userSearchField != null) {
            userSearchField.setOnAction(event -> searchUsers());
        }

        if (orderSearchField != null) {
            orderSearchField.setOnAction(event -> searchOrders());
        }

        if (notificationList != null) {
            notificationList.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Notification selected = notificationList.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        openOrderFromNotification(selected);
                    }
                }
            });
        }
    }

    private Integer extractOrderIdFromMessage(String message) {
        if (message == null || message.isBlank()) return null;

        Pattern pattern = Pattern.compile("#(\\d+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private void openOrderFromNotification(Notification notification) {
        if (notification == null || notification.getMessage() == null) return;

        Integer orderId = extractOrderIdFromMessage(notification.getMessage());
        if (orderId == null) {
            alert("Notification", "Order ID was not found in this notification.");
            return;
        }

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null || mainTabPane == null) return;

        if (ordersTab == null || !mainTabPane.getTabs().contains(ordersTab)) {
            alert("Notification", "Orders tab is not available.");
            return;
        }

        if (orderSearchField != null) {
            orderSearchField.setText(String.valueOf(orderId));
        }

        mainTabPane.getSelectionModel().select(ordersTab);
        searchOrders();
    }

    private void initInteractions() {
        if (restaurantList != null) {
            restaurantList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) return;

                restaurantService.setSelectedRestaurant(null);
                for (Restaurant restaurant : customOperations.getAllRecords(Restaurant.class)) {
                    if (restaurant.getId() == newVal.getId()) {
                        restaurantService.setSelectedRestaurant(restaurant);
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
        if (restaurantList == null) return;

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
        if (dishTilePane == null) return;

        dishTilePane.getChildren().clear();
        Restaurant selectedRestaurant = restaurantService.getSelectedRestaurant();
        if (selectedRestaurant == null) return;

        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            if (dish.getRestaurant() != null && dish.getRestaurant().getId() == selectedRestaurant.getId()) {
                if (!dish.isAvailable()) continue;

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
        cartService.addToCart(dish);
        refreshCart();
    }

    private void refreshCart() {
        if (cartList == null) return;

        cartList.getItems().clear();
        cartList.getItems().addAll(cartService.getCartItems().values());

        if (cartTotalLabel != null) {
            cartTotalLabel.setText("Total: $" + String.format("%.2f", cartService.getTotal()));
        }
    }

    public void clearCart() {
        cartService.clearCart();
        refreshCart();
    }

    public void placeOrder() {
        if (!(Session.getCurrentUser() instanceof Client client)) {
            return;
        }

        if (restaurantService.getSelectedRestaurant() == null) {
            alert("Restaurant", "Select a restaurant first.");
            return;
        }

        if (cartService.isEmpty()) {
            alert("Cart", "Cart is empty.");
            return;
        }

        Restaurant selectedRestaurant = restaurantService.getSelectedRestaurant();
        String instructions = specialInstructionsArea == null ? "" : specialInstructionsArea.getText();
        FoodOrder order = orderService.createOrder(client, selectedRestaurant, instructions, cartService.getCartItems());

        if (selectedRestaurant != null) {
            createNotification(selectedRestaurant,
                    "New order #" + order.getId() + " from " + client.getUsername());
        }

        clearCart();
        if (specialInstructionsArea != null) {
            specialInstructionsArea.clear();
        }

        loadOrders();

        if (mainTabPane != null && ordersTab != null) {
            mainTabPane.getSelectionModel().select(ordersTab);
        }

        alert("Success", "Order created.");
    }

    private void openUserForEdit(User user) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("edit-user-form.fxml"));
            Parent parent = loader.load();

            EditUserForm controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(parent);
            stage.setTitle("Edit User");
            stage.setScene(scene);
            stage.showAndWait();

            refreshUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUserTable() {
        if (idColumn != null) {
            idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (usernameColumn != null) {
            usernameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        }
        if (emailColumn != null) {
            emailColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        }
        if (phoneColumn != null) {
            phoneColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
        }
        if (dateCreatedColumn != null) {
            dateCreatedColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateCreated()));
        }
        if (roleColumn != null) {
            roleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRole()));
        }
        if (activeColumn != null) {
            activeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getActive()));
        }
    }

    private void loadUserList() {
        if (userList == null) return;
        userList.getItems().clear();
        userList.getItems().addAll(customOperations.getAllRecords(User.class));
    }

    private void loadUserTable() {
        if (userTable == null) return;
        userTable.getItems().clear();

        for (User user : customOperations.getAllRecords(User.class)) {
            String dateCreated = user.getDateCreated() == null ? "" : user.getDateCreated().toString();
            String email = user.getEmail() == null ? "" : user.getEmail();
            String phone = user.getPhoneNum() == null ? "" : user.getPhoneNum();
            String role = user.getClass().getSimpleName();
            String active = user.isActive() ? "Yes" : "No";

            userTable.getItems().add(new UserTableParameters(
                    user.getId(),
                    user.getUsername(),
                    email,
                    phone,
                    dateCreated,
                    role,
                    active
            ));
        }
    }

    private void syncTableSelection(User selectedUser) {
        if (selectedUser == null || userTable == null) return;

        for (UserTableParameters row : userTable.getItems()) {
            if (row.getId() == selectedUser.getId()) {
                userTable.getSelectionModel().select(row);
                userTable.scrollTo(row);
                break;
            }
        }
    }

    public void refreshUsers() {
        loadUserList();
        loadUserTable();
    }

    public void searchUsers() {
        if (userTable == null) return;

        String query = userSearchField == null ? "" : userSearchField.getText();
        if (query == null) query = "";

        String search = query.trim().toLowerCase();

        userTable.getItems().clear();
        if (userList != null) {
            userList.getItems().clear();
        }

        for (User user : customOperations.getAllRecords(User.class)) {
            String username = user.getUsername() == null ? "" : user.getUsername().toLowerCase();
            String email = user.getEmail() == null ? "" : user.getEmail().toLowerCase();
            String phone = user.getPhoneNum() == null ? "" : user.getPhoneNum().toLowerCase();
            String role = user.getClass().getSimpleName() == null ? "" : user.getClass().getSimpleName().toLowerCase();

            boolean matches =
                    username.equals(search) ||
                            email.equals(search) ||
                            phone.equals(search) ||
                            role.equals(search);

            if (matches) {
                String dateCreated = user.getDateCreated() == null ? "" : user.getDateCreated().toString();
                String active = user.isActive() ? "Yes" : "No";

                UserTableParameters row = new UserTableParameters(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail() == null ? "" : user.getEmail(),
                        user.getPhoneNum() == null ? "" : user.getPhoneNum(),
                        dateCreated,
                        user.getClass().getSimpleName(),
                        active
                );

                userTable.getItems().add(row);

                if (userList != null) {
                    userList.getItems().add(user);
                }
            }
        }
    }

    public void resetUserSearch() {
        if (userSearchField != null) {
            userSearchField.clear();
        }
        refreshUsers();
    }

    public void loadRegForm() throws IOException {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null || !currentUser.isAdmin()) {
            alert("Access denied", "Only admin can create users.");
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("create-user-form.fxml"));
        Parent parent = loader.load();

        CreateUserForm controller = loader.getController();
        controller.setData(true, null);

        stage.setScene(new Scene(parent));
        stage.setTitle("Create User");
        stage.showAndWait();

        refreshUsers();
    }

    public void deleteUser() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null || !currentUser.isAdmin()) {
            alert("Access denied", "Only admin can delete users.");
            return;
        }

        User user = userList == null ? null : userList.getSelectionModel().getSelectedItem();
        if (user != null) {
            customOperations.delete(user.getId(), User.class);
            refreshUsers();
        }
    }

    private void initOrderTable() {
        if (orderIdColumn != null) {
            orderIdColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (buyerColumn != null) {
            buyerColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBuyer()));
        }
        if (restaurantColumn != null) {
            restaurantColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRestaurant()));
        }
        if (driverColumn != null) {
            driverColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDriver()));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        }
        if (priceColumn != null) {
            priceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getTotalPrice()));
        }
    }

    private void loadOrders() {
        if (orderTable == null) return;
        orderTable.getItems().clear();

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {

            if (currentUser != null && !currentUser.isAdmin()) {

                if (currentUser instanceof Restaurant) {
                    if (order.getRestaurant() == null || order.getRestaurant().getId() != currentUser.getId()) {
                        continue;
                    }
                } else if (currentUser instanceof Driver) {
                    boolean isFreeReady = order.getDeliveryPerson() == null &&
                            order.getStatus() == OrderStatus.READY_FOR_PICKUP;

                    boolean isMyOrder = order.getDeliveryPerson() != null &&
                            order.getDeliveryPerson().getId() == currentUser.getId();

                    if (!isFreeReady && !isMyOrder) {
                        continue;
                    }
                } else if (currentUser instanceof Client) {
                    if (order.getBuyer() == null || order.getBuyer().getId() != currentUser.getId()) {
                        continue;
                    }
                }
            }

            String buyer = order.getBuyer() == null ? "-" : order.getBuyer().getUsername();
            String restaurant = order.getRestaurant() == null ? "-" : order.getRestaurant().getUsername();
            String driver = order.getDeliveryPerson() == null ? "FREE" : order.getDeliveryPerson().getUsername();
            String status = order.getStatus() == null ? "-" : order.getStatus().name();

            orderTable.getItems().add(new OrderTableRow(
                    order.getId(),
                    buyer,
                    restaurant,
                    driver,
                    status,
                    order.getTotalPrice()
            ));
        }
    }

    public void searchOrders() {
        if (orderTable == null) return;

        String query = orderSearchField == null ? "" : orderSearchField.getText();
        if (query == null) query = "";

        String search = query.trim().toLowerCase();

        orderTable.getItems().clear();

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {

            if (currentUser != null && !currentUser.isAdmin()) {

                if (currentUser instanceof Restaurant) {
                    if (order.getRestaurant() == null || order.getRestaurant().getId() != currentUser.getId()) {
                        continue;
                    }
                } else if (currentUser instanceof Driver) {
                    boolean isFreeReady = order.getDeliveryPerson() == null
                            && order.getStatus() == OrderStatus.READY_FOR_PICKUP;

                    boolean isMyOrder = order.getDeliveryPerson() != null
                            && order.getDeliveryPerson().getId() == currentUser.getId();

                    if (!isFreeReady && !isMyOrder) {
                        continue;
                    }
                } else if (currentUser instanceof Client) {
                    if (order.getBuyer() == null || order.getBuyer().getId() != currentUser.getId()) {
                        continue;
                    }
                }
            }

            String orderIdText = String.valueOf(order.getId());
            String buyer = order.getBuyer() == null ? "-" : order.getBuyer().getUsername();
            String restaurant = order.getRestaurant() == null ? "-" : order.getRestaurant().getUsername();
            String driver = order.getDeliveryPerson() == null ? "FREE" : order.getDeliveryPerson().getUsername();
            String status = order.getStatus() == null ? "-" : order.getStatus().name();
            String createdAt = order.getCreatedAt() == null ? "" : order.getCreatedAt().toString();

            boolean matches;

            if (search.isBlank()) {
                matches = true;
            } else if (currentUser != null && currentUser.isAdmin()) {
                matches =
                        orderIdText.equals(search) ||
                                buyer.toLowerCase().equals(search) ||
                                restaurant.toLowerCase().equals(search) ||
                                driver.toLowerCase().equals(search) ||
                                status.toLowerCase().equals(search) ||
                                createdAt.toLowerCase().equals(search);
            } else if (currentUser instanceof Restaurant) {
                matches =
                        orderIdText.equals(search) ||
                                buyer.toLowerCase().equals(search) ||
                                status.toLowerCase().equals(search) ||
                                createdAt.toLowerCase().equals(search);
            } else {
                matches =
                        orderIdText.equals(search) ||
                                buyer.toLowerCase().equals(search) ||
                                restaurant.toLowerCase().equals(search) ||
                                driver.toLowerCase().equals(search) ||
                                status.toLowerCase().equals(search);
            }

            if (matches) {
                orderTable.getItems().add(new OrderTableRow(
                        order.getId(),
                        buyer,
                        restaurant,
                        driver,
                        status,
                        order.getTotalPrice()
                ));
            }
        }
    }

    public void resetOrderSearch() {
        if (orderSearchField != null) {
            orderSearchField.clear();
        }
        refreshOrders();
    }

    public void refreshOrders() {
        loadOrders();
    }

    private FoodOrder findOrderBySelectedRow() {
        OrderTableRow selectedRow = orderTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Order", "Select an order first.");
            return null;
        }

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            if (order.getId() == selectedRow.getId()) {
                return order;
            }
        }

        alert("Order", "Selected order not found.");
        return null;
    }

    private boolean canRestaurantManageOrder(FoodOrder order) {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return false;
        }

        if (!(currentUser instanceof Restaurant) &&
                (currentUser == null || !currentUser.isAdmin())) {
            alert("Access denied", "Only restaurant or admin can manage restaurant statuses.");
            return false;
        }

        if (currentUser instanceof Restaurant restaurant) {
            if (order.getRestaurant() == null ||
                    order.getRestaurant().getId() != restaurant.getId()) {
                alert("Access denied", "You can manage only your restaurant orders.");
                return false;
            }
        }

        return true;
    }

    private void applyOrderButtonVisibility() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null) return;

        boolean isAdmin = currentUser.isAdmin();
        boolean isRestaurant = currentUser instanceof Restaurant;
        boolean isDriver = currentUser instanceof Driver;

        if (refreshOrdersButton != null) {
            refreshOrdersButton.setVisible(true);
            refreshOrdersButton.setManaged(true);
        }

        if (takeOrderButton != null) {
            takeOrderButton.setVisible(isDriver || isAdmin);
            takeOrderButton.setManaged(isDriver || isAdmin);
        }

        if (completeOrderButton != null) {
            completeOrderButton.setVisible(isDriver || isAdmin);
            completeOrderButton.setManaged(isDriver || isAdmin);
        }

        if (confirmOrderButton != null) {
            confirmOrderButton.setVisible(isRestaurant || isAdmin);
            confirmOrderButton.setManaged(isRestaurant || isAdmin);
        }

        if (prepareOrderButton != null) {
            prepareOrderButton.setVisible(isRestaurant || isAdmin);
            prepareOrderButton.setManaged(isRestaurant || isAdmin);
        }

        if (readyForPickupButton != null) {
            readyForPickupButton.setVisible(isRestaurant || isAdmin);
            readyForPickupButton.setManaged(isRestaurant || isAdmin);
        }

        if (cancelOrderButton != null) {
            boolean canCancel = isRestaurant || isDriver || isAdmin;
            cancelOrderButton.setVisible(canCancel);
            cancelOrderButton.setManaged(canCancel);
        }
    }

    public void confirmOrder() {
        FoodOrder order = findOrderBySelectedRow();
        if (order == null) return;
        if (!canRestaurantManageOrder(order)) return;

        if (order.getStatus() != OrderStatus.PENDING) {
            alert("Order", "Only PENDING orders can be confirmed.");
            return;
        }

        order.setStatus(OrderStatus.CONFIRMED);
        customOperations.update(order);

        if (order.getBuyer() != null) {
            createNotification(order.getBuyer(), "Your order #" + order.getId() + " was confirmed by restaurant.");
        }

        loadOrders();
        alert("Success", "Order confirmed.");
    }

    public void prepareOrder() {
        FoodOrder order = findOrderBySelectedRow();
        if (order == null) return;
        if (!canRestaurantManageOrder(order)) return;

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            alert("Order", "Only CONFIRMED orders can be moved to PREPARING.");
            return;
        }

        order.setStatus(OrderStatus.PREPARING);
        customOperations.update(order);

        if (order.getBuyer() != null) {
            createNotification(order.getBuyer(), "Your order #" + order.getId() + " is now being prepared.");
        }

        loadOrders();
        alert("Success", "Order is now preparing.");
    }

    public void readyForPickupOrder() {
        FoodOrder order = findOrderBySelectedRow();
        if (order == null) return;
        if (!canRestaurantManageOrder(order)) return;

        if (order.getStatus() != OrderStatus.PREPARING) {
            alert("Order", "Only PREPARING orders can be marked READY_FOR_PICKUP.");
            return;
        }

        order.setStatus(OrderStatus.READY_FOR_PICKUP);
        customOperations.update(order);

        if (order.getBuyer() != null) {
            createNotification(order.getBuyer(), "Your order #" + order.getId() + " is ready for pickup.");
        }

        loadOrders();
        alert("Success", "Order is ready for pickup.");
    }

    public void cancelOrder() {
        FoodOrder order = findOrderBySelectedRow();
        if (order == null) return;

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null) return;

        boolean isAdmin = currentUser.isAdmin();
        boolean isRestaurant = currentUser instanceof Restaurant;
        boolean isDriver = currentUser instanceof Driver;

        if (!isAdmin && !isRestaurant && !isDriver) {
            alert("Access denied", "You cannot cancel orders.");
            return;
        }

        if (isDriver && !isAdmin) {
            Driver driver = (Driver) currentUser;

            if (order.getDeliveryPerson() == null) {
                alert("Order", "This order has no assigned driver.");
                return;
            }

            if (order.getDeliveryPerson().getId() != driver.getId()) {
                alert("Order", "You can cancel only your own delivery.");
                return;
            }

            if (order.getStatus() != OrderStatus.IN_DELIVERY) {
                alert("Order", "Driver can cancel only IN_DELIVERY orders.");
                return;
            }

            order.setDeliveryPerson(null);
            order.setStatus(OrderStatus.READY_FOR_PICKUP);
            customOperations.update(order);

            if (order.getRestaurant() != null) {
                createNotification(order.getRestaurant(), "Driver returned order #" + order.getId() + " to READY_FOR_PICKUP.");
            }

            if (order.getBuyer() != null) {
                createNotification(order.getBuyer(), "Driver cancelled delivery for order #" + order.getId() + ".");
            }

            loadOrders();
            alert("Success", "Order returned to READY_FOR_PICKUP.");
            return;
        }

        if (isRestaurant || isAdmin) {
            if (!isAdmin && !canRestaurantManageOrder(order)) {
                return;
            }

            if (order.getStatus() == OrderStatus.DELIVERED) {
                alert("Order", "Delivered order cannot be cancelled.");
                return;
            }

            order.setStatus(OrderStatus.CANCELLED);
            customOperations.update(order);

            if (order.getBuyer() != null) {
                createNotification(order.getBuyer(), "Your order #" + order.getId() + " was cancelled.");
            }

            loadOrders();
            alert("Success", "Order cancelled.");
        }
    }

    public void takeOrder() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (!(currentUser instanceof Driver) && (currentUser == null || !currentUser.isAdmin())) {
            alert("Access denied", "Only driver or admin can take orders.");
            return;
        }

        OrderTableRow selectedRow = orderTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Order", "Select an order first.");
            return;
        }

        Driver driver = currentUser instanceof Driver ? (Driver) currentUser : null;

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            if (order.getId() == selectedRow.getId()) {

                if (driver != null) {
                    if (order.getDeliveryPerson() != null && order.getDeliveryPerson().getId() != driver.getId()) {
                        alert("Order", "This order is already taken by another driver.");
                        return;
                    }

                    if (order.getDeliveryPerson() != null && order.getDeliveryPerson().getId() == driver.getId()) {
                        alert("Order", "This order is already assigned to you.");
                        return;
                    }
                }

                if (order.getStatus() != OrderStatus.READY_FOR_PICKUP) {
                    alert("Order", "Driver can take only READY_FOR_PICKUP orders.");
                    return;
                }

                if (driver != null) {
                    order.setDeliveryPerson(driver);
                }

                order.setStatus(OrderStatus.IN_DELIVERY);
                customOperations.update(order);

                if (order.getBuyer() != null) {
                    createNotification(order.getBuyer(), "Driver has taken your order #" + order.getId() + ".");
                }

                if (order.getRestaurant() != null) {
                    createNotification(order.getRestaurant(), "Driver picked up order #" + order.getId() + ".");
                }

                loadOrders();
                alert("Success", "Order assigned to you.");
                return;
            }
        }
    }

    public void completeOrder() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (!(currentUser instanceof Driver) && (currentUser == null || !currentUser.isAdmin())) {
            alert("Access denied", "Only driver or admin can complete orders.");
            return;
        }

        OrderTableRow selectedRow = orderTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Order", "Select an order first.");
            return;
        }

        Driver driver = currentUser instanceof Driver ? (Driver) currentUser : null;

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            if (order.getId() == selectedRow.getId()) {

                if (order.getDeliveryPerson() == null) {
                    alert("Order", "This order has no driver yet.");
                    return;
                }

                if (driver != null && order.getDeliveryPerson().getId() != driver.getId()) {
                    alert("Order", "You can complete only your own orders.");
                    return;
                }

                if (order.getStatus() != OrderStatus.IN_DELIVERY) {
                    alert("Order", "Only IN_DELIVERY orders can be completed.");
                    return;
                }

                order.setStatus(OrderStatus.DELIVERED);
                order.setDeliveredAt(LocalDateTime.now());
                customOperations.update(order);

                if (order.getBuyer() != null) {
                    createNotification(order.getBuyer(), "Your order #" + order.getId() + " was delivered.");
                }

                if (order.getRestaurant() != null) {
                    createNotification(order.getRestaurant(), "Order #" + order.getId() + " was delivered.");
                }

                loadOrders();
                alert("Success", "Order completed.");
                return;
            }
        }
    }

    private void openDishForEdit(Dish dish) {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (currentUser == null) {
            return;
        }

        boolean isAdmin = currentUser.isAdmin();
        boolean isRestaurant = currentUser instanceof Restaurant;

        if (!isAdmin && !isRestaurant) {
            alert("Access denied", "Only admin or restaurant can edit dishes.");
            return;
        }

        if (isRestaurant) {
            if (dish.getRestaurant() == null || dish.getRestaurant().getId() != currentUser.getId()) {
                alert("Access denied", "You can edit only your own dishes.");
                return;
            }
        }

        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("create-dish-form.fxml"));
            Parent parent = loader.load();

            CreateDishForm controller = loader.getController();
            controller.setData(false, dish);

            Scene scene = new Scene(parent);
            stage.setTitle("Edit Dish");
            stage.setScene(scene);
            stage.showAndWait();

            loadDishes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDishTable() {
        if (dishIdColumn != null) {
            dishIdColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (dishCaloriesColumn != null) {
            dishCaloriesColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCalories()));
        }
        if (dishCategoryColumn != null) {
            dishCategoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory() == null ? "" : c.getValue().getCategory()));
        }
        if (dishDescriptionColumn != null) {
            dishDescriptionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription() == null ? "" : c.getValue().getDescription()));
        }
        if (dishImageUrlColumn != null) {
            dishImageUrlColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getImageUrl() == null ? "" : c.getValue().getImageUrl()));
        }
        if (dishAvailableColumn != null) {
            dishAvailableColumn.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAvailable()));
        }
        if (dishPreparationColumn != null) {
            dishPreparationColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPreparationTimeMin()));
        }
        if (dishPriceColumn != null) {
            dishPriceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()));
        }
        if (dishTitleColumn != null) {
            dishTitleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle() == null ? "" : c.getValue().getTitle()));
        }
        if (dishWeightColumn != null) {
            dishWeightColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getWeight()));
        }
        if (dishRestaurantColumn != null) {
            dishRestaurantColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRestaurant() == null ? 0 : c.getValue().getRestaurant().getId()));
        }
    }

    private void initReviewTable() {
        if (reviewTable == null) return;

        reviewIdColumn.setCellValueFactory(c -> c.getValue().idProperty());
        reviewTypeColumn.setCellValueFactory(c -> c.getValue().reviewTypeProperty());
        reviewerNameColumn.setCellValueFactory(c -> c.getValue().reviewerNameProperty());
        restaurantNameColumn.setCellValueFactory(c -> c.getValue().restaurantNameProperty());
        driverNameColumn.setCellValueFactory(c -> c.getValue().driverNameProperty());
        clientNameColumn.setCellValueFactory(c -> c.getValue().clientNameProperty());
        reviewRatingColumn.setCellValueFactory(c -> c.getValue().ratingProperty());
        reviewTitleColumn.setCellValueFactory(c -> c.getValue().titleProperty());
        reviewTextColumn.setCellValueFactory(c -> c.getValue().textProperty());
        reviewDateColumn.setCellValueFactory(c -> c.getValue().reviewDateProperty());
    }

    private void loadDishes() {
        if (dishTable == null) return;

        dishTable.getItems().clear();

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null) return;

        Restaurant restaurantUser = currentUser instanceof Restaurant ? (Restaurant) currentUser : null;
        boolean isAdmin = currentUser.isAdmin();

        if (!isAdmin && restaurantUser == null) return;

        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            if (!isAdmin && restaurantUser != null) {
                if (dish.getRestaurant() == null || dish.getRestaurant().getId() != restaurantUser.getId()) {
                    continue;
                }
            }
            dishTable.getItems().add(dish);
        }
    }

    public void refreshDishes() {
        loadDishes();
    }

    public void createDish() throws IOException {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (!(currentUser instanceof Restaurant) && (currentUser == null || !currentUser.isAdmin())) {
            alert("Access denied", "Only restaurant or admin can create dishes.");
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("create-dish-form.fxml"));
        Parent parent = loader.load();

        CreateDishForm controller = loader.getController();
        controller.setData(true, null);

        Scene scene = new Scene(parent);
        stage.setTitle("Create Dish");
        stage.setScene(scene);
        stage.showAndWait();

        loadDishes();
    }

    public void deleteDish() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (!(currentUser instanceof Restaurant) && (currentUser == null || !currentUser.isAdmin())) {
            alert("Access denied", "Only restaurant or admin can delete dishes.");
            return;
        }

        Dish dish = dishTable.getSelectionModel().getSelectedItem();
        if (dish != null) {
            customOperations.delete(dish.getId(), Dish.class);
            loadDishes();
        }
    }

    public void loadNotifications() {
        if (notificationList == null) return;

        notificationList.getItems().clear();

        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null) return;

        for (Notification notification : customOperations.getAllRecords(Notification.class)) {
            if (notification.getUser() != null && notification.getUser().getId() == currentUser.getId()) {
                notificationList.getItems().add(notification);
            }
        }
    }

    private void createNotification(User user, String message) {
        orderService.createNotification(user, message);
    }

    private void applyRoleVisibility() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }
        if (currentUser == null) return;

        boolean isAdmin = currentUser.isAdmin();
        boolean isRestaurant = currentUser instanceof Restaurant;
        boolean isClient = currentUser instanceof Client;
        boolean isDriver = currentUser instanceof Driver;

        if (isAdmin) {
            if (ordersTab != null) {
                ordersTab.setText("Order Management");
            }
            return;
        }

        if (isClient) {
            if (ordersTab != null) {
                ordersTab.setText("My Orders");
            }

            if (mainTabPane.getTabs().contains(userTab)) {
                mainTabPane.getTabs().remove(userTab);
            }
            if (mainTabPane.getTabs().contains(dishTab)) {
                mainTabPane.getTabs().remove(dishTab);
            }
            if (mainTabPane.getTabs().contains(statisticsTab)) {
                mainTabPane.getTabs().remove(statisticsTab);
            }

            return;
        }

        if (ordersTab != null) {
            ordersTab.setText("Order Management");
        }

        if (mainTabPane.getTabs().contains(restaurantsTab)) {
            mainTabPane.getTabs().remove(restaurantsTab);
        }

        if (!isAdmin && mainTabPane.getTabs().contains(userTab)) {
            mainTabPane.getTabs().remove(userTab);
        }

        if (!isRestaurant && mainTabPane.getTabs().contains(dishTab)) {
            mainTabPane.getTabs().remove(dishTab);
        }

        if (!isRestaurant && mainTabPane.getTabs().contains(statisticsTab)) {
            mainTabPane.getTabs().remove(statisticsTab);
        }

        if (!isRestaurant && !isDriver && !isAdmin) {
            if (mainTabPane.getTabs().contains(ordersTab)) {
                mainTabPane.getTabs().remove(ordersTab);
            }
        }
    }

    public void loadData() {
        if (restaurantsTab != null && restaurantsTab.isSelected()) {
            loadRestaurantsView(null);
        } else if (userTab != null && userTab.isSelected()) {
            loadUserList();
            loadUserTable();
        } else if (ordersTab != null && ordersTab.isSelected()) {
            loadOrders();
            loadMessagesForSelectedOrder();
        } else if (dishTab != null && dishTab.isSelected()) {
            loadDishes();
        } else if (reviewsTab != null && reviewsTab.isSelected()) {
            loadReviews();
        } else if (notificationsTab != null && notificationsTab.isSelected()) {
            loadNotifications();
        }
    }

    public void logout() throws IOException {
        stopMessageAutoRefresh();
        Session.clear();

        Stage stage = (Stage) mainTabPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Hungry!");
        stage.setScene(scene);
        stage.show();
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void sendMessage() {
        OrderTableRow selectedRow = orderTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Messages", "Select an order first.");
            return;
        }

        String text = newMessageArea.getText();
        if (text == null || text.isBlank()) {
            alert("Messages", "Message is empty.");
            return;
        }

        FoodOrder targetOrder = null;
        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            if (order.getId() == selectedRow.getId()) {
                targetOrder = order;
                break;
            }
        }

        if (targetOrder == null) {
            alert("Messages", "Order not found.");
            return;
        }

        if (!canCurrentUserAccessOrder(targetOrder)) {
            alert("Access denied", "You cannot write messages in this order.");
            return;
        }

        if (isOrderChatReadOnly(targetOrder)) {
            alert("Messages", "Chat is read-only for completed or cancelled orders.");
            return;
        }

        Message message = new Message(text.trim(), targetOrder, Session.getCurrentUser());
        customOperations.create(message);

        newMessageArea.clear();
        loadMessagesForSelectedOrder();
    }

    public void loadMessagesForSelectedOrder() {
        if (orderMessagesList == null || orderTable == null) return;

        orderMessagesList.getItems().clear();

        OrderTableRow selectedRow = orderTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            currentOrder = null;
            stopMessageAutoRefresh();
            return;
        }

        currentOrder = null;
        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            if (order.getId() == selectedRow.getId()) {
                currentOrder = order;
                break;
            }
        }

        if (currentOrder == null) {
            stopMessageAutoRefresh();
            return;
        }

        refreshMessages();
    }

    private boolean canCurrentUserAccessOrder(FoodOrder order) {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return false;
        }
        if (currentUser == null || order == null) return false;

        if (currentUser.isAdmin()) return true;

        if (order.getBuyer() != null && order.getBuyer().getId() == currentUser.getId()) return true;
        if (order.getRestaurant() != null && order.getRestaurant().getId() == currentUser.getId()) return true;
        if (order.getDeliveryPerson() != null && order.getDeliveryPerson().getId() == currentUser.getId()) return true;

        return false;
    }

    private boolean isOrderChatReadOnly(FoodOrder order) {
        if (order == null || order.getStatus() == null) return false;

        return order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED;
    }

    // ============================= REVIEWS METHODS =============================

    public void loadReviews() {
        User currentUser = Session.getCurrentUser();
        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        List<Review> allReviews = customOperations.getAllRecords(Review.class);
        List<Review> filteredReviews = new ArrayList<>();

        if (currentUser instanceof Restaurant restaurant) {
            for (Review review : allReviews) {
                if (review.getReviewedRestaurant() != null &&
                    review.getReviewedRestaurant().getId() == restaurant.getId()) {
                    filteredReviews.add(review);
                }
            }
        } else {
            filteredReviews = allReviews;
        }

        String searchTerm = reviewSearchField != null ? reviewSearchField.getText().toLowerCase() : "";
        if (!searchTerm.isEmpty()) {
            filteredReviews = filterReviews(filteredReviews, searchTerm);
        }

        List<ReviewTableRow> reviewRows = new ArrayList<>();
        for (Review review : filteredReviews) {
            reviewRows.add(new ReviewTableRow(review));
        }

        reviewTable.getItems().setAll(reviewRows);

        if (currentUser instanceof Restaurant restaurant) {
            calculateRestaurantStats(restaurant);
        } else {
            calculateAllStats(allReviews);
        }
    }

    private List<Review> filterReviews(List<Review> reviews, String searchTerm) {
        List<Review> filtered = new ArrayList<>();
        for (Review review : reviews) {
            String reviewer = review.getReviewer() != null ? review.getReviewer().getUsername().toLowerCase() : "";
            String restaurant = review.getReviewedRestaurant() != null ? review.getReviewedRestaurant().getUsername().toLowerCase() : "";
            String driver = review.getReviewedDriver() != null ? review.getReviewedDriver().getUsername().toLowerCase() : "";
            String client = review.getReviewedClient() != null ? review.getReviewedClient().getUsername().toLowerCase() : "";
            String text = review.getText() != null ? review.getText().toLowerCase() : "";
            String title = review.getTitle() != null ? review.getTitle().toLowerCase() : "";

            if (reviewer.contains(searchTerm) ||
                    restaurant.contains(searchTerm) ||
                    driver.contains(searchTerm) ||
                    client.contains(searchTerm) ||
                    text.contains(searchTerm) ||
                    title.contains(searchTerm)) {
                filtered.add(review);
            }
        }
        return filtered;
    }

    private void calculateRestaurantStats(Restaurant restaurant) {
        List<Review> restaurantReviews = new ArrayList<>();
        for (Review review : customOperations.getAllRecords(Review.class)) {
            if (review.getReviewedRestaurant() != null &&
                review.getReviewedRestaurant().getId() == restaurant.getId()) {
                restaurantReviews.add(review);
            }
        }

        if (restaurantReviews.isEmpty()) {
            averageRatingLabel.setText("Average Rating: N/A");
            reviewsCountLabel.setText("Total Reviews: 0");
            return;
        }

        int totalRating = 0;
        for (Review review : restaurantReviews) {
            totalRating += review.getRating();
        }
        double average = (double) totalRating / restaurantReviews.size();

        averageRatingLabel.setText(String.format("Average Rating: %.1f / 5.0", average));
        reviewsCountLabel.setText("Total Reviews: " + restaurantReviews.size());
    }

    private void calculateAllStats(List<Review> allReviews) {
        if (allReviews.isEmpty()) {
            averageRatingLabel.setText("Average Rating: N/A");
            reviewsCountLabel.setText("Total Reviews: 0");
            return;
        }

        int totalRating = 0;
        for (Review review : allReviews) {
            totalRating += review.getRating();
        }

        double average = (double) totalRating / allReviews.size();
        averageRatingLabel.setText(String.format("Average Rating: %.1f / 5.0", average));
        reviewsCountLabel.setText("Total Reviews: " + allReviews.size());
    }

    public void searchReviews() {
        loadReviews();
    }

    public void resetReviewSearch() {
        if (reviewSearchField != null) {
            reviewSearchField.clear();
            loadReviews();
        }
    }

    public void deleteSelectedReview() {
        ReviewTableRow selectedRow = reviewTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Delete Review", "Select a review first.");
            return;
        }

        final int reviewId = selectedRow.getId();

        User currentUser = Session.getCurrentUser();
        if (!currentUser.isAdmin()) {
            alert("Delete Review", "Only administrators can delete reviews.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Review");
        confirm.setHeaderText("Are you sure you want to delete this review?");
        confirm.setContentText("Type: " + selectedRow.getReviewType() + "\n" +
                                "Reviewer: " + selectedRow.getReviewerName() + "\n" +
                                "Rating: " + selectedRow.getRating() + " / 5");

        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                customOperations.delete(reviewId, Review.class);
                alert("Delete Review", "Review deleted successfully.");
                loadReviews();
            }
        });
    }
}