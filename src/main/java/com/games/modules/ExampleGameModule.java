package com.games.modules;

import com.games.BaseGameModule;
import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Example game module to demonstrate the dynamic game discovery system.
 * This shows how easy it is to add new games without modifying the GUI.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class ExampleGameModule extends BaseGameModule {
    
    private static final String GAME_ID = "example-game";
    private static final String GAME_NAME = "Example Game";
    private static final String GAME_DESCRIPTION = "A simple example game to demonstrate dynamic discovery";
    
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
        return 15; // 15 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.MEDIUM;
    }
    
    @Override
    public String getGameCategory() {
        return "Puzzle";
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
    protected String getGameBasePath() {
        return "/games/example";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return ExampleGameController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof ExampleGameController) {
            ExampleGameController exampleController = (ExampleGameController) controller;
            exampleController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        GameOptions options = new GameOptions();
        options.setOption("exampleOption", "exampleValue");
        options.setOption("difficulty", "medium");
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.LOCAL_MULTIPLAYER, 2, options);
        
        // Add example state data
        gameState.setStateValue("exampleData", "This is example game data");
        gameState.setStateValue("score", 0);
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading Example Game state");
        
        if (gameState != null) {
            String exampleData = gameState.getStringStateValue("exampleData", "No data");
            int score = gameState.getIntStateValue("score", 0);
            
            Logging.info("📊 Loaded example game state - Data: " + exampleData + ", Score: " + score);
        }
    }
    
    /**
     * Example game controller (placeholder).
     * In a real implementation, this would be a separate controller class.
     */
    public static class ExampleGameController {
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            Logging.info("🎯 Initializing Example Game with " + playerCount + " players");
            Logging.info("🎮 Game mode: " + gameMode.getDisplayName());
            
            // Example game initialization logic would go here
            String exampleOption = gameOptions.getStringOption("exampleOption", "default");
            Logging.info("⚙️ Example option: " + exampleOption);
        }
    }
} 