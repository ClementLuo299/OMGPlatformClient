package com.network.IO;

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
import javafx.application.Platform;

/**
 * Handles HTTP communication between the program and the server
 *
 * @authors Clement Luo,
 * @date March 4, 2025
 */
public class HTTPHandler {
    //HTTP client
    private static final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(HTTPConfig.HTTP_TIMEOUT)).build();

    //JSON parser
    private static final Gson gson = new Gson();

    /**
     * Registers an user account on the server.
     */
    public static void register(String username, String password){
        //Create user account object and convert to json
        UserAccount user = new UserAccount(username, password);
        String json = gson.toJson(user);

        //Build HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTPConfig.getRegistrationUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(HTTPConfig.HTTP_REQUEST_TIMEOUT))
                .build();

        //Send HTTP request asynchronously
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            int status = response.statusCode();
                            String body = response.body();

                            Platform.runLater(() -> {
                                if(status == HttpURLConnection.HTTP_CREATED) {
                                    //Show success dialog
                                    System.out.println("Success");
                                }
                                else {
                                    //Show error
                                    System.out.println("Fail " + body);
                                }
                            });
                        })
                        .exceptionally(e -> {
                            Platform.runLater(() -> {
                                Throwable cause = e.getCause();
                                if(cause instanceof java.net.ConnectException) {
                                    //Show error dialog
                                    System.out.println("Unable to connect to server.");
                                }
                                else if(cause instanceof java.net.http.HttpTimeoutException) {
                                    //Show error dialog
                                    System.out.println("Request timed out");
                                }
                                else {
                                    //Show error dialog
                                    System.out.println("Unexpected error: " + e.getMessage());
                                }
                            });
                            return null;
                        });
    }

    /**
     *
     */
    public static String login(String username, String password) throws IOException {
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
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    static class LoginRequest {
        private final String username;
        private final String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static void main(String[] args) throws Exception {
        login("bob5","pass");
    }
}
