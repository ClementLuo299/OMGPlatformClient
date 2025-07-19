package com.games.modules.connect4;

import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * Connect4 game module implementation.
 * Demonstrates a more complex game with different board dimensions.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class Connect4Module implements GameModule {
    
    private static final String GAME_ID = "connect4";
    private static final String GAME_NAME = "Connect Four";
    private static final String GAME_DESCRIPTION = "Drop tokens to connect four in a row";
    
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
        return 10; // 10 minutes
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
        return "/games/connect4";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return Connect4Controller.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof Connect4Controller) {
            Connect4Controller connect4Controller = (Connect4Controller) controller;
            connect4Controller.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        // This would be implemented to save the current game state
        // For now, return a basic game state
        GameOptions options = new GameOptions();
        options.setOption("boardWidth", 7);
        options.setOption("boardHeight", 6);
        options.setOption("aiDifficulty", "medium");
        options.setOption("timeLimit", 60);
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.LOCAL_MULTIPLAYER, 2, options);
        
        // Add game-specific state data
        gameState.setStateValue("currentPlayer", "Red");
        gameState.setStateValue("movesCount", 0);
        gameState.setStateValue("gameBoard", new String[6][7]); // 6x7 board
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading Connect4 game state");
        
        if (gameState != null) {
            // Load game-specific state data
            String currentPlayer = gameState.getStringStateValue("currentPlayer", "Red");
            int movesCount = gameState.getIntStateValue("movesCount", 0);
            
            Logging.info("📊 Loaded game state - Current player: " + currentPlayer + ", Moves: " + movesCount);
        }
    }
} 