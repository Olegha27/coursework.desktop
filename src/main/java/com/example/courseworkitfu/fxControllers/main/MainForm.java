package com.example.courseworkitfu.fxControllers.main;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainForm implements Initializable {

    public Label titleLabel;
    public Label currentUserLabel;
    public TabPane mainTabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert(
                    "Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\nPlease use the web application."
            );
            Platform.exit();
            return;
        }

        if (titleLabel != null) {
            titleLabel.setText("Coursework IT Food");
        }

        if (currentUserLabel != null) {
            if (currentUser != null) {
                currentUserLabel.setText(
                        "Logged in as: " + currentUser.getUsername()
                                + " (" + currentUser.getClass().getSimpleName() + ")"
                );
            } else {
                currentUserLabel.setText("Not authorized");
            }
        }
    }

    public void logout() {
        try {
            Session.setCurrentUser(null);

            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/com/example/courseworkitfu/auth/login-form.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to logout.");
        }
    }

    private boolean isDesktopAccessAllowed(User user) {
        if (user == null) {
            return false;
        }

        return user.isAdmin()
                || user.getClass().getSimpleName().equalsIgnoreCase("Restaurant");
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}