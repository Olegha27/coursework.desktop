package com.example.courseworkitfu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 private LocalDateTime createdAt;
 private LocalDateTime deliveredAt;

 @ManyToOne
 private Client buyer;

 @ManyToOne
 private Driver deliveryPerson;

 @ManyToOne
 private Restaurant restaurant;

 @Enumerated(EnumType.STRING)
 private Cuisine cuisine;

 @Enumerated(EnumType.STRING)
 private OrderStatus status;

 private double totalPrice;
 private double deliveryFee;
 private String paymentMethod;
 private int estimatedDeliveryMin;
 private String specialInstructions;

 @ManyToMany
 private List<Dish> items;

 public int getId() { return id; }
 public void setId(int id) { this.id = id; }
 public LocalDateTime getCreatedAt() { return createdAt; }
 public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
 public LocalDateTime getDeliveredAt() { return deliveredAt; }
 public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
 public Client getBuyer() { return buyer; }
 public void setBuyer(Client buyer) { this.buyer = buyer; }
 public Driver getDeliveryPerson() { return deliveryPerson; }
 public void setDeliveryPerson(Driver deliveryPerson) { this.deliveryPerson = deliveryPerson; }
 public Restaurant getRestaurant() { return restaurant; }
 public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
 public Cuisine getCuisine() { return cuisine; }
 public void setCuisine(Cuisine cuisine) { this.cuisine = cuisine; }
 public OrderStatus getStatus() { return status; }
 public void setStatus(OrderStatus status) { this.status = status; }
 public double getTotalPrice() { return totalPrice; }
 public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
 public double getDeliveryFee() { return deliveryFee; }
 public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
 public String getPaymentMethod() { return paymentMethod; }
 public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
 public int getEstimatedDeliveryMin() { return estimatedDeliveryMin; }
 public void setEstimatedDeliveryMin(int estimatedDeliveryMin) { this.estimatedDeliveryMin = estimatedDeliveryMin; }
 public String getSpecialInstructions() { return specialInstructions; }
 public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
 public List<Dish> getItems() { return items; }
 public void setItems(List<Dish> items) { this.items = items; }

}
