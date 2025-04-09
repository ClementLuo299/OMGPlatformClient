package gamelogic.pieces;

import gamelogic.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


    // CONSTRUCTORS

    /**
     * Instantiates a Card Pile from a specified List of Cards
     *
     * @param cardList The given List of Cards to store in this Card Pile
     */
    public CardPile(List<Card> cardList) {
        this.cards = cardList;
    }

    /**
     * Instantiates a face-down Card Pile with a "New Deck Order" List of Cards
     */
    public CardPile() {
        // The temporary list of cards to add to this pile
        List<Card> generatedList = new ArrayList<>();
        // The numerical representation of what suit to apply to the card
        int suitCount = 1;

        // Loops to add new cards to the temporary list until 52 cards are reached
        while (suitCount < 5) {
            // Loops through each suit to add 13 cards to the deck
            for (int i = 1; i < 14; i++) {
                // Initiates the card to be added to the temporary list
                Card cardToAdd;

                // Checks what suit to apply to the card and applies it
                // Clubs and Hearts are in reverse rank order
                switch (suitCount) {
                    case 1:
                        cardToAdd = new Card(PieceType.CARD, false, SuitType.SPADES, i, true);
                        break;
                    case 2:
                        cardToAdd = new Card(PieceType.CARD, false, SuitType.DIAMONDS, i, true);
                        break;
                    case 3:
                        cardToAdd = new Card(PieceType.CARD, false, SuitType.CLUBS, Math.abs(i - 14), true);
                        break;
                    case 4:
                        cardToAdd = new Card(PieceType.CARD, false, SuitType.HEARTS, Math.abs(i - 14), true);
                        break;
                    default:
                        cardToAdd = null;
                        break;
                }

                // Adds the card to the temporary deck
                generatedList.add(cardToAdd);

            }
            // Increments the count to move onto the next suit
            suitCount++;
        }

        // Sets the pile's cards as the generated list
        this.cards = generatedList;
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

    /**
     * Gets the size of this Card Pile
     *
     * @return The integer Size of this Card Pile
     */
    public int getSize() {
        return cards.size();
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

    /**
     * Removes a Card from this Card Pile
     *
     * @param cardToRemove The given Card to remove from the Card Pile
     */
    public void removeCard(Card cardToRemove) {
        this.cards.remove(cardToRemove);
    }

    /**
     * Removes all cards from this Card Pile
     */
    public void clear() {
        this.cards.clear();
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
     * Overhead Shuffle Algorithm
     * Simulates an Overhead Shuffle on this Card Pile where the Pile is cut at a random point and then randomly recombined over several iterations
     */
    public void overheadShuffleOld() {
        // List which will store the Overhead Shuffle Result
        List<Card> shuffledCards = new ArrayList<>();
        // The number of times the deck will be cut (Between 11 and 21)
        int overheadRepetition = randomNum(10 + 1) + 11;

            // DEBUG: Shows the number of cuts selected and the original deck
            System.out.println("Times to Cut: " + overheadRepetition);
            System.out.println("Original Deck:\n" + this.toString());

        // Overheads the deck as many times as was randomly selected
        for (int i = 0; i < overheadRepetition; i++) {
            // Used to choose how many cards are cut from the top (Between 5 and 20)
            int randomCut = randomNum(15 + 1) + 5;

                // DEBUG: Shows the number of cards to cut
                System.out.println("Cards to Cut: " + randomCut);

            // Lists that store the top and bottom half of the deck
            List<Card> topCards = new ArrayList<>(cards.subList(0, randomCut));
            List<Card> bottomCards = new ArrayList<>(cards.subList(randomCut, cards.size()));

            // Loops until each half of the deck has been added to the shuffled deck
            while (!topCards.isEmpty() || !bottomCards.isEmpty()) {
                // Checks that there are still cards to pull from the bottom, switching to the other half after
                if (!bottomCards.isEmpty()) {
                    // Puts the bottom half on the top of the deck
                    shuffledCards.add(bottomCards.removeFirst());
                } else {
                    // Puts the top half on the bottom of the deck
                    shuffledCards.add(topCards.removeFirst());
                }
            }

            // Sets the main deck to the result of this iteration of overhand shuffling
            this.cards = shuffledCards;

                // DEBUG: Shows the result of the last cut
                System.out.println("Current Deck:\n" + this.toString());

            // Empties the temporary deck for reuse if there is another iteration
            if (i != overheadRepetition - 1) {
                shuffledCards = new ArrayList<>();
            }
        }
        // Sets the current Card Pile to the shuffled Card Pile
        this.cards = shuffledCards;
    }

    /**
     * Overhead Shuffle Algorithm
     * Simulates an Overhead Shuffle on this Card Pile where the top of the Pile is cut and then piled onto by subsequent cuts
     */
    public void overheadShuffle() {
        // List which will store the Overhead Shuffle Result
        List<Card> shuffledCards = new ArrayList<>();
        // The number of times the deck will be run through (Between 5 and 10)
        int overheadRepetition = randomNum(5 + 1) + 5;

            // DEBUG: Shows the number of overheads selected and the original deck
            System.out.println("Times to Overhead: " + overheadRepetition);
            System.out.println("Original Deck:\n" + this.toString());


        // Runs through as many overheads as were selected
        for (int i = 0; i < overheadRepetition; i++) {
            // The body of cards that is cut
            List<Card> bodyCards = cards;
            // The cards cut from the body
            List<Card> topCards;


            // Cuts the top from the deck repeatedly until it is depleted, putting the cuts into a reordered pile
            while (!bodyCards.isEmpty()) {
                // Used to choose how many cards are cut from the top (Between 5 and 15)
                int randomCut = randomNum(10 + 1) + 5;

                    // DEBUG: Shows the number of cuts selected
                    System.out.println("Cards to Cut: " + randomCut);

                // Checks that there are enough cards to make a cut, moving the rest of the bottom into the pile if not
                if (bodyCards.size() >= randomCut) {
                    // Stores the cut into the top half and the rest into the bottom half
                    topCards = new ArrayList<>(bodyCards.subList(0, randomCut));
                    bodyCards = new ArrayList<>(bodyCards.subList(randomCut, bodyCards.size()));

                        // DEBUG: Shows cards in topCards
                        System.out.println("Bottom big enough to cut");
                        System.out.println("Current Cut:\n" + topCards.toString());

                } else {
                    // Stores the remaining cards in the top half
                    topCards = bodyCards;

                        // DEBUG: Shows cards in topCards
                        System.out.println("Bottom too small to cut");
                        System.out.println("Current Cut:\n" + topCards.toString());
                }

                // Places the cut cards onto the shuffled card pile
                topCards.addAll(shuffledCards);

                    // DEBUG: Shows cards in shuffledCards
                    System.out.println("Current Stack Pile:\n" + topCards.toString());

                // Updates the shuffled card pile
                shuffledCards = topCards;

                    // DEBUG: Shows cards in shuffledCards
                    System.out.println("Current Shuffle Pile 1:\n" + shuffledCards.toString());

                // Clears cut cards for next overhead
                topCards.clear();

                    // DEBUG: Shows cards in shuffledCards
                    System.out.println("Current Shuffle Pile 2:\n" + shuffledCards.toString());
            }

            // Sets the main deck to the result of this iteration of overhand shuffling
            this.cards = shuffledCards;

                // DEBUG: Shows the result of the last cut
                System.out.println("Current Deck:\n" + this.toString());

            // Empties the temporary deck for reuse if there is another iteration
            if (i != overheadRepetition - 1) {
                shuffledCards = new ArrayList<>();
            }
        }
        // Sets the current Card Pile to the shuffled Card Pile
        this.cards = shuffledCards;
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

