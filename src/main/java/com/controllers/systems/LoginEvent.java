package com.controllers.systems;

public class LoginEvent {
    private final String username;

    public LoginEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
