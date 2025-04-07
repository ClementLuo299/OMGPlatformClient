package gamelogic.connectfour;

import gamelogic.*;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;

import java.util.*;

/**
 * Objects of this class store a ConnectFour Game board/grid
 * Instantiated by Connect4Game
 * Author: Scott Brown
 */

public class ConnectGrid {

    // the board is stored as a 2D array of Checker pointers in the following format:

    //    0 1 2 3 4 5 6 <- column
    // 0| n n n n n n n
    // 1| n n n n n n n
    // 2| n n n n n n n
    // 3| n n n n n n n
    // 4| n n n n n n n
    // 5| n n n n n n n

    // n for null, player1 is yellow, player2 is red
    // in the grid, no checker is ever held, or stacked. (held=false, stacked=false)
    // xPosition is the column, yPosition is the row

    protected static final int ROWS = 6;
    protected static final int COLS = 7;
    private Checker[][] board; // stores the current board

    /**
     * Default constructor
     */
    public ConnectGrid() {
        this.board = new Checker[COLS][ROWS]; //initialize the board

        // set every pointer to null
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLS; j++) {
                this.board[j][i] = null;
            }
        }
    }

    // GETTERS
    // gets the checker at a given position
    public Checker at(int col, int row){
        return this.board[col][row];
    }

    // SETTERS
    // sets a position to a given checker
    public void setPosition(int col, int row, Checker setTo){
        this.board[col][row] = setTo;
    }


    /**
     *
     * Override toString method to display the board
     * @return null
    */
    @Override
    public String toString() {

        String board_str = "   0 1 2 3 4 5 6\n";
        for(int i=0; i<ROWS; i++){
            board_str+=i+"| ";
            for(int j=0; j<COLS; j++) {
                if(this.board[j][i].getColour()==Colour.YELLOW){
                    board_str+="Y ";
                } else if(this.board[j][i].getColour()==Colour.RED){
                    board_str+="R ";
                } else {
                    board_str+="n ";
                }
            }
            board_str+="\n";
        }
        board_str+="\n";

        // the string will be set to and print as:
        //    0 1 2 3 4 5 6
        // 0| n n n n n n n
        // 1| n n n n n n n
        // 2| n n n n n n n
        // 3| n n n n n n n
        // 4| n n n n n n n
        // 5| n n n n n n n

        // n for "null" or unoccupied positions
        // Y for player1 or yellow
        // R rod player2 or red

        System.out.print(board_str);
        return board_str;
    }




}
