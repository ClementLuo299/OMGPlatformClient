package gamelogic;


import gamelogic.pieces.*;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.util.*;


/**
 * Tests the functionality of Cards
 *
 * @authors Dylan Shiels, Sameer Askar
 * @date March 14, 2025
 */
public class CardTests {

    /**
     * Tests if Cards effectively return Strings
     */
    @Test
    public void testCreation() {
        // Instantiates the Cards to test
        Card testAce = new Card(PieceType.CARD, true, SuitType.DIAMONDS, 1, false);
        Card testKing = new Card(PieceType.CARD, true, SuitType.SPADES, 13, false);
        Card testQueen = new Card(PieceType.CARD, true, SuitType.HEARTS, 12, false);
        Card testJack = new Card(PieceType.CARD, true, SuitType.CLUBS, 11, false);
        Card testTen = new Card(PieceType.CARD, true, SuitType.DIAMONDS, 10, false);
        Card testEight = new Card(PieceType.CARD, true, SuitType.SPADES, 8, false);
        Card testFive = new Card(PieceType.CARD, true, SuitType.HEARTS, 5, false);
        Card testTwo = new Card(PieceType.CARD, true, SuitType.CLUBS, 2, false);

        // Prints the Cards
        System.out.println(testAce.toString());
        System.out.println(testKing.toString());
        System.out.println(testQueen.toString());
        System.out.println(testJack.toString());
        System.out.println(testTen.toString());
        System.out.println(testEight.toString());
        System.out.println(testFive.toString());
        System.out.println(testTwo.toString());
    }
}