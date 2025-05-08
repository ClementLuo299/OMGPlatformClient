package networking.records;

import gamelogic.GameType;
import networking.accounts.UserAccount;

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

    //Get game with the highest wins
    public GameType getHighestWins(){
        //
        return null;
    }

    //Get game that is the most played
    public GameType getMostPlayed(){
        //
        return null;
    }
}
