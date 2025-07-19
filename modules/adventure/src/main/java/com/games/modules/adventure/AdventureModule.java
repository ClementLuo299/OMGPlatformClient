package com.games.modules.adventure;

import com.games.BaseGameModule;
import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Adventure game module implementation.
 * Story-driven adventure game.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class AdventureModule extends BaseGameModule {
    
    private static final String GAME_ID = "adventure";
    private static final String GAME_NAME = "Quest Explorer";
    private static final String GAME_DESCRIPTION = "Embark on epic quests in a fantasy world";
    
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
        return "Adventure";
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
        return 45; // 45 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.VARIABLE;
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
        return "/games/adventure";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return AdventureController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof AdventureController) {
            AdventureController adventureController = (AdventureController) controller;
            adventureController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    /**
     * Adventure game controller class.
     */
    public static class AdventureController {
        private GameMode gameMode;
        private int playerCount;
        private GameOptions gameOptions;
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            this.gameMode = gameMode;
            this.playerCount = playerCount;
            this.gameOptions = gameOptions;
            
            Logging.info("🎮 Initializing Quest Explorer - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
            Logging.info("🗺️ Starting new adventure quest");
        }
    }
} 