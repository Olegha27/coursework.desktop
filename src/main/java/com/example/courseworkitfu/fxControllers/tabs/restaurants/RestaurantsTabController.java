package com.example.courseworkitfu.fxControllers.tabs.restaurants;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class RestaurantsTabController implements Initializable {

    public ListView<RestaurantCardRow> restaurantList;
    public TilePane dishTilePane;
    public ListView<CartItemRow> cartList;
    public Label selectedRestaurantLabel;
    public Label cartTotalLabel;
    public TextArea specialInstructionsArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedRestaurantLabel != null) {
            selectedRestaurantLabel.setText("Restaurants tab loaded");
        }

        if (cartTotalLabel != null) {
            cartTotalLabel.setText("Total: $0.00");
        }
    }

    public void clearCart() {
    }

    public void placeOrder() {
    }
}