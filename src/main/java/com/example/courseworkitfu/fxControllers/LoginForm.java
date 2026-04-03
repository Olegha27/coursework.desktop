package com.example.courseworkitfu.fxControllers;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Login failed");
                alert.setContentText("Wrong credentials");
                alert.showAndWait();
                return;
            }

            boolean desktopAllowed = user.isAdmin() || user instanceof Restaurant;
            if (!desktopAllowed) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Access denied");
                alert.setHeaderText("Desktop access is not allowed");
                alert.setContentText(
                        "Customers and drivers must use the web application.\n" +
                        "Desktop version is available only for administrators and restaurants."
                );
                alert.showAndWait();
                return;
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration");
        alert.setHeaderText("Registration unavailable in desktop app");
        alert.setContentText(
                "Customers and drivers must register via the web application.\n" +
                "Restaurant and administrator accounts should be created by the administrator."
        );
        alert.showAndWait();
    }
}
