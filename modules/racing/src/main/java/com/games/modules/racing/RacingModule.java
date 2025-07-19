package com.games.modules.racing;

import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Racing game module implementation.
 * Fast-paced racing action.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class RacingModule implements GameModule {
    
    private static final String GAME_ID = "racing";
    private static final String GAME_NAME = "Speed Racer";
    private static final String GAME_DESCRIPTION = "High-speed racing with multiple tracks";
    
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
        return "Arcade";
    }
    
    @Override
    public int getMinPlayers() {
        return 1;
    }
    
    @Override
    public int getMaxPlayers() {
        return 8;
    }
    
    @Override
    public int getEstimatedDuration() {
        return 5; // 5 minutes
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
        return true;
    }
    
    @Override
    protected String getGameBasePath() {
        return "/games/racing";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return RacingController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof RacingController) {
            RacingController racingController = (RacingController) controller;
            racingController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    /**
     * Racing game controller class.
     */
    public static class RacingController {
        private GameMode gameMode;
        private int playerCount;
        private GameOptions gameOptions;
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            this.gameMode = gameMode;
            this.playerCount = playerCount;
            this.gameOptions = gameOptions;
            
            Logging.info("🎮 Initializing Speed Racer - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
            Logging.info("🏁 Starting new racing game");
        }
    }
} 