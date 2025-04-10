package networking.IO;

import gamelogic.AbstractGame;
import gamelogic.GameType;
import gamelogic.Player;
import networking.accounts.GameRecord;
import networking.accounts.UserAccount;
import networking.sessions.GameSession;

/**
 * Handles communication between the backend and gameplay.
 *
 * @authors Clement Luo,
 * @date March 4, 2025
 */
public class GameIOHandler {

    private GameSession session;

    /**
     * basic constructor
     */
    public GameIOHandler() {
        this.session = new GameSession();
    }

    public void setupGame(AbstractGame game, UserAccount player1, UserAccount player2) {
        session.setGame(game);
        session.addAccount(player1);
        session.addAccount(player2);
    }

    public void endGame() {
        GameRecord record = session.generateGameRecord();
    }

}
