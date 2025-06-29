package com.network.responses;

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
} 