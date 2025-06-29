package com.network;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a JWT token for user authentication.
 * Handles token storage, validation, and expiration checking.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class JWTToken {
    private final String token;
    private final String username;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiresAt;
    private final String tokenType;

    /**
     * Creates a new JWT token.
     *
     * @param token The JWT token string
     * @param username The username associated with this token
     * @param issuedAt When the token was issued
     * @param expiresAt When the token expires
     * @param tokenType The type of token (e.g., "Bearer")
     */
    public JWTToken(String token, String username, LocalDateTime issuedAt, LocalDateTime expiresAt, String tokenType) {
        this.token = token;
        this.username = username;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.tokenType = tokenType;
    }

    /**
     * Creates a JWT token from a server response.
     * Assumes the server returns a JSON object with token information.
     *
     * @param token The JWT token string
     * @param username The username associated with this token
     * @param expiresInSeconds Seconds until token expires (from now)
     * @return A new JWT token instance
     */
    public static JWTToken fromServerResponse(String token, String username, long expiresInSeconds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(expiresInSeconds);
        return new JWTToken(token, username, now, expiresAt, "Bearer");
    }

    /**
     * Creates a JWT token from a server response with explicit expiration time.
     *
     * @param token The JWT token string
     * @param username The username associated with this token
     * @param expiresAt When the token expires
     * @return A new JWT token instance
     */
    public static JWTToken fromServerResponse(String token, String username, LocalDateTime expiresAt) {
        return new JWTToken(token, username, LocalDateTime.now(), expiresAt, "Bearer");
    }

    /**
     * Checks if the token is expired.
     *
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Checks if the token will expire within the specified number of minutes.
     *
     * @param minutes Number of minutes to check ahead
     * @return true if the token will expire within the specified time, false otherwise
     */
    public boolean expiresWithin(int minutes) {
        LocalDateTime warningTime = LocalDateTime.now().plusMinutes(minutes);
        return expiresAt.isBefore(warningTime);
    }

    /**
     * Gets the time remaining until expiration in seconds.
     *
     * @return Seconds remaining until expiration, negative if expired
     */
    public long getSecondsUntilExpiration() {
        return java.time.Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
    }

    /**
     * Gets the authorization header value for this token.
     *
     * @return The authorization header value (e.g., "Bearer eyJhbGciOiJIUzI1NiIs...")
     */
    public String getAuthorizationHeader() {
        return tokenType + " " + token;
    }

    // Getters
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public String getTokenType() { return tokenType; }

    @Override
    public String toString() {
        return "JWTToken{" +
                "username='" + username + '\'' +
                ", issuedAt=" + issuedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                ", expiresAt=" + expiresAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                ", expired=" + isExpired() +
                '}';
    }
} 