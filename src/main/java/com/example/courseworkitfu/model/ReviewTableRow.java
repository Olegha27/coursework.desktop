package com.example.courseworkitfu.model;

import javafx.beans.property.*;

public class ReviewTableRow {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty reviewerName;
    private final SimpleStringProperty restaurantName;
    private final SimpleStringProperty driverName;
    private final SimpleStringProperty clientName;
    private final SimpleIntegerProperty rating;
    private final SimpleStringProperty title;
    private final SimpleStringProperty text;
    private final SimpleStringProperty reviewDate;
    private final SimpleStringProperty reviewType;

    public ReviewTableRow(Review review) {
        this.id = new SimpleIntegerProperty(review.getId());
        this.reviewerName = new SimpleStringProperty(review.getReviewer() != null ? review.getReviewer().getUsername() : "Unknown");
        this.restaurantName = new SimpleStringProperty(review.getReviewedRestaurant() != null ? review.getReviewedRestaurant().getUsername() : "");
        this.driverName = new SimpleStringProperty(review.getReviewedDriver() != null ? review.getReviewedDriver().getUsername() : "");
        this.clientName = new SimpleStringProperty(review.getReviewedClient() != null ? review.getReviewedClient().getUsername() : "");
        this.rating = new SimpleIntegerProperty(review.getRating());
        this.title = new SimpleStringProperty(review.getTitle() != null ? review.getTitle() : "");
        this.text = new SimpleStringProperty(review.getText() != null ? review.getText() : "");
        this.reviewDate = new SimpleStringProperty(review.getReviewDate() != null ? review.getReviewDate().toString() : "");
        this.reviewType = new SimpleStringProperty(getReviewType(review));
    }

    private String getReviewType(Review review) {
        if (review.getReviewedRestaurant() != null) return "Restaurant";
        if (review.getReviewedDriver() != null) return "Driver";
        if (review.getReviewedClient() != null) return "Client";
        return "Unknown";
    }

    // Getters for table columns
    public Integer getId() { return id.get(); }
    public String getReviewerName() { return reviewerName.get(); }
    public String getRestaurantName() { return restaurantName.get(); }
    public String getDriverName() { return driverName.get(); }
    public String getClientName() { return clientName.get(); }
    public Integer getRating() { return rating.get(); }
    public String getTitle() { return title.get(); }
    public String getText() { return text.get(); }
    public String getReviewDate() { return reviewDate.get(); }
    public String getReviewType() { return reviewType.get(); }

    // Table columns use these
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty reviewerNameProperty() { return reviewerName; }
    public SimpleStringProperty restaurantNameProperty() { return restaurantName; }
    public SimpleStringProperty driverNameProperty() { return driverName; }
    public SimpleStringProperty clientNameProperty() { return clientName; }
    public SimpleIntegerProperty ratingProperty() { return rating; }
    public SimpleStringProperty titleProperty() { return title; }
    public SimpleStringProperty textProperty() { return text; }
    public SimpleStringProperty reviewDateProperty() { return reviewDate; }
    public SimpleStringProperty reviewTypeProperty() { return reviewType; }
}
