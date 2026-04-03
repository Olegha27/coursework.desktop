package com.example.courseworkitfu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItem {
    private Dish dish;
    private int quantity;

    public float getLineTotal() {
        return dish.getPrice() * quantity;
    }
}