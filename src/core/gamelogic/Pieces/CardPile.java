package core.gamelogic.Pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;

/**
 * Handles the creation and handling of Piles of Cards for Playing Card Games.
 *
 * @authors Sameer Askar, Dylan Shiels
 * @date March 13, 2025
 */
public class CardPile {
    // ATTRIBUTES

    // The List of Cards held in this Card Pile
    private List<Card> cards;


    // CONSTRUCTOR

    /**
     * Instantiates a Card Pile with a List of Cards
     *
     * @param cardList The given List of Cards to store in this Card Pile
     */
    public CardPile(List<Card> cardList) {
        this.cards = cardList;
    }


    // GETTERS

    /**
     * Gets the list of Cards contained in this Card Pile
     *
     * @return The List of Cards within this Card Pile
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Gets the Card on the top of this Card Pile
     *
     * @return The topmost Card of this Card Pile
     */
    public Card getTopCard() {
        return cards.getFirst();
    }

    /**
     * Gets the Card on the bottom of this Card Pile
     *
     * @return The bottommost Card of this Card Pile
     */
    public Card getBottomCard() {
        return cards.getLast();
    }

    /**
     * Gets the Card at a given index of this Card Pile
     *
     * @param index The given index to
     * @return The Card at the given index of this Card Pile
     */
    public Card getSpecificCard(int index) {
        return cards.get(index);
    }


    // SETTERS

    /**
     * Adds a new Card to the top of the Card Pile
     *
     * @param newCard The given Card to add to the Card Pile
     */
    public void addCards(Card newCard) {
        // List to store the new Card Pile based on the size required
        List<Card> newCards = new ArrayList<>();

        // Adds the given Card to the top of the new Card Pile
        newCards.add(newCard);
        // Adds the Cards in the current Card Pile under the given Card
        newCards.addAll(cards);

        // Sets the current Card Pile to the new Card Pile
        this.cards = newCards;
    }

    /**
     * Adds a multiple new Cards to the top of the Card Pile
     *
     * @param newCardList The given Cards to add to the Card Pile
     */
    public void addCards(List<Card> newCardList) {
        // List to store the new Card Pile based on the size required
        List<Card> newCards = new ArrayList<>();

        // Adds the given Card List to the top of the new Card Pile
        newCards.addAll(newCardList);
        // Adds the Cards in the current Card Pile under the given Cards
        newCards.addAll(cards);

        // Sets the current Card Pile to the new Card Pile
        this.cards = newCards;
    }


    // METHODS

    /**
     * Generates a random number to use for selecting a random Card given a number of Cards
     *
     * @param cardAmount The given integer number of Cards to randomly choose from
     * @return The randomly selected integer number for the Card to be selected
     */
    private static int randomNum(int cardAmount) {
        // Random number generator to use for selection
        Random random = new Random();

        // Randomly generates a number reflecting the given amount of Cards
        int randomNumber = random.nextInt(cardAmount);

        // Returns the randomly generated number
        return randomNumber;
    }

    /**
     * Riffle Shuffle Algorithm
     * Simulates a Riffle Shuffle on this Card Pile where the Card Pile is cut in half and each half is riffled together
     */
    public void riffleShuffle() {
        // The List which will store the shuffle result
        List<Card> shuffledCards = new ArrayList<>();
        // The size of half the Card Pile for cutting before riffling
        int deckHalf = (int) (cards.size() / 2);
        // The size of the Card Pile for halting the loop
        int deckSize = cards.size();

        // The Lists for temporarily storing each half of the Card Pile for splitting
        List<Card> topHalf = new ArrayList<>();
        List<Card> bottomHalf = new ArrayList<>();

        // Loops through the Card List, first from the start to the midpoint, then from the midpoint to the end
        // Each half is placed into separate Lists
        for (int i = 0; i < deckHalf; i++) {
            topHalf.add(cards.get(i));
        }
        for (int i = deckHalf; i < deckSize; i++) {
            bottomHalf.add(cards.get(i));
        }

        // Performs the Riffle Shuffle on the Card Pile until all the Cards are in the Shuffled Cards List
        while (shuffledCards.size() < deckSize) {
            // Randomly generates a number to determine which half of the Cards goes first
            int randomNum = randomNum(2);

            // Checks the random number to perform a single Riffle on each half
            if (randomNum == 1) {
                // Adds the bottom card of the top half first.
                shuffledCards.add(topHalf.getLast());
                shuffledCards.add(bottomHalf.getLast());

                // Removes those Cards from each half.
                topHalf.removeLast();
                bottomHalf.removeLast();

            } else if (randomNum == 0) {
                // Adds the bottom card of the bottom half first.
                shuffledCards.add(bottomHalf.getLast());
                shuffledCards.add(topHalf.getLast());

                // Removes those Cards from each half.
                bottomHalf.removeLast();
                topHalf.removeLast();
            }
        }

        // Sets the current Card Pile to the shuffled Card Pile
        this.cards = shuffledCards;
    }

