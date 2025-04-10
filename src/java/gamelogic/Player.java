package gamelogic;

import gamelogic.pieces.Card;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.Checker;
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

    private int level;

    private int expInLevel;

    private int nextLevelThreshold;


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

    public int getLevel()  {
        return this.level;
    }

    public void setLevel(int num)  {
        this.level = num;
    }

    public int getExpInLevel ()  {
        return this.expInLevel;
    }

    public void setExpInLevel(int num)  {
        this.expInLevel = num;
    }

    public int getNextLevelThreshold()  {
        return this.nextLevelThreshold;
    }

    public void setNextLevelThreshold(int num)  {
        this.nextLevelThreshold = num;
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
     * Sets the contents of this Player's Hand to a List of Game Pieces
     *
     * @param hand The given List of Game Pieces to put into this Player's Hand
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
     * Moves a specified Card from the Player's Hand to a specified Card Pile
     *
     * @param cardToMove The given Card to be moved from the Player's Hand
     * @param whereToMove The given Card Pile where the Card will be moved
     */
    public void moveFromHand(Card cardToMove, CardPile whereToMove) {
        this.hand.remove(cardToMove);

        whereToMove.addCards(cardToMove);
    }

    /**
     * Moves a specified Checker from the Player's Hand to a specified List
     *
     * @param checkerToMove The given Checker to be moved from the Player's Hand
     * @param whereToMove The given List of Checkers where the Checker will be moved
     */
    public void moveFromHand(Checker checkerToMove, List<Checker> whereToMove) {
        this.hand.remove(checkerToMove);

        whereToMove.add(checkerToMove);
    }

    /**
     * Checks if a Game Piece is in a Player's Hand
     *
     * @param pieceToCheck The given Game Piece to check a Player's Hand for
     * @return The boolean Presence of the given Piece in a Player's Hand
     */
    public boolean checkHand(GamePiece pieceToCheck) {
        // Loops through the Player's Hand to check for the given Game Piece
        for (GamePiece currentPiece : hand) {
            if (pieceToCheck == currentPiece) {
                // Returns true if the Player holds the given Piece
                return true;
            }
        }

        // Returns false if the Player doesn't hold the given Piece
        return false;
    }

    /**
     * Sorts any Cards that are in a Player's Hand by Rank
     */
    public void sortHand() {
        // Lists to temporarily store cards by suit
        List<Card> spades = new ArrayList<>();
        List<Card> diamonds = new ArrayList<>();
        List<Card> clubs = new ArrayList<>();
        List<Card> hearts = new ArrayList<>();
        
        // Temporary list to hold all cards for sorting
        List<GamePiece> tempHand = new ArrayList<>(hand);
        
        // Clear the hand to rebuild it sorted
        hand.clear();

        // Categorize cards by suit
        for (GamePiece currentPiece : tempHand) {
            if (currentPiece.getType() == PieceType.CARD) {
                Card currentCard = (Card) currentPiece;
                switch (currentCard.getSuit()) {
                    case SPADES:
                        spades.add(currentCard);
                        break;
                    case DIAMONDS:
                        diamonds.add(currentCard);
                        break;
                    case CLUBS:
                        clubs.add(currentCard);
                        break;
                    case HEARTS:
                        hearts.add(currentCard);
                        break;
                }
            } else {
                // If not a card, add it back immediately
                hand.add(currentPiece);
            }
        }

        // Sort each suit list by rank (highest to lowest)
        sortCardsByRank(spades);
        sortCardsByRank(diamonds);
        sortCardsByRank(clubs);
        sortCardsByRank(hearts);
        
        // Add sorted cards back to hand in order by suit
        hand.addAll(spades);
        hand.addAll(hearts);
        hand.addAll(diamonds);
        hand.addAll(clubs);
    }
    
    /**
     * Helper method to sort a list of cards by rank (highest to lowest)
     * 
     * @param cards The list of cards to sort
     */
    private void sortCardsByRank(List<Card> cards) {
        cards.sort((card1, card2) -> Integer.compare(card2.getRank(), card1.getRank()));
    }

    /**
     * Checks if the player is online
     *
     * @return true if the player is online, false otherwise
     */
    public boolean isOnline() {
        // Since this is a stub implementation, always return true
        // In a real application, this would check the player's connection status
        return true;
    }

}
