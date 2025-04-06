package gamelogic.tictactoe;

import gamelogic.*;

import java.util.List;

/**
 * Handles the logic of an ongoing Tic Tac Toe game
 *
 * @authors Scott Brown
 * @date April 3, 2025
 */

public class TicTacToeGame extends Game {

    //private String board;
    private static final char PLAYER_ONE_CHAR = 'X';
    private static final char PLAYER_TWO_CHAR = 'O';
    private Player player1;
    private Player player2;
    private String board;

    // the board will be stored in a string in the following format:
    // "123456789" where 1-9 will be X, for player 1, O for player 2 or
    // the initial number for an unclaimed box.
    // _____________
    // | 1 | 2 | 3 |
    // | 4 | 5 | 6 |
    // | 7 | 8 | 9 |
    // '---'---'---'

    // default constructor for a TicTacToe game
    public TicTacToeGame (List<Player> players) {
        super(GameType.CONNECT4, players, 5);
        this.board = "123456789";
        this.player1 = players.get(0);
        this.player2 = players.get(1);
    }


    // for placing an X or O on the board
    public void place(Player turn, int position) {
        if(this.board.charAt(position)==PLAYER_ONE_CHAR ||
           this.board.charAt(position)==PLAYER_TWO_CHAR) {
            //throw new IllegalMoveExeption();
        }
        if(turn==player1) {
            this.board = this.board.substring(0, position) + PLAYER_ONE_CHAR + this.board.substring(position + 1);
        } else if(turn==player2) {
            this.board = this.board.substring(0, position) + PLAYER_TWO_CHAR + this.board.substring(position + 1);
        } else {
            //throw new PlayerException();
        }
    }

    // returns the winning player, null if the game does not yet have a winner
    public Player getWinner(){
        if(this.board.charAt(0)==PLAYER_ONE_CHAR && this.board.charAt(0)==this.board.charAt(1) && this.board.charAt(1)==this.board.charAt(2) ||
                this.board.charAt(3)==PLAYER_ONE_CHAR && this.board.charAt(3)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(5) ||
                this.board.charAt(6)==PLAYER_ONE_CHAR && this.board.charAt(6)==this.board.charAt(7) && this.board.charAt(7)==this.board.charAt(8) ||
                this.board.charAt(0)==PLAYER_ONE_CHAR && this.board.charAt(0)==this.board.charAt(3) && this.board.charAt(3)==this.board.charAt(6) ||
                this.board.charAt(1)==PLAYER_ONE_CHAR && this.board.charAt(1)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(7) ||
                this.board.charAt(2)==PLAYER_ONE_CHAR && this.board.charAt(2)==this.board.charAt(5) && this.board.charAt(5)==this.board.charAt(8) ||
                this.board.charAt(0)==PLAYER_ONE_CHAR && this.board.charAt(0)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(8) ||
                this.board.charAt(2)==PLAYER_ONE_CHAR && this.board.charAt(2)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(6)
                ){
            return player1;
        } else if(this.board.charAt(0)==PLAYER_TWO_CHAR && this.board.charAt(0)==this.board.charAt(1) && this.board.charAt(1)==this.board.charAt(2) ||
                this.board.charAt(3)==PLAYER_TWO_CHAR && this.board.charAt(3)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(5) ||
                this.board.charAt(6)==PLAYER_TWO_CHAR && this.board.charAt(6)==this.board.charAt(7) && this.board.charAt(7)==this.board.charAt(8) ||
                this.board.charAt(0)==PLAYER_TWO_CHAR && this.board.charAt(0)==this.board.charAt(3) && this.board.charAt(3)==this.board.charAt(6) ||
                this.board.charAt(1)==PLAYER_TWO_CHAR && this.board.charAt(1)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(7) ||
                this.board.charAt(2)==PLAYER_TWO_CHAR && this.board.charAt(2)==this.board.charAt(5) && this.board.charAt(5)==this.board.charAt(8) ||
                this.board.charAt(0)==PLAYER_TWO_CHAR && this.board.charAt(0)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(8) ||
                this.board.charAt(2)==PLAYER_TWO_CHAR && this.board.charAt(2)==this.board.charAt(4) && this.board.charAt(4)==this.board.charAt(6)
                ){
            return player2;
        } else {
            return null;
        }
    }


    // Whether or not the game is tied
    public boolean drew() {
        if(getWinner()==null){
            return false;
        }
        return this.board.charAt(0) != '1' &&
                this.board.charAt(1) != '2' &&
                this.board.charAt(2) != '3' &&
                this.board.charAt(3) != '4' &&
                this.board.charAt(4) != '5' &&
                this.board.charAt(5) != '6' &&
                this.board.charAt(6) != '7' &&
                this.board.charAt(7) != '8' &&
                this.board.charAt(8) != '9';
    }

    public String getBoard() {
        return this.board;
    }
}