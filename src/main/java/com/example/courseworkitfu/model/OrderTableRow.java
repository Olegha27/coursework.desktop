package com.example.courseworkitfu.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderTableRow {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty buyer;
    private final SimpleStringProperty restaurant;
    private final SimpleStringProperty driver;
    private final SimpleStringProperty status;
    private final SimpleDoubleProperty totalPrice;

    public OrderTableRow(int id,
                         String buyer,
                         String restaurant,
                         String driver,
                         String status,
                         double totalPrice) {
        this.id = new SimpleIntegerProperty(id);
        this.buyer = new SimpleStringProperty(buyer);
        this.restaurant = new SimpleStringProperty(restaurant);
        this.driver = new SimpleStringProperty(driver);
        this.status = new SimpleStringProperty(status);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
    }

    public int getId() {
        return id.get();
    }

    public String getBuyer() {
        return buyer.get();
    }

    public String getRestaurant() {
        return restaurant.get();
    }

    public String getDriver() {
        return driver.get();
    }

    public String getStatus() {
        return status.get();
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }
}