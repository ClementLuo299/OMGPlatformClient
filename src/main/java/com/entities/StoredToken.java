package com.entities;

import java.time.LocalDateTime;

/**
 * POJO for securely storing JWT token and metadata on disk.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public class StoredToken {
    private String token;
    private String username;
    private LocalDateTime expiresAt;

    /**
     * Default constructor.
     */
    public StoredToken() {
    }

    /**
     * Constructor with all fields.
     */
    public StoredToken(String token, String username, LocalDateTime expiresAt) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    /**
     * Gets the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the expiration date/time.
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the expiration date/time.
     */
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "StoredToken{" +
                "token='[REDACTED]'" +
                ", username='" + username + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
} 