package gamelogic.connectfour;

import gamelogic.*;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;
import java.util.List;

import static gamelogic.connectfour.ConnectGrid.ROWS;
import static gamelogic.connectfour.ConnectGrid.COLS;

/**
 * Connect Four game implementation
 * Extends the base Game class with Connect Four specific logic
 * Integrates ConnectGrid for board management
 */
public class ConnectFour extends Game {

    private static final char PLAYER_ONE_CHAR = '1';
    private static final char PLAYER_TWO_CHAR = '2';

    // The board represented as a string for UI compatibility
    private String boardString;
    
    // The board represented using ConnectGrid for internal game logic
    private ConnectGrid connectGrid;

    /**
     * Default constructor for Connect Four game
     * @param players List of players, should contain exactly 2 players
     */
    public ConnectFour(List<Player> players) {
        super(GameType.CONNECT_FOUR, players, 21); // Average moves is about 21 for Connect Four
        if (players.size() != 2) {
            throw new IllegalArgumentException("Connect Four requires exactly 2 players");
        }
        this.boardString = "oooooooooooooooooooooooooooooooooooooooooo";
        this.connectGrid = new ConnectGrid();
        
        // Setting initial turn holder
        setTurnHolder(players.get(0));
    }

    /**
     * Get the current board state
     * @return The board state as a string
     */
    public String getBoard() {
        return this.boardString;
    }

    /**
     * Drop a piece in the specified column
     * @param player The player making the move
     * @param column The column to drop the piece (0-6)
     */
    public void drop(Player player, int column) {
        List<Player> players = getPlayers();
        
        // Check if the column exists and is not full
        if (column < 0 || column >= COLS || this.boardString.charAt(column) != 'o') {
            // Column is full or invalid, can't drop here
            return;
        }
        
        if (player != players.get(0) && player != players.get(1)) {
            // Invalid player
            return;
        }
        
        // Find the lowest empty position in the column
        int lowestEmptyRow = -1;
        for (int row = ROWS - 1; row >= 0; row--) {
            int index = row * COLS + column;
            if (this.boardString.charAt(index) == 'o') {
                lowestEmptyRow = row;
                break;
            }
        }
        
        if (lowestEmptyRow >= 0) {
            // Place the piece at the lowest empty position
            int index = lowestEmptyRow * COLS + column;
            char playerChar = (player == players.get(0)) ? PLAYER_ONE_CHAR : PLAYER_TWO_CHAR;
            
            // Update string representation
            this.boardString = this.boardString.substring(0, index) + playerChar + this.boardString.substring(index + 1);
            
            // Update ConnectGrid representation
            Colour checkerColor = (player == players.get(0)) ? Colour.YELLOW : Colour.RED;
            Checker newChecker = new Checker(PieceType.CHECKER, false, checkerColor, false, lowestEmptyRow, column);
            this.connectGrid.setPosition(column, lowestEmptyRow, newChecker);
            
            // Check if game is over
            Player winner = checkWinner();
            if (winner != null) {
                setWinner(winner);
            }
            
            // Switch turn to next player
            setTurnHolder(player == players.get(0) ? players.get(1) : players.get(0));
        }
    }

    /**
     * Check if a player has won
     * @return The winning player, or null if no winner
     */
    private Player checkWinner() {
        List<Player> players = getPlayers();
        
        // Using string representation for compatibility
        // looking for horizontal 4 in a row
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS-3; col++){
                int index = row * COLS + col;
                char current = this.boardString.charAt(index);
                if (current != 'o' && 
                    current == this.boardString.charAt(index + 1) && 
                    current == this.boardString.charAt(index + 2) && 
                    current == this.boardString.charAt(index + 3)) {
                    
                    if(current == PLAYER_ONE_CHAR){
                        return players.get(0);
                    } else if(current == PLAYER_TWO_CHAR){
                        return players.get(1);
                    }
                }
            }
        }

        // looking for vertical 4 in a row
        for(int row = 0; row < ROWS-3; row++){
            for(int col = 0; col < COLS; col++){
                int index = row * COLS + col;
                char current = this.boardString.charAt(index);
                if (current != 'o' && 
                    current == this.boardString.charAt(index + COLS) && 
                    current == this.boardString.charAt(index + (2 * COLS)) && 
                    current == this.boardString.charAt(index + (3 * COLS))) {
                    
                    if(current == PLAYER_ONE_CHAR){
                        return players.get(0);
                    } else if(current == PLAYER_TWO_CHAR){
                        return players.get(1);
                    }
                }
            }
        }

        // looking for diagonally-right 4 in a row (\)
        for(int row = 0; row < ROWS-3; row++){
            for(int col = 0; col < COLS-3; col++){
                int index = row * COLS + col;
                char current = this.boardString.charAt(index);
                if (current != 'o' && 
                    current == this.boardString.charAt(index + COLS + 1) && 
                    current == this.boardString.charAt(index + (2 * COLS) + 2) && 
                    current == this.boardString.charAt(index + (3 * COLS) + 3)) {
                    
                    if(current == PLAYER_ONE_CHAR){
                        return players.get(0);
                    } else if(current == PLAYER_TWO_CHAR){
                        return players.get(1);
                    }
                }
            }
        }

        // looking for diagonally-left 4 in a row (/)
        for(int row = 0; row < ROWS-3; row++){
            for(int col = 3; col < COLS; col++){
                int index = row * COLS + col;
                char current = this.boardString.charAt(index);
                if (current != 'o' && 
                    current == this.boardString.charAt(index + COLS - 1) && 
                    current == this.boardString.charAt(index + (2 * COLS) - 2) && 
                    current == this.boardString.charAt(index + (3 * COLS) - 3)) {
                    
                    if(current == PLAYER_ONE_CHAR){
                        return players.get(0);
                    } else if(current == PLAYER_TWO_CHAR){
                        return players.get(1);
                    }
                }
            }
        }

        // if no 4 in a row was found
        return null;
    }

    /**
     * Check if the game is a draw
     * @return True if the game is a draw, false otherwise
     */
    public boolean isDrawn() {
        // If there was a winner, it's not a draw
        if (checkWinner() != null) {
            return false;
        }
        
        // Check if the board is full
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int index = row * COLS + col;
                // If there is an empty cell, it's not a draw yet
                if (this.boardString.charAt(index) == 'o') {
                    return false;
                }
            }
        }
        
        // If no winner and the board is full, it's a draw
        return true;
    }
    
    /**
     * Get the players in this game
     * @return List of players
     */
    public List<Player> getPlayers() {
        // Now we can use the getter from the Game class
        return super.getPlayers();
    }
}
