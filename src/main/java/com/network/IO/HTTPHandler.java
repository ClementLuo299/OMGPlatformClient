package com.network.IO;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.config.HTTPConfig;
import com.google.gson.Gson;
import com.models.UserAccount;

/**
 * Handles HTTP communication between the program and the server
 *
 * @authors Clement Luo,
 * @date March 4, 2025
 */
public class HTTPHandler {
    //HTTP client (Times out after 5 seconds)
    private static final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    //JSON parser
    private static final Gson gson = new Gson();

    /**
     *
     */
    public static void addUser(String username, String password) throws Exception {
        UserAccount user = new UserAccount(username, password);
        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTPConfig.getRegistrationUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }

    /**
     *
     */
    public static List<User> getAllUsers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.1.88:8080/getusers")) // Replace with your actual IP
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Convert JSON array to List<com.network.IO.User>
        User[] users = gson.fromJson(response.body(), User[].class);
        return Arrays.asList(users);
    }

    public static void main(String[] args) throws Exception {
        addUser("bob5","");
    }
}
