package gamelogic.whist;

import gamelogic.Game;
import gamelogic.GamePiece;
import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.pieces.Card;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.SuitType;
import networking.accounts.UserAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the logic for Whist games
 *
 * @authors Dylan Shiels
 * @date April 6, 2025
 */
public class WhistGame extends Game {
    // ATTRIBUTES

    // The number of rounds this game has gone through
    private int round;
    // The stage of gameplay this game is in
    private StageType stage;
    // The current trump suit of this game
    private SuitType trump;
    // The deck used for this game
    private CardPile deck;
    // The draw pile used for this game
    private CardPile draw;
    // The discard pile for this game
    private CardPile discard;
    // The list which holds the current trick
    private List<Card> trick;


    // CONSTRUCTOR

    /**
     * Instantiates a Game with a specified Game Type, List of participating Players, and the average Plays required to win
     *
     * @param gameType The given Game Type for this Game
     * @param players  The given List of Players for this Game
     * @param avgPlays The given integer Average Plays for this Game
     */
    public WhistGame(GameType gameType, List<Player> players, int avgPlays) {
        super(gameType, players, 8);
        this.round = 0;
        this.stage = StageType.DEAL;
        this.deck = new CardPile();
    }


    // GETTERS

    /**
     * Gets the current Round of this Whist Game
     *
     * @return The integer Round of this Whist Game
     */
    public int getRound() {
        return round;
    }

    /**
     * Gets the current Stage of this Whist Game
     *
     * @return The Stage Type of this Whist Game
     */
    public StageType getGameStage() {
        return stage;
    }

    /**
     * Gets the current Trump Suit of this Whist Game
     *
     * @return The Trump Suit Type of this Whist Game
     */
    public SuitType getTrump() {
        return trump;
    }

    /**
     * Gets the current Deck of this Whist Game
     *
     * @return The Deck Card Pile of this Whist Game
     */
    public CardPile getDeck() {
        return deck;
    }

    /**
     * Gets the current Trick of this Whist Game
     *
     * @return The List of Cards in the current Trick of this Whist Game
     */
    public List<Card> getTrick() {
        return trick;
    }

    /**
     * Gets the current Draw Pile of this Whist Game
     *
     * @return The Draw Card Pile of this Whist Game
     */
    public CardPile getDraw() {
        return draw;
    }

    /**
     * Gets the current Discard Pile of this Whist Game
     *
     * @return The Discard Card Pile of this Whist Game
     */
    public CardPile getDiscard() {
        return discard;
    }


    // SETTERS

    /**
     * Sets the current Round of this Whist Game
     *
     * @param gameRound The given integer Round for this Whist Game
     */
    public void setRound(int gameRound) {
        this.round = gameRound;
    }

    /**
     * Sets the current Stage for this round of this Whist Game
     *
     * @param gameStage The given Stage Type for this Whist Game round
     */
    public void setGameStage(StageType gameStage) {
        this.stage = gameStage;
    }

    /**
     * Sets the current Trump for this round of this Whist Game
     *
     * @param trumpSuit The given Suit Type Trump for this Whist Game round
     */
    public void setTrump(SuitType trumpSuit) {
        this.trump = trumpSuit;
    }

    /**
     * Sets the current Draw Pile for this round of this Whist Game
     *
     * @param drawPile The given Discard Card Pile for this Whist Game round
     */
    public void setDraw(CardPile drawPile) {
        this.draw = drawPile;
    }

    /**
     * Sets the current Discard Pile for this round of this Whist Game
     *
     * @param discardPile The given Discard Card Pile for this Whist Game round
     */
    public void setDiscard(CardPile discardPile) {
        this.discard = discardPile;
    }


    // METHODS


    public void dealCard(Card cardToDeal, Player cardHolder) {

    }


    public void playCard(Card cardToPlay, Player cardPlayer) {

    }


    public void takeCard(Card cardToTake, Player cardTaker) {

    }

