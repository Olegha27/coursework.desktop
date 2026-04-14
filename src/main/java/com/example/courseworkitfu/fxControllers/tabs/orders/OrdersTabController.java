package com.example.courseworkitfu.fxControllers.tabs.orders;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.FoodOrder;
import com.example.courseworkitfu.model.Notification;
import com.example.courseworkitfu.model.OrderStatus;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersTabController implements Initializable {

    public TextField orderSearchField;

    public TableView<OrderTableParameters> orderTable;
    public TableColumn<OrderTableParameters, Number> idColumn;
    public TableColumn<OrderTableParameters, String> clientColumn;
    public TableColumn<OrderTableParameters, String> restaurantColumn;
    public TableColumn<OrderTableParameters, String> driverColumn;
    public TableColumn<OrderTableParameters, String> statusColumn;
    public TableColumn<OrderTableParameters, Number> totalPriceColumn;
    public TableColumn<OrderTableParameters, String> createdAtColumn;
    public TableColumn<OrderTableParameters, String> paymentMethodColumn;

    public ListView<OrderMessageRow> orderMessagesList;
    public TextArea newMessageArea;

    private CustomOperations customOperations;
    private FoodOrder selectedOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);
        initOrderTable();
        initInteractions();
        refreshOrders();
    }

    private void initOrderTable() {
        if (idColumn != null) {
            idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (clientColumn != null) {
            clientColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getClient()));
        }
        if (restaurantColumn != null) {
            restaurantColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRestaurant()));
        }
        if (driverColumn != null) {
            driverColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDriver()));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        }
        if (totalPriceColumn != null) {
            totalPriceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getTotalPrice()));
        }
        if (createdAtColumn != null) {
            createdAtColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCreatedAt()));
        }
        if (paymentMethodColumn != null) {
            paymentMethodColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPaymentMethod()));
        }
    }

    private void initInteractions() {
        if (orderTable != null) {
            orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) {
                    selectedOrder = null;
                    clearMessages();
                    return;
                }

                selectedOrder = findOrderById(newVal.getId());
                loadOrderMessages();
            });
        }

        if (orderSearchField != null) {
            orderSearchField.setOnAction(e -> searchOrders());
        }
    }

    public void refreshOrders() {
        if (orderTable == null) {
            return;
        }

        orderTable.getItems().clear();

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            orderTable.getItems().add(new OrderTableParameters(
                    order.getId(),
                    getUsername(safeInvoke(order, "getBuyer")),
                    getUsername(safeInvoke(order, "getRestaurant")),
                    getUsername(safeInvoke(order, "getDriver")),
                    order.getStatus() == null ? "" : order.getStatus().name(),
                    getDouble(order, "getTotalPrice"),
                    getString(order, "getCreatedAt"),
                    getString(order, "getPaymentMethod")
            ));
        }

        if (selectedOrder != null) {
            selectedOrder = findOrderById(selectedOrder.getId());
            loadOrderMessages();
        }
    }

    public void searchOrders() {
        if (orderTable == null) {
            return;
        }

        String query = orderSearchField == null ? "" : orderSearchField.getText();
        if (query == null) {
            query = "";
        }

        String search = query.trim().toLowerCase();
        orderTable.getItems().clear();

        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            String id = String.valueOf(order.getId());
            String client = getUsername(safeInvoke(order, "getBuyer")).toLowerCase();
            String restaurant = getUsername(safeInvoke(order, "getRestaurant")).toLowerCase();
            String driver = getUsername(safeInvoke(order, "getDriver")).toLowerCase();
            String status = order.getStatus() == null ? "" : order.getStatus().name().toLowerCase();

            boolean matches =
                    id.equals(search) ||
                            client.contains(search) ||
                            restaurant.contains(search) ||
                            driver.contains(search) ||
                            status.equals(search);

            if (matches) {
                orderTable.getItems().add(new OrderTableParameters(
                        order.getId(),
                        getUsername(safeInvoke(order, "getBuyer")),
                        getUsername(safeInvoke(order, "getRestaurant")),
                        getUsername(safeInvoke(order, "getDriver")),
                        order.getStatus() == null ? "" : order.getStatus().name(),
                        getDouble(order, "getTotalPrice"),
                        getString(order, "getCreatedAt"),
                        getString(order, "getPaymentMethod")
                ));
            }
        }
    }

    public void resetOrderSearch() {
        if (orderSearchField != null) {
            orderSearchField.clear();
        }
        refreshOrders();
    }

    public void takeOrder() {
        updateOrderStatus(OrderStatus.CONFIRMED, "Order confirmed.");
    }

    public void confirmOrder() {
        updateOrderStatus(OrderStatus.CONFIRMED, "Order confirmed.");
    }

    public void prepareOrder() {
        updateOrderStatus(OrderStatus.PREPARING, "Order is now preparing.");
    }

    public void readyForPickupOrder() {
        updateOrderStatus(OrderStatus.READY_FOR_PICKUP, "Order is ready for pickup.");
    }

    public void completeOrder() {
        updateOrderStatus(OrderStatus.DELIVERED, "Order completed.");
    }

    public void cancelOrder() {
        updateOrderStatus(OrderStatus.CANCELLED, "Order cancelled.");
    }

    public void sendMessage() {
        if (selectedOrder == null) {
            alert("Order", "Select an order first.");
            return;
        }

        if (newMessageArea == null || newMessageArea.getText() == null || newMessageArea.getText().isBlank()) {
            alert("Message", "Enter a message first.");
            return;
        }

        String text = newMessageArea.getText().trim();

        // Если у тебя есть отдельная entity типа OrderMessage / Message / ChatMessage,
        // сюда нужно будет вставить create(...) этой entity.
        // Пока делаю безопасный fallback: создаётся notification получателям заказа.

        Object buyer = safeInvoke(selectedOrder, "getBuyer");
        Object restaurant = safeInvoke(selectedOrder, "getRestaurant");
        Object driver = safeInvoke(selectedOrder, "getDriver");
        User currentUser = Session.getCurrentUser();

        if (buyer instanceof User user && currentUser != null && user.getId() != currentUser.getId()) {
            customOperations.create(new Notification("Order #" + selectedOrder.getId() + ": " + text, user));
        }
        if (restaurant instanceof User user && currentUser != null && user.getId() != currentUser.getId()) {
            customOperations.create(new Notification("Order #" + selectedOrder.getId() + ": " + text, user));
        }
        if (driver instanceof User user && currentUser != null && user.getId() != currentUser.getId()) {
            customOperations.create(new Notification("Order #" + selectedOrder.getId() + ": " + text, user));
        }

        if (orderMessagesList != null) {
            orderMessagesList.getItems().add(new OrderMessageRow(
                    -1,
                    currentUser == null ? "System" : currentUser.getUsername(),
                    text,
                    java.time.LocalDateTime.now().toString()
            ));
        }

        newMessageArea.clear();
    }

    private void updateOrderStatus(OrderStatus newStatus, String successText) {
        if (selectedOrder == null) {
            alert("Order", "Select an order first.");
            return;
        }

        try {
            selectedOrder.setStatus(newStatus);
            customOperations.update(selectedOrder);

            notifyOrderParticipants(selectedOrder, successText);

            refreshOrders();
            alert("Success", successText);
        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to update order status.");
        }
    }

    private void notifyOrderParticipants(FoodOrder order, String text) {
        Object buyer = safeInvoke(order, "getBuyer");
        Object restaurant = safeInvoke(order, "getRestaurant");
        Object driver = safeInvoke(order, "getDriver");

        if (buyer instanceof User user) {
            customOperations.create(new Notification("Order #" + order.getId() + ": " + text, user));
        }
        if (restaurant instanceof User user) {
            customOperations.create(new Notification("Order #" + order.getId() + ": " + text, user));
        }
        if (driver instanceof User user) {
            customOperations.create(new Notification("Order #" + order.getId() + ": " + text, user));
        }
    }

    private void loadOrderMessages() {
        clearMessages();

        if (selectedOrder == null || orderMessagesList == null) {
            return;
        }

        // Если у тебя есть отдельная таблица сообщений заказа, сюда надо загрузку из entity.
        // Пока загружаю системную строку как заглушку, чтобы UI не был пустым.

        orderMessagesList.getItems().add(new OrderMessageRow(
                0,
                "System",
                "Order #" + selectedOrder.getId() + " selected.",
                getString(selectedOrder, "getCreatedAt")
        ));
    }

    private void clearMessages() {
        if (orderMessagesList != null) {
            orderMessagesList.getItems().clear();
        }
    }

    private FoodOrder findOrderById(int id) {
        for (FoodOrder order : customOperations.getAllRecords(FoodOrder.class)) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    private Object safeInvoke(Object target, String methodName) {
        if (target == null) return null;

        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (Exception e) {
            return null;
        }
    }

    private String getUsername(Object userLike) {
        if (userLike == null) return "";
        try {
            Method method = userLike.getClass().getMethod("getUsername");
            Object value = method.invoke(userLike);
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String getString(Object target, String methodName) {
        Object value = safeInvoke(target, methodName);
        return value == null ? "" : value.toString();
    }

    private double getDouble(Object target, String methodName) {
        Object value = safeInvoke(target, methodName);
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        return 0.0;
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}