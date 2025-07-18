package com.games.modules;

import com.games.BaseGameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Tetris game module - a classic puzzle game.
 * Placeholder implementation for testing the dynamic game library.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class TetrisModule extends BaseGameModule {
    
    private static final String GAME_ID = "tetris";
    private static final String GAME_NAME = "Tetris";
    private static final String GAME_DESCRIPTION = "Classic puzzle game where you arrange falling blocks to clear lines";
    
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
        return 10; // 10 minutes
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
        return "/games/tetris";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return TetrisController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof TetrisController) {
            TetrisController tetrisController = (TetrisController) controller;
            tetrisController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        GameOptions options = new GameOptions();
        options.setOption("level", 1);
        options.setOption("lines", 0);
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.SINGLE_PLAYER, 1, options);
        
        // Add tetris game state data
        gameState.setStateValue("score", 0);
        gameState.setStateValue("level", 1);
        gameState.setStateValue("linesCleared", 0);
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading Tetris game state");
        
        if (gameState != null) {
            int score = gameState.getIntStateValue("score", 0);
            int level = gameState.getIntStateValue("level", 1);
            int linesCleared = gameState.getIntStateValue("linesCleared", 0);
            
            Logging.info("🧩 Loaded tetris game state - Score: " + score + ", Level: " + level + ", Lines: " + linesCleared);
        }
    }
    
    /**
     * Tetris game controller (placeholder).
     */
    public static class TetrisController {
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            Logging.info("🧩 Initializing Tetris game");
            Logging.info("🎮 Game mode: " + gameMode.getDisplayName());
            
            int level = gameOptions.getIntOption("level", 1);
            int lines = gameOptions.getIntOption("lines", 0);
            
            Logging.info("⚙️ Tetris settings - Level: " + level + ", Lines: " + lines);
        }
    }
} 