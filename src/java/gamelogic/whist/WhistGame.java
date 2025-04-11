package gamelogic.whist;

import gamelogic.*;
import gamelogic.pieces.Card;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.SuitType;

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
    // The player who is the dealer for this round
    private Player dealer;


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
        this.trump = null;
        this.deck = new CardPile();
        this.trick = new ArrayList<>();
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
    
    /**
     * Gets the current Dealer of this Whist Game
     * 
     * @return The Player who is the dealer
     */
    public Player getDealer() {
        return dealer;
    }

    /**
     * Gets the Player whose turn it is currently
     * 
     * @return The Player whose turn it is
     */
    public Player getCurrentTurn() {
        return getTurnHolder();
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
    
    /**
     * Sets the current Dealer for this round of this Whist Game
     * 
     * @param dealer The Player who will be the dealer
     */
    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }


    // METHODS

    /**
     * Deals a card from a Card Pile to the Hand of a Player
     *
     * @param deck The given Card Pile to deal from
     * @param cardToDeal The given Card to deal from the Pile
     * @param cardHolder The given Player who receives the Card
     */
    public void dealCard(CardPile deck, Card cardToDeal, Player cardHolder) {
        // Makes the card not visible to all players by making its held status true
        cardToDeal.take();

        // Adds the dealt Card to the Player's hand
        cardHolder.addToHand(cardToDeal);

        // Removes the dealt Card from the Deck
        deck.removeCard(cardToDeal);
    }

    /**
     * Plays a Card from a Player's hand into the current Trick
     *
     * @param cardToPlay The given Card to play
     */
    public void playCard(Card cardToPlay) {
        // Makes the card visible to all players by making its held status false
        cardToPlay.release();

        // Adds the played Card to the Trick list
        trick.add(cardToPlay);
    }

    /**
     * Takes a Card from a Card Pile and places it into a Player's Hand
     *
     * @param source The given Card Pile the Player is drawing from
     * @param cardToTake The given Card the Player is drawing
     * @param cardTaker The given Player who is drawing a Card
     */
    public void takeCard(CardPile source, Card cardToTake, Player cardTaker) {
        // Makes the card not visible to all players by making its held status true
        cardToTake.take();

        // Adds the Card to the Player's hand
        cardTaker.addToHand(cardToTake);

        // Makes the card face up so the Player can view it
        if (cardToTake.isFaceDown()) {
            cardToTake.flip();
        }

        // Sorts the Hand of the Player
        cardTaker.sortHand();

        // Removes the Card from the Card Pile it was drawn from
        source.removeCard(cardToTake);
    }

    /**
     * Compares two Cards to see which Card has the highest Rank
     *
     * @param card1 The first given Card to compare
     * @param card2 The second given Card to compare
     * @return The highest Ranking Card
     */
    public Card compareCards(Card card1, Card card2) {
        // The Rank of each card
        int card1Rank = card1.getRank();
        int card2Rank = card2.getRank();
        // The Card that has the highest Rank
        Card winningCard = null;

        // Checks if Trump cards are in play to account for them
        if (trump != null) {
            // Adds 14 to the rank of trump suited cards to give trump advantage in comparison
            if (card1.getSuit() == trump) {
                card1Rank = card1Rank + 14;
            }
            if (card2.getSuit() == trump) {
                card2Rank = card2Rank + 14;
            }
        }

        // Awards the comparison to the highest ranking card
        if (card1Rank > card2Rank) {
            winningCard = card1;
        }
        if (card2Rank > card1Rank) {
            winningCard = card2;
        }

        // Returns null if the Cards are tied
        return winningCard;
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
        // The winning card
        Card winningCard;

        // The Players in this Game
        Player player1 = this.getPlayers().getFirst();
        Player player2 = this.getPlayers().getLast();


        // Compares the leading and following card to see which is highest ranked
        winningCard = compareCards(leadCard, followingCard);

        // Awards the trick to the leading card if the following card doesn't follow suit and isn't a trump card
        if (followingCard.getSuit() != leadCard.getSuit() && followingCard.getSuit() != trump) {
            winningCard = leadCard;
        }


        // Checks each Player's hand for the winning Card to determine which Player won the Trick
        if (player1.checkHand(winningCard)) {
            return player1;
        }
        if (player2.checkHand(winningCard)) {
            return player2;
        }

        // Returns null if no one wins
        return null;
    }

    /**
     * Discards the Cards played in the current Trick, removing them from the Trick and each Players' Hand
     */
    public void completeTrick() {
        // The participating Players
        Player player1 = this.getPlayers().getFirst();
        Player player2 = this.getPlayers().getLast();

        // Makes all Cards in the Trick face-down and Discards them
        for (Card currentCard : trick) {
            // Flips face-up cards down
            if (!currentCard.isFaceDown()) {
                currentCard.flip();
            }

            // Discards cards
            discard.addCards(currentCard);
        }

        // Removes all Cards in the Trick from Players' Hands
        for (GamePiece cardToRemove : trick) {
            // Player 1's Hand
            if (player1.checkHand(cardToRemove)) {
                player1.removeFromHand(cardToRemove);
            }
            // Player 2's Hand
            if (player2.checkHand(cardToRemove)) {
                player2.removeFromHand(cardToRemove);
            }
        }

        // Empties the Trick for the next Trick
        trick.clear();
    }

    /**
     * Sots a Player's Hand and reveals their Cards to them
     *
     * @param holder The given Player to reveal their Hand to
     */
    public void showHand(Player holder) {
        // The hand of the Player
        List<GamePiece> hand = holder.getHand();

        // Sorts the Player's Hand
        holder.sortHand();

        // Goes through the list of cards in the Player's hand to flip them
        for (GamePiece currentPiece : hand) {
            // Ensures the piece is flippable
            if (currentPiece.getType() == PieceType.CARD) {
                Card currentCard = (Card) currentPiece;
                // Makes the card face up so the Player can view it
                if (currentCard.isFaceDown()) {
                    currentCard.flip();
                }
            }
        }
    }

    /**
     * Resets the Deck for new rounds
     * Will be removed later, as the same deck is supposed to be used over multiple rounds.
     */
    public void resetDeck() {
        deck.clear();
        this.deck = new CardPile();
    }

    /**
     * Moves this Whist Game to the next Stage of gameplay
     */
    public void nextStage() {
        switch (stage) {
            case DEAL:
                this.stage = StageType.DRAFT;
                break;
            case DRAFT:
                this.stage = StageType.DUEL;
                break;
            case DUEL:
                this.stage = null;
                break;
        }
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
     * @param leadCard The given Leading Card of the Trick
     * @return The List of Playable Cards for the specified Player
     */
    public List<Card> getPlayableCards(Player holder, Card leadCard) {
        // The temporary list to add valid cards to
        List<Card> validCards = new ArrayList<>();
        // The temporary list to add hand cards to
        List<Card> cardsInHand = new ArrayList<>();
        // The hand of the cardholder
        List<GamePiece> cardholderHand = holder.getHand();
        // The suit of the leading card
        SuitType suitToFollow = leadCard.getSuit();


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