package com.network.responses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.network.JWTToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Response object for user login authentication.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class LoginResponse {
    private final int statusCode;
    private final String responseBody;
    private final JWTToken jwtToken;

    public LoginResponse(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.jwtToken = parseJWTTokenFromResponse();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }

    /**
     * Gets the JWT token from the response.
     *
     * @return The JWT token, or null if not present or invalid
     */
    public JWTToken getJWTToken() {
        return jwtToken;
    }

    /**
     * Checks if the response contains a JWT token.
     *
     * @return true if a JWT token is present, false otherwise
     */
    public boolean hasJWTToken() {
        return jwtToken != null;
    }

    /**
     * Parses JWT token information from the server response.
     * Assumes the server returns JSON in one of these formats:
     * 1. {"token": "jwt_token_string", "expires_in": 3600}
     * 2. {"access_token": "jwt_token_string", "expires_in": 3600}
     * 3. {"token": "jwt_token_string", "expires_at": "2025-06-30T10:30:00"}
     *
     * @return The parsed JWT token, or null if parsing fails
     */
    private JWTToken parseJWTTokenFromResponse() {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return null;
        }

        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // Try different possible token field names
            String token = null;
            if (jsonResponse.has("token")) {
                token = jsonResponse.get("token").getAsString();
            } else if (jsonResponse.has("access_token")) {
                token = jsonResponse.get("access_token").getAsString();
            } else if (jsonResponse.has("jwt")) {
                token = jsonResponse.get("jwt").getAsString();
            }

            if (token == null || token.trim().isEmpty()) {
                return null;
            }

            // Try to get username from response
            String username = null;
            if (jsonResponse.has("username")) {
                username = jsonResponse.get("username").getAsString();
            } else if (jsonResponse.has("user")) {
                username = jsonResponse.get("user").getAsString();
            }

            // Handle expiration time
            if (jsonResponse.has("expires_in")) {
                // Server provides expiration in seconds from now
                long expiresInSeconds = jsonResponse.get("expires_in").getAsLong();
                return JWTToken.fromServerResponse(token, username, expiresInSeconds);
            } else if (jsonResponse.has("expires_at")) {
                // Server provides explicit expiration timestamp
                String expiresAtString = jsonResponse.get("expires_at").getAsString();
                LocalDateTime expiresAt = LocalDateTime.parse(expiresAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return JWTToken.fromServerResponse(token, username, expiresAt);
            } else {
                // Default to 1 hour expiration if not specified
                return JWTToken.fromServerResponse(token, username, 3600);
            }

        } catch (Exception e) {
            // Log parsing error but don't throw exception
            System.err.println("Failed to parse JWT token from response: " + e.getMessage());
            return null;
        }
    }
} 