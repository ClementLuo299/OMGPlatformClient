package gameLogicTests;
import java.gamelogic.PieceType;
import java.gamelogic.Pieces.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;


public class ShufflingTests {
    private CardPile cardPile;
    private ArrayList<Card> cards;
    @Before
    public void setUp() {
        cards = new ArrayList<>();
        for (int i = 1; i <=13; i++) {
            cards.add(new Card(PieceType.CARD, false, SuitType.CLUBS, i, true));
        }
        for (int i = 1; i <=13; i++) {
            cards.add(new Card(PieceType.CARD, false, SuitType.DIAMONDS, i, true));
        }
        for (int i = 1; i <=13; i++) {
            cards.add(new Card(PieceType.CARD, false, SuitType.SPADES, i, true));
        }
        for (int i = 1; i <=13; i++) {
            cards.add(new Card(PieceType.CARD, false, SuitType.HEARTS, i, true));
        }
        cardPile = new CardPile(cards);
    }

    @Test
    public void testRiffleShuffle() {
        System.out.println("Original deck: " + cardPile.toString());
        int i = 0;
        while (i < 10) {
            cardPile.riffleShuffle();
            System.out.println("Current deck: " + cardPile.toString());
            i++;
        }

        System.out.println("Riffle shuffle deck: " + cardPile.toString() + "\n");
        assertEquals(52, cardPile.getSize());
    }

    @Test
    public void testScrambleShuffle() {
        System.out.println("Original deck: " + cardPile.toString());
        cardPile.scrambleShuffle();
        System.out.println("Scramble shuffle deck: " + cardPile.toString() + "\n");

        assertEquals(52, cardPile.getSize());
    }

    @Test
    public void testCut() {
        System.out.println("Original deck: " + cardPile.toString());
        cardPile.cut();
        System.out.println("Cut deck: " + cardPile.toString() + "\n");

        assertEquals(52, cardPile.getSize());
    }

    @Test
    public void testOverheadShuffle() {
        System.out.println("Original deck: " + cardPile.toString());
        cardPile.overheadShuffle();
        System.out.println("Overhead Shuffle deck: " + cardPile.toString() + "\n");
        assertEquals(52, cardPile.getSize());
    }

}
