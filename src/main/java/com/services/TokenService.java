package com.services;

import com.network.JWTToken;
import com.utils.error_handling.Logging;

/**
 * Manages JWT tokens for user authentication.
 * Handles token storage, retrieval, validation, and lifecycle management.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class TokenService {
    private JWTToken currentToken;

    /**
     * Creates a new TokenService instance.
     */
    public TokenService() {
        // Initialize with no token
        this.currentToken = null;
    }

    /**
     * Stores a JWT token for the current session.
     *
     * @param token The JWT token to store
     */
    public void setToken(JWTToken token) {
        this.currentToken = token;
        Logging.info("JWT token stored for user: " + token.getUsername());
    }

    /**
     * Gets the current JWT token.
     *
     * @return The current JWT token, or null if no token is stored
     */
    public JWTToken getToken() {
        return currentToken;
    }

    /**
     * Gets the current JWT token string.
     *
     * @return The current JWT token string, or null if no token is stored
     */
    public String getTokenString() {
        return currentToken != null ? currentToken.getToken() : null;
    }

    /**
     * Gets the authorization header value for the current token.
     *
     * @return The authorization header value, or null if no token is stored
     */
    public String getAuthorizationHeader() {
        return currentToken != null ? currentToken.getAuthorizationHeader() : null;
    }

    /**
     * Checks if a valid token is currently stored.
     *
     * @return true if a valid token is stored, false otherwise
     */
    public boolean hasValidToken() {
        return currentToken != null && !currentToken.isExpired();
    }

    /**
     * Checks if the current token is expired.
     *
     * @return true if the token is expired or doesn't exist, false otherwise
     */
    public boolean isTokenExpired() {
        return currentToken == null || currentToken.isExpired();
    }

    /**
     * Gets the username associated with the current token.
     *
     * @return The username, or null if no token is stored
     */
    public String getCurrentUsername() {
        return currentToken != null ? currentToken.getUsername() : null;
    }

    /**
     * Clears the current token (logout).
     */
    public void clearToken() {
        if (currentToken != null) {
            Logging.info("JWT token cleared for user: " + currentToken.getUsername());
        }
        this.currentToken = null;
    }

    /**
     * Checks if the current token will expire soon and logs a warning.
     *
     * @param warningMinutes Minutes before expiration to start warning
     */
    public void checkTokenExpiration(int warningMinutes) {
        if (currentToken != null && currentToken.expiresWithin(warningMinutes)) {
            long minutesLeft = currentToken.getSecondsUntilExpiration() / 60;
            Logging.warning("JWT token will expire in " + minutesLeft + " minutes for user: " + currentToken.getUsername());
        }
    }

    /**
     * Refreshes the token if it's about to expire.
     * This method should be called periodically to ensure the token stays valid.
     *
     * @param refreshThresholdMinutes Minutes before expiration to refresh the token
     * @return true if the token was refreshed, false otherwise
     */
    public boolean refreshTokenIfNeeded(int refreshThresholdMinutes) {
        if (currentToken != null && currentToken.expiresWithin(refreshThresholdMinutes)) {
            Logging.info("Token refresh needed for user: " + currentToken.getUsername());
            // TODO: Implement token refresh logic
            // This would typically make an HTTP request to refresh the token
            return true;
        }
        return false;
    }

    /**
     * Gets the time remaining until token expiration in seconds.
     *
     * @return Seconds remaining until expiration, negative if expired, null if no token
     */
    public Long getSecondsUntilExpiration() {
        return currentToken != null ? currentToken.getSecondsUntilExpiration() : null;
    }

    /**
     * Checks if the token will expire within the specified number of minutes.
     *
     * @param minutes Number of minutes to check ahead
     * @return true if the token will expire within the specified time, false otherwise
     */
    public boolean expiresWithin(int minutes) {
        return currentToken != null && currentToken.expiresWithin(minutes);
    }
} 