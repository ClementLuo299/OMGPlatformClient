package gamelogic.checkers;

import gamelogic.Game;
import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;

import java.util.ArrayList;
import java.util.List;

/**
 * Checkers game class
 * Handles main game logic for checkers
 * Author: Sameer Askar
 */

public class CheckersGame extends Game {
    private CheckerBoard board;
    /**
     * Default constructor to set up the game by calling superclass to get all the methods and variables from it
     * @param players
     */
    public CheckersGame(List<Player> players) {
        super(GameType.CHECKERS, players, 25);
        board = new CheckerBoard(8, players);
        // Player 1 starts the game (white pieces)
        setTurnHolder(players.get(0));
    }

    /**
     * Method to make move
     * Will be called ONLY AFTER getValidMoves and only clickable squares are the valid moves to avoid bugs and errors
     * @param player
     * @param checker
     * @param x
     * @param y
     * @return list of coordinates for double jump or null
     */
    public List<int[]> makeMove(Player player, Checker checker, int x, int y) {
        //check if move will eat opposing piece
        int distanceX = x - checker.getXPosition(); //distance must be > 1 in order for a piece to be eaten
        int distanceY = y - checker.getYPosition();
        if (Math.abs(distanceX) > 1) { //means a piece has been eaten (hopped over it)

            Checker eaten = board.getChecker(checker.getXPosition() + distanceX/2, checker.getYPosition() + distanceY/2); //will find eaten piece
            //Set new position for checker
            board.updatePosition(checker, x, y, player);

            //remove the eaten piece
            board.removeChecker(eaten); //calls a method that removes the checker
            //remove for player
            if (eaten.getColour() == Colour.WHITE) {
                getPlayers().get(0).removeFromHand(eaten);
                getPlayers().get(1).addToSpoils(eaten);
            } else {
                getPlayers().get(1).removeFromHand(eaten);
                getPlayers().get(0).addToSpoils(eaten);
            }


            //if y value for WHITE is 8, 1 for black, then make checker a king
            Colour colour = checker.getColour();
            if (colour == Colour.WHITE) {
                if (y == 8) {
                    checker.stack();
                }
            } else if (colour == Colour.BLACK) {
                if (y == 1) {
                    checker.stack();
                }
            }

            //check if double jump is possible
            return board.checkDouble(checker, player);
        } else {
            board.updatePosition(checker, x, y, player);
            //if y value for WHITE is 8, 1 for black, then make checker a king
            Colour colour = checker.getColour();
            if (colour == Colour.WHITE) {
                if (y == 8) {
                    checker.stack();
                }
            } else if (colour == Colour.BLACK) {
                if (y == 1) {
                    checker.stack();
                }
            }
            return null;
        }
    }

    /**
     * Method checks if a player has won the game
     * Player wins if either:
     * 1. The opponent has no more pieces
     * 2. The opponent has no valid moves left
     * 
     * @param player The player to check if they've won
     * @return boolean true if player has won, false otherwise
     */
    public boolean gameWon(Player player) {
        Player opponent = (player == getPlayers().get(0)) ? getPlayers().get(1) : getPlayers().get(0);
        
        // Check if opponent has no pieces left
        if (opponent.getHand().isEmpty()) {
            return true;
        }
        
        // Check if opponent has no valid moves left
        List<int[]> allPossibleMoves = new ArrayList<>();
        
        // For each of opponent's pieces, check if they have any valid moves
        for (Checker checker : board.getCheckers()) {
            if ((checker.getColour() == Colour.BLACK && opponent == getPlayers().get(1)) ||
                (checker.getColour() == Colour.WHITE && opponent == getPlayers().get(0))) {
                
                List<int[]> pieceMoves = getValidMoves(checker.getXPosition(), checker.getYPosition());
                if (pieceMoves != null && !pieceMoves.isEmpty()) {
                    allPossibleMoves.addAll(pieceMoves);
                }
            }
        }
        
        // If the opponent has no valid moves, the current player wins
        return allPossibleMoves.isEmpty();
    }

