package com.network.responses;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.utils.error_handling.Logging;

/**
 * Response object for user registration.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class RegistrationResponse {
    private final int statusCode;
    private final String responseBody;

    public RegistrationResponse(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public boolean isSuccess() {
        return statusCode == 201;
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
            return "Registration successful";
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
            case 400:
                return "Invalid registration data. Please check your information.";
            case 409:
                return "Username already exists. Please choose a different username.";
            case 500:
                return "Server error. Please try again later.";
            case 503:
                return "Service temporarily unavailable";
            default:
                return "Registration failed. Please try again.";
        }
    }
} 