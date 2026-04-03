package com.example.courseworkitfu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Dish {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 private int calories;

 private String category;

 private String description;

 private String imageUrl;

 private boolean isAvailable;

 private int preparationTimeMin;

 private float price;

 private String title;

 private float weight;

 @ManyToOne
 @JoinColumn(name = "restaurant_id")
 private Restaurant restaurant;

 public int getId() { return id; }
 public void setId(int id) { this.id = id; }
 public int getCalories() { return calories; }
 public void setCalories(int calories) { this.calories = calories; }
 public String getCategory() { return category; }
 public void setCategory(String category) { this.category = category; }
 public String getDescription() { return description; }
 public void setDescription(String description) { this.description = description; }
 public String getImageUrl() { return imageUrl; }
 public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
 public boolean isAvailable() { return isAvailable; }
 public void setAvailable(boolean available) { isAvailable = available; }
 public int getPreparationTimeMin() { return preparationTimeMin; }
 public void setPreparationTimeMin(int preparationTimeMin) { this.preparationTimeMin = preparationTimeMin; }
 public float getPrice() { return price; }
 public void setPrice(float price) { this.price = price; }
 public String getTitle() { return title; }
 public void setTitle(String title) { this.title = title; }
 public float getWeight() { return weight; }
 public void setWeight(float weight) { this.weight = weight; }
 public Restaurant getRestaurant() { return restaurant; }
 public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

}
