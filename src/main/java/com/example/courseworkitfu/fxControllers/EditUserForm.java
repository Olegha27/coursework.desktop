package com.example.courseworkitfu.fxControllers;

import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.hibernateOperations.JpaUtil;
import com.example.courseworkitfu.model.Client;
import com.example.courseworkitfu.model.Driver;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.model.VehicleType;
import com.example.courseworkitfu.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalTime;

public class EditUserForm {

    private final CustomOperations customOperations =
            new CustomOperations(JpaUtil.getEntityManagerFactory());

    private User editingUser;

    private final ToggleGroup roleGroup = new ToggleGroup();

    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField repeatPasswordField;

    @FXML public TextField emailField;
    @FXML public TextField phoneField;

    @FXML public RadioButton restaurantRadio;
    @FXML public RadioButton clientRadio;
    @FXML public RadioButton driverRadio;

    @FXML public Label errorLabel;

    @FXML public TextField clientNameField;
    @FXML public TextField clientSurnameField;
    @FXML public TextField cardNoField;
    @FXML public TextField clientAddressField;
    @FXML public DatePicker clientBirthDatePicker;

    @FXML public TextField driverNameField;
    @FXML public TextField driverSurnameField;
    @FXML public TextField driverLicenceField;
    @FXML public ComboBox<String> vehicleTypeCombo;
    @FXML public DatePicker driverBirthDatePicker;
    @FXML public TextField vehiclePlateField;

    @FXML public TextArea restaurantDescField;
    @FXML public TextField restaurantAddressField;
    @FXML public TextField cuisineTypeField;
    @FXML public TextField openingTimeField;
    @FXML public TextField closingTimeField;

    @FXML private TitledPane clientTitledPane;
    @FXML private TitledPane driverTitledPane;
    @FXML private TitledPane restaurantTitledPane;

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

    public void setUser(User user) {
        this.editingUser = user;

        if (user == null) return;

        usernameField.setText(user.getUsername());
        passwordField.setText("");
        repeatPasswordField.setText("");

        if (user.getEmail() != null) {
            emailField.setText(user.getEmail());
        }
        if (user.getPhoneNum() != null) {
            phoneField.setText(user.getPhoneNum());
        }

        if (user instanceof Client client) {
            clientRadio.setSelected(true);
            radioChanged();

            if (client.getName() != null) clientNameField.setText(client.getName());
            if (client.getSurname() != null) clientSurnameField.setText(client.getSurname());
            if (client.getCardNo() != null) cardNoField.setText(client.getCardNo());
            if (client.getAddress() != null) clientAddressField.setText(client.getAddress());
            if (client.getDateOfBirth() != null) clientBirthDatePicker.setValue(client.getDateOfBirth());

        } else if (user instanceof Driver driver) {
            driverRadio.setSelected(true);
            radioChanged();

            if (driver.getName() != null) driverNameField.setText(driver.getName());
            if (driver.getSurname() != null) driverSurnameField.setText(driver.getSurname());
            if (driver.getDriverLicence() != null) driverLicenceField.setText(driver.getDriverLicence());
            if (driver.getVehicleType() != null) vehicleTypeCombo.setValue(driver.getVehicleType().name());
            if (driver.getBirthDate() != null) driverBirthDatePicker.setValue(driver.getBirthDate());
            if (driver.getVehiclePlateNumber() != null) vehiclePlateField.setText(driver.getVehiclePlateNumber());

        } else if (user instanceof Restaurant restaurant) {
            restaurantRadio.setSelected(true);
            radioChanged();

            if (restaurant.getDescription() != null) restaurantDescField.setText(restaurant.getDescription());
            if (restaurant.getAddress() != null) restaurantAddressField.setText(restaurant.getAddress());
            if (restaurant.getCuisineType() != null) cuisineTypeField.setText(restaurant.getCuisineType());
            if (restaurant.getOpeningTime() != null) openingTimeField.setText(restaurant.getOpeningTime().toString());
            if (restaurant.getClosingTime() != null) closingTimeField.setText(restaurant.getClosingTime().toString());
        }

        if (clientRadio != null) clientRadio.setDisable(true);
        if (driverRadio != null) driverRadio.setDisable(true);
        if (restaurantRadio != null) restaurantRadio.setDisable(true);
    }

    @FXML
    public void radioChanged() {
        boolean client = clientRadio != null && clientRadio.isSelected();
        boolean driver = driverRadio != null && driverRadio.isSelected();
        boolean restaurant = restaurantRadio != null && restaurantRadio.isSelected();

        if (clientTitledPane != null) {
            clientTitledPane.setVisible(client);
            clientTitledPane.setManaged(client);
            clientTitledPane.setExpanded(client);
        }

        if (driverTitledPane != null) {
            driverTitledPane.setVisible(driver);
            driverTitledPane.setManaged(driver);
            driverTitledPane.setExpanded(driver);
        }

        if (restaurantTitledPane != null) {
            restaurantTitledPane.setVisible(restaurant);
            restaurantTitledPane.setManaged(restaurant);
            restaurantTitledPane.setExpanded(restaurant);
        }
    }

    @FXML
    public void saveUser() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }

        if (editingUser == null) {
            errorLabel.setText("User not found.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();

        if (username == null || username.trim().isEmpty()) {
            errorLabel.setText("Username required");
            return;
        }

        editingUser.setUsername(username.trim());
        editingUser.setEmail(emailField.getText());
        editingUser.setPhoneNum(phoneField.getText());

        boolean wantsToChangePassword =
                (password != null && !password.isBlank()) ||
                        (repeatPassword != null && !repeatPassword.isBlank());

        if (wantsToChangePassword) {
            if (password == null || password.length() < 3) {
                errorLabel.setText("Password too short");
                return;
            }

            if (!password.equals(repeatPassword)) {
                errorLabel.setText("Passwords do not match");
                return;
            }

            editingUser.setPassword(PasswordUtils.hashPassword(password));
        }

        if (editingUser instanceof Client client) {
            client.setName(clientNameField.getText());
            client.setSurname(clientSurnameField.getText());
            client.setCardNo(cardNoField.getText());
            client.setAddress(clientAddressField.getText());
            client.setDateOfBirth(clientBirthDatePicker.getValue());

        } else if (editingUser instanceof Driver driver) {
            driver.setName(driverNameField.getText());
            driver.setSurname(driverSurnameField.getText());
            driver.setDriverLicence(driverLicenceField.getText());
            driver.setBirthDate(driverBirthDatePicker.getValue());
            driver.setVehiclePlateNumber(vehiclePlateField.getText());

            if (vehicleTypeCombo.getValue() != null && !vehicleTypeCombo.getValue().isBlank()) {
                driver.setVehicleType(VehicleType.valueOf(vehicleTypeCombo.getValue()));
            }

        } else if (editingUser instanceof Restaurant restaurant) {
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
            customOperations.update(editingUser);
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Save failed");
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
