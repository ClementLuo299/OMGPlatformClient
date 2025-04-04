package gamelogic.connectfour;

import gamelogic.*;

public class ConnectFour extends AbstractGame {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int PLAYER_ONE_CHAR = '1';
    private static final int PLAYER_TWO_CHAR = '2';


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


    // default constructor
    public ConnectFour (Player player1, Player player2) {
        this.board = "oooooooooooooooooooooooooooooooooooooooooo";
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Get the current board state
     * @return The board state as a string
     */
    public String getBoard() {
        return this.board;
    }

    // for dropping a checker
    // column will be a number 0-6 respectively corresponding to columns 1-7 on the gameboard
    public void drop(Player player, int column) {
        // Check if the column exists and is not full
        if (column < 0 || column >= COLS || this.board.charAt(column) != 'o') {
            // Column is full or invalid, can't drop here
            return;
        }
        
        if (player != player1 && player != player2) {
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
            char playerChar = (player == player1) ? (char)PLAYER_ONE_CHAR : (char)PLAYER_TWO_CHAR;
            
            this.board = this.board.substring(0, index) + playerChar + this.board.substring(index + 1);
        }
    }

    public Player getWinner(){
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
                        return player1;
                    } else if(current == PLAYER_TWO_CHAR){
                        return player2;
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
                        return player1;
                    } else if(current == PLAYER_TWO_CHAR){
                        return player2;
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
                        return player1;
                    } else if(current == PLAYER_TWO_CHAR){
                        return player2;
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
                        return player1;
                    } else if(current == PLAYER_TWO_CHAR){
                        return player2;
                    }
                }
            }
        }

        // if no 4 in a row was found
        return null;
    }


    public boolean drew() {
        // If there was a winner, it's not a draw
        if (this.getWinner() != null) {
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


}
