package com.example.courseworkitfu.session;

import com.example.courseworkitfu.model.CartItemRow;
import com.example.courseworkitfu.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Session {

    private static User currentUser;
    private static Map<Integer, CartItemRow> pendingCart;

    private Session() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
        pendingCart = null;
    }

    public static Map<Integer, CartItemRow> getPendingCart() {
        return pendingCart;
    }

    public static void setPendingCart(Map<Integer, CartItemRow> cart) {
        pendingCart = cart;
    }

    public static boolean hasPendingCart() {
        return pendingCart != null && !pendingCart.isEmpty();
    }

    public static void clearPendingCart() {
        pendingCart = null;
    }
}