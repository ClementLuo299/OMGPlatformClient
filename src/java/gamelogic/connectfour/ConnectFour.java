package gamelogic.connectfour;

import gamelogic.*;
import java.util.List;

/**
 * Connect Four game implementation
 * Extends the base Game class with Connect Four specific logic
 */
public class ConnectFour extends Game {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char PLAYER_ONE_CHAR = '1';
    private static final char PLAYER_TWO_CHAR = '2';

    // the board will be stored in a string in the following format:
    // "oooooooooooooooooooooooooooooooooooooooooo"
    // where the 42 'o's corresponds to the 7*6 grid of the connect four board
    // these will be replaced by '1', for player 1, or '2' for player 2
    // for example, the board:
    // O O O O O O O
    // O O O O 1 O O
    // O O 2 2 2 O O
    // O O 1 2 2 O O
    // O O 1 1 2 1 O
    // O 1 2 1 1 2 1
    // would be stored as
    // "ooooooooooo1oooo222oooo122oooo1121oo121121"

    // Board state
    private String board;

    /**
     * Default constructor for Connect Four game
     * @param players List of players, should contain exactly 2 players
     */
    public ConnectFour(List<Player> players) {
        super(GameType.CONNECT_FOUR, players, 21); // Average moves is about 21 for Connect Four
        if (players.size() != 2) {
            throw new IllegalArgumentException("Connect Four requires exactly 2 players");
        }
        this.board = "oooooooooooooooooooooooooooooooooooooooooo";
        // Setting initial turn holder
        setTurnHolder(players.get(0));
    }

    /**
     * Get the current board state
     * @return The board state as a string
     */
    public String getBoard() {
        return this.board;
    }

    /**
     * Drop a piece in the specified column
     * @param player The player making the move
     * @param column The column to drop the piece (0-6)
     */
    public void drop(Player player, int column) {
        List<Player> players = getPlayers();
        
        // Check if the column exists and is not full
        if (column < 0 || column >= COLS || this.board.charAt(column) != 'o') {
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
            if (this.board.charAt(index) == 'o') {
                lowestEmptyRow = row;
                break;
            }
        }
        
        if (lowestEmptyRow >= 0) {
            // Place the piece at the lowest empty position
            int index = lowestEmptyRow * COLS + column;
            char playerChar = (player == players.get(0)) ? PLAYER_ONE_CHAR : PLAYER_TWO_CHAR;
            
            this.board = this.board.substring(0, index) + playerChar + this.board.substring(index + 1);
            
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
        
        // looking for horizontal 4 in a row
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS-3; col++){
                int index = row * COLS + col;
                char current = this.board.charAt(index);
                if (current != 'o' && 
                    current == this.board.charAt(index + 1) && 
                    current == this.board.charAt(index + 2) && 
                    current == this.board.charAt(index + 3)) {
                    
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
                char current = this.board.charAt(index);
                if (current != 'o' && 
                    current == this.board.charAt(index + COLS) && 
                    current == this.board.charAt(index + (2 * COLS)) && 
                    current == this.board.charAt(index + (3 * COLS))) {
                    
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
                char current = this.board.charAt(index);
                if (current != 'o' && 
                    current == this.board.charAt(index + COLS + 1) && 
                    current == this.board.charAt(index + (2 * COLS) + 2) && 
                    current == this.board.charAt(index + (3 * COLS) + 3)) {
                    
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
                char current = this.board.charAt(index);
                if (current != 'o' && 
                    current == this.board.charAt(index + COLS - 1) && 
                    current == this.board.charAt(index + (2 * COLS) - 2) && 
                    current == this.board.charAt(index + (3 * COLS) - 3)) {
                    
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
                if (this.board.charAt(index) == 'o') {
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
