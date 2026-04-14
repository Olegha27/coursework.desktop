package com.example.courseworkitfu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
@NoArgsConstructor
@Getter
@Setter
public class Review {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 private String title;
 private String text;
 private int rating;
 private LocalDateTime reviewDate;

 @ManyToOne
 private User reviewer;

 @ManyToOne
 private Restaurant reviewedRestaurant;

 @ManyToOne
 private Driver reviewedDriver;

 @ManyToOne
 private Client reviewedClient;

 public int getId() { return id; }
 public void setId(int id) { this.id = id; }
 public String getTitle() { return title; }
 public void setTitle(String title) { this.title = title; }
 public String getText() { return text; }
 public void setText(String text) { this.text = text; }
 public int getRating() { return rating; }
 public void setRating(int rating) { this.rating = rating; }
 public LocalDateTime getReviewDate() { return reviewDate; }
 public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }
 public User getReviewer() { return reviewer; }
 public void setReviewer(User reviewer) { this.reviewer = reviewer; }
 public Restaurant getReviewedRestaurant() { return reviewedRestaurant; }
 public void setReviewedRestaurant(Restaurant reviewedRestaurant) { this.reviewedRestaurant = reviewedRestaurant; }
 public Driver getReviewedDriver() { return reviewedDriver; }
 public void setReviewedDriver(Driver reviewedDriver) { this.reviewedDriver = reviewedDriver; }
 public Client getReviewedClient() { return reviewedClient; }
 public void setReviewedClient(Client reviewedClient) { this.reviewedClient = reviewedClient; }

 @Override
 public String toString() {
 return "Review{" +
 "id=" + id +
 ", rating=" + rating +
 ", title='" + title + '\'' +
 ", reviewer=" + (reviewer != null ? reviewer.getUsername() : "null") +
 '}';
 }

}
