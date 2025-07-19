package com.games.modules.checkers;

import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Checkers game module implementation.
 * Classic board game with strategic piece movement.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class CheckersModule implements GameModule {
    
    private static final String GAME_ID = "checkers";
    private static final String GAME_NAME = "Checkers";
    private static final String GAME_DESCRIPTION = "Classic strategy game with diagonal piece movement";
    
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
    public String getGameCategory() {
        return "Strategy";
    }
    
    @Override
    public int getMinPlayers() {
        return 2;
    }
    
    @Override
    public int getMaxPlayers() {
        return 2;
    }
    
    @Override
    public int getEstimatedDuration() {
        return 20; // 20 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.MEDIUM;
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
        return true; // vs AI
    }
    
    @Override
    protected String getGameBasePath() {
        return "/games/checkers";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return CheckersController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof CheckersController) {
            CheckersController checkersController = (CheckersController) controller;
            checkersController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    /**
     * Checkers game controller class.
     */
    public static class CheckersController {
        private GameMode gameMode;
        private int playerCount;
        private GameOptions gameOptions;
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            this.gameMode = gameMode;
            this.playerCount = playerCount;
            this.gameOptions = gameOptions;
            
            Logging.info("🎮 Initializing Checkers - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
            Logging.info("🆕 Starting new Checkers game");
        }
    }
} 