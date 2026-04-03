package com.example.courseworkitfu.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {

    public Admin() {
        this("", "", "", "");
    }

    public Admin(String username, String password, String phoneNum) {
        super(username, password, phoneNum);
    }

    public Admin(String username, String password, String phoneNum, String email) {
        super(username, password, phoneNum, email);
    }

    @Override
    public String renderUserData() {
        return "Administrator: " + username;
    }
}
