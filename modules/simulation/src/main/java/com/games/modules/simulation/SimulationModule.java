package com.games.modules.simulation;

import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Simulation game module implementation.
 * Life simulation game.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class SimulationModule implements GameModule {
    
    private static final String GAME_ID = "simulation";
    private static final String GAME_NAME = "Life Simulator";
    private static final String GAME_DESCRIPTION = "Build and manage your virtual world";
    
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
        return "Simulation";
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
        return 30; // 30 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.MEDIUM;
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
        return "/games/simulation";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return SimulationController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof SimulationController) {
            SimulationController simulationController = (SimulationController) controller;
            simulationController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    /**
     * Simulation game controller class.
     */
    public static class SimulationController {
        private GameMode gameMode;
        private int playerCount;
        private GameOptions gameOptions;
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            this.gameMode = gameMode;
            this.playerCount = playerCount;
            this.gameOptions = gameOptions;
            
            Logging.info("🎮 Initializing Life Simulator - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
            Logging.info("🏗️ Starting new simulation world");
        }
    }
} 