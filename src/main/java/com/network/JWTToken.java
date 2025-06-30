package com.network;

import com.utils.error_handling.Logging;
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
        
        // Log token creation
        Logging.info("JWT token created for user: '" + username + 
                    "', issued: " + issuedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + 
                    ", expires: " + expiresAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + 
                    ", type: " + tokenType);
        
        // Log token length for security monitoring
        if (token != null) {
            Logging.info("JWT token length: " + token.length() + " characters");
        }
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
        Logging.info("Creating JWT token from server response - Username: '" + username + 
                    "', Expires in: " + expiresInSeconds + " seconds");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(expiresInSeconds);
        
        JWTToken jwtToken = new JWTToken(token, username, now, expiresAt, "Bearer");
        
        Logging.info("JWT token created successfully from server response for user: '" + username + "'");
        return jwtToken;
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
        Logging.info("Creating JWT token from server response with explicit expiration - Username: '" + username + 
                    "', Expires at: " + expiresAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        JWTToken jwtToken = new JWTToken(token, username, LocalDateTime.now(), expiresAt, "Bearer");
        
        Logging.info("JWT token created successfully from server response with explicit expiration for user: '" + username + "'");
        return jwtToken;
    }

    /**
     * Checks if the token is expired.
     *
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired() {
        boolean expired = LocalDateTime.now().isAfter(expiresAt);
        
        if (expired) {
            Logging.warning("JWT token is EXPIRED for user: '" + username + 
                          "', expired at: " + expiresAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            Logging.debug("JWT token is VALID for user: '" + username + 
                         "', expires at: " + expiresAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        
        return expired;
    }

    /**
     * Checks if the token will expire within the specified number of minutes.
     *
     * @param minutes Number of minutes to check ahead
     * @return true if the token will expire within the specified time, false otherwise
     */
    public boolean expiresWithin(int minutes) {
        LocalDateTime warningTime = LocalDateTime.now().plusMinutes(minutes);
        boolean willExpire = expiresAt.isBefore(warningTime);
        
        if (willExpire) {
            long minutesLeft = getSecondsUntilExpiration() / 60;
            Logging.warning("JWT token will expire within " + minutes + " minutes for user: '" + username + 
                          "', expires in: " + minutesLeft + " minutes");
        } else {
            Logging.debug("JWT token will NOT expire within " + minutes + " minutes for user: '" + username + "'");
        }
        
        return willExpire;
    }

    /**
     * Gets the time remaining until expiration in seconds.
     *
     * @return Seconds remaining until expiration, negative if expired
     */
    public long getSecondsUntilExpiration() {
        long secondsLeft = java.time.Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
        
        if (secondsLeft <= 0) {
            Logging.warning("JWT token has EXPIRED for user: '" + username + 
                          "', expired " + Math.abs(secondsLeft) + " seconds ago");
        } else if (secondsLeft < 300) { // Less than 5 minutes
            Logging.warning("JWT token expires soon for user: '" + username + 
                          "', expires in: " + secondsLeft + " seconds");
        } else {
            Logging.debug("JWT token time remaining for user: '" + username + 
                         "', expires in: " + secondsLeft + " seconds");
        }
        
        return secondsLeft;
    }

    /**
     * Gets the authorization header value for this token.
     *
     * @return The authorization header value (e.g., "Bearer eyJhbGciOiJIUzI1NiIs...")
     */
    public String getAuthorizationHeader() {
        String authHeader = tokenType + " " + token;
        
        Logging.debug("Generated authorization header for user: '" + username + 
                     "', header type: " + tokenType + ", token length: " + token.length());
        
        return authHeader;
    }

    // Getters
    public String getToken() { 
        Logging.debug("JWT token retrieved for user: '" + username + "'");
        return token; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public LocalDateTime getIssuedAt() { 
        return issuedAt; 
    }
    
    public LocalDateTime getExpiresAt() { 
        return expiresAt; 
    }
    
    public String getTokenType() { 
        return tokenType; 
    }

    @Override
    public String toString() {
        String tokenInfo = "JWTToken{" +
                "username='" + username + '\'' +
                ", issuedAt=" + issuedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                ", expiresAt=" + expiresAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                ", expired=" + isExpired() +
                '}';
        
        Logging.debug("JWT token toString() called for user: '" + username + "'");
        return tokenInfo;
    }
} 