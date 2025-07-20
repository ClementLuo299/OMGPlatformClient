package com.services;

import com.game.GameModule;
import com.game.GameOptions;
import com.game.GameState;
import com.game.enums.GameDifficulty;
import com.game.enums.GameMode;
import com.utils.error_handling.Logging;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing and launching game modules.
 * Handles game registration, discovery, and launching with proper lifecycle management.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class GameManager {
    
    private static GameManager instance;
    private final Map<String, GameModule> registeredGames;
    private final Map<String, GameState> savedGames;
    private GameModule currentGame;
    private Stage currentGameStage;
    
    private GameManager() {
        this.registeredGames = new ConcurrentHashMap<>();
        this.savedGames = new ConcurrentHashMap<>();
        this.currentGame = null;
        this.currentGameStage = null;
    }
    
    /**
     * Gets the singleton instance of GameManager.
     * @return The GameManager instance
     */
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    /**
     * Registers a game module.
     * @param gameModule The game module to register
     */
    public void registerGame(GameModule gameModule) {
        if (gameModule == null) {
            Logging.warning("Attempted to register null game module");
            return;
        }
        
        String gameId = gameModule.getGameId();
        if (gameId == null || gameId.trim().isEmpty()) {
            Logging.warning("Game module has null or empty game ID");
            return;
        }
        
        registeredGames.put(gameId, gameModule);
        Logging.info("🎮 Registered game: " + gameModule.getGameName() + " (ID: " + gameId + ")");
    }
    
    /**
     * Unregisters a game module.
     * @param gameId The game ID to unregister
     */
    public void unregisterGame(String gameId) {
        GameModule game = registeredGames.remove(gameId);
        if (game != null) {
            Logging.info("🗑️ Unregistered game: " + game.getGameName() + " (ID: " + gameId + ")");
        }
    }
    
    /**
     * Gets all registered games.
     * @return List of all registered game modules
     */
    public List<GameModule> getAllGames() {
        return new ArrayList<>(registeredGames.values());
    }
    
    /**
     * Gets a specific game module.
     * @param gameId The game ID
     * @return The game module, or null if not found
     */
    public GameModule getGame(String gameId) {
        return registeredGames.get(gameId);
    }
    
    /**
     * Checks if a game is registered.
     * @param gameId The game ID
     * @return true if the game is registered
     */
    public boolean isGameRegistered(String gameId) {
        return registeredGames.containsKey(gameId);
    }
    
    /**
     * Gets the number of registered games.
     * @return Number of registered games
     */
    public int getGameCount() {
        return registeredGames.size();
    }
    
    /**
     * Launches a game with the specified parameters.
     * 
     * @param gameId The game ID to launch
     * @param primaryStage The primary JavaFX stage
     * @param gameMode The game mode
     * @param playerCount Number of players
     * @param gameOptions Game-specific options
     * @return The game scene, or null if launch failed
     */
    public Scene launchGame(String gameId, Stage primaryStage, GameMode gameMode, 
                          int playerCount, GameOptions gameOptions) {
        
        Logging.info("🚀 Launching game: " + gameId + " with mode: " + gameMode.getDisplayName());
        
        // Check if game is registered
        GameModule game = getGame(gameId);
        if (game == null) {
            Logging.error("❌ Game not found: " + gameId);
            return null;
        }
        
        // Validate player count
        if (playerCount < game.getMinPlayers() || playerCount > game.getMaxPlayers()) {
            Logging.error("❌ Invalid player count: " + playerCount + " for game: " + gameId);
            return null;
        }
        
        // Validate game mode support
        if (!isGameModeSupported(game, gameMode)) {
            Logging.error("❌ Game mode not supported: " + gameMode.getDisplayName() + " for game: " + gameId);
            return null;
        }
        
        try {
            // Close current game if any
            closeCurrentGame();
            
            // Launch the new game (this now just creates the scene, doesn't set it on stage)
            Scene gameScene = game.launchGame(primaryStage, gameMode, playerCount, gameOptions);
            
            if (gameScene != null) {
                currentGame = game;
                currentGameStage = primaryStage;
                
                // Set the game scene on the stage
                primaryStage.setScene(gameScene);
                primaryStage.setTitle(game.getGameName() + " - " + gameMode.getDisplayName());
                primaryStage.setResizable(false);
                
                Logging.info("✅ Game launched successfully: " + game.getGameName());
            } else {
                Logging.error("❌ Failed to launch game: " + game.getGameName());
            }
            
            return gameScene;
            
        } catch (Exception e) {
            Logging.error("❌ Error launching game: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Launches a game and integrates it with the ScreenManager for proper navigation.
     * 
     * @param gameId The game ID to launch
     * @param gameMode The game mode
     * @param playerCount Number of players
     * @param gameOptions Game-specific options
     * @return true if the game was launched successfully
     */
    public boolean launchGameIntegrated(String gameId, GameMode gameMode, 
                                      int playerCount, GameOptions gameOptions) {
        
        Logging.info("🎮 Launching integrated game: " + gameId + " with mode: " + gameMode.getDisplayName());
        
        // Check if game is registered
        GameModule game = getGame(gameId);
        if (game == null) {
            Logging.error("❌ Game not found: " + gameId);
            return false;
        }
        
        // Validate player count
        if (playerCount < game.getMinPlayers() || playerCount > game.getMaxPlayers()) {
            Logging.error("❌ Invalid player count: " + playerCount + " for game: " + gameId);
            return false;
        }
        
        // Validate game mode support
        if (!isGameModeSupported(game, gameMode)) {
            Logging.error("❌ Game mode not supported: " + gameMode.getDisplayName() + " for game: " + gameId);
            return false;
        }
        
        try {
            // Close current game if any
            closeCurrentGame();
            
            // Special handling for TicTacToe - use module's own screen system
            if ("tictactoe".equals(gameId)) {
                return launchTicTacToeIntegrated(game, gameMode, playerCount, gameOptions);
            }
            
            // For other games, use the generic approach
            return launchGenericGameIntegrated(game, gameMode, playerCount, gameOptions);
            
        } catch (Exception e) {
            Logging.error("❌ Error launching integrated game: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Launches TicTacToe using its module-specific screen system.
     * 
     * @param game The TicTacToe game module
     * @param gameMode The game mode
     * @param playerCount Number of players
     * @param gameOptions Game-specific options
     * @return true if the game was launched successfully
     */
    private boolean launchTicTacToeIntegrated(GameModule game, GameMode gameMode, 
                                            int playerCount, GameOptions gameOptions) {
        Logging.info("🎮 Launching TicTacToe using module-specific screen system");
        
        try {
            // For now, use the generic approach but with TicTacToe-specific initialization
            com.core.screens.ScreenManager screenManager = com.core.screens.ScreenManager.getInstance();
            
            // Navigate to the game using the navigateToGame method
            Object controller = screenManager.navigateToGame(
                game.getGameFxmlPath(),
                game.getGameCssPath(),
                game.getGameName() + " - " + gameMode.getDisplayName()
            );
            
            if (controller != null) {
                currentGame = game;
                currentGameStage = screenManager.getMainStage();
                
                // Initialize the game controller with game parameters
                initializeGameController(controller, gameMode, playerCount, gameOptions);
                
                Logging.info("✅ TicTacToe launched successfully using main app's screen system");
                return true;
            } else {
                Logging.error("❌ Failed to get controller from TicTacToe navigation");
                return false;
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error launching TicTacToe: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Launches a generic game using the main app's screen system.
     * 
     * @param game The game module
     * @param gameMode The game mode
     * @param playerCount Number of players
     * @param gameOptions Game-specific options
     * @return true if the game was launched successfully
     */
    private boolean launchGenericGameIntegrated(GameModule game, GameMode gameMode, 
                                              int playerCount, GameOptions gameOptions) {
        Logging.info("🎮 Launching generic game using main app's screen system");
        
        try {
            com.core.screens.ScreenManager screenManager = com.core.screens.ScreenManager.getInstance();
            
            // Navigate to the game using the new navigateToGame method
            Object controller = screenManager.navigateToGame(
                game.getGameFxmlPath(),
                game.getGameCssPath(),
                game.getGameName() + " - " + gameMode.getDisplayName()
            );
            
            if (controller != null) {
                currentGame = game;
                currentGameStage = screenManager.getMainStage();
                
                // Initialize the game controller if it has an initializeGame method
                initializeGameController(controller, gameMode, playerCount, gameOptions);
                
                Logging.info("✅ Generic game launched successfully: " + game.getGameName());
                return true;
            } else {
                Logging.error("❌ Failed to get controller from game navigation");
                return false;
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error navigating to generic game: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Initializes the game controller with game parameters.
     * 
     * @param controller The game controller
     * @param gameMode The game mode
     * @param playerCount Number of players
     * @param gameOptions Game options
     */
    private void initializeGameController(Object controller, GameMode gameMode, 
                                        int playerCount, GameOptions gameOptions) {
        try {
            // Use reflection to call the initializeGame method if it exists
            java.lang.reflect.Method initMethod = controller.getClass().getMethod(
                "initializeGame", 
                GameMode.class, 
                int.class, 
                GameOptions.class
            );
            
            if (initMethod != null) {
                initMethod.invoke(controller, gameMode, playerCount, gameOptions);
                Logging.info("✅ Game controller initialized with parameters");
            }
        } catch (NoSuchMethodException e) {
            Logging.info("ℹ️ Game controller doesn't have initializeGame method, skipping initialization");
        } catch (Exception e) {
            Logging.error("❌ Error initializing game controller: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if a game supports a specific game mode.
     * @param game The game module
     * @param gameMode The game mode to check
     * @return true if the game mode is supported
     */
    private boolean isGameModeSupported(GameModule game, GameMode gameMode) {
        switch (gameMode) {
            case SINGLE_PLAYER:
                return game.supportsSinglePlayer();
            case LOCAL_MULTIPLAYER:
                return game.supportsLocalMultiplayer();
            case ONLINE_MULTIPLAYER:
                return game.supportsOnlineMultiplayer();
            default:
                return false;
        }
    }
    
    /**
     * Closes the currently running game.
     */
    public void closeCurrentGame() {
        if (currentGame != null) {
            Logging.info("🔄 Closing current game: " + currentGame.getGameName());
            
            try {
                currentGame.onGameClose();
            } catch (Exception e) {
                Logging.error("❌ Error closing game: " + e.getMessage(), e);
            }
            
            currentGame = null;
            currentGameStage = null;
        }
    }
    
    /**
     * Gets the currently running game.
     * @return The current game module, or null if no game is running
     */
    public GameModule getCurrentGame() {
        return currentGame;
    }
    
    /**
     * Gets the current game stage.
     * @return The current game stage, or null if no game is running
     */
    public Stage getCurrentGameStage() {
        return currentGameStage;
    }
    
    /**
     * Saves the current game state.
     * @return The saved game state, or null if no game is running
     */
    public GameState saveCurrentGame() {
        if (currentGame == null) {
            Logging.warning("⚠️ No game running to save");
            return null;
        }
        
        try {
            GameState gameState = currentGame.getGameState();
            if (gameState != null) {
                savedGames.put(gameState.getGameId() + "_" + System.currentTimeMillis(), gameState);
                Logging.info("💾 Saved game state: " + currentGame.getGameName());
                return gameState;
            }
        } catch (Exception e) {
            Logging.error("❌ Error saving game state: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Loads a saved game state.
     * @param gameState The game state to load
     * @return true if the game was loaded successfully
     */
    public boolean loadGame(GameState gameState) {
        if (gameState == null) {
            Logging.warning("⚠️ No game state provided to load");
            return false;
        }
        
        GameModule game = getGame(gameState.getGameId());
        if (game == null) {
            Logging.error("❌ Game not found for saved state: " + gameState.getGameId());
            return false;
        }
        
        try {
            game.loadGameState(gameState);
            Logging.info("📂 Loaded game state: " + game.getGameName());
            return true;
        } catch (Exception e) {
            Logging.error("❌ Error loading game state: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Gets all saved games.
     * @return List of all saved game states
     */
    public List<GameState> getAllSavedGames() {
        return new ArrayList<>(savedGames.values());
    }
    
    /**
     * Gets saved games for a specific game.
     * @param gameId The game ID
     * @return List of saved game states for the specified game
     */
    public List<GameState> getSavedGames(String gameId) {
        List<GameState> gameStates = new ArrayList<>();
        for (GameState state : savedGames.values()) {
            if (gameId.equals(state.getGameId())) {
                gameStates.add(state);
            }
        }
        return gameStates;
    }
    
    /**
     * Deletes a saved game.
     * @param gameState The game state to delete
     */
    public void deleteSavedGame(GameState gameState) {
        if (gameState != null) {
            savedGames.values().removeIf(state -> 
                state.getGameId().equals(gameState.getGameId()) && 
                state.getSaveTime().equals(gameState.getSaveTime()));
            Logging.info("🗑️ Deleted saved game: " + gameState.getDisplayName());
        }
    }
    
    /**
     * Gets games filtered by criteria.
     * @param gameMode The game mode to filter by (null for all modes)
     * @param maxPlayers Maximum number of players (null for any)
     * @param difficulty The difficulty level (null for any)
     * @return List of games matching the criteria
     */
    public List<GameModule> getGamesByCriteria(GameMode gameMode, Integer maxPlayers, 
                                              GameDifficulty difficulty) {
        List<GameModule> filteredGames = new ArrayList<>();
        
        for (GameModule game : registeredGames.values()) {
            boolean matches = true;
            
            if (gameMode != null && !isGameModeSupported(game, gameMode)) {
                matches = false;
            }
            
            if (maxPlayers != null && game.getMaxPlayers() > maxPlayers) {
                matches = false;
            }
            
            if (difficulty != null && game.getDifficulty() != difficulty) {
                matches = false;
            }
            
            if (matches) {
                filteredGames.add(game);
            }
        }
        
        return filteredGames;
    }
} 