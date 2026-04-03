package com.example.courseworkitfu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Restaurant")
public final class Restaurant extends User{
 private String description;
 private String address;
 private LocalDateTime happyHours;
 private String cuisineType;
 private double rating;
 private boolean isOpen;
 private LocalTime openingTime;
 private LocalTime closingTime;
 @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
 private List<Dish> menu;

 public String getDescription() { return description; }
 public void setDescription(String description) { this.description = description; }
 public String getAddress() { return address; }
 public void setAddress(String address) { this.address = address; }
 public LocalDateTime getHappyHours() { return happyHours; }
 public void setHappyHours(LocalDateTime happyHours) { this.happyHours = happyHours; }
 public String getCuisineType() { return cuisineType; }
 public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
 public double getRating() { return rating; }
 public void setRating(double rating) { this.rating = rating; }
 public boolean isOpen() { return isOpen; }
 public void setOpen(boolean open) { isOpen = open; }
 public LocalTime getOpeningTime() { return openingTime; }
 public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }
 public LocalTime getClosingTime() { return closingTime; }
 public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }
 public List<Dish> getMenu() { return menu; }
 public void setMenu(List<Dish> menu) { this.menu = menu; }

 public Restaurant(String username, String password, String phoneNum, String description, String address, LocalDateTime happyHours) {
 super(username, password, phoneNum);
 this.description = description;
 this.address = address;
 this.happyHours = happyHours;
 this.isOpen = true;
 }

 public Restaurant(String username, String password, String phoneNum, String description, String address, LocalDateTime happyHours, String cuisineType, LocalTime openingTime, LocalTime closingTime) {
 super(username, password, phoneNum);
 this.description = description;
 this.address = address;
 this.happyHours = happyHours;
 this.cuisineType = cuisineType;
 this.openingTime = openingTime;
 this.closingTime = closingTime;
 this.isOpen = true;
 }
}
