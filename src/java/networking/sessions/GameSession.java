package networking.sessions;

import gamelogic.Game;
import gamelogic.GameType;
import gamelogic.Player;
import networking.IO.GameIOHandler;
import networking.accounts.GameRecord;
import networking.accounts.UserAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * A gameplay session.
 *
 * @author Clement Luo
 */
public class GameSession {

    private Game game;
    private GameType gt;
    private boolean open; //If the game session is open to join
    private String dateTimeCreated;
    private ArrayList<UserAccount> accounts;

    public GameSession() {
        this.game = null;
        this.open = true;
        this.accounts = new ArrayList<>();
    }

    public GameType getGameType(){
        return gt;
    }

    public boolean playerInGame(UserAccount account){
        return accounts.contains(account);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setDateTimeCreated(String dateTimeCreated){
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getDateTimeCreated(){
        return dateTimeCreated;
    }

    public List<UserAccount> getAccounts(){
        return accounts;
    }

    public void addAccount(UserAccount account) {
        accounts.add(account);
    }

    public boolean getStatus(){
        return open;
    }

    public void closeSession(){
        open = false;
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
