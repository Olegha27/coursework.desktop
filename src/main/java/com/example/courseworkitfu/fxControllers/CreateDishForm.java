package com.example.courseworkitfu.fxControllers;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Dish;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateDishForm {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField caloriesField;
    @FXML private TextField categoryField;
    @FXML private TextArea descriptionField;
    @FXML private TextField imageUrlField;
    @FXML private TextField preparationTimeField;
    @FXML private TextField weightField;
    @FXML private CheckBox availableCheckBox;
    @FXML private Label errorLabel;

    private final CustomOperations customOperations = new CustomOperations(HelloApplication.emf);

    private boolean isCreateMode = true;
    private Dish editingDish;

    public void setData(boolean isCreateMode, Dish dish) {
        this.isCreateMode = isCreateMode;
        this.editingDish = dish;

        if (!isCreateMode && dish != null) {
            titleField.setText(dish.getTitle());
            priceField.setText(String.valueOf(dish.getPrice()));
            caloriesField.setText(String.valueOf(dish.getCalories()));
            categoryField.setText(dish.getCategory() == null ? "" : dish.getCategory());
            descriptionField.setText(dish.getDescription() == null ? "" : dish.getDescription());
            imageUrlField.setText(dish.getImageUrl() == null ? "" : dish.getImageUrl());
            preparationTimeField.setText(String.valueOf(dish.getPreparationTimeMin()));
            weightField.setText(String.valueOf(dish.getWeight()));
            availableCheckBox.setSelected(dish.isAvailable());
        }
    }

    @FXML
    public void saveDish() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }

        String title = titleField.getText();
        String category = categoryField.getText();
        String description = descriptionField.getText();
        String imageUrl = imageUrlField.getText();

        if (title == null || title.trim().isEmpty()) {
            errorLabel.setText("Title is required.");
            return;
        }

        float price;
        int calories;
        int preparationTimeMin;
        float weight;

        try {
            price = Float.parseFloat(priceField.getText());
        } catch (Exception e) {
            errorLabel.setText("Price must be a number.");
            return;
        }

        try {
            calories = Integer.parseInt(caloriesField.getText());
        } catch (Exception e) {
            errorLabel.setText("Calories must be an integer.");
            return;
        }

        try {
            preparationTimeMin = Integer.parseInt(preparationTimeField.getText());
        } catch (Exception e) {
            errorLabel.setText("Preparation time must be an integer.");
            return;
        }

        try {
            weight = Float.parseFloat(weightField.getText());
        } catch (Exception e) {
            errorLabel.setText("Weight must be a number.");
            return;
        }

        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            errorLabel.setText("No active user.");
            return;
        }

        Dish dish;

        if (isCreateMode) {
            dish = new Dish();

            if (currentUser instanceof Restaurant restaurant) {
                dish.setRestaurant(restaurant);
            } else if (currentUser.isAdmin()) {
                if (editingDish != null && editingDish.getRestaurant() != null) {
                    dish.setRestaurant(editingDish.getRestaurant());
                }
            }
        } else {
            if (editingDish == null) {
                errorLabel.setText("Dish not found.");
                return;
            }
            dish = editingDish;
        }

        dish.setTitle(title.trim());
        dish.setPrice(price);
        dish.setCalories(calories);
        dish.setCategory(category == null ? "" : category.trim());
        dish.setDescription(description == null ? "" : description.trim());
        dish.setImageUrl(imageUrl == null ? "" : imageUrl.trim());
        dish.setPreparationTimeMin(preparationTimeMin);
        dish.setWeight(weight);
        dish.setAvailable(availableCheckBox.isSelected());

        if (isCreateMode) {
            customOperations.create(dish);
        } else {
            customOperations.update(dish);
        }

        closeWindow();
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}