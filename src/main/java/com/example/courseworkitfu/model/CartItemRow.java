package com.example.courseworkitfu.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CartItemRow {

    private final SimpleIntegerProperty dishId;
    private final SimpleStringProperty title;
    private final SimpleDoubleProperty unitPrice;
    private final SimpleIntegerProperty quantity;

    public CartItemRow(int dishId, String title, double unitPrice, int quantity) {
        this.dishId = new SimpleIntegerProperty(dishId);
        this.title = new SimpleStringProperty(title);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getDishId() {
        return dishId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int q) {
        quantity.set(q);
    }

    public double getTotal() {
        return getUnitPrice() * getQuantity();
    }

    @Override
    public String toString() {
        return getTitle() + " x" + getQuantity() + " = $" + String.format("%.2f", getTotal());
    }
}