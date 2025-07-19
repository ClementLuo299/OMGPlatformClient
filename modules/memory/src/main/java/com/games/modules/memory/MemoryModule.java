package com.games.modules.memory;

import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Memory game module implementation.
 * Card matching memory game.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class MemoryModule implements GameModule {
    
    private static final String GAME_ID = "memory";
    private static final String GAME_NAME = "Memory Match";
    private static final String GAME_DESCRIPTION = "Test your memory by matching pairs of cards";
    
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
        return "Card";
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
        return 8; // 8 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.EASY;
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
        return "/games/memory";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return MemoryController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof MemoryController) {
            MemoryController memoryController = (MemoryController) controller;
            memoryController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    /**
     * Memory game controller class.
     */
    public static class MemoryController {
        private GameMode gameMode;
        private int playerCount;
        private GameOptions gameOptions;
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            this.gameMode = gameMode;
            this.playerCount = playerCount;
            this.gameOptions = gameOptions;
            
            Logging.info("🎮 Initializing Memory Match - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
            Logging.info("🧠 Starting new memory game");
        }
    }
} 