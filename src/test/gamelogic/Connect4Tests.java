package gamelogic;
import gamelogic.connectfour.ConnectFour;
import networking.accounts.UserAccount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

public class Connect4Tests {

    private ConnectFour game;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player1 = new Player(new UserAccount("Player 1", "pass1"));
        player2 = new Player(new UserAccount("Player 2", "pass2"));
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        game = new ConnectFour(playerList);
    }

    @Test
    public void testDropPiece() {
        game.drop(player1, 3);
        assertEquals('1', game.getBoard().charAt(38)); // Bottom-most row
    }

/*    @Test
    public void testColumnFull() {
        for (int i = 0; i < 6; i++) {
            game.drop(i % 2 == 0 ? player1 : player2, 2);
        }
        try {
            game.drop(player1, 2);
            fail("Expected an IllegalMoveException for a full column.");
        } catch (Exception e) {

        }
    }*/

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
        game.drop(player2, 0);
        game.drop(player2, 0);
        game.drop(player2, 0);
        game.drop(player1, 0);

        game.drop(player2, 1);
        game.drop(player2, 1);
        game.drop(player1, 1);

        game.drop(player2, 2);
        game.drop(player1, 2);

        game.drop(player1, 3);
        printBoard();
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testDiagonalLeftWin() {
        game.drop(player2, 3);
        game.drop(player2, 3);
        game.drop(player2, 3);
        game.drop(player1, 3);
        game.drop(player2, 2);
        game.drop(player2, 2);
        game.drop(player1, 2);
        game.drop(player2, 1);
        game.drop(player1, 1);
        game.drop(player1, 0);
        printBoard();
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testDrawGame() {
        for(int i=0; i<7; i++){
            if(i%2==0){
                game.drop(player1, i);
                game.drop(player1, i);
                game.drop(player1, i);
                game.drop(player2, i);
                game.drop(player1, i);
                game.drop(player1, i);
            } else {
                game.drop(player2, i);
                game.drop(player2, i);
                game.drop(player2, i);
                game.drop(player1, i);
                game.drop(player2, i);
                game.drop(player2, i);
            }
        }

        assertEquals(true, game.isDrawn());
    }

    private void printBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                System.out.print(game.getBoard().charAt(row * 7 + col) + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }
}
