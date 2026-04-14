package com.example.courseworkitfu.fxControllers.tabs.notifications;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Notification;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationsTabController implements Initializable {

    public ListView<NotificationRow> notificationList;
    public Label notificationsCountLabel;

    private CustomOperations customOperations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);
        initInteractions();
        refreshNotifications();
    }

    private void initInteractions() {
        if (notificationList != null) {
            notificationList.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    NotificationRow selected = notificationList.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        alert("Notification", selected.getFullText());
                    }
                }
            });
        }
    }

    public void refreshNotifications() {
        if (notificationList == null) {
            return;
        }

        notificationList.getItems().clear();

        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            updateCount();
            return;
        }

        for (Notification notification : customOperations.getAllRecords(Notification.class)) {
            if (notification.getUser() != null && notification.getUser().getId() == currentUser.getId()) {
                notificationList.getItems().add(new NotificationRow(
                        notification.getId(),
                        safe(notification.getMessage()),
                        notification.getCreatedAt() == null ? "" : notification.getCreatedAt().toString()
                ));
            }
        }

        updateCount();
    }

    public void deleteSelectedNotification() {
        NotificationRow selectedRow = notificationList == null ? null : notificationList.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Notification", "Select a notification first.");
            return;
        }

        Notification selectedNotification = null;
        for (Notification notification : customOperations.getAllRecords(Notification.class)) {
            if (notification.getId() == selectedRow.getId()) {
                selectedNotification = notification;
                break;
            }
        }

        if (selectedNotification == null) {
            alert("Notification", "Selected notification was not found.");
            return;
        }

        customOperations.delete(selectedNotification.getId(), Notification.class);
        refreshNotifications();
    }

    private void updateCount() {
        if (notificationsCountLabel != null && notificationList != null) {
            notificationsCountLabel.setText("Notifications: " + notificationList.getItems().size());
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}