package com.network;

import com.services.TokenService;
import com.utils.error_handling.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Handles authenticated HTTP requests by automatically including JWT tokens.
 * Extends the base HTTPHandler functionality for authenticated endpoints.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class AuthenticatedHTTPHandler {

    private final TokenService tokenService;

    /**
     * Creates a new AuthenticatedHTTPHandler with the specified TokenService.
     *
     * @param tokenService The TokenService to use for token management
     */
    public AuthenticatedHTTPHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Makes an authenticated GET request to the specified URL.
     *
     * @param urlString The URL to make the request to
     * @return The response body as a string
     * @throws IOException if the request fails
     */
    public String authenticatedGet(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        
        // Add authorization header if token is available
        addAuthorizationHeader(conn);

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Makes an authenticated POST request to the specified URL.
     *
     * @param urlString The URL to make the request to
     * @param jsonData The JSON data to send in the request body
     * @return The response body as a string
     * @throws IOException if the request fails
     */
    public String authenticatedPost(String urlString, String jsonData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // Add authorization header if token is available
        addAuthorizationHeader(conn);

        // Send the request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Makes an authenticated PUT request to the specified URL.
     *
     * @param urlString The URL to make the request to
     * @param jsonData The JSON data to send in the request body
     * @return The response body as a string
     * @throws IOException if the request fails
     */
    public String authenticatedPut(String urlString, String jsonData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // Add authorization header if token is available
        addAuthorizationHeader(conn);

        // Send the request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Makes an authenticated DELETE request to the specified URL.
     *
     * @param urlString The URL to make the request to
     * @return The response body as a string
     * @throws IOException if the request fails
     */
    public String authenticatedDelete(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/json");
        
        // Add authorization header if token is available
        addAuthorizationHeader(conn);

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Adds the authorization header to the connection if a valid token is available.
     *
     * @param conn The HTTP connection to add the header to
     */
    private void addAuthorizationHeader(HttpURLConnection conn) {
        if (tokenService.hasValidToken()) {
            String authHeader = tokenService.getAuthorizationHeader();
            conn.setRequestProperty("Authorization", authHeader);
            Logging.info("Added authorization header for user: " + tokenService.getCurrentUsername());
        } else {
            Logging.warning("No valid token available for authenticated request");
        }
    }

    /**
     * Checks if a valid token is available for authenticated requests.
     *
     * @return true if a valid token is available, false otherwise
     */
    public boolean hasValidToken() {
        return tokenService.hasValidToken();
    }

    /**
     * Gets the current username from the token service.
     *
     * @return The current username, or null if no token is available
     */
    public String getCurrentUsername() {
        return tokenService.getCurrentUsername();
    }
} 