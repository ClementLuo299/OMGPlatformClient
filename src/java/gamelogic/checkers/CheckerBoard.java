package gamelogic.checkers;

import gamelogic.PieceType;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;

import java.util.*;

/**
 * CheckerBoard class that will handle logic for the checkerboard as well as moves being made
 * Called as an object from the CheckersGame class
 * Author: Sameer Askar
 */

public class CheckerBoard {
    private List<Checker> board; //holds all the checkers in the board
    private int size; //will be used for methods that involve setup or display of the board (for loops)

    /**
     * Default constructor that initializes the gameboard
     * @param size
     */
    public CheckerBoard(int size) {
        this.size = size;
        board = new ArrayList<>(); //initialize the board
        setupBoard(); //method that sets up the board and pieces in the correct places
    }

    /**
     * Method that sets up the pieces in the default positions on the board
     * @Param none
     * @Return void
     */
    private void setupBoard() {
        for (int y = 1; y <= 3; y++) { //add white pieces on first 3 rows
            if (y == 2) { //staggers the piece placement to keep them on the correct squares
                for (int x = 2; x <= size; x+=2) { //add checkers on squares 2,4,6,8
                    board.add(new Checker(PieceType.CHECKER, false, Colour.WHITE, false, y, x));
                }
            } else {
                for (int x = 1; x <= size; x+=2) { //add checkers on squares 1,3,5,7
                    board.add(new Checker(PieceType.CHECKER, false, Colour.WHITE, false, y, x));
                }
            }
        }

        for (int y = 6; y <= size; y++) { //add black pieces
            if (y == 2) {
                for (int x = 1; x <= size; x+=2) { //add checkers on squares 1,3,5,7
                    board.add(new Checker(PieceType.CHECKER, false, Colour.BLACK, false, y, x));
                }
            } else {
                for (int x = 2; x <= size; x+=2) { //add checkers on squares 2,4,6,8
                    board.add(new Checker(PieceType.CHECKER, false, Colour.BLACK, false, y, x));
                }
            }
        }
    }

    public Checker getChecker(int x, int y) {
        for (Checker checker : board) {
            if (checker.getXPosition() == x && checker.getYPosition() == y) { //checks to see if checker is at correct coord to return
                return checker;
            }
        }
        return null;
    }

    /**
     * Override toString method to display the board
     * @return null
     */
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
}
