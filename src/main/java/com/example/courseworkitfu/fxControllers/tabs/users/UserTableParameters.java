package com.example.courseworkitfu.fxControllers.tabs.users;

import lombok.Getter;

@Getter
public class UserTableParameters {
    private final int id;
    private final String username;
    private final String email;
    private final String phone;
    private final String dateCreated;
    private final String role;
    private final String active;

    public UserTableParameters(int id,
                               String username,
                               String email,
                               String phone,
                               String dateCreated,
                               String role,
                               String active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.dateCreated = dateCreated;
        this.role = role;
        this.active = active;
    }

}