package com.example.courseworkitfu.fxControllers.tabs.dishes;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.fxControllers.dishes.CreateDishForm;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Dish;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DishesTabController implements Initializable {

    public TableView<DishTableParameters> dishTable;

    public TableColumn<DishTableParameters, Number> idColumn;
    public TableColumn<DishTableParameters, String> titleColumn;
    public TableColumn<DishTableParameters, String> descriptionColumn;
    public TableColumn<DishTableParameters, Number> priceColumn;
    public TableColumn<DishTableParameters, Number> weightColumn;
    public TableColumn<DishTableParameters, Number> caloriesColumn;
    public TableColumn<DishTableParameters, String> categoryColumn;
    public TableColumn<DishTableParameters, Boolean> availableColumn;
    public TableColumn<DishTableParameters, String> restaurantColumn;

    private CustomOperations customOperations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);
        initDishTable();
        refreshDishes();
    }

    private void initDishTable() {
        if (idColumn != null) {
            idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (titleColumn != null) {
            titleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        }
        if (descriptionColumn != null) {
            descriptionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        }
        if (priceColumn != null) {
            priceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()));
        }
        if (weightColumn != null) {
            weightColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getWeight()));
        }
        if (caloriesColumn != null) {
            caloriesColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCalories()));
        }
        if (categoryColumn != null) {
            categoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        }
        if (availableColumn != null) {
            availableColumn.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAvailable()));
        }
        if (restaurantColumn != null) {
            restaurantColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRestaurant()));
        }
    }

    public void refreshDishes() {
        if (dishTable == null) {
            return;
        }

        dishTable.getItems().clear();

        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            dishTable.getItems().add(new DishTableParameters(
                    dish.getId(),
                    dish.getTitle() == null ? "" : dish.getTitle(),
                    dish.getDescription() == null ? "" : dish.getDescription(),
                    dish.getPrice(),
                    dish.getWeight(),
                    dish.getCalories(),
                    dish.getCategory() == null ? "" : dish.getCategory(),
                    dish.isAvailable(),
                    dish.getRestaurant() == null ? "" : dish.getRestaurant().getUsername()
            ));
        }
    }

    public void createDish() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert(
                    "Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\nPlease use the web application."
            );
            Platform.exit();
            return;
        }

        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/com/example/courseworkitfu/dishes/create-dish-form.fxml")
            );
            Parent parent = loader.load();

            CreateDishForm controller = loader.getController();
            if (controller != null) {
                controller.setData(true, null);
            }

            stage.setScene(new Scene(parent));
            stage.setTitle("Create Dish");
            stage.showAndWait();

            refreshDishes();
        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to open create dish form.");
        }
    }

    public void deleteDish() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert(
                    "Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\nPlease use the web application."
            );
            Platform.exit();
            return;
        }

        DishTableParameters selectedRow = dishTable == null ? null : dishTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Dish", "Select a dish first.");
            return;
        }

        Dish selectedDish = null;
        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            if (dish.getId() == selectedRow.getId()) {
                selectedDish = dish;
                break;
            }
        }

        if (selectedDish == null) {
            alert("Dish", "Selected dish was not found.");
            return;
        }

        customOperations.delete(selectedDish.getId(), Dish.class);
        refreshDishes();
    }

    private boolean isDesktopAccessAllowed(User user) {
        if (user == null) {
            return false;
        }
        return user.isAdmin() || user.getClass().getSimpleName().equalsIgnoreCase("Restaurant");
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}