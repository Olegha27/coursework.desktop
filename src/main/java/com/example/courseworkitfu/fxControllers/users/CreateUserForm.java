package com.example.courseworkitfu.fxControllers.users;

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

public class CreateUserForm {

    private final CustomOperations customOperations =
            new CustomOperations(JpaUtil.getEntityManagerFactory());

    private boolean createMode = true;
    private User editingUser;

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
    private void createAccount() {
        try {
            onRegister();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(boolean createMode, User user) {
        this.createMode = createMode;
        this.editingUser = user;

        if (createMode) {
            if (formTitleLabel != null) formTitleLabel.setText("Create User");
            if (saveButton != null) saveButton.setText("Create Account");
            if (cancelButton != null) cancelButton.setText("Back");
        } else {
            if (formTitleLabel != null) formTitleLabel.setText("Edit User");
            if (saveButton != null) saveButton.setText("Save Changes");
            if (cancelButton != null) cancelButton.setText("Cancel");
        }

        if (user == null) return;

        usernameField.setText(user.getUsername());
        passwordField.setText("");
        repeatPasswordField.setText("");

        if (user.getEmail() != null) emailField.setText(user.getEmail());
        if (user.getPhoneNum() != null) phoneField.setText(user.getPhoneNum());

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

        User user;

        if (createMode) {
            if (p1 == null || p1.length() < 3) {
                errorLabel.setText("Password too short");
                return;
            }
            if (!p1.equals(p2)) {
                errorLabel.setText("Passwords do not match");
                return;
            }

            if (clientRadio.isSelected()) {
                user = new Client();
            } else if (driverRadio.isSelected()) {
                user = new Driver();
            } else if (restaurantRadio.isSelected()) {
                user = new Restaurant();
            } else {
                user = new User();
            }
        } else {
            if (editingUser == null) {
                errorLabel.setText("User for editing not found");
                return;
            }
            user = editingUser;
        }

        user.setUsername(u.trim());
        user.setEmail(emailField.getText());
        user.setPhoneNum(phoneField.getText());

        if (createMode) {
            user.setPassword(PasswordUtils.hashPassword(p1));
        } else {
            boolean wantsToChangePassword =
                    p1 != null && !p1.isBlank() || p2 != null && !p2.isBlank();

            if (wantsToChangePassword) {
                if (p1 == null || p1.length() < 3) {
                    errorLabel.setText("Password too short");
                    return;
                }
                if (!p1.equals(p2)) {
                    errorLabel.setText("Passwords do not match");
                    return;
                }
                user.setPassword(PasswordUtils.hashPassword(p1));
            }
        }

        if (user instanceof Client client) {
            client.setName(clientNameField.getText());
            client.setSurname(clientSurnameField.getText());
            client.setCardNo(cardNoField.getText());
            client.setAddress(clientAddressField.getText());
            client.setDateOfBirth(clientBirthDatePicker.getValue());

        } else if (user instanceof Driver driver) {
            driver.setName(driverNameField.getText());
            driver.setSurname(driverSurnameField.getText());
            driver.setDriverLicence(driverLicenceField.getText());
            driver.setBirthDate(driverBirthDatePicker.getValue());
            driver.setVehiclePlateNumber(vehiclePlateField.getText());

            if (vehicleTypeCombo.getValue() != null && !vehicleTypeCombo.getValue().isBlank()) {
                driver.setVehicleType(VehicleType.valueOf(vehicleTypeCombo.getValue()));
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
            if (createMode) {
                customOperations.create(user);
            } else {
                customOperations.update(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Save failed");
            return;
        }

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onBackToLogin() throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();

        if (!createMode) {
            stage.close();
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Hungry!");
        stage.show();
    }
}
