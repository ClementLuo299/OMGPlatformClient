package com.games.modules.example;

import com.games.GameModule;
import com.games.enums.GameDifficulty;
import com.games.enums.GameMode;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Example game module - demonstrates the standardized Main class structure.
 * This is the main entry point for the example game module.
 *
 * @authors Clement Luo
 * @date July 19, 2025
 * @since 1.0
 */
public class Main implements GameModule {
    
    private static final String GAME_ID = "example";
    private static final String GAME_NAME = "Example Game";
    private static final String GAME_DESCRIPTION = "Template game for development and testing";
    
    @Override
    public String getGameId() {
        return GAME_ID;
    }
    
    @Override
    public String getGameName() {
        return GAME_NAME;
    }
    
    @Override
    public String getGameDescription() {
        return GAME_DESCRIPTION;
    }
    
    @Override
    public int getMinPlayers() {
        return 1;
    }
    
    @Override
    public int getMaxPlayers() {
        return 4;
    }
    
    @Override
    public int getEstimatedDuration() {
        return 10; // 10 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.EASY;
    }
    
    @Override
    public String getGameCategory() {
        return "Classic";
    }
    
    @Override
    public boolean supportsOnlineMultiplayer() {
        return true;
    }
    
    @Override
    public boolean supportsLocalMultiplayer() {
        return true;
    }
    
    @Override
    public boolean supportsSinglePlayer() {
        return true;
    }
    
    @Override
    public String getGameFxmlPath() {
        return "/games/example/fxml/example.fxml";
    }
    
    @Override
    public String getGameCssPath() {
        return "/games/example/css/example.css";
    }
    
    @Override
    public String getGameIconPath() {
        return "/games/example/icons/example_icon.png";
    }
    
    @Override
    public Scene launchGame(Stage stage, GameMode mode, int playerCount, GameOptions options) {
        try {
            Logging.info("🎮 Launching " + GAME_NAME + " in " + mode + " mode with " + playerCount + " players");
            
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getGameFxmlPath()));
            Scene scene = new Scene(loader.load());
            
            // Apply CSS if available
            if (getGameCssPath() != null) {
                scene.getStylesheets().add(getClass().getResource(getGameCssPath()).toExternalForm());
            }
            
            // Set up the stage
            stage.setTitle(GAME_NAME);
            stage.setScene(scene);
            stage.setResizable(true);
            
            // Show the game
            stage.show();
            
            Logging.info("✅ " + GAME_NAME + " launched successfully");
            return scene;
            
        } catch (Exception e) {
            Logging.error("❌ Error launching " + GAME_NAME + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public GameState getGameState() {
        return new GameState(GAME_ID, "Not Started", 0, 0);
    }
    
    @Override
    public void pauseGame() {
        Logging.info("⏸️ " + GAME_NAME + " paused");
    }
    
    @Override
    public void resumeGame() {
        Logging.info("▶️ " + GAME_NAME + " resumed");
    }
    
    @Override
    public void endGame() {
        Logging.info("🏁 " + GAME_NAME + " ended");
    }
    
    @Override
    public void onGameClose() {
        Logging.info("🔒 " + GAME_NAME + " closing - cleaning up resources");
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading game state for " + GAME_NAME);
    }
} 