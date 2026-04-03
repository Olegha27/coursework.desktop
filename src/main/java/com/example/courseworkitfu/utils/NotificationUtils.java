package com.example.courseworkitfu.utils;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationUtils {

    public static void showInfo(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .showInformation();
    }

    public static void showSuccess(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .showConfirm();
    }

    public static void showWarning(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .showWarning();
    }

    public static void showError(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .showError();
    }
}