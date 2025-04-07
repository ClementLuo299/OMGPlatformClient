package gamelogic.connectfour;

import gamelogic.*;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;

import java.util.List;

import static gamelogic.connectfour.ConnectGrid.ROWS;
import static gamelogic.connectfour.ConnectGrid.COLS;

/**
 * Handles the logic of an ongoing ConnectFour game
 *
 * @authors Scott Brown
 * @date April 3, 2025
 */

public class Connect4Game extends Game {

    // ATTRIBUTES
    private ConnectGrid board;
    private Player player1; // yellow
    private Player player2; // red

    // CONSTRUCTOR
    public Connect4Game (List<Player> players) {
        // by default player1 is the player in the list at index 0
        // and player2 is the player in the list at index 1
        super(GameType.CONNECT_FOUR, players, 24);
        this.board = new ConnectGrid();
        this.player1 = players.get(0);
        this.player2 = players.get(2);
    }

    // METHODS

    // for dropping a checker
    // row will be a number 0-6 respectively corresponding to columns 1-7 on the gameboard
    public void drop(Player player, int column) {
        // if the passed column is full or does not exist
        if(column>COLS-1 || column<0 || this.board.at(column, 0)!=null){
            //throw new IllegalMoveExeption();
        }
        //if(player!=this.player1 && player!=this.player2){
        //throw new PlayerExeption();
        //}

        // if no checker has been dropped in this column
        if(this.board.at(column, ROWS-1)!=null){
            if(player==player1){
                // drop a player1 checker in the given column
                this.board.setPosition(column,ROWS-1, new Checker(PieceType.CHECKER, false, Colour.YELLOW, false, ROWS-1, column));
            } else {
                // drop a player2 checker in the given column
                this.board.setPosition(column,ROWS-1, new Checker(PieceType.CHECKER, false, Colour.RED, false, ROWS-1, column));
            }
        }
        //searching all rows in the given column
        for(int i=0; i<ROWS-1; i++){ // 0 to ROWS-1 is top-down
            // find the first taken row in the given column
            if(this.board.at(column, i)==null) {
                // place the char at the position above the found checker
                if(player==player1){
                    this.board.setPosition(column, i,  new Checker(PieceType.CHECKER, false, Colour.YELLOW, false, i, column));
                } else {
                    this.board.setPosition(column, i,  new Checker(PieceType.CHECKER, false, Colour.RED, false, i, column));
                }
            }
        }
    }

    public Player getWinner(){
        // looking for horizontal 4 in a row
        for(int i=0; i<ROWS; i++){
            // need 4 in a row, -3 is avoiding error: out of bounds
            for(int j=0; j<COLS-3; j++){
                if(this.board.at(j,i+1)==this.board.at(j,i) &&
                        this.board.at(j,i+2)==this.board.at(j,i) &&
                        this.board.at(j, i+3)==this.board.at(j,i)){
                    if(this.board.at(j,i).getColour()==Colour.YELLOW){ // yellow wins (player1)
                        return player1;
                    } else if(this.board.at(j, i).getColour()==Colour.RED){
                        return player2;
                    }
                }
            }
        }

        // looking for vertical 4 in a row
        for(int i=0; i<ROWS-3; i++){// need 4 in a row, avoiding error: out of bounds
            for(int j=0; j<COLS; j++){
                if(this.board.at(j+1, i)==this.board.at(j, i) &&
                        this.board.at(j+2, i)==this.board.at(j, i) &&
                        this.board.at(j+3, i)==this.board.at(j, i)){
                    if(this.board.at(j, i).getColour()==Colour.YELLOW){
                        return player1;
                    } else if(this.board.at(j, i).getColour()==Colour.RED){
                        return player2;
                    }
                }
            }
        }

        // looking for diagonally-right 4 in a row (\)
        for(int i=0; i<ROWS-3; i++){// need 4 in a row, avoiding error: out of bounds
            for(int j=0; j<COLS-3; j++){
                if(this.board.at(j+1,i+1)==this.board.at(j,i) &&
                        this.board.at(j+2,i+2)==this.board.at(j, i) &&
                        this.board.at(j+3,i+3)==this.board.at(j, i)){
                    if(this.board.at(j,i).getColour()==Colour.YELLOW){
                        return player1;
                    } else if(this.board.at(j, i).getColour()==Colour.RED){
                        return player2;
                    }
                }
            }
        }

        // looking for diagonally-left 4 in a row (/)
        for(int i=0; i<ROWS-3; i++){// need 4 in a row, avoiding error: out of bounds
            for(int j=3; j<COLS; j++){ // can only win this way with a checker in j>=3
                if(this.board.at(j-1,i+1)==this.board.at(j, i) &&
                        this.board.at(j-2,i+2)==this.board.at(j, i) &&
                        this.board.at(j-3, i+3)==this.board.at(j, i)){
                    if(this.board.at(j, i).getColour()==Colour.YELLOW){
                        return player1;
                    } else if(this.board.at(j, i).getColour()==Colour.RED){
                        return player2;
                    }
                }
            }
        }

        // if no 4 in a row was found
        return null; // player=null means no winner (yet)
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
                if(this.board.at(j, i)!=null){
                    return false;
                }
            }
        }
        return true;
    }




}
