package com.example.courseworkitfu.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserTableParameters {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty email;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty dateCreated;
    private final SimpleStringProperty role;
    private final SimpleStringProperty active;

    public UserTableParameters(int id,
                               String username,
                               String email,
                               String phone,
                               String dateCreated,
                               String role,
                               String active) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.dateCreated = new SimpleStringProperty(dateCreated);
        this.role = new SimpleStringProperty(role);
        this.active = new SimpleStringProperty(active);
    }

    public int getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getActive() {
        return active.get();
    }
}