package com.example.courseworkitfu.services;

import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.Dish;
import com.example.courseworkitfu.model.Restaurant;
import com.example.courseworkitfu.model.DishCardRow;
import com.example.courseworkitfu.model.RestaurantCardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantService {
    private final CustomOperations customOperations;
    private Restaurant selectedRestaurant;

    public RestaurantService(CustomOperations customOperations) {
        this.customOperations = customOperations;
    }

    public List<RestaurantCardRow> loadAllRestaurants() {
        List<RestaurantCardRow> restaurants = new ArrayList<>();

        for (Restaurant restaurant : customOperations.getAllRecords(Restaurant.class)) {
            restaurants.add(new RestaurantCardRow(
                restaurant.getId(),
                restaurant.getUsername(),
                restaurant.getDescription() == null ? "" : restaurant.getDescription(),
                restaurant.getAddress() == null ? "" : restaurant.getAddress(),
                restaurant.getCuisineType() == null ? "" : restaurant.getCuisineType(),
                String.valueOf(restaurant.getRating()),
                ""
            ));
        }

        return restaurants;
    }

    public List<DishCardRow> loadRestaurantDishes(Integer restaurantId) {
        List<DishCardRow> dishes = new ArrayList<>();

        if (restaurantId == null) {
            return dishes;
        }

        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            if (dish.getRestaurant() != null && dish.getRestaurant().getId() == restaurantId) {
                if (!dish.isAvailable()) continue;

                dishes.add(new DishCardRow(
                    dish.getId(),
                    dish.getTitle(),
                    dish.getCategory() == null ? "" : dish.getCategory(),
                    dish.getDescription() == null ? "" : dish.getDescription(),
                    dish.getPrice(),
                    dish.getWeight(),
                    dish.getCalories(),
                    dish.isAvailable(),
                    dish.getImageUrl() == null ? "" : dish.getImageUrl()
                ));
            }
        }

        return dishes;
    }

    public void setSelectedRestaurant(Restaurant restaurant) {
        this.selectedRestaurant = restaurant;
    }

    public Restaurant getSelectedRestaurant() {
        return selectedRestaurant;
    }

    public Restaurant findRestaurantById(Integer id) {
        if (id == null) return null;

        for (Restaurant restaurant : customOperations.getAllRecords(Restaurant.class)) {
            if (Objects.equals(restaurant.getId(), id)) {
                return restaurant;
            }
        }

        return null;
    }

    public Dish findDishById(Integer id) {
        if (id == null) return null;

        for (Dish dish : customOperations.getAllRecords(Dish.class)) {
            if (Objects.equals(dish.getId(),id)) {
                return dish;
            }
        }

        return null;
    }
}