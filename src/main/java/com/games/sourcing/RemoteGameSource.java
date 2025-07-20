package com.games.sourcing;

import com.config.ModuleConfig;
import com.games.GameModule;
import com.utils.error_handling.Logging;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class RemoteGameSource implements GameSource {
    
    private final String serverUrl;
    @Setter
    private boolean enabled;
    
    public RemoteGameSource() {
        this(ModuleConfig.DEFAULT_REMOTE_SERVER_URL);
    }
    
    public RemoteGameSource(String serverUrl) {
        this.serverUrl = serverUrl;
        this.enabled = false; // Disabled by default for now
    }
    
    @Override
    public String getName() {
        return ModuleConfig.REMOTE_SOURCE_NAME;
    }
    
    @Override
    public boolean isAvailable() {
        return enabled && serverUrl != null && !serverUrl.trim().isEmpty();
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
        this.enabled = enabled;
        Logging.info("🔄 Remote game source " + (enabled ? "enabled" : "disabled"));
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