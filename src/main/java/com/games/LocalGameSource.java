package com.games;

import com.games.modules.tictactoe.TicTacToeModule;
import com.games.modules.Connect4Module;
import com.games.modules.example.ExampleGameModule;
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
            // Add known local games
            games.add(new TicTacToeModule());
            games.add(new Connect4Module());
            games.add(new ExampleGameModule());
            
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