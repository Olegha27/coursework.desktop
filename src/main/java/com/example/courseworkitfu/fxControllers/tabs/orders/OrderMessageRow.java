package com.example.courseworkitfu.fxControllers.tabs.orders;

public class OrderMessageRow {
    private final int id;
    private final String sender;
    private final String message;
    private final String createdAt;

    public OrderMessageRow(int id, String sender, String message, String createdAt) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        String time = createdAt == null || createdAt.isBlank() ? "" : " [" + createdAt + "]";
        return sender + time + "\n" + message;
    }
}