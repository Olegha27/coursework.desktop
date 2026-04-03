package com.example.courseworkitfu.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DishCardRow {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty category;
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty weight;
    private final SimpleIntegerProperty calories;
    private final SimpleBooleanProperty available;
    private final SimpleStringProperty imageUrl;

    public DishCardRow(int id,
                       String title,
                       String category,
                       String description,
                       double price,
                       double weight,
                       int calories,
                       boolean available,
                       String imageUrl) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.weight = new SimpleDoubleProperty(weight);
        this.calories = new SimpleIntegerProperty(calories);
        this.available = new SimpleBooleanProperty(available);
        this.imageUrl = new SimpleStringProperty(imageUrl);
    }

    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getDescription() {
        return description.get();
    }

    public double getPrice() {
        return price.get();
    }

    public double getWeight() {
        return weight.get();
    }

    public int getCalories() {
        return calories.get();
    }

    public boolean isAvailable() {
        return available.get();
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    @Override
    public String toString() {
        return getTitle() + " | $" + getPrice() + " | " + getWeight() + "g";
    }
}