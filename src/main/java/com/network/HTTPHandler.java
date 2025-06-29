package com.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.config.HTTPConfig;
import com.google.gson.Gson;
import com.entities.UserAccount;
import com.network.requests.LoginRequest;
import com.network.requests.RegistrationRequest;
import com.network.responses.LoginResponse;
import com.network.responses.RegistrationResponse;
import javafx.application.Platform;

/**
 * Handles HTTP communication between the program and the server
 *
 * @authors Clement Luo,
 * @date March 4, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class HTTPHandler {
    //HTTP client
    private static final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(HTTPConfig.HTTP_TIMEOUT)).build();

    //JSON parser
    private static final Gson gson = new Gson();

    /**
     * Registers an user account on the server.
     */
    public static RegistrationResponse register(String username, String password, String fullName, String dateOfBirth) throws IOException {
        URL url = new URL(HTTPConfig.getRegistrationUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Create registration request object with correct field names
        RegistrationRequest request = new RegistrationRequest(username, password, fullName, dateOfBirth);
        String jsonInput = gson.toJson(request);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 201) ? conn.getInputStream() : conn.getErrorStream();
        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        
        return new RegistrationResponse(responseCode, responseBody);
    }

    /**
     * Authenticates a user with the provided credentials.
     */
    public static LoginResponse login(String username, String password) throws IOException {
        URL url = new URL(HTTPConfig.getLoginUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = gson.toJson(new LoginRequest(username, password));

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        
        return new LoginResponse(responseCode, responseBody);
    }
}
