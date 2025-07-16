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
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

import com.config.HTTPConfig;
import com.google.gson.Gson;
import com.entities.UserAccount;
import com.network.requests.LoginRequest;
import com.network.requests.RegistrationRequest;
import com.network.responses.LoginResponse;
import com.network.responses.RegistrationResponse;
import com.utils.error_handling.Logging;
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

    // Trust manager that accepts all certificates (for development only)
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        }
    };

    /**
     * Registers an user account on the server.
     */
    public static RegistrationResponse register(String username, String password, String fullName, String dateOfBirth) throws IOException {
        String registrationUrl = HTTPConfig.getRegistrationUrl();
        Logging.info("🔗 Attempting to register user at: " + registrationUrl);
        
        URL url = new URL(registrationUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // Set timeouts to prevent hanging
        conn.setConnectTimeout(HTTPConfig.HTTP_TIMEOUT * 1000);
        conn.setReadTimeout(HTTPConfig.HTTP_REQUEST_TIMEOUT * 1000);
        
        Logging.info("⏱️ Set connection timeout: " + HTTPConfig.HTTP_TIMEOUT + "s, read timeout: " + HTTPConfig.HTTP_REQUEST_TIMEOUT + "s");

        // If it's an HTTPS connection, disable hostname verification for development
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setHostnameVerifier((hostname, session) -> true);
            Logging.info("🔓 Disabled hostname verification for HTTPS connection");
        }

        // Create registration request object with correct field names
        RegistrationRequest request = new RegistrationRequest(username, password, fullName, dateOfBirth);
        String jsonInput = gson.toJson(request);
        Logging.info("📤 Sending registration request: " + jsonInput);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            Logging.info("✅ Registration request sent successfully");
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 201) ? conn.getInputStream() : conn.getErrorStream();
        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        
        Logging.info("📥 Registration response - Status: " + responseCode + ", Body: " + responseBody);
        
        return new RegistrationResponse(responseCode, responseBody);
    }

    /**
     * Authenticates a user with the provided credentials.
     */
    public static LoginResponse login(String username, String password) throws IOException {
        String loginUrl = HTTPConfig.getLoginUrl();
        Logging.info("🔗 Attempting to login at: " + loginUrl);
        
        URL url = new URL(loginUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // Set timeouts to prevent hanging
        conn.setConnectTimeout(HTTPConfig.HTTP_TIMEOUT * 1000);
        conn.setReadTimeout(HTTPConfig.HTTP_REQUEST_TIMEOUT * 1000);
        
        Logging.info("⏱️ Set connection timeout: " + HTTPConfig.HTTP_TIMEOUT + "s, read timeout: " + HTTPConfig.HTTP_REQUEST_TIMEOUT + "s");

        // If it's an HTTPS connection, disable hostname verification for development
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setHostnameVerifier((hostname, session) -> true);
            Logging.info("🔓 Disabled hostname verification for HTTPS connection");
        }

        String jsonInput = gson.toJson(new LoginRequest(username, password));
        Logging.info("📤 Sending login request: " + jsonInput);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            Logging.info("✅ Login request sent successfully");
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        
        Logging.info("📥 Login response - Status: " + responseCode + ", Body: " + responseBody);
        
        return new LoginResponse(responseCode, responseBody);
    }
}
