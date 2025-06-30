package com.services;

import com.network.JWTToken;
import com.utils.error_handling.Logging;
import java.time.LocalDateTime;
import com.entities.StoredToken;
import com.services.LocalStorageService;
import java.time.format.DateTimeFormatter;

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
    private static final String TOKEN_STORAGE_KEY = "auth_token";
    private final LocalStorageService localStorageService = LocalStorageService.getInstance();

    /**
     * Creates a new TokenService instance.
     */
    public TokenService() {
        // Initialize with no token
        this.currentToken = null;
    }

    /**
     * Stores a JWT token for the current session and persists it securely.
     *
     * @param token The JWT token to store
     */
    public void setToken(JWTToken token) {
        if (token == null) {
            Logging.warning("Attempted to store null JWT token");
            return;
        }
        
        // Log the token being stored
        Logging.info("Storing JWT token for user: '" + token.getUsername() + 
                    "', expires at: " + token.getExpiresAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // If there was a previous token, log the replacement
        if (this.currentToken != null) {
            Logging.info("Replacing existing JWT token for user: '" + this.currentToken.getUsername() + 
                        "' with new token for user: '" + token.getUsername() + "'");
        }
        
        this.currentToken = token;
        Logging.info("JWT token stored successfully for user: '" + token.getUsername() + "'");
        // Persist token to disk
        persistTokenToDisk(token);
    }

    /**
     * Loads the JWT token from disk if available and not expired.
     * Call this on app startup if you want to restore the session.
     */
    public void loadTokenFromDisk() {
        Logging.info("Attempting to load JWT token from disk...");
        if (localStorageService.hasEncryptedData(TOKEN_STORAGE_KEY)) {
            StoredToken stored = localStorageService.getEncryptedData(TOKEN_STORAGE_KEY, StoredToken.class);
            if (stored != null && stored.getToken() != null && stored.getExpiresAt() != null) {
                if (stored.getExpiresAt().isAfter(LocalDateTime.now())) {
                    this.currentToken = toJWTToken(stored);
                    Logging.info("Loaded valid JWT token from disk for user: '" + stored.getUsername() + "'");
                } else {
                    Logging.warning("Stored JWT token on disk is expired. Deleting...");
                    localStorageService.deleteEncryptedData(TOKEN_STORAGE_KEY);
                    this.currentToken = null;
                }
            } else {
                Logging.warning("No valid stored token found on disk");
                this.currentToken = null;
            }
        } else {
            Logging.info("No stored JWT token found on disk");
        }
    }

    /**
     * Gets the current JWT token.
     *
     * @return The current JWT token, or null if no token is stored
     */
    public JWTToken getToken() {
        if (currentToken == null) {
            Logging.debug("No JWT token available for retrieval");
            return null;
        }
        
        Logging.debug("Retrieved JWT token for user: '" + currentToken.getUsername() + "'");
        return currentToken;
    }

    /**
     * Gets the current JWT token string.
     *
     * @return The current JWT token string, or null if no token is stored
     */
    public String getTokenString() {
        if (currentToken == null) {
            Logging.debug("No JWT token string available");
            return null;
        }
        
        Logging.debug("Retrieved JWT token string for user: '" + currentToken.getUsername() + 
                     "', length: " + currentToken.getToken().length() + " characters");
        return currentToken.getToken();
    }

    /**
     * Gets the authorization header value for the current token.
     *
     * @return The authorization header value, or null if no token is stored
     */
    public String getAuthorizationHeader() {
        if (currentToken == null) {
            Logging.debug("No authorization header available - no token stored");
            return null;
        }
        
        String authHeader = currentToken.getAuthorizationHeader();
        Logging.debug("Generated authorization header for user: '" + currentToken.getUsername() + 
                     "', header length: " + authHeader.length() + " characters");
        return authHeader;
    }

    /**
     * Checks if a valid token is currently stored.
     *
     * @return true if a valid token is stored, false otherwise
     */
    public boolean hasValidToken() {
        if (currentToken == null) {
            Logging.debug("No JWT token stored");
            return false;
        }
        
        boolean isValid = !currentToken.isExpired();
        
        if (isValid) {
            Logging.debug("Valid JWT token found for user: '" + currentToken.getUsername() + "'");
        } else {
            Logging.warning("JWT token is invalid (expired) for user: '" + currentToken.getUsername() + "'");
        }
        
        return isValid;
    }

    /**
     * Checks if the current token is expired.
     *
     * @return true if the token is expired or doesn't exist, false otherwise
     */
    public boolean isTokenExpired() {
        if (currentToken == null) {
            Logging.debug("No JWT token to check for expiration");
            return true;
        }
        
        boolean expired = currentToken.isExpired();
        
        if (expired) {
            Logging.warning("JWT token is expired for user: '" + currentToken.getUsername() + "'");
        } else {
            Logging.debug("JWT token is not expired for user: '" + currentToken.getUsername() + "'");
        }
        
        return expired;
    }

    /**
     * Gets the username associated with the current token.
     *
     * @return The username, or null if no token is stored
     */
    public String getCurrentUsername() {
        if (currentToken == null) {
            Logging.debug("No username available - no token stored");
            return null;
        }
        
        Logging.debug("Retrieved username: '" + currentToken.getUsername() + "' from current token");
        return currentToken.getUsername();
    }

    /**
     * Clears the current token (logout) and deletes it from disk.
     */
    public void clearToken() {
        if (currentToken == null) {
            Logging.debug("No JWT token to clear");
            return;
        }
        String username = currentToken.getUsername();
        Logging.info("Clearing JWT token for user: '" + username + "'");
        this.currentToken = null;
        // Delete token from disk
        localStorageService.deleteEncryptedData(TOKEN_STORAGE_KEY);
        Logging.info("JWT token cleared successfully for user: '" + username + "'");
    }

    /**
     * Checks if the current token will expire soon and logs a warning.
     *
     * @param warningMinutes Minutes before expiration to start warning
     */
    public void checkTokenExpiration(int warningMinutes) {
        if (currentToken == null) {
            Logging.debug("No JWT token to check for expiration");
            return;
        }
        
        if (currentToken.expiresWithin(warningMinutes)) {
            long minutesLeft = currentToken.getSecondsUntilExpiration() / 60;
            Logging.warning("JWT token will expire in " + minutesLeft + " minutes for user: '" + currentToken.getUsername() + "'");
        } else {
            Logging.debug("JWT token will not expire within " + warningMinutes + " minutes for user: '" + currentToken.getUsername() + "'");
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
        if (currentToken == null) {
            Logging.debug("No JWT token to check for refresh");
            return false;
        }
        
        if (currentToken.expiresWithin(refreshThresholdMinutes)) {
            Logging.info("Token refresh needed for user: '" + currentToken.getUsername() + 
                        "' (expires within " + refreshThresholdMinutes + " minutes)");
            // TODO: Implement token refresh logic
            // This would typically make an HTTP request to refresh the token
            return true;
        } else {
            Logging.debug("Token refresh not needed for user: '" + currentToken.getUsername() + 
                         "' (does not expire within " + refreshThresholdMinutes + " minutes)");
            return false;
        }
    }

    /**
     * Gets the time remaining until token expiration in seconds.
     *
     * @return Seconds remaining until expiration, negative if expired, null if no token
     */
    public Long getSecondsUntilExpiration() {
        if (currentToken == null) {
            Logging.debug("No JWT token to check expiration time");
            return null;
        }
        
        Long secondsLeft = currentToken.getSecondsUntilExpiration();
        Logging.debug("JWT token expires in " + secondsLeft + " seconds for user: '" + currentToken.getUsername() + "'");
        return secondsLeft;
    }

    /**
     * Checks if the token will expire within the specified number of minutes.
     *
     * @param minutes Number of minutes to check ahead
     * @return true if the token will expire within the specified time, false otherwise
     */
    public boolean expiresWithin(int minutes) {
        if (currentToken == null) {
            Logging.debug("No JWT token to check if expires within " + minutes + " minutes");
            return false;
        }
        
        boolean willExpire = currentToken.expiresWithin(minutes);
        
        if (willExpire) {
            Logging.warning("JWT token will expire within " + minutes + " minutes for user: '" + currentToken.getUsername() + "'");
        } else {
            Logging.debug("JWT token will not expire within " + minutes + " minutes for user: '" + currentToken.getUsername() + "'");
        }
        
        return willExpire;
    }

    /**
     * Persists the JWT token to disk as a StoredToken.
     */
    private void persistTokenToDisk(JWTToken token) {
        if (token == null) return;
        StoredToken stored = new StoredToken(token.getToken(), token.getUsername(), token.getExpiresAt());
        localStorageService.storeEncryptedData(TOKEN_STORAGE_KEY, stored);
        Logging.info("JWT token persisted to disk for user: '" + token.getUsername() + "'");
    }

    /**
     * Converts a StoredToken to a JWTToken.
     */
    private JWTToken toJWTToken(StoredToken stored) {
        if (stored == null) return null;
        return new JWTToken(stored.getToken(), stored.getUsername(), LocalDateTime.now(), stored.getExpiresAt(), "Bearer");
    }
} 