package gameLogic;

import gamelogic.connectfour.ConnectFour;
import gamelogic.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectFourTests {
    private ConnectFour game;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        game = new ConnectFour(player1, player2);
    }

    @Test
    public void testDropPiece() {
        game.drop(player1, 3);
        assertEquals('1', game.getBoard().charAt(35)); // Bottom-most row
    }

    @Test
    public void testColumnFull() {
        for (int i = 0; i < 6; i++) {
            game.drop(i % 2 == 0 ? player1 : player2, 2);
        }
        try {
            game.drop(player1, 2);
            fail("Expected an IllegalMoveException for a full column.");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalMoveException);
        }
    }

    @Test
    public void testHorizontalWin() {
        game.drop(player1, 0);
        game.drop(player1, 1);
        game.drop(player1, 2);
        game.drop(player1, 3);
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testVerticalWin() {
        game.drop(player2, 4);
        game.drop(player2, 4);
        game.drop(player2, 4);
        game.drop(player2, 4);
        assertEquals(player2, game.getWinner());
    }

    @Test
    public void testDiagonalRightWin() {
        game.drop(player1, 0);
        game.drop(player2, 1);
        game.drop(player1, 1);
        game.drop(player2, 2);
        game.drop(player2, 2);
        game.drop(player1, 2);
        game.drop(player2, 3);
        game.drop(player2, 3);
        game.drop(player2, 3);
        game.drop(player1, 3);
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testDiagonalLeftWin() {
        game.drop(player1, 3);
        game.drop(player2, 2);
        game.drop(player1, 2);
        game.drop(player2, 1);
        game.drop(player2, 1);
        game.drop(player1, 1);
        game.drop(player2, 0);
        game.drop(player2, 0);
        game.drop(player2, 0);
        game.drop(player1, 0);
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testDrawGame() {
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                game.drop((row + col) % 2 == 0 ? player1 : player2, col);
            }
        }
        assertTrue(game.drew());
    }
}