    /**
     * Scramble Shuffle Algorithm
     * Simulates a Scramble Shuffle on this Card Pile where all the Cards are dispersed at random on the table and brought back together
     */
    public void scrambleShuffle() {
        // The List which will store the shuffle result
        List<Card> shuffledCards = new ArrayList<>();
        // The size of the deck for halting the loop
        int deckSize = cards.size();

        // The list for temporarily storing the Card Pile for Shuffling
        List<Card> unshuffledCards = cards;

        // Performs the Scramble Shuffle on the Card Pile until all the Cards are in the Shuffled Cards List
        while (shuffledCards.size() < deckSize) {
            // Randomly chooses a number to determine which Card is shuffled
            int randomNum = randomNum(unshuffledCards.size());
            // Adds the randomly selected Card
            shuffledCards.add(unshuffledCards.get(randomNum));
            // Removes that Card from the
            unshuffledCards.remove(randomNum);
        }

        // Sets the current Card Pile to the shuffled Card Pile
        this.cards = shuffledCards;
    }


    /**
     * Cut Algorithm
     * Simulates a Cut on this Card Pile at a random point and flips the top half to the bottom and bottom to top
     */
    public void cut() {
        // The List which will store the Cut result
        List<Card> cutDeck = new ArrayList<>();
        // The size of the deck for halting loops and getting random values
        int deckSize = cards.size();

        // Randomly chooses where the Card Pile will be cut
        int randomNum = randomNum(deckSize);

        // Loops through the Card List, first from the cut-point to the end, then from the start to the cut-point
        // The bottom cut is put over the top cut
        for (int i = randomNum; i < deckSize; i++) {
            cutDeck.add(cards.get(i));
        }
        for (int i = 0; i < randomNum; i++) {
            cutDeck.add(cards.get(i));
        }

        // Sets the current Card Pile to the cut Card Pile
        this.cards = cutDeck;
    }

    /**
     * Overhead shuffle algorithm
     * Simulates when the deck is cut and cards are dispersed randomly throughout the deck
     *
     * @Param card list
     *
     * @Return shuffled card list
     */
    public void overheadShuffle(List<Card> cards) {
        List<Card> shuffledCards = new ArrayList<>(); //list for shuffled deck
        int overheadRepetition = randomNum(10+1); //will allow for up to 10 repetitions of the overhead shuffle
        for (int i = 0; i < overheadRepetition; i++) {
            int randomCut = randomNum(cards.size()); //cuts the deck at a random point
            List<Card> topCards = cards.subList(0, randomCut);
            List<Card> bottomCards = cards.subList(randomCut, cards.size());

        }
    }


    /**
     * Creates a String which represents this Card Pile
     *
     * @return The String of the ordered Card List
     */
    @Override
    public String toString() {
        // Initialized String to return
        StringBuilder cardList = new StringBuilder("[");

        // Variables the track when last element comes
        int index = 0;
        int size = cards.size();

        // Loops through the Card Pile to append the name of each Card within
        for (Card card : cards) {
            cardList.append(card.toString());

            // Adds a comma between entries only if it is not the final element
            if (index < size - 1) {
                cardList.append(", ");
            }

            // Increments the index
            index++;
        }

        // Finalizes the String and returns it
        cardList.append("]");
        return cardList.toString();
    }
}

