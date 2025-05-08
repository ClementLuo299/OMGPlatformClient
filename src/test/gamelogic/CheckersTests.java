package gamelogic;

import gamelogic.checkers.CheckersGame;
import networking.accounts.UserAccount;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;



public class CheckersTests {
    CheckersGame game;
    List<Player> players;

    @Before
    public void setUp() {
        players = new ArrayList<>();
        players.add(new Player(new UserAccount("guest1", "pass")));
        players.add(new Player(new UserAccount("guest2", "pass")));
        game = new CheckersGame(players);
    }

    @Test
    public void testStartingBoard() { //tests the toString method and ensures the board is set up properly
        game.getBoard().toString();
    }

    @Test
    public void testGetChecker() { //checks that checkers can be found at their correct coordinates
        assertEquals(1, game.getBoard().getChecker(1, 1).getXPosition());
        assertEquals(1, game.getBoard().getChecker(1, 1).getYPosition());
        assertEquals(2, game.getBoard().getChecker(2, 8).getXPosition());
        assertEquals(8, game.getBoard().getChecker(2, 8).getYPosition());
    }

    @Test
    public void testValidMoves() {
        for (int[] arr : game.getValidMoves(1, 3)) {
            System.out.print(Arrays.toString(arr) + " ");
        }
        System.out.println();
        for (int[] arr : game.getValidMoves(6, 6)) {
            System.out.print(Arrays.toString(arr) + " ");
        }
        System.out.println();
        for (int[] arr : game.getValidMoves(5, 3)) {
            System.out.print(Arrays.toString(arr) + " ");
        }
        System.out.println();
    }

    @Test
    public void testMovePiece() { //tests the moving of a piece
        System.out.println();
        game.makeMove(players.get(0), game.getBoard().getChecker(5, 3), 6, 4);
        game.makeMove(players.get(1), game.getBoard().getChecker(4, 6), 5, 5);
        game.makeMove(players.get(0), game.getBoard().getChecker(6, 4), 4, 6);
        game.makeMove(players.get(1), game.getBoard().getChecker(6, 6), 5, 5);
        game.makeMove(players.get(1), game.getBoard().getChecker(7, 7), 6, 6);
        game.makeMove(players.get(1), game.getBoard().getChecker(6, 8), 7, 7);
        game.makeMove(players.get(0), game.getBoard().getChecker(4, 6), 6, 8);
        game.getBoard().toString();
        System.out.println();
        System.out.println(game.getBoard().getChecker(6, 8).isStacked());
        for (int[] arr : game.getValidMoves(6, 8)) {
            System.out.print(Arrays.toString(arr) + " ");
        }

        System.out.println();
    }


}
