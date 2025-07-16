package com.services;

import com.config.WebSocketConfig;
import com.utils.error_handling.Logging;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.framing.Framedata;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Service for managing WebSocket connections and messaging.
 * Handles connection establishment, message sending, and connection lifecycle.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class WebSocketService {
    
    private WebSocketClient webSocketClient;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private final AtomicBoolean isConnecting = new AtomicBoolean(false);
    private String currentUsername;
    
    // Trust manager that accepts all certificates (for development only)
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        }
    };
    
    /**
     * Connects to the WebSocket server
     * @param username The username to identify this connection
     * @return CompletableFuture that completes when connection is established
     */
    public CompletableFuture<Boolean> connect(String username) {
        if (isConnected.get()) {
            Logging.info("WebSocket already connected");
            return CompletableFuture.completedFuture(true);
        }
        
        if (isConnecting.get()) {
            Logging.info("WebSocket connection already in progress");
            return CompletableFuture.completedFuture(false);
        }
        
        this.currentUsername = username;
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                isConnecting.set(true);
                Logging.info("🔌 Attempting to connect to WebSocket server: " + WebSocketConfig.getGeneralWebSocketUrl());
                
                URI uri = new URI(WebSocketConfig.getGeneralWebSocketUrl());
                
                // Configure SSL context to trust all certificates for development
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                
                webSocketClient = new WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Logging.info("🔗 WebSocket connection opened successfully");
                        Logging.info("📊 Handshake status: " + handshakedata.getHttpStatus() + " " + handshakedata.getHttpStatusMessage());
                        isConnected.set(true);
                        isConnecting.set(false);
                        
                        // Send a test message after successful connection
                        sendTestMessage();
                    }
                    
                    @Override
                    public void onMessage(String message) {
                        Logging.info("📨 WebSocket message received: " + message);
                        handleIncomingMessage(message);
                    }
                    
                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Logging.info("🔌 WebSocket connection closed - Code: " + code + ", Reason: " + reason + ", Remote: " + remote);
                        isConnected.set(false);
                        isConnecting.set(false);
                    }
                    
                    @Override
                    public void onError(Exception ex) {
                        Logging.error("❌ WebSocket error occurred: " + ex.getMessage(), ex);
                        isConnected.set(false);
                        isConnecting.set(false);
                    }
                };
                
                webSocketClient.setConnectionLostTimeout(WebSocketConfig.WS_CONNECTION_TIMEOUT);
                webSocketClient.connect();
                
                // Wait for connection to be established
                int attempts = 0;
                while (!isConnected.get() && attempts < 10) {
                    Thread.sleep(500);
                    attempts++;
                }
                
                if (isConnected.get()) {
                    Logging.info("✅ WebSocket connection established successfully");
                } else {
                    Logging.warning("⏰ WebSocket connection timeout - server may not be running");
                }
                
                return isConnected.get();
                
            } catch (Exception e) {
                Logging.error("❌ Failed to connect to WebSocket server: " + e.getMessage(), e);
                isConnecting.set(false);
                return false;
            }
        });
    }
    
    /**
     * Sends a test message after successful login
     */
    private void sendTestMessage() {
        if (isConnected.get() && currentUsername != null) {
            String testMessage = "{\"type\":\"test\",\"username\":\"" + currentUsername + "\",\"message\":\"Hello from OMG Platform!\"}";
            boolean sent = sendMessage(testMessage);
            if (sent) {
                Logging.info("✅ Test message sent to WebSocket server for user: " + currentUsername);
                Logging.info("📤 Message content: " + testMessage);
            } else {
                Logging.warning("❌ Failed to send test message to WebSocket server for user: " + currentUsername);
            }
        } else {
            Logging.warning("Cannot send test message: WebSocket not connected or username is null");
        }
    }
    
    /**
     * Sends a message through the WebSocket connection
     * @param message The message to send
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String message) {
        if (!isConnected.get() || webSocketClient == null) {
            Logging.warning("Cannot send message: WebSocket not connected");
            return false;
        }
        
        try {
            webSocketClient.send(message);
            Logging.info("Message sent via WebSocket: " + message);
            return true;
        } catch (Exception e) {
            Logging.error("Failed to send WebSocket message: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Handles incoming WebSocket messages
     * @param message The received message
     */
    private void handleIncomingMessage(String message) {
        try {
            // For now, just log the message
            // In a real application, you would parse the message and handle different types
            Logging.info("Processing incoming WebSocket message: " + message);
            
            // TODO: Add message parsing and handling logic based on message type
            // Example: handle game updates, chat messages, notifications, etc.
            
        } catch (Exception e) {
            Logging.error("Error handling incoming WebSocket message: " + e.getMessage(), e);
        }
    }
    
    /**
     * Disconnects from the WebSocket server
     */
    public void disconnect() {
        if (webSocketClient != null && isConnected.get()) {
            Logging.info("Disconnecting from WebSocket server");
            webSocketClient.close();
            isConnected.set(false);
            isConnecting.set(false);
        }
    }
    
    /**
     * Checks if the WebSocket is currently connected
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return isConnected.get();
    }
    
    /**
     * Gets the current username associated with this WebSocket connection
     * @return The current username
     */
    public String getCurrentUsername() {
        return currentUsername;
    }
} 