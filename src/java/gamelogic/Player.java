package gamelogic;

import networking.accounts.UserAccount;

/**
 * Stores all relevant data of a Player within a Game
 *
 * @authors Scott Brown, Dylan Shiels
 * @date April 1, 2025
 */
public class Player {
    // ATTRIBUTES

    // The account UUID of the player
    private final UserAccount account;
    // The username of the player
    private final String username;
    // The number of plays this player has made
    private int plays;
    // The number of points this player has earned within a game
    private int score;

    // CONSTRUCTOR

    /**
     * Instantiate a Player for a Game through their account
     *
     * @param account The given Account to create a Player from
     */
    public Player(UserAccount account) {
        this.account = account;
        this.username = this.account.getUsername();
        this.plays = 0;
        this.score = 0;

    }


    // GETTERS

    /**
     * Gets the Account attached to this Player
     *
     * @return The User Account of this Player
     */
    public UserAccount getAccount() {
        return this.account;
    }

    /**
     * Gets the Username of this Player
     *
     * @return The String Username of this Player
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Gets the Plays of this Player
     *
     * @return The integer Plays for this Player
     */
    public int getPlays() {
        return plays;
    }

    /**
     * Gets the Points of this Player
     *
     * @return The integer Points for this Player
     */
    public int getScore() {
        return score;
    }


    // SETTERS

    /**
     * Adds a Play to this Player
     */
    public void addPlay() {
        this.plays = plays++;
    }

    /**
     * Adds a specified number of points to this Player
     *
     * @param pointsToAdd The given integer Points to award this Player
     */
    public void addPoints(int pointsToAdd) {
        this.score = score + pointsToAdd;
    }
}
