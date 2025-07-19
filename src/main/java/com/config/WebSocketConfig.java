package com.config;

/**
 * Configuration class for WebSocket connections.
 * Stores WebSocket server settings and endpoints.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class WebSocketConfig {
    
    // WebSocket server URL - update this with your server's IP and port
    private static final String WS_SERVER_URL = "ws://192.168.2.82:8080";
    
    // WebSocket endpoints
    private static final String WS_GENERAL_ENDPOINT = "/websocket";
    private static final String WS_GAME_ENDPOINT = "/websocket/game";
    private static final String WS_CHAT_ENDPOINT = "/websocket/chat";
    
    /**
     * Gets the general WebSocket URL for basic messaging
     * @return WebSocket URL for general communication
     */
    public static String getGeneralWebSocketUrl() {
        return WS_SERVER_URL + WS_GENERAL_ENDPOINT;
    }
    
    /**
     * Gets the game WebSocket URL for game-specific messaging
     * @return WebSocket URL for game communication
     */
    public static String getGameWebSocketUrl() {
        return WS_SERVER_URL + WS_GAME_ENDPOINT;
    }
    
    /**
     * Gets the chat WebSocket URL for chat messaging
     * @return WebSocket URL for chat communication
     */
    public static String getChatWebSocketUrl() {
        return WS_SERVER_URL + WS_CHAT_ENDPOINT;
    }
    
    /**
     * Gets the base WebSocket server URL
     * @return Base WebSocket server URL
     */
    public static String getWebSocketServerUrl() {
        return WS_SERVER_URL;
    }
    
    // WebSocket connection settings
    public static final int WS_CONNECTION_TIMEOUT = 5000; // 5 seconds
    public static final int WS_RECONNECT_ATTEMPTS = 3;
    public static final int WS_RECONNECT_DELAY = 1000; // 1 second
} 