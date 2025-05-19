package com.config;

/**
 * Stores the URL of the server endpoints
 *
 * @authors Clement Luo,
 * @date May 10, 2025
 */
public class HTTPConfig {
    //URL of the server
    private static final String SERVER_URL = "http://192.168.1.88:8080";

    //User account endpoints
    private static final String USERS_URL = "/users";
    private static final String REGISTRATION_ENDPOINT = "/register";
    private static final String LOGIN_ENDPOINT = "/login";

    /**
     *
     */
    public static String getServerUrl() {
        return SERVER_URL;
    }

    /**
     *
     */
    public static String getRegistrationUrl() {
        return SERVER_URL + USERS_URL +  REGISTRATION_ENDPOINT;
    }

    /**
     *
     */
    public static String getLoginUrl() {
        return SERVER_URL + USERS_URL + LOGIN_ENDPOINT;
    }

    //Timeout duration of HTTP client (in seconds)
    public static final int HTTP_TIMEOUT = 5;

    //Timeout duration of HTTP request (in seconds)
    public static final int HTTP_REQUEST_TIMEOUT = 10;
}
