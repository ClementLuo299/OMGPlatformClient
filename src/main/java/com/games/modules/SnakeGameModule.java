package com.games.modules;

import com.games.BaseGameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Snake game module - a classic arcade game.
 * Placeholder implementation for testing the dynamic game library.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class SnakeGameModule extends BaseGameModule {
    
    private static final String GAME_ID = "snake";
    private static final String GAME_NAME = "Snake";
    private static final String GAME_DESCRIPTION = "Classic arcade game where you control a snake to eat food and grow longer";
    
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
        return 1;
    }
    
    @Override
    public int getEstimatedDuration() {
        return 5; // 5 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.EASY;
    }
    
    @Override
    public String getGameCategory() {
        return "Arcade";
    }
    
    @Override
    public boolean supportsOnlineMultiplayer() {
        return false;
    }
    
    @Override
    public boolean supportsLocalMultiplayer() {
        return false;
    }
    
    @Override
    public boolean supportsSinglePlayer() {
        return true;
    }
    
    @Override
    protected String getGameBasePath() {
        return "/games/snake";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return SnakeGameController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof SnakeGameController) {
            SnakeGameController snakeController = (SnakeGameController) controller;
            snakeController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        GameOptions options = new GameOptions();
        options.setOption("speed", "medium");
        options.setOption("gridSize", 20);
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.SINGLE_PLAYER, 1, options);
        
        // Add snake game state data
        gameState.setStateValue("score", 0);
        gameState.setStateValue("snakeLength", 3);
        gameState.setStateValue("foodPosition", "10,10");
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading Snake game state");
        
        if (gameState != null) {
            int score = gameState.getIntStateValue("score", 0);
            int snakeLength = gameState.getIntStateValue("snakeLength", 3);
            String foodPosition = gameState.getStringStateValue("foodPosition", "10,10");
            
            Logging.info("🐍 Loaded snake game state - Score: " + score + ", Length: " + snakeLength + ", Food: " + foodPosition);
        }
    }
    
    /**
     * Snake game controller (placeholder).
     */
    public static class SnakeGameController {
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            Logging.info("🐍 Initializing Snake game");
            Logging.info("🎮 Game mode: " + gameMode.getDisplayName());
            
            String speed = gameOptions.getStringOption("speed", "medium");
            int gridSize = gameOptions.getIntOption("gridSize", 20);
            
            Logging.info("⚙️ Snake settings - Speed: " + speed + ", Grid: " + gridSize + "x" + gridSize);
        }
    }
} 