package com.games.modules.puzzle;

import com.games.BaseGameModule;
import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Puzzle game module implementation.
 * Brain-teasing puzzle challenges.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class PuzzleModule extends BaseGameModule {
    
    private static final String GAME_ID = "puzzle";
    private static final String GAME_NAME = "Brain Teasers";
    private static final String GAME_DESCRIPTION = "Collection of challenging logic puzzles";
    
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
        return "Puzzle";
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
        return 15; // 15 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.HARD;
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
        return "/games/puzzle";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return PuzzleController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof PuzzleController) {
            PuzzleController puzzleController = (PuzzleController) controller;
            puzzleController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    /**
     * Puzzle game controller class.
     */
    public static class PuzzleController {
        private GameMode gameMode;
        private int playerCount;
        private GameOptions gameOptions;
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            this.gameMode = gameMode;
            this.playerCount = playerCount;
            this.gameOptions = gameOptions;
            
            Logging.info("🎮 Initializing Brain Teasers - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
            Logging.info("🧩 Starting new puzzle challenge");
        }
    }
} 