    /**
     * Checks which Player wins a Trick based on the Rank of the Cards
     *
     * @return The Player who won the Trick
     */
    public Player getTrickWinner() {
        // The cards in the Trick
        Card leadCard = trick.getFirst();
        Card followingCard = trick.getLast();
        // The rank of the cards
        int leadRank = leadCard.getRank();
        int followingRank = followingCard.getRank();
        // The winning card
        Card winningCard = null;


        // Adds 14 to the rank of trump suited cards to give trump advantage in comparison
        if (leadCard.getSuit() == trump) {
            leadRank = leadRank + 14;
        }
        if (followingCard.getSuit() == trump) {
            followingRank = followingRank + 14;
        }

        // Awards the trick to whoever had the highest ranking card
        if (leadRank > followingRank) {
            winningCard = leadCard;
        }
        if (followingRank > leadRank) {
            winningCard = followingCard;
        }

        // Awards the trick to the leading card if the following card doesn't follow suit and isn't a trump card
        if (followingCard.getSuit() != leadCard.getSuit() && followingCard.getSuit() != trump) {
            winningCard = leadCard;
        }


        // Loops through each Player's hand to determine who is associated with the winning card
        // Player 1's Hand
        for (GamePiece currentPiece : this.getPlayers().getFirst().getHand()) {
            // Casts the current piece to a card to check suit
            Card currentCard = (Card) currentPiece;

            // Checks if the winning card is in their hand to declare them winner
            if (currentCard == winningCard) {
                return this.getPlayers().getFirst();
            }
        }

        // Player 2's Hand
        for (GamePiece currentPiece : this.getPlayers().getLast().getHand()) {
            // Casts the current piece to a card to check suit
            Card currentCard = (Card) currentPiece;

            // Checks if the winning card is in their hand to declare them winner
            if (currentCard == winningCard) {
                return this.getPlayers().getLast();
            }
        }

        // Returns null if no one wins
        return null;
    }


    /**
     * Gets the list of Playable Cards in a Player's hand who is leading in a Trick
     *
     * @param holder The given Player whose Hand is to be checked
     * @return The List of Playable Cards for the specified Player
     */
    public List<Card> getPlayableCards(Player holder) {
        // The temporary list to add hand cards to
        List<Card> cardsInHand = new ArrayList<>();
        // The hand of the cardholder
        List<GamePiece> cardholderHand = holder.getHand();

        // Adds all the Player's cards into a Card List
        for (GamePiece currentPiece : cardholderHand) {
            // Casts the current piece to a card to check suit
            Card currentCard = (Card) currentPiece;

            // Puts the current card into the hand list for ease of access
            cardsInHand.add(currentCard);
        }

        // Returns the valid cards
        return cardsInHand;
    }

    /**
     * Gets the list of Playable Cards in a Player's hand who is following in a Trick
     *
     * @param holder The given Player whose Hand is to be checked
     * @param leader The given Leading Card of the Trick
     * @return The List of Playable Cards for the specified Player
     */
    public List<Card> getPlayableCards(Player holder, Card leader) {
        // The temporary list to add valid cards to
        List<Card> validCards = new ArrayList<>();
        // The temporary list to add hand cards to
        List<Card> cardsInHand = new ArrayList<>();
        // The hand of the cardholder
        List<GamePiece> cardholderHand = holder.getHand();
        // The suit of the leading card
        SuitType suitToFollow = leader.getSuit();


        // Goes through the Hand of the Cardholder to determine their valid cards
        for (GamePiece currentPiece : cardholderHand) {
            // Casts the current piece to a card to check suit
            Card currentCard = (Card) currentPiece;

            // Puts the current card into the hand list for ease of access
            cardsInHand.add(currentCard);

            // Checks if the current card follows suit to validate it
            if (currentCard.getSuit() == suitToFollow) {
                validCards.add(currentCard);
            }
        }

        // Validates all cards in hand if the player cannot follow suit
        if (validCards.isEmpty()) {
            validCards = cardsInHand;
        }

        // Returns the valid cards
        return validCards;
    }


}
