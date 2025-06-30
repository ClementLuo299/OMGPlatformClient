package com.entities;

import java.time.LocalDateTime;

/**
 * POJO for securely storing JWT token and metadata on disk.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class StoredToken {
    private String token;
    private String username;
    private LocalDateTime expiresAt;

    public StoredToken() {}

    public StoredToken(String token, String username, LocalDateTime expiresAt) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    @Override
    public String toString() {
        return "StoredToken{" +
                "token='[REDACTED]'" +
                ", username='" + username + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
} 