package com.games.sourcing;

import com.games.GameModule;
import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Discovers games from the modules directory dynamically.
 * This source scans the modules folder and loads game modules at runtime.
 * 
 * @authors Clement Luo
 * @date July 19, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public class LocalGameSource implements GameSource {
    
    private static final String SOURCE_NAME = "Local Games";
    
    @Override
    public String getName() {
        return SOURCE_NAME;
    }
    
    @Override
    public boolean isAvailable() {
        return true; // Local source is always available
    }
    
    @Override
    public List<GameModule> discoverGames() {
        Logging.info("🔍 Discovering games from modules directory...");
        
        try {
            List<GameModule> games = ModuleLoader.loadAllModules();
            Logging.info("✅ Discovered " + games.size() + " games from modules directory");
            return games;
            
        } catch (Exception e) {
            Logging.error("❌ Error discovering games from modules: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
} 