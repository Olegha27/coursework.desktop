package com.example.courseworkitfu.fxControllers.tabs.restaurants;

public class DishCardRow {
    private final int id;
    private final String title;
    private final String category;
    private final String description;
    private final double price;
    private final double weight;
    private final int calories;
    private final boolean available;
    private final String imageUrl;

    public DishCardRow(int id,
                       String title,
                       String category,
                       String description,
                       double price,
                       double weight,
                       int calories,
                       boolean available,
                       String imageUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.calories = calories;
        this.available = available;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}