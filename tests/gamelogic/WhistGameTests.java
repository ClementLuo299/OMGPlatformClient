package gamelogic.whist;

import java.io.*;
import java.util.*;

import gamelogic.GamePiece;
import gamelogic.GameType;
import gamelogic.PieceType;
import gamelogic.Player;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.SuitType;
import gamelogic.pieces.Card;

import networking.accounts.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of Whist Games
 *
 * @authors Dylan Shiels, Scott Brown
 * @date April 6, 2025
 */


class WhistGameTests {

    private Player player1;
    private Player player2;
    private WhistGame game;

    @BeforeEach
    void setUp() {
        player1 = new Player(new UserAccount("TestUser1", "password1"));
        player2 = new Player(new UserAccount("testUser2", "password2"));
        List<Player> playerList = new ArrayList<Player>();
        playerList.add(player1);
        playerList.add(player2);

        game = new WhistGame(GameType.WHIST, playerList, 8);
    }

    @Test
    void testTheSetUp() {
        assertEquals(game.getRound(), 0);
        assertEquals(game.getGameStage(), StageType.DEAL);
        assertEquals(game.getTrick(), new ArrayList<>());
        assertEquals(game.getTrickWinner(), null);
        assertEquals(game.getGameStage().getDisplayName(), "Dealing");
    }

    @Test
    public void testSettersAndGetters() {
        game.setRound(5);
        assertEquals(game.getRound(), 5);

        game.setGameStage(StageType.DRAFT);
        assertEquals(game.getGameStage(), StageType.DRAFT);

        game.setTrump(SuitType.HEARTS);
        assertEquals(game.getTrump(), SuitType.HEARTS);

        CardPile myCardPile = new CardPile();
        game.setDraw(myCardPile);
        assertEquals(game.getDraw(), myCardPile);

        game.setDiscard(myCardPile);
        assertEquals(game.getDiscard(), myCardPile);

        game.setDealer(player1);
        assertEquals(game.getDealer(), player1);

    }


    @Test
    void dealCardTest() {
        CardPile deck = new CardPile();
        Card eightOfSpades = new Card(8, SuitType.SPADES);
        deck.addCard(eightOfSpades);

        game.dealCard(deck, eightOfSpades, player1);
        assertNotEquals(deck.getTopCard(), eightOfSpades);
        assertEquals(player1.checkHand(eightOfSpades), true);
    }



    @Test
    void playCardAndGetTrick() {
        Card eightOfSpades = new Card(8, SuitType.SPADES);
        List<Card> trickDeck = new ArrayList<>();
        trickDeck.add(eightOfSpades);

        player1.addToHand(eightOfSpades);
        game.playCard(eightOfSpades);

        assertEquals(game.getTrick(), trickDeck);
    }

    @Test
    void takeCard() {
        CardPile deck = new CardPile();
        Card eightOfSpades = new Card(8, SuitType.SPADES);
        deck.addCard(eightOfSpades);

        game.takeCard(deck, eightOfSpades, player1);
        assertNotEquals(deck.getTopCard(), eightOfSpades);
        assertEquals(player1.checkHand(eightOfSpades), true);

    }

    @Test
    void compareCards() {
        Card eightOfSpades = new Card(8, SuitType.SPADES);
        Card eightOfHearts = new Card(8, SuitType.HEARTS);
        Card nineOfSpades = new Card(9, SuitType.SPADES);

        assertEquals(game.compareCards(eightOfHearts, eightOfSpades), null);
        assertEquals(game.compareCards(nineOfSpades, eightOfSpades), nineOfSpades);

        game.setTrump(SuitType.HEARTS);

        assertEquals(game.compareCards(eightOfHearts, eightOfSpades), eightOfHearts);
        assertEquals(game.compareCards(nineOfSpades, eightOfHearts), eightOfHearts);

    }


