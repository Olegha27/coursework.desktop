package com.example.courseworkitfu.fxControllers.tabs.orders;

public class OrderTableParameters {
    private final int id;
    private final String client;
    private final String restaurant;
    private final String driver;
    private final String status;
    private final double totalPrice;
    private final String createdAt;
    private final String paymentMethod;

    public OrderTableParameters(int id,
                                String client,
                                String restaurant,
                                String driver,
                                String status,
                                double totalPrice,
                                String createdAt,
                                String paymentMethod) {
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.driver = driver;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getDriver() {
        return driver;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}