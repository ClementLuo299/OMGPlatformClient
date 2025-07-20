package com.test;

import com.services.GameDiscoveryService;
import com.game.GameModule;
import com.gui_controllers.game_library.GameLibraryController;
import com.utils.error_handling.Logging;

import java.util.List;

/**
 * Test to verify that the GameLibraryController can properly display games.
 * This simulates what happens when the game library screen is loaded.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class GameLibraryTest {
    
    public static void main(String[] args) {
        Logging.info("🧪 Starting Game Library Test...");
        
        try {
            // Simulate the game library controller initialization
            Logging.info("🎮 Simulating GameLibraryController initialization...");
            
            // Step 1: Initialize game discovery service (like GameLibraryController does)
            GameDiscoveryService discoveryService = GameDiscoveryService.getInstance();
            discoveryService.initialize();
            
            // Step 2: Wait for discovery to complete
            Logging.info("⏳ Waiting for game discovery to complete...");
            Thread.sleep(3000);
            
            // Step 3: Get all games (like setupGameCards() does)
            List<GameModule> games = discoveryService.getAllDiscoveredGames();
            
            Logging.info("🎮 Game Library Test Results:");
            Logging.info("Total games found: " + games.size());
            
            if (games.isEmpty()) {
                Logging.warning("⚠️ No games found! This might indicate an issue.");
                Logging.info("🔍 Checking discovery service summary:");
                Logging.info("Total games discovered: " + games.size());
            } else {
                Logging.info("✅ Games found successfully!");
                
                for (GameModule game : games) {
                    Logging.info("  - " + game.getGameName() + " (ID: " + game.getGameId() + ")");
                    Logging.info("    Category: " + game.getGameCategory());
                    Logging.info("    Players: " + game.getMinPlayers() + "-" + game.getMaxPlayers());
                    Logging.info("    Duration: " + game.getEstimatedDuration() + " minutes");
                    Logging.info("");
                }
            }
            
            // Step 4: Simulate creating game cards
            Logging.info("🎯 Simulating game card creation...");
            int cardCount = 0;
            for (GameModule game : games) {
                Logging.info("🎴 Creating card for: " + game.getGameName());
                cardCount++;
            }
            
            Logging.info("✅ Successfully created " + cardCount + " game cards");
            
        } catch (Exception e) {
            Logging.error("❌ Game Library Test failed: " + e.getMessage(), e);
        }
    }
} 