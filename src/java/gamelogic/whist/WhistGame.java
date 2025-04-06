package gamelogic.whist;

import gamelogic.Game;
import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.SuitType;

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

}
