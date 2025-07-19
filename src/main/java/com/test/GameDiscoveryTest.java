package com.test;

import com.games.GameRegistry;
import com.games.GameModule;
import com.utils.error_handling.Logging;

import java.util.List;

/**
 * Simple test to verify game discovery is working.
 * Run this to check if games are being discovered and registered.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class GameDiscoveryTest {
    
    public static void main(String[] args) {
        Logging.info("🧪 Starting Game Discovery Test...");
        
        try {
            // Get the game registry instance
            GameRegistry registry = GameRegistry.getInstance();
            
            // Initialize the registry (this will discover games)
            registry.initialize();
            
            // Wait a bit for async discovery to complete
            Thread.sleep(2000);
            
            // Get all discovered games
            List<GameModule> games = registry.getAllGames();
            
            Logging.info("🎮 Found " + games.size() + " games:");
            
            for (GameModule game : games) {
                Logging.info("  - " + game.getGameName() + " (ID: " + game.getGameId() + ")");
                Logging.info("    Category: " + game.getGameCategory());
                Logging.info("    Difficulty: " + game.getDifficulty().getDisplayName());
                Logging.info("    Players: " + game.getMinPlayers() + "-" + game.getMaxPlayers());
                Logging.info("    Duration: " + game.getEstimatedDuration() + " minutes");
                Logging.info("    Supports:");
                Logging.info("      - Single Player: " + game.supportsSinglePlayer());
                Logging.info("      - Local Multiplayer: " + game.supportsLocalMultiplayer());
                Logging.info("      - Online Multiplayer: " + game.supportsOnlineMultiplayer());
                Logging.info("");
            }
            
            // Print summary
            Logging.info(registry.getGamesSummary());
            
        } catch (Exception e) {
            Logging.error("❌ Test failed: " + e.getMessage(), e);
        }
    }
} 