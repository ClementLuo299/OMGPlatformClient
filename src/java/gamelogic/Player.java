package gamelogic;

import gamelogic.pieces.Card;
import gamelogic.pieces.SuitType;
import networking.accounts.UserAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all relevant data of a Player within a Game
 *
 * @authors Scott Brown, Dylan Shiels
 * @date April 1, 2025
 */
public class Player {
    // ATTRIBUTES

    // The account UUID of the player
    private final UserAccount account;
    // The username of the player
    private final String username;
    // The number of plays this player has made
    private int plays;
    // The number of points this player has earned within a game
    private int score;
    // The player's game pieces
    private List<GamePiece> hand;
    // The player's captured game pieces
    private List<GamePiece> spoils;


    // CONSTRUCTOR

    /**
     * Instantiates a Player for a Game through their account
     *
     * @param account The given Account to create a Player from
     */
    public Player(UserAccount account) {
        this.account = account;
        this.username = this.account.getUsername();
        this.plays = 0;
        this.score = 0;
        this.hand = new ArrayList<>();
        this.spoils = new ArrayList<>();
    }


    // GETTERS

    /**
     * Gets the Account attached to this Player
     *
     * @return The User Account of this Player
     */
    public UserAccount getAccount() {
        return this.account;
    }

    /**
     * Gets the Username of this Player
     *
     * @return The String Username of this Player
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the Plays of this Player
     *
     * @return The integer Plays for this Player
     */
    public int getPlays() {
        return plays;
    }

    /**
     * Gets the Points of this Player
     *
     * @return The integer Points for this Player
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the Hand of this Player
     *
     * @return The List of Game Pieces held by this Player
     */
    public List<GamePiece> getHand() {
        return hand;
    }

    /**
     * Gets the Spoils of this Player
     *
     * @return The List of Game Pieces captured by this Player
     */
    public List<GamePiece> getSpoils() {
        return spoils;
    }


    // SETTERS

    /**
     * Adds a Play to this Player
     */
    public void addPlay() {
        this.plays = plays++;
    }

    /**
     * Adds a specified number of points to this Player
     *
     * @param pointsToAdd The given integer Points to award this Player
     */
    public void addPoints(int pointsToAdd) {
        this.score = score + pointsToAdd;
    }

    /**
     * method to set the updated list after modifications
     *
     * @param hand
     */
    public void setHand(List<GamePiece> hand) {
        this.hand = hand;
    }


    // METHODS

    /**
     * Adds a specified Game Piece to the Player's Hand
     *
     * @param pieceToAdd The given Game Piece to add to the Hand of this Player
     */
    public void addToHand(GamePiece pieceToAdd) {
        this.hand.add(pieceToAdd);
    }

    /**
     * Adds a specified Game Piece to the Player's Spoils
     *
     * @param pieceToAdd The given Game Piece to add to the Spoils of this Player
     */
    public void addToSpoils(GamePiece pieceToAdd) {
        this.spoils.add(pieceToAdd);
    }

    /**
     * Removes a specified Game Piece from the Player's Hand
     *
     * @param pieceToRemove The given Game Piece to be removed from the Player's Hand
     */
    public void removeFromHand(GamePiece pieceToRemove) {
        this.hand.remove(pieceToRemove);
    }

    /**
     * Sorts any Cards that are in a Player's Hand by Rank
     */
    public void sortHand() {
        // The lists of Cards of each Suit Type
        List<Card> spades = new ArrayList<>();
        List<Card> diamonds = new ArrayList<>();
        List<Card> clubs = new ArrayList<>();
        List<Card> hearts = new ArrayList<>();


        // Goes through the Player's Hand to sort Cards into Suits
        for (GamePiece currentPiece : hand) {
            // Checks if the Piece is a Card
            if (currentPiece.getType() == PieceType.CARD) {
                // Casts the Piece to a Card for ease of use
                Card currentCard = (Card) currentPiece;
                // Obtains the Suit of the Card
                SuitType currentSuit = currentCard.getSuit();

                // Puts the Card into designated Suit list and removes it from the Player's Hand
                switch (currentSuit) {
                    case SPADES:
                        spades.add(currentCard);
                        removeFromHand(currentCard);
                        break;
                    case DIAMONDS:
                        diamonds.add(currentCard);
                        removeFromHand(currentCard);
                        break;
                    case CLUBS:
                        clubs.add(currentCard);
                        removeFromHand(currentCard);
                        break;
                    case HEARTS:
                        hearts.add(currentCard);
                        removeFromHand(currentCard);
                        break;
                }
            }
        }

        // Sorts each Suit by Rank
        // This is done by iterating from highest to lowest Rank, moving any cards of that Rank to the top of the list

        // Sort Spades
        for (int rank = 13; rank > 0; rank--) {
            for (int i = 0; i < spades.size(); i++) {
                // Gets the current card from the list
                Card currentCard = spades.get(i);

                // Moves the current card to the front of the list if it matches the Rank value
                if (currentCard.getRank() == rank) {
                    // Moves the matching card to the top of the list
                    spades.remove(currentCard);
                    spades.add(currentCard);

                    // Moves i back by one to keep place in the list
                    i--;
               }
            }
        }

        // Sort Diamonds
        for (int rank = 13; rank > 0; rank--) {
            for (int i = 0; i < diamonds.size(); i++) {
                // Gets the current card from the list
                Card currentCard = diamonds.get(i);

                // Moves the current card to the front of the list if it matches the Rank value
                if (currentCard.getRank() == rank) {
                    // Moves the matching card to the top of the list
                    diamonds.remove(currentCard);
                    diamonds.add(currentCard);

                    // Moves i back by one to keep place in the list
                    i--;
                }
            }
        }

        // Sort Clubs
        for (int rank = 13; rank > 0; rank--) {
            for (int i = 0; i < clubs.size(); i++) {
                // Gets the current card from the list
                Card currentCard = clubs.get(i);

                // Moves the current card to the front of the list if it matches the Rank value
                if (currentCard.getRank() == rank) {
                    // Moves the matching card to the top of the list
                    clubs.remove(currentCard);
                    clubs.add(currentCard);

                    // Moves i back by one to keep place in the list
                    i--;
                }
            }
        }

        // Sort Hearts
        for (int rank = 13; rank > 0; rank--) {
            for (int i = 0; i < hearts.size(); i++) {
                // Gets the current card from the list
                Card currentCard = hearts.get(i);

                // Moves the current card to the front of the list if it matches the Rank value
                if (currentCard.getRank() == rank) {
                    // Moves the matching card to the top of the list
                    hearts.remove(currentCard);
                    hearts.add(currentCard);

                    // Moves i back by one to keep place in the list
                    i--;
                }
            }
        }


    }
}
