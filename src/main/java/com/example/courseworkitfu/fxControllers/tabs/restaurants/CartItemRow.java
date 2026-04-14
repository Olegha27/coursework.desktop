package com.example.courseworkitfu.fxControllers.tabs.restaurants;

import lombok.Setter;

public class CartItemRow {
    private final int dishId;
    private final String title;
    private final double price;
    @Setter
    private int quantity;

    public CartItemRow(int dishId, String title, double price, int quantity) {
        this.dishId = dishId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public int getDishId() {
        return dishId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return title + " x" + quantity + " — $" + String.format("%.2f", getTotal());
    }
}