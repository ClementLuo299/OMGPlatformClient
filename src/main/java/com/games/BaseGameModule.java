package com.games;

import com.utils.error_handling.Logging;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Base class for game modules that provides common functionality.
 * Reduces code duplication and provides a consistent structure for all games.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public abstract class BaseGameModule implements GameModule {
    
    /**
     * Gets the game's base resource path.
     * @return The base path for this game's resources
     */
    protected abstract String getGameBasePath();
    
    /**
     * Gets the game's controller class.
     * @return The controller class for this game
     */
    protected abstract Class<?> getGameControllerClass();
    
    /**
     * Gets the game's controller instance after loading.
     * @param controller The loaded controller
     */
    protected abstract void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions);
    
    @Override
    public Scene launchGame(Stage primaryStage, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        Logging.info("🎮 Launching " + getGameName() + " with mode: " + gameMode.getDisplayName() + ", players: " + playerCount);
        
        try {
            // Load the game's FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getGameFxmlPath()));
            
            // Create the scene
            Scene scene = new Scene(loader.load());
            
            // Apply CSS if available
            String cssPath = getGameCssPath();
            if (cssPath != null && !cssPath.isEmpty()) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }
            
            // Set up the stage
            primaryStage.setTitle(getGameName() + " - " + gameMode.getDisplayName());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            // Get the controller and initialize it
            Object controller = loader.getController();
            if (controller != null) {
                initializeGameController(controller, gameMode, playerCount, gameOptions);
            }
            
            Logging.info("✅ " + getGameName() + " launched successfully");
            return scene;
            
        } catch (IOException e) {
            Logging.error("❌ Failed to launch " + getGameName() + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public String getGameIconPath() {
        return getGameBasePath() + "/icons/" + getGameId() + ".png";
    }
    
    @Override
    public String getGameFxmlPath() {
        return getGameBasePath() + "/fxml/" + getGameId() + ".fxml";
    }
    
    @Override
    public String getGameCssPath() {
        return getGameBasePath() + "/css/" + getGameId() + ".css";
    }
    
    @Override
    public String getGameCategory() {
        return "Classic"; // Default category - subclasses can override
    }
    
    @Override
    public void onGameClose() {
        Logging.info("🔄 " + getGameName() + " closing - cleaning up resources");
        // Subclasses can override this to add specific cleanup
    }
    
    @Override
    public GameState getGameState() {
        // Default implementation - subclasses should override
        GameOptions options = new GameOptions();
        options.setOption("gameType", getGameId());
        options.setOption("saveTime", System.currentTimeMillis());
        
        GameState gameState = new GameState(getGameId(), getGameName(), GameMode.LOCAL_MULTIPLAYER, getMinPlayers(), options);
        
        // Add basic state data
        gameState.setStateValue("gameId", getGameId());
        gameState.setStateValue("gameName", getGameName());
        gameState.setStateValue("saveTimestamp", System.currentTimeMillis());
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading " + getGameName() + " game state");
        
        if (gameState != null) {
            // Load basic state data
            String gameId = gameState.getStringStateValue("gameId", getGameId());
            String gameName = gameState.getStringStateValue("gameName", getGameName());
            long saveTimestamp = gameState.getIntStateValue("saveTimestamp", 0);
            
            Logging.info("📊 Loaded game state - Game: " + gameName + ", Saved: " + saveTimestamp);
        }
    }
} 