package gamelogic.checkers;

import gamelogic.Game;
import gamelogic.GamePiece;
import gamelogic.GameType;
import gamelogic.Player;

import java.util.*;

public class CheckersGame extends Game {
    /**
     * Default constructor to set up the game by calling superclass to get all the methods and variables from it
     * @param players
     */
    public CheckersGame(List<Player> players) {
        super(GameType.CHECKERS, players, 25);
    }
}