package com.example.courseworkitfu.fxControllers;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.hibernateOperations.JpaUtil;
import com.example.courseworkitfu.model.Client;
import com.example.courseworkitfu.model.Driver;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.model.VehicleType;
import com.example.courseworkitfu.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;

public class RegisterForm {

    private final CustomOperations customOperations =
            new CustomOperations(JpaUtil.getEntityManagerFactory());

    @FXML public Label formTitleLabel;
    @FXML public Button saveButton;
    @FXML public Button cancelButton;

    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField repeatPasswordField;

    @FXML public TextField emailField;
    @FXML public TextField phoneField;

    @FXML public RadioButton restaurantRadio;
    @FXML public RadioButton clientRadio;
    @FXML public RadioButton driverRadio;

    @FXML private Pane clientPane;
    @FXML private Pane driverPane;
    @FXML private Pane restaurantPane;

    @FXML public Label errorLabel;

    @FXML public TextField clientNameField;
    @FXML public TextField clientSurnameField;
    @FXML public TextField clientAddressField;

    @FXML public TextField driverNameField;
    @FXML public TextField driverSurnameField;
    @FXML public TextField driverLicenceField;
    @FXML public ComboBox<String> vehicleTypeCombo;
    @FXML public TextField vehiclePlateField;

    @FXML public TextArea restaurantDescField;
    @FXML public TextField restaurantAddressField;
    @FXML public TextField cuisineTypeField;
    @FXML public TextField openingTimeField;
    @FXML public TextField closingTimeField;

    private final ToggleGroup roleGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        if (vehicleTypeCombo != null) {
            vehicleTypeCombo.getItems().setAll("BIKE", "CAR", "LEGS", "SCOOTER");
        }

        if (clientRadio != null) clientRadio.setToggleGroup(roleGroup);
        if (driverRadio != null) driverRadio.setToggleGroup(roleGroup);
        if (restaurantRadio != null) restaurantRadio.setToggleGroup(roleGroup);

        if (clientRadio != null) {
            clientRadio.setSelected(true);
        }

        radioChanged();
    }

    @FXML
    private void radioChanged() {
        boolean client = clientRadio != null && clientRadio.isSelected();
        boolean driver = driverRadio != null && driverRadio.isSelected();
        boolean rest = restaurantRadio != null && restaurantRadio.isSelected();

        if (clientPane != null) {
            clientPane.setVisible(client);
            clientPane.setManaged(client);
            clientPane.setDisable(!client);
        }
        if (driverPane != null) {
            driverPane.setVisible(driver);
            driverPane.setManaged(driver);
            driverPane.setDisable(!driver);
        }
        if (restaurantPane != null) {
            restaurantPane.setVisible(rest);
            restaurantPane.setManaged(rest);
            restaurantPane.setDisable(!rest);
        }
    }

    @FXML
    public void onRegister() throws IOException {
        if (errorLabel != null) errorLabel.setText("");

        String u = usernameField.getText();
        String p1 = passwordField.getText();
        String p2 = repeatPasswordField.getText();

        if (u == null || u.trim().isEmpty()) {
            errorLabel.setText("Username required");
            return;
        }

        if (p1 == null || p1.length() < 3) {
            errorLabel.setText("Password too short (minimum 3 characters)");
            return;
        }
        if (!p1.equals(p2)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        boolean isClient = clientRadio != null && clientRadio.isSelected();
        boolean isDriver = driverRadio != null && driverRadio.isSelected();
        boolean isRestaurant = restaurantRadio != null && restaurantRadio.isSelected();

        User user;
        if (isRestaurant) {
            user = new Restaurant();
        } else if (isDriver) {
            user = new Driver();
        } else {
            user = new Client();
            isClient = true;
        }

        user.setUsername(u);
        user.setPassword(PasswordUtils.hashPassword(p1));
        user.setEmail(emailField.getText());
        user.setPhoneNum(phoneField.getText());
        user.setAdmin(false);
        user.setActive(true);

        if (user instanceof Client) {
            ((Client) user).setName(clientNameField.getText());
            ((Client) user).setSurname(clientSurnameField.getText());
            ((Client) user).setAddress(clientAddressField.getText());
        } else if (user instanceof Driver driver) {
            driver.setName(driverNameField.getText());
            driver.setSurname(driverSurnameField.getText());
            driver.setDriverLicence(driverLicenceField.getText());
            driver.setVehiclePlateNumber(vehiclePlateField.getText());

            if (vehicleTypeCombo != null && vehicleTypeCombo.getValue() != null &&
                    !vehicleTypeCombo.getValue().isBlank()) {
                try {
                    driver.setVehicleType(VehicleType.valueOf(vehicleTypeCombo.getValue()));
                } catch (IllegalArgumentException e) {
                    errorLabel.setText("Invalid vehicle type");
                    return;
                }
            }
        } else if (user instanceof Restaurant restaurant) {
            restaurant.setDescription(restaurantDescField.getText());
            restaurant.setAddress(restaurantAddressField.getText());
            restaurant.setCuisineType(cuisineTypeField.getText());

            try {
                if (openingTimeField.getText() != null && !openingTimeField.getText().isBlank()) {
                    restaurant.setOpeningTime(LocalTime.parse(openingTimeField.getText().trim()));
                }
                if (closingTimeField.getText() != null && !closingTimeField.getText().isBlank()) {
                    restaurant.setClosingTime(LocalTime.parse(closingTimeField.getText().trim()));
                }
            } catch (Exception e) {
                errorLabel.setText("Time format must be HH:mm");
                return;
            }
        }

        try {
            customOperations.create(user);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("duplicate")) {
                errorLabel.setText("Username already exists");
            } else {
                errorLabel.setText("Registration failed: " + e.getMessage());
            }
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration");
        alert.setHeaderText("Registration successful!");
        alert.setContentText("You can now log in with your new account.");
        alert.showAndWait();

        onBackToLogin();
    }

    @FXML
    public void onBackToLogin() throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Hungry! - Login");
        stage.show();
    }
}
