package com.test;

import com.services.WebSocketService;
import com.core.ServiceManager;
import com.utils.error_handling.Logging;

/**
 * Simple test class to demonstrate WebSocket functionality.
 * This class can be used to test WebSocket connections independently.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class WebSocketTest {
    
    public static void main(String[] args) {
        Logging.info("🧪 Starting WebSocket test...");
        
        try {
            // Get WebSocket service from ServiceManager
            WebSocketService webSocketService = ServiceManager.getInstance().getWebSocketService();
            
            // Test connection
            String testUsername = "testuser";
            Logging.info("🔌 Testing WebSocket connection for user: " + testUsername);
            
            webSocketService.connect(testUsername).thenAccept(success -> {
                if (success) {
                    Logging.info("✅ WebSocket test successful!");
                    
                    // Test sending a custom message
                    String customMessage = "{\"type\":\"custom\",\"username\":\"" + testUsername + "\",\"message\":\"This is a custom test message!\"}";
                    boolean sent = webSocketService.sendMessage(customMessage);
                    
                    if (sent) {
                        Logging.info("📤 Custom test message sent successfully");
                    } else {
                        Logging.warning("❌ Failed to send custom test message");
                    }
                    
                    // Disconnect after a short delay
                    try {
                        Thread.sleep(2000);
                        webSocketService.disconnect();
                        Logging.info("🔌 WebSocket test completed - connection closed");
                    } catch (InterruptedException e) {
                        Logging.error("Test interrupted: " + e.getMessage(), e);
                    }
                    
                } else {
                    Logging.error("❌ WebSocket test failed - could not establish connection");
                    Logging.info("💡 Make sure the WebSocket server is running at: " + com.config.WebSocketConfig.getGeneralWebSocketUrl());
                }
            }).exceptionally(throwable -> {
                Logging.error("❌ WebSocket test failed with exception: " + throwable.getMessage(), throwable);
                return null;
            });
            
        } catch (Exception e) {
            Logging.error("❌ WebSocket test setup failed: " + e.getMessage(), e);
        }
    }
} 