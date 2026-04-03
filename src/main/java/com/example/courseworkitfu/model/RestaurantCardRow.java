package com.example.courseworkitfu.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RestaurantCardRow {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty description;
    private final SimpleStringProperty address;
    private final SimpleStringProperty cuisineType;
    private final SimpleStringProperty rating;
    private final SimpleStringProperty imageUrl;

    public RestaurantCardRow(int id,
                             String title,
                             String description,
                             String address,
                             String cuisineType,
                             String rating,
                             String imageUrl) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.address = new SimpleStringProperty(address);
        this.cuisineType = new SimpleStringProperty(cuisineType);
        this.rating = new SimpleStringProperty(rating);
        this.imageUrl = new SimpleStringProperty(imageUrl);
    }

    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getCuisineType() {
        return cuisineType.get();
    }

    public String getRating() {
        return rating.get();
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    @Override
    public String toString() {
        return getTitle() + " | " + getCuisineType() + " | " + getAddress();
    }
}