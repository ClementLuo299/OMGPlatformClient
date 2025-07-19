package com.games.sourcing;

import com.games.GameModule;
import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Discovers games from remote servers or APIs.
 * This source can fetch games from external game repositories.
 * 
 * @authors Clement Luo
 * @date July 19, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public class RemoteGameSource implements GameSource {
    
    private static final String SOURCE_NAME = "Remote Games";
    private static final String DEFAULT_SERVER_URL = "https://api.omgplatform.com/games";
    
    private final String serverUrl;
    private boolean isEnabled;
    
    public RemoteGameSource() {
        this(DEFAULT_SERVER_URL);
    }
    
    public RemoteGameSource(String serverUrl) {
        this.serverUrl = serverUrl;
        this.isEnabled = false; // Disabled by default for now
    }
    
    @Override
    public String getName() {
        return SOURCE_NAME;
    }
    
    @Override
    public boolean isAvailable() {
        return isEnabled && serverUrl != null && !serverUrl.trim().isEmpty();
    }
    
    @Override
    public List<GameModule> discoverGames() {
        Logging.info("🔍 Discovering remote games from: " + serverUrl);
        List<GameModule> games = new ArrayList<>();
        
        if (!isAvailable()) {
            Logging.info("⏸️ Remote game source is not available or disabled");
            return games;
        }
        
        try {
            // TODO: Implement actual remote game fetching
            // This would typically involve:
            // 1. Making HTTP requests to the game server
            // 2. Parsing JSON/XML responses
            // 3. Dynamically loading game modules
            // 4. Validating game integrity
            
            Logging.info("🌐 Remote game fetching not yet implemented");
            
        } catch (Exception e) {
            Logging.error("❌ Error discovering remote games: " + e.getMessage(), e);
        }
        
        return games;
    }
    
    /**
     * Enables or disables this remote source.
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        Logging.info("🔄 Remote game source " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Gets the server URL for this source.
     * @return The server URL
     */
    public String getServerUrl() {
        return serverUrl;
    }
    
    /**
     * Sets the server URL for this source.
     * @param serverUrl The new server URL
     */
    public void setServerUrl(String serverUrl) {
        // This would need to be implemented with proper URL validation
        Logging.info("🔄 Server URL would be updated to: " + serverUrl);
    }
} 