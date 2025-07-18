package com.games.modules.chess;

import com.games.BaseGameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Chess game module - a classic strategy game.
 * Placeholder implementation for testing the dynamic game library.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class ChessModule extends BaseGameModule {
    
    private static final String GAME_ID = "chess";
    private static final String GAME_NAME = "Chess";
    private static final String GAME_DESCRIPTION = "Classic strategy board game for two players";
    
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
        return 2;
    }
    
    @Override
    public int getMaxPlayers() {
        return 2;
    }
    
    @Override
    public int getEstimatedDuration() {
        return 60; // 60 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.HARD;
    }
    
    @Override
    public String getGameCategory() {
        return "Strategy";
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
        return "/games/chess";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return ChessController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof ChessController) {
            ChessController chessController = (ChessController) controller;
            chessController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        GameOptions options = new GameOptions();
        options.setOption("timeControl", "10+0");
        options.setOption("aiLevel", "medium");
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.LOCAL_MULTIPLAYER, 2, options);
        
        // Add chess game state data
        gameState.setStateValue("currentPlayer", "white");
        gameState.setStateValue("moveCount", 0);
        gameState.setStateValue("boardState", "initial");
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading Chess game state");
        
        if (gameState != null) {
            String currentPlayer = gameState.getStringStateValue("currentPlayer", "white");
            int moveCount = gameState.getIntStateValue("moveCount", 0);
            String boardState = gameState.getStringStateValue("boardState", "initial");
            
            Logging.info("♟️ Loaded chess game state - Player: " + currentPlayer + ", Moves: " + moveCount + ", Board: " + boardState);
        }
    }
    
    /**
     * Chess game controller (placeholder).
     */
    public static class ChessController {
        
        public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
            Logging.info("♟️ Initializing Chess game with " + playerCount + " players");
            Logging.info("🎮 Game mode: " + gameMode.getDisplayName());
            
            String timeControl = gameOptions.getStringOption("timeControl", "10+0");
            String aiLevel = gameOptions.getStringOption("aiLevel", "medium");
            
            Logging.info("⚙️ Chess settings - Time: " + timeControl + ", AI Level: " + aiLevel);
        }
    }
} 