package gamelogic;

import gamelogic.tictactoe.TicTacToeGame;
import networking.accounts.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTests {

    private UserAccount user1;
    private UserAccount user2;
    private Player player1;
    private Player player2;
    private List<Player> players;
    private TicTacToeGame game;

    @BeforeEach
    void setUp() {
        this.user1 = new UserAccount("Name1", "Pass");
        this.user2 = new UserAccount("Name2", "Pass");
        this.player1 = new Player(user1);
        this.player2 = new Player(user2);
        this.players.add(player1);
        this.players.add(player2);
        this.game = new TicTacToeGame(players);
    }

    @Test
    void testInitialBoardHasNoWinner() {
        assertNull(game.getWinner());
    }

    @Test
    void testPlaceMovePlayer1() {
        game.place(player1, 0);
        assertEquals('X', game.getBoard().charAt(0));
    }

    @Test
    void testPlaceMovePlayer2() {
        game.place(player2, 1);
        assertEquals('O', game.getBoard().charAt(1));
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
        assertTrue(game.isDrawn());
        assertNull(game.getWinner());
    }

    @Test
    void testPlayer1WinsIfPlayer2Offline() {
        player2.getAccount().logOff();
        assertEquals(player1, game.getWinner());
    }

    @Test
    void testPlayer2WinsIfPlayer1Offline() {
        player1.getAccount().logOff();
        assertEquals(player2, game.getWinner());
    }

    @Test
    void testIllegalMoveIgnored() {
        game.place(player1, 0);
        char before = game.getBoard().charAt(0);
        game.place(player2, 0); // Should be ignored since it's already taken
        assertEquals(before, game.getBoard().charAt(0));
    }
}
