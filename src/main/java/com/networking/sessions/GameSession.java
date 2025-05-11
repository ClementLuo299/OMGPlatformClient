package com.networking.sessions;

//import gamelogic.Game;
//import gamelogic.GameType;
//import gamelogic.Player;
import com.networking.accounts.GameRecord;
import com.networking.accounts.UserAccount;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A gameplay session.
 *
 * @author Clement Luo
 */
public class GameSession {

    //private Game game;
    //private GameType gt;
    private boolean open; //If the game session is open to join
    private String dateTimeCreated;
    private ArrayList<UserAccount> accounts;

    public GameSession() {
        //this.game = null;
        this.open = true;
        this.accounts = new ArrayList<>();
    }

    public GameSession(String game, UserAccount player1, UserAccount player2) {
        //this.game = game;
        //this.gt = game.getGameType();
        this.open = true;
        this.dateTimeCreated = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.accounts = new ArrayList<>();
        accounts.add(player1);
        accounts.add(player2);
    }

    //GETTERS

    //public GameType getGameType(){
    //    return gt;
    //}

    public String getDateTimeCreated(){
        return dateTimeCreated;
    }

    public List<UserAccount> getAccounts(){
        return accounts;
    }

    public boolean getStatus(){
        return open;
    }

    //SETTERS

    //public void setGame(Game game) {
        //this.game = game;
        //this.gt = game.getGameType();
    //}

    public void setDateTimeCreated(String dateTimeCreated){
        this.dateTimeCreated = dateTimeCreated;
    }

    public void addAccount(UserAccount account) {
        accounts.add(account);
    }

    public void closeSession(){
        open = false;
    }

    //METHODS

    public boolean playerInGame(UserAccount account){
        return accounts.contains(account);
    }

    public GameRecord generateGameRecord() {
        //Player winnerP = game.getWinner();
        //*************************************************
        // connect Player to Account, pass it into GameRecord here
        int winner = 1;
        int score = 0;
        GameRecord record = new GameRecord("sample", accounts.get(0), accounts.get(1), winner, score);
        return record;
    }

}
