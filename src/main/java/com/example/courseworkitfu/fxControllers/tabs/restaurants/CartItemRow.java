package com.example.courseworkitfu.fxControllers.tabs.restaurants;

public class CartItemRow {
    private final int dishId;
    private final String title;
    private final double price;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return title + " x" + quantity + " — $" + String.format("%.2f", getTotal());
    }
}