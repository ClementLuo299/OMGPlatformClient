package com.games;


import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Discovers games from the local classpath and modules directory.
 * This source finds games that are bundled with the application.
 * 
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class LocalGameSource implements GameDiscoveryService.GameSource {
    
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
        Logging.info("🔍 Discovering local games...");
        List<GameModule> games = new ArrayList<>();
        
        try {
            // No local games currently available
            // Games are now in separate modules
            
            // TODO: Add more local games here as they are developed
            // games.add(new CheckersModule());
            // games.add(new ChessModule());
            // games.add(new BattleshipModule());
            // games.add(new SnakeModule());
            // games.add(new TetrisModule());
            
            Logging.info("✅ Discovered " + games.size() + " local games");
            
        } catch (Exception e) {
            Logging.error("❌ Error discovering local games: " + e.getMessage(), e);
        }
        
        return games;
    }
} 