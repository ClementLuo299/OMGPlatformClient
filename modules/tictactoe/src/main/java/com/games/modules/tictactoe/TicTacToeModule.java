package com.games.modules.tictactoe;

import com.games.BaseGameModule;
import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

/**
 * TicTacToe game module implementation.
 * Demonstrates how to create a game module that integrates with the main application.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class TicTacToeModule extends BaseGameModule {
    
    private static final String GAME_ID = "tictactoe";
    private static final String GAME_NAME = "Tic Tac Toe";
    private static final String GAME_DESCRIPTION = "Classic 3x3 grid game for two players";
    
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
        return 5; // 5 minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.EASY;
    }
    
    @Override
    public String getGameCategory() {
        return "Classic";
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
        return "/games/tictactoe";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return TicTacToeController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof TicTacToeController) {
            TicTacToeController ticTacToeController = (TicTacToeController) controller;
            ticTacToeController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public GameState getGameState() {
        // This would be implemented to save the current game state
        // For now, return a basic game state
        GameOptions options = new GameOptions();
        options.setOption("boardSize", 3);
        options.setOption("aiDifficulty", "medium");
        options.setOption("timeLimit", 30);
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.LOCAL_MULTIPLAYER, 2, options);
        
        // Add game-specific state data
        gameState.setStateValue("currentPlayer", "X");
        gameState.setStateValue("movesCount", 0);
        gameState.setStateValue("gameBoard", new String[3][3]); // 3x3 board
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading TicTacToe game state");
        
        if (gameState != null) {
            // Load game-specific state data
            String currentPlayer = gameState.getStringStateValue("currentPlayer", "X");
            int movesCount = gameState.getIntStateValue("movesCount", 0);
            
            Logging.info("📊 Loaded game state - Current player: " + currentPlayer + ", Moves: " + movesCount);
        }
    }
} 