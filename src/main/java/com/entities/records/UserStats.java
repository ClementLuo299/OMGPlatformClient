package com.entities.records;

import com.entities.UserAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores user data.
 *
 * @authors Clement Luo,
 * @date March 22, 2025
 */
public class UserStats {
    private UserAccount account;
    private List<GameStats> gameStatsList;

    public UserStats(UserAccount account){
        this.account = account;
        gameStatsList = new ArrayList<>();
    }
}
