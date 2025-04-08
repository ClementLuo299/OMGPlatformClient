package gamelogic.whist;

import gamelogic.Game;
import gamelogic.GamePiece;
import gamelogic.GameType;
import gamelogic.Player;
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
    // The number of shuffles performed
    private int shuffleCount;
    // The player who leads the current trick
    private Player trickLeader;


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
        this.draw = new CardPile();
        this.discard = new CardPile();
        this.trick = new ArrayList<>();
        this.shuffleCount = 0;
        this.trickLeader = null;
        
        // Initialize the deck with 52 cards
        initializeDeck();
    }
    
    /**
     * Initializes the deck with a standard 52-card deck
     * Creates 13 cards of each suit (Ace through King)
     */
    public void initializeDeck() {
        // Clear any existing cards
        deck.clear();
        
        // Add all 52 cards, 13 from each suit
        for (SuitType suit : SuitType.values()) {
            for (int rank = 1; rank <= 13; rank++) {
                deck.addCards(new Card(rank, suit));
            }
        }
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
     * Gets the current shuffle count
     *
     * @return The number of times the deck has been shuffled
     */
    public int getShuffleCount() {
        return shuffleCount;
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
     * Sets the shuffle count
     *
     * @param count The number of times the deck has been shuffled
     */
    public void setShuffleCount(int count) {
        this.shuffleCount = count;
    }
    
    /**
     * Increment the shuffle count by 1 and shuffles the deck
     */
    public void incrementShuffleCount() {
        this.shuffleCount++;
        shuffleDeck();
    }
    
    /**
     * Shuffles the deck randomly
     */
    public void shuffleDeck() {
        if (deck != null) {
            deck.shuffle();
        }
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
        // Adds the dealt Card to the Player's hand
        cardHolder.addToHand(cardToDeal);

        // Removes the dealt Card from the Deck
        deck.removeCard(cardToDeal);
    }
    
    /**
     * Deals all cards to players automatically
     * Each player receives 13 cards from the deck
     */
    public void dealAllCards() {
        if (stage != StageType.DEAL || shuffleCount < 3) {
            return; // Can only deal in DEAL stage after 3 shuffles
        }
        
        Player player1 = this.getPlayers().getFirst();
        Player player2 = this.getPlayers().getLast();
        
        // Deal 13 cards to each player alternately
        for (int i = 0; i < 13; i++) {
            // Deal to player 1
            if (deck.getCards().size() > 0) {
                Card p1Card = (Card) deck.getCards().getFirst();
                dealCard(deck, p1Card, player1);
            }
            
            // Deal to player 2
            if (deck.getCards().size() > 0) {
                Card p2Card = (Card) deck.getCards().getFirst();
                dealCard(deck, p2Card, player2);
            }
        }
        
        // After dealing, move to DRAFT stage
        setGameStage(StageType.DRAFT);
    }
    
    /**
     * Reveals the trump suit for the round
     * Selects the first card from the remaining deck as trump
     * 
     * @return The revealed trump suit
     */
    public SuitType revealTrump() {
        if (stage != StageType.DRAFT || deck.getCards().isEmpty()) {
            return null; // Can only reveal trump in DRAFT stage with cards in deck
        }
        
        // Get the first card from remaining deck as trump
        Card trumpCard = (Card) deck.getCards().getFirst();
        SuitType trumpSuit = trumpCard.getSuit();
        
        // Set the trump
        setTrump(trumpSuit);
        
        // After revealing trump, move to DUEL stage
        setGameStage(StageType.DUEL);
        
        return trumpSuit;
    }

    /**
     * Plays a Card from a Player's hand into the current Trick
     *
     * @param cardToPlay The given Card to play
     * @param player The player who is playing the card
     */
    public void playCard(Card cardToPlay, Player player) {
        // Makes the card visible to all players by making its held status false
        cardToPlay.release();

        // Adds the played Card to the Trick list
        trick.add(cardToPlay);
        
        // Remove from player's hand
        player.removeFromHand(cardToPlay);
        
        // Set trickLeader if this is the first card of the trick
        if (trick.size() == 1) {
            trickLeader = player;
        }
        
        // If this completes a trick (2 cards), determine winner and process
        if (trick.size() == 2) {
            processTrick();
        }
    }
    
    /**
     * Process the current trick to determine the winner and update the game state
     */
    private void processTrick() {
        if (trick.size() < 2) {
            return; // Can't determine winner with fewer than 2 cards
        }
        
        // Start with the first card as current winner
        Card winningCard = trick.get(0);
        Player winningPlayer = trickLeader;
        
        // Compare each subsequent card against the current winner
        for (int i = 1; i < trick.size(); i++) {
            Card nextCard = trick.get(i);
            Card result = compareCards(winningCard, nextCard);
            
            // If the result is the next card, update winning card and player
            if (result == nextCard) {
                winningCard = nextCard;
                winningPlayer = getOtherPlayer(trickLeader);
            }
        }
        
        // Winner takes all cards from this trick
        for (Card card : trick) {
            winningPlayer.addToSpoils(card);
        }
        
        // Set the winner as the next player to lead
        trickLeader = winningPlayer;
        
        // Clear trick data for next trick
        trick.clear();
        
        // If we've played all cards in hand, the round is over
        if (getPlayers().getFirst().getHand().isEmpty() && getPlayers().getLast().getHand().isEmpty()) {
            // Calculate scores for this round
            calculateScores();
            setGameStage(StageType.DEAL);
            round++;
        }
    }
    
    /**
     * Helper method to get the other player
     * 
     * @param currentPlayer The current player
     * @return The other player in the game
     */
    private Player getOtherPlayer(Player currentPlayer) {
        List<Player> players = getPlayers();
        return players.getFirst() == currentPlayer ? players.getLast() : players.getFirst();
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

        // Removes the Card from the Card Pile it was drawn from
        source.removeCard(cardToTake);
    }

    /**
     * Compares two cards and returns the winning card based on Whist rules:
     * - If second card is same suit as first, higher rank wins
     * - If second card is not same suit as first, first card wins unless second card is trump
     * - If both cards are trump, higher rank wins
     *
     * @param firstCard The first card played (led)
     * @param secondCard The second card played
     * @return The winning card
     */
    private Card compareCards(Card firstCard, Card secondCard) {
        // Get led suit (suit of first card)
        SuitType ledSuit = firstCard.getSuit();
        
        // Check if either card is trump
        boolean firstIsTrump = firstCard.getSuit() == trump;
        boolean secondIsTrump = secondCard.getSuit() == trump;
        
        // If both cards are trump, higher rank wins
        if (firstIsTrump && secondIsTrump) {
            return firstCard.getRank() > secondCard.getRank() ? firstCard : secondCard;
        }
        
        // If only second card is trump, second card wins
        if (!firstIsTrump && secondIsTrump) {
            return secondCard;
        }
        
        // If only first card is trump, first card wins
        if (firstIsTrump && !secondIsTrump) {
            return firstCard;
        }
        
        // If second card follows suit (same as led suit)
        if (secondCard.getSuit() == ledSuit) {
            // Higher rank wins
            return firstCard.getRank() > secondCard.getRank() ? firstCard : secondCard;
        }
        
        // If second card doesn't follow suit and isn't trump, first card wins
        return firstCard;
    }
    
    /**
     * Helper method to assign comparison values to suits
     * 
     * @param suit The suit to get a value for
     * @return The numerical value of the suit for comparison
     */
    private int getSuitValue(SuitType suit) {
        switch (suit) {
            case SPADES: return 4;
            case HEARTS: return 3;
            case DIAMONDS: return 2;
            case CLUBS: return 1;
            default: return 0;
        }
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
    
    /**
     * Calculate and update scores after a round has completed
     */
    public void calculateScores() {
        Player player1 = this.getPlayers().getFirst();
        Player player2 = this.getPlayers().getLast();
        
        // Calculate points based on tricks won
        int player1Tricks = countTricksWon(player1);
        int player2Tricks = countTricksWon(player2);
        
        // Calculate points (tricks - 6)
        int player1Points = player1Tricks - 6;
        int player2Points = player2Tricks - 6;
        
        // Add points to players
        player1.addPoints(player1Points);
        player2.addPoints(player2Points);
    }
    
    /**
     * Count the number of tricks won by a player
     * 
     * @param player The player to count tricks for
     * @return The number of tricks won
     */
    private int countTricksWon(Player player) {
        // Each trick has 2 cards
        return player.getSpoils().size() / 2;
    }
    
    /**
     * Clear all players' spoils to prepare for a new round
     */
    public void clearSpoils() {
        for (Player player : getPlayers()) {
            player.getSpoils().clear();
        }
    }
    
    /**
     * Checks if the game has ended (a player has reached 6 points)
     * 
     * @return true if game has ended, false otherwise
     */
    public boolean isGameOver() {
        Player player1 = this.getPlayers().getFirst();
        Player player2 = this.getPlayers().getLast();
        
        return player1.getScore() >= 6 || player2.getScore() >= 6;
    }
    
    /**
     * Prepare for a new round by resetting state
     */
    public void prepareNewRound() {
        // Increment round
        this.round++;
        
        // Reset stage
        this.stage = StageType.DEAL;
        
        // Reset trump
        this.trump = null;
        
        // Reset shuffle count
        this.shuffleCount = 0;
        
        // Clear trick
        this.trick.clear();
        
        // Reset deck, draw, discard
        this.deck = new CardPile();
        this.draw = new CardPile();
        this.discard = new CardPile();
        
        // Clear player hands and spoils
        Player player1 = this.getPlayers().getFirst();
        Player player2 = this.getPlayers().getLast();
        
        player1.setHand(new ArrayList<>());
        player2.setHand(new ArrayList<>());
    }
}
