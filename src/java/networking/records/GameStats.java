package networking.records;

import gamelogic.GameType;
import networking.accounts.UserAccount;

/**
 * Stores user data for a game.
 *
 * @authors Clement Luo,
 * @date March 22, 2025
 */
public class GameStats {
    GameType game;
    UserAccount account;
    int wins;
    int losses;
    double totalTime;

    public GameStats(){
        //
    }
}
