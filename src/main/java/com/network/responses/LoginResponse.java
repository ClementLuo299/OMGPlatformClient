package com.network.responses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.network.JWTToken;
import com.utils.error_handling.Logging;

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
            Logging.warning("Cannot parse JWT token from empty or null response body");
            return null;
        }

        Logging.info("Attempting to parse JWT token from server response");
        Logging.debug("Response body: " + responseBody);

        // Check if response contains JWT library error messages
        if (isJWTLibraryError(responseBody)) {
            Logging.error("Server returned JWT library error: " + responseBody);
            return null;
        }

        // Check if response is not valid JSON
        if (!isValidJSON(responseBody)) {
            Logging.error("Server response is not valid JSON: " + responseBody);
            return null;
        }

        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // Try different possible token field names
            String token = null;
            String tokenFieldName = null;
            
            if (jsonResponse.has("token")) {
                token = jsonResponse.get("token").getAsString();
                tokenFieldName = "token";
            } else if (jsonResponse.has("access_token")) {
                token = jsonResponse.get("access_token").getAsString();
                tokenFieldName = "access_token";
            } else if (jsonResponse.has("jwt")) {
                token = jsonResponse.get("jwt").getAsString();
                tokenFieldName = "jwt";
            }

            if (token == null || token.trim().isEmpty()) {
                Logging.warning("No JWT token found in server response. Available fields: " + 
                              jsonResponse.keySet());
                return null;
            }

            Logging.info("Found JWT token in field: '" + tokenFieldName + "', length: " + token.length() + " characters");

            // Try to get username from response
            String username = null;
            String usernameFieldName = null;
            
            if (jsonResponse.has("username")) {
                username = jsonResponse.get("username").getAsString();
                usernameFieldName = "username";
            } else if (jsonResponse.has("user")) {
                username = jsonResponse.get("user").getAsString();
                usernameFieldName = "user";
            }

            if (username != null) {
                Logging.info("Found username in field: '" + usernameFieldName + "': '" + username + "'");
            } else {
                Logging.info("No username found in server response, attempting to extract from JWT token");
                // Try to extract username from JWT token payload
                username = extractUsernameFromJWTToken(token);
                if (username != null) {
                    Logging.info("Extracted username from JWT token: '" + username + "'");
                } else {
                    Logging.warning("Could not extract username from JWT token");
                }
            }

            // Handle expiration time
            JWTToken jwtToken = null;
            
            if (jsonResponse.has("expires_in")) {
                // Server provides expiration in seconds from now
                long expiresInSeconds = jsonResponse.get("expires_in").getAsLong();
                Logging.info("Found expiration in field 'expires_in': " + expiresInSeconds + " seconds");
                jwtToken = JWTToken.fromServerResponse(token, username, expiresInSeconds);
                
            } else if (jsonResponse.has("expires_at")) {
                // Server provides explicit expiration timestamp
                String expiresAtString = jsonResponse.get("expires_at").getAsString();
                Logging.info("Found expiration in field 'expires_at': " + expiresAtString);
                LocalDateTime expiresAt = LocalDateTime.parse(expiresAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                jwtToken = JWTToken.fromServerResponse(token, username, expiresAt);
                
            } else {
                // Try to extract expiration from JWT token payload
                LocalDateTime tokenExpiration = extractExpirationFromJWTToken(token);
                if (tokenExpiration != null) {
                    Logging.info("Found expiration in JWT token payload: " + tokenExpiration.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    jwtToken = JWTToken.fromServerResponse(token, username, tokenExpiration);
                } else {
                    // Default to 1 hour expiration if not specified anywhere
                    Logging.warning("No expiration information found in server response or JWT token, defaulting to 1 hour");
                    jwtToken = JWTToken.fromServerResponse(token, username, 3600);
                }
            }

            if (jwtToken != null) {
                Logging.info("Successfully parsed JWT token from server response for user: '" + username + "'");
            } else {
                Logging.error("Failed to create JWT token from parsed data");
            }

            return jwtToken;

        } catch (Exception e) {
            Logging.error("Failed to parse JWT token from response: " + e.getMessage(), e);
            Logging.debug("Response body that failed to parse: " + responseBody);
            return null;
        }
    }

    /**
     * Checks if the response body contains JWT library error messages.
     * These indicate server-side JWT configuration issues.
     *
     * @param responseBody The response body to check
     * @return true if the response contains JWT library errors, false otherwise
     */
    private boolean isJWTLibraryError(String responseBody) {
        if (responseBody == null) return false;
        
        String lowerBody = responseBody.toLowerCase();
        return lowerBody.contains("jwt") && 
               (lowerBody.contains("key byte array") || 
                lowerBody.contains("hmac-sha") || 
                lowerBody.contains("rfc 7518") ||
                lowerBody.contains("signature algorithm") ||
                lowerBody.contains("not secure enough") ||
                lowerBody.contains("unable to find an implementation") ||
                lowerBody.contains("service loader") ||
                lowerBody.contains("jjwt-impl") ||
                lowerBody.contains("serializer") ||
                lowerBody.contains("backing implementation"));
    }

    /**
     * Checks if the response body is valid JSON.
     *
     * @param responseBody The response body to check
     * @return true if the response is valid JSON, false otherwise
     */
    private boolean isValidJSON(String responseBody) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return false;
        }
        
        try {
            JsonParser.parseString(responseBody);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets a user-friendly error message based on the response.
     * Handles various error scenarios including server-side JWT issues.
     *
     * @return A user-friendly error message
     */
    public String getUserFriendlyErrorMessage() {
        if (isSuccess()) {
            return "Login successful";
        }
        
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return "Server returned an empty response";
        }
        
        // Handle JWT library errors
        if (isJWTLibraryError(responseBody)) {
            return "Server authentication system is temporarily unavailable. Please try again later.";
        }
        
        // Handle non-JSON responses
        if (!isValidJSON(responseBody)) {
            return "Server returned an invalid response. Please try again.";
        }
        
        // Try to extract error message from JSON response
        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            if (jsonResponse.has("error")) {
                return jsonResponse.get("error").getAsString();
            } else if (jsonResponse.has("message")) {
                return jsonResponse.get("message").getAsString();
            } else if (jsonResponse.has("detail")) {
                return jsonResponse.get("detail").getAsString();
            }
        } catch (Exception e) {
            Logging.debug("Failed to extract error message from JSON response: " + e.getMessage());
        }
        
        // Default error message based on status code
        switch (statusCode) {
            case 401:
                return "Invalid username or password";
            case 403:
                return "Access denied";
            case 404:
                return "User not found";
            case 500:
                return "Server error. Please try again later.";
            case 503:
                return "Service temporarily unavailable";
            default:
                return "Login failed. Please try again.";
        }
    }

    /**
     * Extracts username from a JWT token.
     *
     * @param token The JWT token to extract username from
     * @return The extracted username, or null if extraction fails
     */
    private String extractUsernameFromJWTToken(String token) {
        try {
            // JWT tokens have 3 parts separated by dots: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                Logging.warning("Invalid JWT token format: expected 3 parts, got " + parts.length);
                return null;
            }
            
            // Decode the payload (second part)
            String payload = parts[1];
            
            // Add padding if needed for base64 decoding
            while (payload.length() % 4 != 0) {
                payload += "=";
            }
            
            // Replace URL-safe characters
            payload = payload.replace('-', '+').replace('_', '/');
            
            // Decode base64
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
            
            Logging.debug("JWT payload: " + decodedPayload);
            
            // Parse the JSON payload
            JsonObject payloadJson = JsonParser.parseString(decodedPayload).getAsJsonObject();
            
            // Extract username from common JWT claim names
            String username = null;
            if (payloadJson.has("sub")) {
                username = payloadJson.get("sub").getAsString();
                Logging.debug("Found username in 'sub' claim: '" + username + "'");
            } else if (payloadJson.has("username")) {
                username = payloadJson.get("username").getAsString();
                Logging.debug("Found username in 'username' claim: '" + username + "'");
            } else if (payloadJson.has("user")) {
                username = payloadJson.get("user").getAsString();
                Logging.debug("Found username in 'user' claim: '" + username + "'");
            } else if (payloadJson.has("name")) {
                username = payloadJson.get("name").getAsString();
                Logging.debug("Found username in 'name' claim: '" + username + "'");
            }
            
            if (username != null) {
                Logging.info("Successfully extracted username from JWT token: '" + username + "'");
            } else {
                Logging.warning("No username found in JWT token payload. Available claims: " + payloadJson.keySet());
            }
            
            return username;
            
        } catch (Exception e) {
            Logging.error("Failed to extract username from JWT token: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Extracts expiration time from a JWT token.
     *
     * @param token The JWT token to extract expiration from
     * @return The extracted expiration time, or null if extraction fails
     */
    private LocalDateTime extractExpirationFromJWTToken(String token) {
        try {
            // JWT tokens have 3 parts separated by dots: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                Logging.warning("Invalid JWT token format: expected 3 parts, got " + parts.length);
                return null;
            }
            
            // Decode the payload (second part)
            String payload = parts[1];
            
            // Add padding if needed for base64 decoding
            while (payload.length() % 4 != 0) {
                payload += "=";
            }
            
            // Replace URL-safe characters
            payload = payload.replace('-', '+').replace('_', '/');
            
            // Decode base64
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
            
            Logging.debug("JWT payload for expiration extraction: " + decodedPayload);
            
            // Parse the JSON payload
            JsonObject payloadJson = JsonParser.parseString(decodedPayload).getAsJsonObject();
            
            // Extract expiration from JWT exp claim (this is the standard expiration claim)
            LocalDateTime expiration = null;
            if (payloadJson.has("exp")) {
                try {
                    long exp = payloadJson.get("exp").getAsLong();
                    expiration = LocalDateTime.ofEpochSecond(exp, 0, java.time.ZoneOffset.UTC);
                    Logging.debug("Found expiration in 'exp' claim: " + expiration.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } catch (NumberFormatException e) {
                    Logging.warning("Invalid expiration value in JWT token: " + payloadJson.get("exp").getAsString());
                    return null;
                } catch (Exception e) {
                    Logging.warning("Failed to parse expiration from JWT token: " + e.getMessage());
                    return null;
                }
            }
            
            if (expiration != null) {
                Logging.info("Successfully extracted expiration from JWT token: " + expiration.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                Logging.warning("No valid expiration information found in JWT token payload. Available claims: " + payloadJson.keySet());
            }
            
            return expiration;
            
        } catch (Exception e) {
            Logging.error("Failed to extract expiration from JWT token: " + e.getMessage(), e);
            return null;
        }
    }
} 