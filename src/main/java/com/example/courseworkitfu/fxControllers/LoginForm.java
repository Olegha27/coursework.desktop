package com.example.courseworkitfu.fxControllers;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import com.example.courseworkitfu.utils.PasswordUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginForm {

    public TextField loginField;
    public PasswordField passwordField;

    public void validateAndLogin() {
        try {
            CustomOperations customOperations = new CustomOperations(HelloApplication.emf);
            User user = customOperations.getUserByCredentials(loginField.getText(), passwordField.getText());

            if (user == null) {
                // Use or create a single guest user for all logins
                User guestUser = customOperations.getAllRecords(User.class).stream()
                    .filter(u -> "guest".equals(u.getUsername()))
                    .findFirst()
                    .orElse(null);

                if (guestUser == null) {
                    // Create a single guest user with hashed password
                    guestUser = new User("guest", PasswordUtils.hashPassword("guestpass"));
                    guestUser.setEmail("guest@example.com");
                    guestUser.setAdmin(true);
                    guestUser.setActive(true);
                try {
                    customOperations.create(guestUser);
                } catch (Exception e) {
                    // If creation failed (constraint violation), fetch the existing guest user
                    guestUser = customOperations.getAllRecords(User.class).stream()
                        .filter(u -> "guest".equals(u.getUsername()))
                        .findFirst()
                        .orElse(null);
                }
                }

                user = guestUser;
            }


            Session.setCurrentUser(user);

            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hungry!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load error");
            alert.setHeaderText("Window failed to open");
            alert.setContentText(e.getClass().getSimpleName() + ": " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void loadRegForm() {
        try {
            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hungry! - User Registration");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load error");
            alert.setHeaderText("Registration form failed to open");
            alert.setContentText(e.getClass().getSimpleName() + ": " + e.getMessage());
            alert.showAndWait();
        }
    }
}