    @Test
    void completeTrick() {
        Card eightOfSpades = new Card(PieceType.CARD, true, SuitType.SPADES, 8, true);
        List<Card> oneCard = new ArrayList<>();
        oneCard.add(eightOfSpades);

        List<Card> NoCards = new ArrayList<>();
        CardPile discardPile = new CardPile(NoCards);
        game.setDiscard(discardPile);

        player1.addToHand(eightOfSpades);
        game.playCard(eightOfSpades);

        assertEquals(game.getTrick(), oneCard);

        game.completeTrick();

        assertEquals(game.getTrick(), NoCards);
        assertEquals(game.getDiscard(), discardPile);
        assertEquals(game.getDiscard().getSize(), 1);

    }

    @Test
    void showHand(){
        Card eightOfSpadesHand = new Card(PieceType.CARD, false, SuitType.SPADES, 8, true);
        Card eightOfSpadesOut = new Card(PieceType.CARD, false, SuitType.SPADES, 8, true);
        player1.addToHand(eightOfSpadesHand);
        game.showHand(player1);

        eightOfSpadesOut.flip();

        assertEquals(player1.getHand().get(0).isFaceDown, eightOfSpades.isFaceDown);
    }



    @Test
    void PopulateDeckTakeCardsAndGetDeck() {
        Card eightOfSpades = new Card(8, SuitType.SPADES);
        List<Card> deck = new ArrayList<>();
        game.resetDeck();

        deck.add(eightOfSpades);

    }


    @Test
    void nextStage() {
        game.setGameStage(StageType.DEAL);
        assertEquals(game.getGameStage(), StageType.DEAL);
        game.nextStage();
        assertEquals(game.getGameStage(), StageType.DRAFT);
        game.nextStage();
        assertEquals(game.getGameStage(), StageType.DUEL);
        game.nextStage();
        assertEquals(game.getGameStage(), null);
    }

    @Test
    void getPlayableCardsTest() {
        CardPile allCards = new CardPile();
        List<Card> spades = new ArrayList<>();
        spades.add(new Card(1, SuitType.SPADES));
        spades.add(new Card(2, SuitType.SPADES));
        spades.add(new Card(3, SuitType.SPADES));
        spades.add(new Card(4, SuitType.SPADES));
        spades.add(new Card(5, SuitType.SPADES));
        spades.add(new Card(6, SuitType.SPADES));
        spades.add(new Card(7, SuitType.SPADES));
        spades.add(new Card(8, SuitType.SPADES));
        spades.add(new Card(9, SuitType.SPADES));
        spades.add(new Card(10, SuitType.SPADES));
        spades.add(new Card(11, SuitType.SPADES));
        spades.add(new Card(12, SuitType.SPADES));
        spades.add(new Card(13, SuitType.SPADES));

        for(Card current : allCards.getCards()){
            player1.addToHand(current);
        }

        List<Card> NoCards = new ArrayList<>();


        assertEquals(game.getPlayableCards(player1), allCards.getCards());

        Card eightOfSpades = new Card(8, SuitType.SPADES);
        List<Card> playable = game.getPlayableCards(player1, eightOfSpades);

        assertEquals(Objects.equals(playable.toString(), spades.toString()), true);

    }


    @Test
    void resetDeckTest() {
        CardPile deck = game.getDeck();
        assertEquals(game.getDeck(), deck);

        game.resetDeck();

        assertEquals(Objects.equals(game.getDeck().toString(), new CardPile().toString()), true);
    }

    @Test
    void TrickWinnerTest (){
        Card eightOfSpades = new Card(PieceType.CARD, false, SuitType.SPADES, 8, true);
        Card nineOfSpades = new Card(PieceType.CARD, false, SuitType.SPADES, 9, true);
        player1.addToHand(eightOfSpades);
        game.playCard(eightOfSpades);
        player2.addToHand(nineOfSpades);
        game.playCard(nineOfSpades);

        assertEquals(game.getTrickWinner(), player2);

    }
    @Test
    void currentTurnTest(){
        Card nineOfSpades = new Card(PieceType.CARD, false, SuitType.SPADES, 9, true);
        player1.addToHand(nineOfSpades);
        Card eightOfSpades = new Card(PieceType.CARD, false, SuitType.SPADES, 8, true);
        player2.addToHand(eightOfSpades);

        game.playCard(nineOfSpades);

        assertEquals(game.getCurrentTurn(), player2);

        game.playCard(eightOfSpades);

        assertEquals(game.getCurrentTurn(), player1);

    }

}