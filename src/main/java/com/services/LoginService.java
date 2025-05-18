package com.services;

/**
 * Business logic for the login screen
 *
 * @authors Clement Luo
 * @date May 17, 2025
 */
public class LoginService {

    /**
     *
     */
    public boolean login(String username, String password) {
        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            //
        }
        return true;
    }

    /**
     *
     */
    public boolean guestLogin(String username) {
        return false;
    }

    /**
     *
     */
    public boolean register(String username) {
        return true;
    }

    /**
     *
     */
    public boolean forgotPassword(String username) {
        return true;
    }
}
