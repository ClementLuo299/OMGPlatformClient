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


    // for dropping a checker
    // row will be a number 0-6 respectively corresponding to columns 1-7 on the gameboard
    public void drop(Player player, int column) {
        // if the passed column is full or does not exist
        if(column>COLS-1 || this.board.charAt(column)!='o'){
            //throw new IllegalMoveExeption();
        }
        if(player!=player1 && player!=player2){
            //throw new PlayerExeption();
        }
        // if no checker has been dropped in this column
        if(this.board.charAt(((ROWS-1)*COLS)+column)=='o'){
            if(player==player1){
                this.board = this.board.substring(0, ((ROWS-1)*COLS)+column) + PLAYER_ONE_CHAR + this.board.substring(((ROWS-1)*COLS)+column+1);
            } else {
                this.board = this.board.substring(0, ((ROWS-1)*COLS)+column) + PLAYER_TWO_CHAR + this.board.substring(((ROWS-1)*COLS)+column+1);
            }
        }
        //searching all rows
        for(int i=0; i<ROWS; i++){
            //find the first taken row in the passed column
            if(this.board.charAt(i*COLS+column)!='o') {
                //place the char at the position above the found checker
                if(player==player1){
                    this.board = this.board.substring(0, (i-1)*COLS+column) + PLAYER_ONE_CHAR + this.board.substring((i-1)*COLS+column+1);
                } else {
                    this.board = this.board.substring(0, (i-1)*COLS+column) + PLAYER_TWO_CHAR + this.board.substring((i-1)*COLS+column+1);
                }
            }
        }
    }

    public Player getWinner(){
        // looking for horizontal 4 in a row
        for(int i=0; i<ROWS; i++){
            // need 4 in a row, avoiding error: out of bounds
            for(int j=0; j<COLS-3; j++){
                if(this.board.charAt(i+j+1)==this.board.charAt(i+j) &&
                   this.board.charAt(i+j+2)==this.board.charAt(i+j) &&
                   this.board.charAt(i+j+3)==this.board.charAt(i+j)){
                    if(this.board.charAt(i+j)==PLAYER_ONE_CHAR){
                        return player1;
                    } else if(this.board.charAt(i+j)==PLAYER_TWO_CHAR){
                        return player2;
                    }
                }
            }
        }

        // looking for vertical 4 in a row
        for(int i=0; i<ROWS-3; i++){// need 4 in a row, avoiding error: out of bounds
            for(int j=0; j<COLS; j++){
                if(this.board.charAt(i+j+COLS)==this.board.charAt(i+j) &&
                        this.board.charAt(i+j+2*COLS)==this.board.charAt(i+j) &&
                        this.board.charAt(i+j+3*COLS)==this.board.charAt(i+j)){
                    if(this.board.charAt(i+j)==PLAYER_ONE_CHAR){
                        return player1;
                    } else if(this.board.charAt(i+j)==PLAYER_TWO_CHAR){
                        return player2;
                    }
                }
            }
        }

        // looking for diagonally-right 4 in a row (\)
        for(int i=0; i<ROWS-3; i++){// need 4 in a row, avoiding error: out of bounds
            for(int j=0; j<COLS-3; j++){
                if(this.board.charAt(i+j+1+COLS)  ==this.board.charAt(i+j) &&
                   this.board.charAt(i+j+2+2*COLS)==this.board.charAt(i+j) &&
                   this.board.charAt(i+j+3+3*COLS)==this.board.charAt(i+j)){
                    if(this.board.charAt(i+j)==PLAYER_ONE_CHAR){
                        return player1;
                    } else if(this.board.charAt(i+j)==PLAYER_TWO_CHAR){
                        return player2;
                    }
                }
            }
        }

        // looking for diagonally-left 4 in a row (/)
        for(int i=0; i<ROWS-3; i++){// need 4 in a row, avoiding error: out of bounds
            for(int j=3; j<COLS; j++){
                if(this.board.charAt(i+j-1+COLS)  ==this.board.charAt(i+j) &&
                        this.board.charAt(i+j-2+2*COLS)==this.board.charAt(i+j) &&
                        this.board.charAt(i+j-3+3*COLS)==this.board.charAt(i+j)){
                    if(this.board.charAt(i+j)==PLAYER_ONE_CHAR){
                        return player1;
                    } else if(this.board.charAt(i+j)==PLAYER_TWO_CHAR){
                        return player2;
                    }
                }
            }
        }

        // if no 4 in a row was found
        return null;
    }


    public boolean drew() {
        // if there was a winner
        if(this.getWinner()!=null){
            return false;
        }
        // looking through all rows and columns
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLS; j++){
                // if there is an open place in the board
                if(this.board.charAt(i+j)=='o'){
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public String toString() {
        StringBuilder grid = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                char c = board.charAt(i * COLS + j);
                grid.append(c == 'o' ? "." : c).append(" ");
            }
            grid.append("\n");
        }
        return grid.toString();
    }


}
