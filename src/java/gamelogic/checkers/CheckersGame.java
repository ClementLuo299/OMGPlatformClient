package gamelogic.checkers;

import gamelogic.Game;
import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;


import java.util.*;

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
    }

    /**
     * Method to make move
     * Will be called ONLY AFTER getValidMoves and only clickable squares are the valid moves to avoid bugs and errors
     * @param player
     * @param x
     * @param y
     * @return list of coordinates for double jump or null
     */
    public List<int[]> makeMove(Player player, Checker checker, int x, int y) {
        //check if move will eat opposing piece
        int distanceX = checker.getXPosition() - x; //distance must be > 1 in order for a piece to be eaten
        int distanceY = checker.getYPosition() - y;
        if (Math.abs(distanceX) > 1) { //means a piece has been eaten (hopped over it)
            //remove eaten piece
            Checker eaten = board.getChecker(checker.getXPosition() + distanceX, checker.getYPosition() + distanceY); //will find eaten piece

            //Set new position for checker
            board.updatePosition(checker, x, y, player);

            //remove the eaten piece
            board.removeChecker(eaten); //calls a method that removes the checker

            //if y value for white is 8, 1 for black, then make checker a king
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
            //if y value for white is 8, 1 for black, then make checker a king
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
     * @param player
     * @return boolean if player has no more pieces
     */
    public boolean gameWon(Player player) {
        return player.getHand().isEmpty();
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
        if (currentChecker.getColour() == Colour.WHITE) { //checks if checker is white or black
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
     * method finds the valid white moves
     * @param moves
     * @param x
     * @param y
     * @param stacked
     */
    public void getWhiteMoves(List<int[]> moves, int x, int y, boolean stacked) {
        if (stacked) { //check forward and backward
            //check up right
            int tempx = x + 1;
            int tempy = y + 1;
            Checker checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.BLACK) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy + 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy + 1}); //can be eaten so jump over black piece
                }
            } //no need to check for white piece since move is invalid if that is the case

            //check up left
            tempx = x - 1;
            tempy = y + 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.BLACK) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy + 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx - 1, tempy + 1}); //can be eaten so jump over black piece
                }
            }

            //check down left
            tempx = x - 1;
            tempy = y - 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.BLACK) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx - 1, tempy - 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy + 1}); //can be eaten so jump over black piece
                }
            }

            //check down right
            tempx = x + 1;
            tempy = y - 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.BLACK) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy - 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy - 1}); //can be eaten so jump over black piece
                }
            }

        } else { //regular checker (prince)
            //check up right
            int tempx = x + 1;
            int tempy = y + 1;
            Checker checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.BLACK) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy + 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy + 1}); //can be eaten so jump over black piece
                }
            } //no need to check for white piece since move is invalid if that is the case

            //check up left
            tempx = x - 1;
            tempy = y + 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.BLACK) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy + 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx - 1, tempy + 1}); //can be eaten so jump over black piece
                }
            }
        }
    }

    /**
     * finds the valid moves for black
     * @param moves
     * @param x
     * @param y
     * @param stacked
     */
    public void getBlackMoves(List<int[]> moves, int x, int y, boolean stacked) {
        if (stacked) { //check forward and backward
            //check up right
            int tempx = x + 1;
            int tempy = y + 1;
            Checker checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.WHITE) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy + 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy + 1}); //can be eaten so jump over black piece
                }
            } //no need to check for white piece since move is invalid if that is the case

            //check up left
            tempx = x - 1;
            tempy = y + 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.WHITE) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy + 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx - 1, tempy + 1}); //can be eaten so jump over black piece
                }
            }

            //check down left
            tempx = x - 1;
            tempy = y - 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.WHITE) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx - 1, tempy - 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy + 1}); //can be eaten so jump over black piece
                }
            }

            //check down right
            tempx = x + 1;
            tempy = y - 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.WHITE) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy - 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy - 1}); //can be eaten so jump over black piece
                }
            }

        } else { //regular checker (prince)
            //check down right
            int tempx = x + 1;
            int tempy = y - 1;
            Checker checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.WHITE) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx + 1, tempy - 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx + 1, tempy - 1}); //can be eaten so jump over black piece
                }
            }

            //check down left
            tempx = x - 1;
            tempy = y - 1;
            checkSquare = board.getChecker(tempx, tempy);
            if (checkSquare == null) {
                moves.add(new int[]{tempx, tempy}); //square is empty and can be moved to
            } else if (checkSquare.getColour() == Colour.WHITE) { //check if black piece can be eaten
                Checker checkEaten = board.getChecker(tempx - 1, tempy - 1);
                if (checkEaten == null) {
                    moves.add(new int[]{tempx - 1, tempy - 1}); //can be eaten so jump over black piece
                }
            }
        }
    }
}