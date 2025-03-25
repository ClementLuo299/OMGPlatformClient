package gameLogicTests;
import core.gamelogic.Pieces.*;
import org.junit.*;
import java.io.*;
import java.util.*;


public class ShufflingTests {
    private List<Card> cards;
    @Before
    public void setUp() {
        cards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(SuitType.CLUBS, i, true));
        }
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(SuitType.DIAMONDS, i, true));
        }
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(SuitType.SPADES, i, true));
        }
        for (int i = 1; i <= 13; i++) {
            cards.add(new Card(SuitType.HEARTS, i, true));
        }
    }

    @Test
    public void testRiffleShuffle() {
        System.out.println("Original deck: " + CardPile.toString(cards));
        int i = 0;
        while (i < 10) {
            cards = CardPile.riffleShuffle(cards);
            System.out.println("Current deck: " + CardPile.toString(cards));
            i++;
        }

        System.out.println("Riffle shuffle deck: " + CardPile.toString(cards));
    }
}
