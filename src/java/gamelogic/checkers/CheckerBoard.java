package gamelogic.checkers;

import gamelogic.PieceType;
import gamelogic.Player;
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
    private List<Player> players;

    /**
     * Default constructor that initializes the gameboard
     * @param size
     */
    public CheckerBoard(int size, List<Player> players) {
        this.size = size;
        board = new ArrayList<>(); //initialize the board
        this.players = players; //set up players
        setupBoard(); //method that sets up the board and pieces in the correct places
    }

    /**
     * Method that sets up the pieces in the default positions on the board
     * Adds checkers into list for board as well as player piece lists
     * @Param none
     * @Return void
     */
    private void setupBoard() {
        for (int y = 1; y <= 3; y++) { //add white pieces on first 3 rows
            if (y == 2) { //staggers the piece placement to keep them on the correct squares
                for (int x = 2; x <= size; x+=2) { //add checkers on squares 2,4,6,8
                    Checker addCheck = new Checker(PieceType.CHECKER, false, Colour.WHITE, false, y, x);
                    board.add(addCheck);
                    players.get(0).addToHand(addCheck);
                }
            } else {
                for (int x = 1; x <= size; x+=2) { //add checkers on squares 1,3,5,7
                    Checker addCheck = new Checker(PieceType.CHECKER, false, Colour.WHITE, false, y, x);
                    board.add(addCheck);
                    players.get(0).addToHand(addCheck);
                }
            }
        }

        for (int y = 6; y <= size; y++) { //add black pieces
            if (y == 7) {
                for (int x = 1; x <= size; x+=2) { //add checkers on squares 1,3,5,7
                    Checker addCheck = new Checker(PieceType.CHECKER, false, Colour.BLACK, false, y, x);
                    board.add(addCheck);
                    players.get(1).addToHand(addCheck);
                }
            } else {
                for (int x = 2; x <= size; x+=2) { //add checkers on squares 2,4,6,8
                    Checker addCheck = new Checker(PieceType.CHECKER, false, Colour.BLACK, false, y, x);
                    board.add(addCheck);
                    players.get(1).addToHand(addCheck);
                }
            }
        }
    }

    /**
     * Finds the checker at the requested coordinates
     * @param x
     * @param y
     * @return checker or null
     */
    public Checker getChecker(int x, int y) {
        for (Checker checker : board) {
            if (checker.getXPosition() == x && checker.getYPosition() == y) { //checks to see if checker is at correct coord to return
                return checker;
            }
        }
        return null;
    }

    /**
     * Removes a checker from the board and from the players piece list
     * @param checker
     */
    public void removeChecker(Checker checker) {
        board.remove(checker);
        if (checker.getColour() == Colour.RED) {
            players.get(1).removeFromHand(checker); //removes the piece from the opposing player's hand
            players.get(0).addToSpoils(checker); //adds it to the spoils of the current player
        } else { //if a black piece is eaten
            players.get(0).removeFromHand(checker);
            players.get(1).addToSpoils(checker);
        }
    }

    /**
     * Method to update the coordinates and position of a checker
     * @param checker
     * @param x
     * @param y
     * @param player
     */
    public void updatePosition(Checker checker, int x, int y, Player player) {
        board.get(board.indexOf(checker)).setXPosition(x);
        board.get(board.indexOf(checker)).setYPosition(y);
    }

    /**
     * Check if a double jump is possible from the current position
     * @param checker The checker to check for double jumps
     * @param player The current player
     * @return List of coordinates for possible double jumps, or empty list if none
     */
    public List<int[]> checkDouble(Checker checker, Player player) {
        List<int[]> coords = new ArrayList<>();
        int x = checker.getXPosition();
        int y = checker.getYPosition();
        //get colour
        Colour color = checker.getColour();

        //check if double is possible and what coordinates
        if (checker.isStacked()) { //checks forward and back
            checkDoubleJumpDirection(coords, x, y, color, 1, 1);  // Up-right
            checkDoubleJumpDirection(coords, x, y, color, 1, -1); // Down-right
            checkDoubleJumpDirection(coords, x, y, color, -1, 1); // Up-left
            checkDoubleJumpDirection(coords, x, y, color, -1, -1); // Down-left
        } else if (color == Colour.BLACK) { //check going down the board
            checkDoubleJumpDirection(coords, x, y, color, 1, -1); // Down-right
            checkDoubleJumpDirection(coords, x, y, color, -1, -1); // Down-left
        } else { //check going up for RED (displayed as WHITE)
            checkDoubleJumpDirection(coords, x, y, color, 1, 1);  // Up-right
            checkDoubleJumpDirection(coords, x, y, color, -1, 1); // Up-left
        }
        return coords; //returns the list of coordinates
    }

    /**
     * Helper method to check for double jumps in a specific direction
     * @param coords List to add valid jump coordinates to
     * @param x Starting x position
     * @param y Starting y position
     * @param color Checker color
     * @param dx X direction (1 or -1)
     * @param dy Y direction (1 or -1)
     */
    private void checkDoubleJumpDirection(List<int[]> coords, int x, int y, Colour color, int dx, int dy) {
        // Check if the adjacent position is within bounds
        int midX = x + dx;
        int midY = y + dy;
        if (midX < 1 || midX > size || midY < 1 || midY > size) {
            return;
        }
        
        // Check if there's an opponent's piece to jump over
        Checker midChecker = getChecker(midX, midY);
        if (midChecker == null || midChecker.getColour() == color) {
            return;
        }
        
        // Check if the landing position is within bounds and empty
        int landX = x + 2 * dx;
        int landY = y + 2 * dy;
        if (landX < 1 || landX > size || landY < 1 || landY > size) {
            return;
        }
        
        if (getChecker(landX, landY) == null) {
            coords.add(new int[]{landX, landY});
        }
    }

    /**
     * Override toString method to display the board
     * @return null
     */
    @Override
    public String toString() {
        Checker checkerToPrint = null;
        boolean squareTaken = false; //will be used in loop for checking if a square is taken
        for (int y = size; y >= 1; y--) { //loops will check every coordinate to see if there is
            for (int x = 1; x <= size; x++) {
                for (Checker checker : board) {
                    if (checker.getXPosition() == x && checker.getYPosition() == y) {
                        squareTaken = true;
                        checkerToPrint = checker;
                    }
                }
                if (squareTaken) {
                    if (checkerToPrint.getColour() == Colour.RED) { //prints the first letter of the colour
                        System.out.print("R "); // W for White (RED in code)
                    } else {
                        System.out.print("B "); // B for Black
                    }
                } else {
                    System.out.print("O "); //empty square
                }
                squareTaken = false;
            }
            System.out.print("\n"); //line break for new row
        }
        return null;
    }

    /**
     * Getter method for list of checkers
     * @return list of checkers
     */
    public List<Checker> getCheckers() {
        return board;
    }
}
