package com.example.courseworkitfu.services;

import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.CartItemRow;
import com.example.courseworkitfu.model.Client;
import com.example.courseworkitfu.model.Dish;
import com.example.courseworkitfu.model.FoodOrder;
import com.example.courseworkitfu.model.OrderStatus;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class OrderService {
    private final CustomOperations customOperations;

    public OrderService(CustomOperations customOperations) {
        this.customOperations = customOperations;
    }

    public FoodOrder createOrder(Client client, Restaurant restaurant, String specialInstructions, Map<Integer, CartItemRow> cart) {
        FoodOrder order = new FoodOrder();
        order.setBuyer(client);
        order.setRestaurant(restaurant);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setSpecialInstructions(specialInstructions);

        double total = 0.0;
        ArrayList<Dish> items = new ArrayList<>();

        for (CartItemRow item : cart.values()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                for (Dish dish : customOperations.getAllRecords(Dish.class)) {
                    if (dish.getId() == item.getDishId()) {
                        items.add(dish);
                        total += dish.getPrice();
                    }
                }
            }
        }

        order.setItems(items);
        order.setTotalPrice(total);
        order.setDeliveryFee(2.99);
        order.setEstimatedDeliveryMin(30);
        order.setPaymentMethod("CARD");

        customOperations.create(order);

        return order;
    }

    public void createNotification(User recipient, String message) {
        com.example.courseworkitfu.model.Notification notification = new com.example.courseworkitfu.model.Notification();
        notification.setUser(recipient);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        customOperations.create(notification);
    }
}