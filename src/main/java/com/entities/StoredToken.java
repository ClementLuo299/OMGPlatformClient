package com.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * POJO for securely storing JWT token and metadata on disk.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredToken {
    private String token;
    private String username;
    private LocalDateTime expiresAt;

    @Override
    public String toString() {
        return "StoredToken{" +
                "token='[REDACTED]'" +
                ", username='" + username + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
} 