    /**
     * Method that finds all the valid moves for a turn
     * @param x
     * @param y
     * @return List of coordinates
     */
    public List<int[]> getValidMoves(int x, int y) {
        List<int[]> coords = new ArrayList<>(); //will hold all the valid coordinates that the piece can move to
        //get the piece on that square
        Checker currentChecker = board.getChecker(x, y); //find the corresponding checker
        if (currentChecker == null) {
            return null; //returns null if no checker is on the square
        }
        if (currentChecker.getColour() == Colour.WHITE) { //checks if checker is WHITE or BLACK
            if (currentChecker.isStacked()) {
                getWhiteMoves(coords, x, y, true);
            } else {
                getWhiteMoves(coords, x, y, false);
            }
        } else { //colour is black
            if (currentChecker.isStacked()) {
                getBlackMoves(coords, x, y, true);
            } else {
                getBlackMoves(coords, x, y, false);
            }
        }
        return coords; //return all the coordinates that are valid
    }

    /**
     * Method finds the valid moves for WHITE pieces
     * @param moves
     * @param x
     * @param y
     * @param stacked
     */
    public void getWhiteMoves(List<int[]> moves, int x, int y, boolean stacked) {
        if (stacked) { //check forward and backward
            // Check all four directions for king pieces
            checkMoveDirection(moves, x, y, Colour.WHITE, 1, 1);   // Up-right
            checkMoveDirection(moves, x, y, Colour.WHITE, -1, 1);  // Up-left
            checkMoveDirection(moves, x, y, Colour.WHITE, 1, -1);  // Down-right
            checkMoveDirection(moves, x, y, Colour.WHITE, -1, -1); // Down-left
        } else { //regular checker (can only move up)
            // Regular WHITE pieces can only move upward
            checkMoveDirection(moves, x, y, Colour.WHITE, 1, 1);   // Up-right
            checkMoveDirection(moves, x, y, Colour.WHITE, -1, 1);  // Up-left
        }
    }

    /**
     * Method finds the valid moves for black pieces
     * @param moves
     * @param x
     * @param y
     * @param stacked
     */
    public void getBlackMoves(List<int[]> moves, int x, int y, boolean stacked) {
        if (stacked) { //check forward and backward
            // Check all four directions for king pieces
            checkMoveDirection(moves, x, y, Colour.BLACK, 1, 1);   // Up-right
            checkMoveDirection(moves, x, y, Colour.BLACK, -1, 1);  // Up-left
            checkMoveDirection(moves, x, y, Colour.BLACK, 1, -1);  // Down-right
            checkMoveDirection(moves, x, y, Colour.BLACK, -1, -1); // Down-left
        } else { //regular checker (can only move down)
            // Regular black pieces can only move downward
            checkMoveDirection(moves, x, y, Colour.BLACK, 1, -1);  // Down-right
            checkMoveDirection(moves, x, y, Colour.BLACK, -1, -1); // Down-left
        }
    }

    /**
     * Helper method to check for valid moves in a specific direction
     * @param moves List to add valid move coordinates to
     * @param x Starting x position
     * @param y Starting y position
     * @param color Checker color
     * @param dx X direction (1 or -1)
     * @param dy Y direction (1 or -1)
     */
    private void checkMoveDirection(List<int[]> moves, int x, int y, Colour color, int dx, int dy) {
        // Check regular move (one square)
        int moveX = x + dx;
        int moveY = y + dy;
        if (moveX >= 1 && moveX <= 8 && moveY >= 1 && moveY <= 8) {
            Checker checkSquare = board.getChecker(moveX, moveY);
            if (checkSquare == null) {
                // Empty square - can move here
                moves.add(new int[]{moveX, moveY});
            } else if (checkSquare.getColour() != color) {
                // Check jump move (capture)
                int jumpX = x + 2 * dx;
                int jumpY = y + 2 * dy;
                if (jumpX >= 1 && jumpX <= 8 && jumpY >= 1 && jumpY <= 8) {
                    Checker jumpSquare = board.getChecker(jumpX, jumpY);
                    if (jumpSquare == null) {
                        // Can jump to this empty square
                        moves.add(new int[]{jumpX, jumpY});
                    }
                }
            }
        }
    }

    /**
     * Get the checker board
     * @return CheckerBoard instance
     */
    public CheckerBoard getBoard () {
        return board;
    }
}