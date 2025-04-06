package gamelogic.tictactoe;

import gamelogic.*;
import java.util.List;

/**
 * TicTacToe game implementation
 * Extends the base Game class with TicTacToe specific logic
 */
public class TicTacToe extends Game {
    
    private static final char PLAYER_ONE_CHAR = 'X';
    private static final char PLAYER_TWO_CHAR = 'O';
    
    // Board state
    private String board;

    // the board will be stored in a string in the following format:
    // "123456789" where 1-9 will be X, for player 1, O for player 2 or
    // the initial number for an unclaimed box.
    // _____________
    // | 1 | 2 | 3 |
    // | 4 | 5 | 6 |
    // | 7 | 8 | 9 |
    // '---'---'---'

    /**
     * Default constructor for TicTacToe game
     * @param players List of players, should contain exactly 2 players
     */
    public TicTacToe(List<Player> players) {
        super(GameType.TICTACTOE, players, 5); // Average moves is about 5 for TicTacToe
        if (players.size() != 2) {
            throw new IllegalArgumentException("TicTacToe requires exactly 2 players");
        }
        this.board = "123456789";
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
     * Place a mark on the board
     * @param turn The player making the move
     * @param position The position to place the mark (0-8)
     */
    public void place(Player turn, int position) {
        List<Player> players = getPlayers();
        
        if (position < 0 || position > 8) {
            return; // Invalid position
        }
        
        if(this.board.charAt(position) == PLAYER_ONE_CHAR ||
           this.board.charAt(position) == PLAYER_TWO_CHAR) {
            return; // Position already taken
        }
        
        if(turn == players.get(0)) {
            this.board = this.board.substring(0, position) + PLAYER_ONE_CHAR + this.board.substring(position + 1);
        } else if(turn == players.get(1)) {
            this.board = this.board.substring(0, position) + PLAYER_TWO_CHAR + this.board.substring(position + 1);
        } else {
            return; // Invalid player
        }
        
        // Check if game is over
        Player winner = checkWinner();
        if (winner != null) {
            setWinner(winner);
        }
        
        // Switch turn to next player
        setTurnHolder(turn == players.get(0) ? players.get(1) : players.get(0));
    }

    /**
     * Check if a player has won
     * @return The winning player, or null if no winner
     */
    private Player checkWinner() {
        List<Player> players = getPlayers();
        
        if(this.board.charAt(0) == PLAYER_ONE_CHAR && this.board.charAt(0) == this.board.charAt(1) && this.board.charAt(1) == this.board.charAt(2) ||
                this.board.charAt(3) == PLAYER_ONE_CHAR && this.board.charAt(3) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(5) ||
                this.board.charAt(6) == PLAYER_ONE_CHAR && this.board.charAt(6) == this.board.charAt(7) && this.board.charAt(7) == this.board.charAt(8) ||
                this.board.charAt(0) == PLAYER_ONE_CHAR && this.board.charAt(0) == this.board.charAt(3) && this.board.charAt(3) == this.board.charAt(6) ||
                this.board.charAt(1) == PLAYER_ONE_CHAR && this.board.charAt(1) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(7) ||
                this.board.charAt(2) == PLAYER_ONE_CHAR && this.board.charAt(2) == this.board.charAt(5) && this.board.charAt(5) == this.board.charAt(8) ||
                this.board.charAt(0) == PLAYER_ONE_CHAR && this.board.charAt(0) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(8) ||
                this.board.charAt(2) == PLAYER_ONE_CHAR && this.board.charAt(2) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(6)) {
            return players.get(0);
        } else if(this.board.charAt(0) == PLAYER_TWO_CHAR && this.board.charAt(0) == this.board.charAt(1) && this.board.charAt(1) == this.board.charAt(2) ||
                this.board.charAt(3) == PLAYER_TWO_CHAR && this.board.charAt(3) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(5) ||
                this.board.charAt(6) == PLAYER_TWO_CHAR && this.board.charAt(6) == this.board.charAt(7) && this.board.charAt(7) == this.board.charAt(8) ||
                this.board.charAt(0) == PLAYER_TWO_CHAR && this.board.charAt(0) == this.board.charAt(3) && this.board.charAt(3) == this.board.charAt(6) ||
                this.board.charAt(1) == PLAYER_TWO_CHAR && this.board.charAt(1) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(7) ||
                this.board.charAt(2) == PLAYER_TWO_CHAR && this.board.charAt(2) == this.board.charAt(5) && this.board.charAt(5) == this.board.charAt(8) ||
                this.board.charAt(0) == PLAYER_TWO_CHAR && this.board.charAt(0) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(8) ||
                this.board.charAt(2) == PLAYER_TWO_CHAR && this.board.charAt(2) == this.board.charAt(4) && this.board.charAt(4) == this.board.charAt(6)) {
            return players.get(1);
        } else {
            return null;
        }
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
        for (int i = 0; i < 9; i++) {
            char c = this.board.charAt(i);
            if (c != PLAYER_ONE_CHAR && c != PLAYER_TWO_CHAR) {
                return false; // Board still has empty spaces
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