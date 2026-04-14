package com.example.courseworkitfu.fxControllers.tabs.restaurants;

import lombok.Getter;

@Getter
public class RestaurantCardRow {
    private final int id;
    private final String title;
    private final String description;
    private final String address;
    private final String cuisineType;
    private final String rating;
    private final String imageUrl;

    public RestaurantCardRow(int id, String title, String description, String address, String cuisineType, String rating, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        String cuisine = cuisineType == null || cuisineType.isBlank() ? "Cuisine not specified" : cuisineType;
        String addressText = address == null || address.isBlank() ? "Address not specified" : address;
        String ratingText = rating == null || rating.isBlank() ? "-" : rating;

        return title + "\n"
                + cuisine + "\n"
                + addressText + "\n"
                + "Rating: " + ratingText;
    }
}