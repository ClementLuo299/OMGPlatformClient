package com.games.modules.solitaire;

import com.games.BaseGameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Solitaire game module - a classic card game.
 * Placeholder implementation for testing the dynamic game library.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class SolitaireModule extends BaseGameModule {
    
    private static final String GAME_ID = "solitaire";
    private static final String GAME_NAME = "Solitaire";
    private static final String GAME_DESCRIPTION = "Classic single-player card game with multiple variations";
    
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
        return 20; // 20 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.MEDIUM;
    }
    
    @Override
    public String getGameCategory() {
        return "Card";
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
        return "/games/solitaire";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return SolitaireController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof SolitaireController) {
            SolitaireController solitaireController = (SolitaireController) controller;
            solitaireController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        GameOptions options = new GameOptions();
        options.setOption("variation", "klondike");
        options.setOption("drawCount", 3);
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.SINGLE_PLAYER, 1, options);
        
        // Add solitaire game state data
        gameState.setStateValue("score", 0);
        gameState.setStateValue("moves", 0);
        gameState.setStateValue("timeElapsed", 0);
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading Solitaire game state");
        
        if (gameState != null) {
            int score = gameState.getIntStateValue("score", 0);
            int moves = gameState.getIntStateValue("moves", 0);
            int timeElapsed = gameState.getIntStateValue("timeElapsed", 0);
            
            Logging.info("🃏 Loaded solitaire game state - Score: " + score + ", Moves: " + moves + ", Time: " + timeElapsed + "s");
        }
    }
    
    /**
     * Solitaire game controller (placeholder).
     */
    public static class SolitaireController {
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            Logging.info("🃏 Initializing Solitaire game");
            Logging.info("🎮 Game mode: " + gameMode.getDisplayName());
            
            String variation = gameOptions.getStringOption("variation", "klondike");
            int drawCount = gameOptions.getIntOption("drawCount", 3);
            
            Logging.info("⚙️ Solitaire settings - Variation: " + variation + ", Draw Count: " + drawCount);
        }
    }
} 