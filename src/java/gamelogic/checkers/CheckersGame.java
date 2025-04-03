package gamelogic.checkers;

import gamelogic.AbstractGame;
import gamelogic.GamePiece;
import gamelogic.Player;

import java.util.*;

public class CheckersGame extends AbstractGame {
    private HashMap<Coordinate, GamePiece> boardMap;
    /**
     * Default constructor to set up the game
     * @param player1
     * @param player2
     * will set up a board with the pieces on the starting squares
     * a setup method will put board into a hashmap to make the board more easily modified
     */
    public CheckersGame(Player player1, Player player2) {
        this.board = "wowowowoowowowowwowowowoooooooooooooooooobobobobboboboboobobobob"; //initializes the starting checkers board with the pieces on the starting square
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Method to setup board as HashMap to track pieces moving throughout the game
     * @return HashMap with pieces imported and coordinates
     */
    public static HashMap<Coordinate, GamePiece> setupBoard() {

    }
}