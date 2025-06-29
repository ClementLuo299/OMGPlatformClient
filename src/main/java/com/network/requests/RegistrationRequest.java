package com.network.requests;

/**
 * Request object for user registration.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class RegistrationRequest {
    private final String username;
    private final String password;
    private final String fullName;
    private final String dateOfBirth;

    public RegistrationRequest(String username, String password, String fullName, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
} 