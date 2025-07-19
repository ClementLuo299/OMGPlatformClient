package com.games.modules.connect4;

import com.games.GameMode;
import com.games.GameOptions;
import com.utils.error_handling.Logging;

/**
 * Connect4 game controller.
 * Handles the game logic and UI interactions for Connect4.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class Connect4Controller {
    
    private GameMode gameMode;
    private int playerCount;
    private GameOptions gameOptions;
    private String[][] gameBoard;
    private String currentPlayer;
    private int movesCount;
    
    /**
     * Initializes the Connect4 game with the specified parameters.
     * 
     * @param gameMode The game mode (SINGLE_PLAYER, LOCAL_MULTIPLAYER, ONLINE_MULTIPLAYER)
     * @param playerCount Number of players
     * @param gameOptions Additional game-specific options
     */
    public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
        this.gameMode = gameMode;
        this.playerCount = playerCount;
        this.gameOptions = gameOptions;
        
        Logging.info("🎮 Initializing Connect4 - Mode: " + gameMode.getDisplayName() + ", Players: " + playerCount);
        
        // Initialize game board
        int width = gameOptions.getIntOption("boardWidth", 7);
        int height = gameOptions.getIntOption("boardHeight", 6);
        gameBoard = new String[height][width];
        
        // Initialize game state
        currentPlayer = "Red";
        movesCount = 0;
        
        // Initialize your game logic here
        startNewGame();
    }
    
    /**
     * Starts a new Connect4 game.
     */
    private void startNewGame() {
        Logging.info("🆕 Starting new Connect4 game");
        
        // Clear the game board
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[0].length; col++) {
                gameBoard[row][col] = null;
            }
        }
        
        currentPlayer = "Red";
        movesCount = 0;
        
        Logging.info("✅ Connect4 game board initialized: " + gameBoard.length + "x" + gameBoard[0].length);
    }
    
    /**
     * Makes a move in the specified column.
     * 
     * @param column The column to drop the token in
     * @return true if the move was successful, false otherwise
     */
    public boolean makeMove(int column) {
        if (column < 0 || column >= gameBoard[0].length) {
            Logging.warning("⚠️ Invalid column: " + column);
            return false;
        }
        
        // Find the lowest empty row in the specified column
        int row = gameBoard.length - 1;
        while (row >= 0 && gameBoard[row][column] != null) {
            row--;
        }
        
        if (row < 0) {
            Logging.warning("⚠️ Column " + column + " is full");
            return false;
        }
        
        // Place the token
        gameBoard[row][column] = currentPlayer;
        movesCount++;
        
        Logging.info("🎯 " + currentPlayer + " placed token at (" + row + ", " + column + ")");
        
        // Check for win
        if (checkWin(row, column)) {
            Logging.info("🏆 " + currentPlayer + " wins!");
            return true;
        }
        
        // Check for draw
        if (movesCount >= gameBoard.length * gameBoard[0].length) {
            Logging.info("🤝 Game is a draw!");
            return true;
        }
        
        // Switch players
        currentPlayer = currentPlayer.equals("Red") ? "Yellow" : "Red";
        
        return true;
    }
    
    /**
     * Checks if the last move resulted in a win.
     * 
     * @param row The row of the last move
     * @param column The column of the last move
     * @return true if the move resulted in a win
     */
    private boolean checkWin(int row, int column) {
        String player = gameBoard[row][column];
        
        // Check horizontal
        if (checkDirection(row, column, 0, 1, player) + checkDirection(row, column, 0, -1, player) >= 3) {
            return true;
        }
        
        // Check vertical
        if (checkDirection(row, column, 1, 0, player) + checkDirection(row, column, -1, 0, player) >= 3) {
            return true;
        }
        
        // Check diagonal (top-left to bottom-right)
        if (checkDirection(row, column, 1, 1, player) + checkDirection(row, column, -1, -1, player) >= 3) {
            return true;
        }
        
        // Check diagonal (top-right to bottom-left)
        if (checkDirection(row, column, 1, -1, player) + checkDirection(row, column, -1, 1, player) >= 3) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Checks in a specific direction for consecutive tokens.
     * 
     * @param startRow Starting row
     * @param startCol Starting column
     * @param deltaRow Row direction
     * @param deltaCol Column direction
     * @param player Player token to check for
     * @return Number of consecutive tokens in the specified direction
     */
    private int checkDirection(int startRow, int startCol, int deltaRow, int deltaCol, String player) {
        int count = 0;
        int row = startRow + deltaRow;
        int col = startCol + deltaCol;
        
        while (row >= 0 && row < gameBoard.length && 
               col >= 0 && col < gameBoard[0].length && 
               gameBoard[row][col] != null && 
               gameBoard[row][col].equals(player)) {
            count++;
            row += deltaRow;
            col += deltaCol;
        }
        
        return count;
    }
    
    /**
     * Resets the Connect4 game.
     */
    public void resetGame() {
        Logging.info("🔄 Resetting Connect4 game");
        startNewGame();
    }
    
    /**
     * Returns to the game library.
     */
    public void returnToLibrary() {
        Logging.info("🏠 Returning to game library from Connect4");
        // This would typically navigate back to the main application
    }
    
    /**
     * Gets the current game board.
     * @return The game board
     */
    public String[][] getGameBoard() {
        return gameBoard;
    }
    
    /**
     * Gets the current player.
     * @return The current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Gets the number of moves made.
     * @return The number of moves
     */
    public int getMovesCount() {
        return movesCount;
    }
} 