package com.network.requests;

/**
 * Request object for user login authentication.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class LoginRequest {
    private final String username;
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
} 