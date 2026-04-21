package com.example.courseworkitfu.services;

import com.example.courseworkitfu.model.CartItemRow;
import com.example.courseworkitfu.model.DishCardRow;

import java.util.LinkedHashMap;
import java.util.Map;

public class CartService {
    private final Map<Integer, CartItemRow> cart;

    public CartService() {
        this.cart = new LinkedHashMap<>();
    }

    public void addToCart(DishCardRow dish) {
        CartItemRow existing = cart.get(dish.getId());

        if (existing == null) {
            cart.put(dish.getId(), new CartItemRow(dish.getId(), dish.getTitle(), dish.getPrice(), 1));
        } else {
            existing.setQuantity(existing.getQuantity() + 1);
        }
    }

    public void clearCart() {
        cart.clear();
    }

    public double getTotal() {
        double total = 0.0;
        for (CartItemRow item : cart.values()) {
            total += item.getTotal();
        }
        return total;
    }

    public Map<Integer, CartItemRow> getCartItems() {
        return new LinkedHashMap<>(cart);
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    public int getItemCount() {
        return cart.size();
    }
}