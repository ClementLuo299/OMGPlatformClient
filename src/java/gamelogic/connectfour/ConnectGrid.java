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
        // for
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

    @Override
    public String toString() {
        for (int y = 1; y <= size; y++) { //loops will check every coordinate to see if there is
            boolean squareTaken = false; //will be used in loop for checking if a square is taken
            for (int x = 1; x <= size; x++) {
                for (Checker checker : board) {
                    if (checker.getXPosition() == x && checker.getYPosition() == y) {
                        squareTaken = true;
                    }
                    if (squareTaken) {
                        if (checker.getColour() == Colour.WHITE) { //prints the first letter of the colour
                            System.out.print("W ");
                        } else {
                            System.out.print("B ");
                        }
                    } else {
                        System.out.print("O "); //empty square
                    }
                }
            }
            System.out.print("\n"); //line break for new row
        }
        return null;
    }
    */




}
