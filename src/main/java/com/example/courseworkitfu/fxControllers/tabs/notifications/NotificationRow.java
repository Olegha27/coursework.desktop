package com.example.courseworkitfu.fxControllers.tabs.notifications;

public class NotificationRow {
    private final int id;
    private final String text;
    private final String createdAt;

    public NotificationRow(int id, String text, String createdAt) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFullText() {
        if (createdAt == null || createdAt.isBlank()) {
            return text;
        }
        return text + "\n\nCreated at: " + createdAt;
    }

    @Override
    public String toString() {
        if (createdAt == null || createdAt.isBlank()) {
            return text;
        }
        return text + "\n" + createdAt;
    }
}