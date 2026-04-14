package com.example.courseworkitfu.fxControllers.tabs.dishes;

import lombok.Getter;

@Getter
public class DishTableParameters {
    private final int id;
    private final String title;
    private final String description;
    private final double price;
    private final double weight;
    private final int calories;
    private final String category;
    private final boolean available;
    private final String restaurant;

    public DishTableParameters(int id, String title, String description, double price, double weight, int calories, String category, boolean available, String restaurant) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.calories = calories;
        this.category = category;
        this.available = available;
        this.restaurant = restaurant;
    }

}