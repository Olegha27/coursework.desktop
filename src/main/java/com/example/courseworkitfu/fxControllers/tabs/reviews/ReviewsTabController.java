package com.example.courseworkitfu.fxControllers.tabs.reviews;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Review;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewsTabController implements Initializable {

    public TextField reviewSearchField;
    public Label averageRatingLabel;
    public Label reviewsCountLabel;

    public TableView<ReviewTableParameters> reviewTable;
    public TableColumn<ReviewTableParameters, Number> idColumn;
    public TableColumn<ReviewTableParameters, String> reviewerColumn;
    public TableColumn<ReviewTableParameters, String> targetColumn;
    public TableColumn<ReviewTableParameters, String> titleColumn;
    public TableColumn<ReviewTableParameters, Number> ratingColumn;
    public TableColumn<ReviewTableParameters, String> textColumn;
    public TableColumn<ReviewTableParameters, String> reviewDateColumn;

    private CustomOperations customOperations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);
        initReviewTable();
        refreshReviews();
    }

    private void initReviewTable() {
        if (idColumn != null) {
            idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (reviewerColumn != null) {
            reviewerColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReviewer()));
        }
        if (targetColumn != null) {
            targetColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTarget()));
        }
        if (titleColumn != null) {
            titleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        }
        if (ratingColumn != null) {
            ratingColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRating()));
        }
        if (textColumn != null) {
            textColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getText()));
        }
        if (reviewDateColumn != null) {
            reviewDateColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReviewDate()));
        }
    }

    public void refreshReviews() {
        List<Review> reviews = customOperations.getAllRecords(Review.class);
        fillReviewTable(reviews);
        updateStats(reviews);
    }

    public void searchReviews() {
        String query = reviewSearchField == null ? "" : reviewSearchField.getText();
        if (query == null) {
            query = "";
        }

        String search = query.trim().toLowerCase();
        List<Review> matched = new ArrayList<>();

        for (Review review : customOperations.getAllRecords(Review.class)) {
            String reviewer = review.getReviewer() == null || review.getReviewer().getUsername() == null
                    ? ""
                    : review.getReviewer().getUsername().toLowerCase();

            String target = getReviewTarget(review).toLowerCase();
            String title = review.getTitle() == null ? "" : review.getTitle().toLowerCase();
            String text = review.getText() == null ? "" : review.getText().toLowerCase();
            String rating = String.valueOf(review.getRating());

            boolean matches =
                    reviewer.contains(search) ||
                            target.contains(search) ||
                            title.contains(search) ||
                            text.contains(search) ||
                            rating.equals(search);

            if (matches) {
                matched.add(review);
            }
        }

        fillReviewTable(matched);
        updateStats(matched);
    }

    public void resetReviewSearch() {
        if (reviewSearchField != null) {
            reviewSearchField.clear();
        }
        refreshReviews();
    }

    public void deleteSelectedReview() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\nPlease use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (currentUser == null || !currentUser.isAdmin()) {
            alert("Access denied", "Only admin can delete reviews.");
            return;
        }

        ReviewTableParameters selectedRow = reviewTable == null ? null : reviewTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            alert("Review", "Select a review first.");
            return;
        }

        Review selectedReview = null;
        for (Review review : customOperations.getAllRecords(Review.class)) {
            if (review.getId() == selectedRow.getId()) {
                selectedReview = review;
                break;
            }
        }

        if (selectedReview == null) {
            alert("Review", "Selected review was not found.");
            return;
        }

        customOperations.delete(selectedReview.getId(), Review.class);
        refreshReviews();
    }

    private void fillReviewTable(List<Review> reviews) {
        if (reviewTable == null) {
            return;
        }

        reviewTable.getItems().clear();

        for (Review review : reviews) {
            reviewTable.getItems().add(new ReviewTableParameters(
                    review.getId(),
                    review.getReviewer() == null ? "" : safe(review.getReviewer().getUsername()),
                    getReviewTarget(review),
                    safe(review.getTitle()),
                    review.getRating(),
                    safe(review.getText()),
                    review.getReviewDate() == null ? "" : review.getReviewDate().toString()
            ));
        }
    }

    private void updateStats(List<Review> reviews) {
        int count = reviews.size();
        double avg = 0.0;

        if (count > 0) {
            int sum = 0;
            for (Review review : reviews) {
                sum += review.getRating();
            }
            avg = (double) sum / count;
        }

        if (reviewsCountLabel != null) {
            reviewsCountLabel.setText("Reviews: " + count);
        }

        if (averageRatingLabel != null) {
            averageRatingLabel.setText("Average rating: " + String.format("%.2f", avg));
        }
    }

    private String getReviewTarget(Review review) {
        if (review.getReviewedRestaurant() != null) {
            String username = review.getReviewedRestaurant().getUsername();
            return username == null ? "Restaurant" : "Restaurant: " + username;
        }

        if (review.getReviewedDriver() != null) {
            String username = review.getReviewedDriver().getUsername();
            return username == null ? "Driver" : "Driver: " + username;
        }

        if (review.getReviewedClient() != null) {
            String username = review.getReviewedClient().getUsername();
            return username == null ? "Client" : "Client: " + username;
        }

        return "";
    }

    private boolean isDesktopAccessAllowed(User user) {
        if (user == null) return false;
        return user.isAdmin() || user.getClass().getSimpleName().equalsIgnoreCase("Restaurant");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}