package com.models.records;

import com.models.accounts.UserAccount;

/**
 * Stores user data for a game.
 *
 * @authors Clement Luo,
 * @date March 22, 2025
 */
public class GameStats {
    UserAccount account;
    int wins;
    int losses;
    double totalTime;

    public GameStats(){
        //
    }
}

