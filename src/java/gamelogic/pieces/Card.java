package gamelogic.pieces;

import gamelogic.GamePiece;
import gamelogic.PieceType;
import gamelogic.pieces.Colour;

/**
 * Handles the creation and handling of Cards for Playing Card Games
 *
 * @authors Sameer Askar, Dylan Shiels
 * @date March 13, 2025
 */
public class Card extends GamePiece {
    // ATTRIBUTES

    // The Suit Type of this Card
    private final SuitType suit;
    // The Rank of this Card
    private final int rank;
    // The Face Visibility of this Card
    private boolean faceDown;


    /**
     * Instantiates a Card Game Piece with all of its required attributes
     *
     * @param type The given Piece Type for this Card
     * @param held The given Held status of this Card
     * @param suit The given Suit Type of this Card
     * @param rank The given integer Rank of this Card
     * @param faceDown The given boolean Face visibility of this Card
     */
    public Card(PieceType type, boolean held, SuitType suit, int rank, boolean faceDown) {
        super(type, held);
        this.suit = suit;
        this.rank = rank;
        this.faceDown = faceDown;
    }

    /**
     * Instantiates a Card Game Piece with default values and specified rank and suit
     *
     * @param rank The given integer Rank of this Card
     * @param suit The given Suit Type of this Card
     */
    public Card(int rank, SuitType suit) {
        super(PieceType.CARD, false);
        this.suit = suit;
        this.rank = rank;
        this.faceDown = true;
    }


    // GETTERS

    /**
     * Gets the Suit Type of this Card
     *
     * @return The Suit Type to be retrieved
     */
    public SuitType getSuit() {
        return suit;
    }

    /**
     * Gets the Rank of this Card
     *
     * @return The integer Rank to be retrieved
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the Face visibility of this Card
     *
     * @return The boolean Face visibility to be retrieved
     */
    public boolean isFaceDown() {
        return faceDown;
    }


    // METHODS

    /**
     * Flips this Card to change its Face visibility
     */
    public void flip() {
        this.faceDown = !faceDown;
    }

    /**
     * Creates a String which represents this Card
     *
     * @return The String of information for this Card
     */
    @Override
    public String toString() {
        // Initialized String to return
        String cardTitle;

        // Checks the Rank of the Card to appropriately title it
        switch (rank) {
            case 1:
                cardTitle = "Ace of " + suit.getDisplayName();
                break;
            case 13:
                cardTitle = "King of " + suit.getDisplayName();
                break;
            case 12:
                cardTitle = "Queen of " + suit.getDisplayName();
                break;
            case 11:
                cardTitle = "Jack of " + suit.getDisplayName();
                break;
            default:
                cardTitle = rank + " of " + suit.getDisplayName();
                break;

        }

        // Returns the resulting String
        return cardTitle;
    }
}