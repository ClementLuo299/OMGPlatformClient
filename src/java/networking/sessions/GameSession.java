package networking.sessions;

import gamelogic.AbstractGame;
import gamelogic.GameType;
import gamelogic.Player;
import networking.IO.GameIOHandler;
import networking.accounts.GameRecord;
import networking.accounts.UserAccount;

import java.util.ArrayList;

/**
 * A gameplay session.
 *
 * @author Clement Luo
 */
public class GameSession {

    private AbstractGame game;
    private GameType gt;
    private GameIOHandler handler;
    private ArrayList<UserAccount> accounts;

    public GameSession() {
        this.game = null;
        this.accounts = new ArrayList<>();
    }

    public void setGame(AbstractGame game) {
        this.game = game;
    }

    public void addAccount(UserAccount account) {
        accounts.add(account);
    }

    public GameRecord generateGameRecord() {
        Player winnerP = game.getWinner();
        //*************************************************
        // connect Player to Account, pass it into GameRecord here
        int winner = 1;
        int score = 0;
        GameRecord record = new GameRecord(gt, accounts.get(0), accounts.get(1), winner, score);
        return record;
    }

}
