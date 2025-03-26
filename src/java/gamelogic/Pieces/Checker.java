package java.gamelogic.Pieces;

import java.gamelogic.GamePiece;
import java.gamelogic.PieceType;

/**
 * Handles the creation and handling of Checkers for Checker Games
 *
 * @authors Dylan Shiels
 * @date March 14, 2025
 */
public class Checker extends GamePiece {
    // ATTRIBUTES

    // The Colour of this Checker
    private final Colour colour;
    // The Stacked status of this Checker
    private boolean stacked;
    // The Y Coordinate of this Checker
    private int yPosition;
    // The X Coordinate of this Checker
    private int xPosition;


    // CONSTRUCTOR

    /**
     * Instantiates a Checker Game Piece with all of its required attributes
     *
     * @param type The given Piece Type for this Checker
     * @param held The given Held status for this Checker
     * @param colour The given Colour for this Checker
     * @param stacked The given Stacked status for this Checker
     * @param yPosition The given Y Coordinate for this Checker
     * @param xPosition The given X Coordinate for this Checker
     */
    public Checker (PieceType type, boolean held, Colour colour, boolean stacked, int yPosition, int xPosition) {
        super(type, held);
        this.colour = colour;
        this.stacked = stacked;
        this.yPosition = yPosition;
        this.xPosition = xPosition;
    }


    // GETTERS

    /**
     * Gets the Colour of this Checker
     *
     * @return The Colour to be retrieved
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Gets the Y Coordinate of this Checker
     *
     * @return The integer Y Coordinate to be retrieved
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Gets the X Coordinate of this Checker
     *
     * @return The integer X Coordinate to be retrieved
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Gets the Stack status of this Checker
     *
     * @return The boolean Stack status to be retrieved
     */
    public boolean isStacked() {
        return stacked;
    }


    // SETTERS

    /**
     * Sets a new Y Coordinate for this Checker
     *
     * @param yPosition The given new Y Coordinate
     */
    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Sets a new X Coordinate for this Checker
     *
     * @param xPosition The given new X Coordinate
     */
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }


    // METHODS

    /**
     * Stacks the Checker to make it a King
     */
    public void stack() {
        this.stacked = true;
    }

    /**
     * Un-stacks the Checker to make it a Soldier
     */
    public void unstack() {
        this.stacked = false;
    }
}