package com.controllers.systems;

/**
 *
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class LoginEvent {
    private final String username;

    public LoginEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
