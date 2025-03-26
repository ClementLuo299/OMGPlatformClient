package main.java.gamelogic;

/**
 * Handles the creation and handling of Game Pieces for Games
 *
 * @authors Dylan Shiels
 * @date March 14, 2025
 */
public class GamePiece {
    // ATTRIBUTES

    private final PieceType type;
    private boolean held;


    // CONSTRUCTOR

    /**
     * Instantiates a Game Piece with a Piece Type and Held status
     *
     * @param type The given Piece Type for this Game Piece
     * @param held The given Held status of this Game Piece
     */
    public GamePiece(PieceType type, boolean held) {
        this.type = type;
        this.held = held;
    }


    // GETTERS

    /**
     * Gets the Piece Type of this Game Piece
     *
     * @return The Piece Type to be retrieved
     */
    public PieceType getType() {
        return this.type;
    }


    // METHODS

    /**
     * Sets the Held status to True for this Game Piece
     */
    protected void take() {
        this.held = true;
    }

    /**
     * Sets the Held status to False for this Game Piece
     */
    protected void release() {
        this.held = false;
    }
}
