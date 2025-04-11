package gamelogic.tictactoe;

import gamelogic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {

    private PlayerMock player1;
    private PlayerMock player2;
    private TicTacToe game;

    @BeforeEach
    void setUp() {
        player1 = new PlayerMock(true);
        player2 = new PlayerMock(true);
        game = new TicTacToe(player1, player2);
    }

    @Test
    void testInitialBoardHasNoWinner() {
        assertNull(game.getWinner());
    }

    @Test
    void testPlaceMovePlayer1() {
        game.place(player1, 0);
        assertEquals('X', game.board.charAt(0));
    }

    @Test
    void testPlaceMovePlayer2() {
        game.place(player2, 1);
        assertEquals('O', game.board.charAt(1));
    }

    @Test
    void testPlayer1WinsRow() {
        game.place(player1, 0);
        game.place(player1, 1);
        game.place(player1, 2);
        assertEquals(player1, game.getWinner());
    }

    @Test
    void testPlayer2WinsColumn() {
        game.place(player2, 0);
        game.place(player2, 3);
        game.place(player2, 6);
        assertEquals(player2, game.getWinner());
    }

    @Test
    void testPlayer1WinsDiagonal() {
        game.place(player1, 0);
        game.place(player1, 4);
        game.place(player1, 8);
        assertEquals(player1, game.getWinner());
    }

    @Test
    void testDrawCondition() {
        // X O X
        // X X O
        // O X O
        game.place(player1, 0);
        game.place(player2, 1);
        game.place(player1, 2);
        game.place(player1, 3);
        game.place(player1, 4);
        game.place(player2, 5);
        game.place(player2, 6);
        game.place(player1, 7);
        game.place(player2, 8);
        assertTrue(game.drew());
        assertNull(game.getWinner());
    }

    @Test
    void testPlayer1WinsIfPlayer2Offline() {
        player2.setOnline(false);
        assertEquals(player1, game.getWinner());
    }

    @Test
    void testPlayer2WinsIfPlayer1Offline() {
        player1.setOnline(false);
        assertEquals(player2, game.getWinner());
    }

    @Test
    void testIllegalMoveIgnored() {
        game.place(player1, 0);
        char before = game.board.charAt(0);
        game.place(player2, 0); // Should be ignored since it's already taken
        assertEquals(before, game.board.charAt(0));
    }
}
