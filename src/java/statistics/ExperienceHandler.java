package statistics;

import gamelogic.Player;
import networking.accounts.UserAccount;

import java.util.List;

/**
 * Handles the awarding of Points to Players at the conclusion of a Game
 *
 * @authors Iman Hamzat, Dylan Shiels
 * @date March 18, 2025
 */
public class ExperienceHandler {
    // ATTRIBUTES

    // The list of players being given experience
    List<Player> players;
    // The UserAccount of a Game's Winner
    UserAccount winnerAccount;
    // The Player instance of a Game's Winner
    Player winnerPlayer;
    // The average plays for this Game
    int avgPlays;

    int totalPlays = 0;

    // CONSTRUCTOR

    /**
     * Instantiates an Experience Handler to award Players for their Gameplay
     *
     * @param players The given List of Players who participated
     * @param winner The given Winning Player of the Game
     * @param avgPlays The integer Average Plays required to win the Game
     */
    public ExperienceHandler(List<Player> players, Player winner, int avgPlays) {
        this.players = players;
        this.winnerAccount = winner.getAccount();
        this.winnerPlayer = winner;
        this.avgPlays = avgPlays;
        for (Player p: players)  {
            totalPlays += p.getPlays();
        }
    }


    // METHODS

    /**
     * Modifies the Intensity Level of the User
     */
    public void modifyIntensityLevel() {
        // Gets the experience and intensity level of the winning player
        int winnerLevel = winnerAccount.getLevel();
        float winnerIntensity = winnerAccount.getSessionIntensityLevel();

        // Goes through the list of all players to modify their intensity level
        for (Player currentPlayer : players) {
            if (currentPlayer != winnerPlayer) {
                // Gets the account of the current Player
                UserAccount currentUser = currentPlayer.getAccount();

                // Gets the levels of the loser
                int loserLevel = currentUser.getLevel();
                float loserIntensity = currentUser.getSessionIntensityLevel();

                // Checks the difference in level to appropriately adjust intensity for both Players
                // Winner is a higher level than their opponent
                if (winnerLevel > loserLevel) {
                    winnerAccount.setSessionIntensityLevel(winnerIntensity);
                    currentUser.setSessionIntensityLevel((float) (loserIntensity - 0.1));

                // Winner is a lower level than their opponent
                } else if (winnerLevel < loserLevel) {
                    winnerAccount.setSessionIntensityLevel((float) (winnerIntensity + 0.2));
                    currentUser.setSessionIntensityLevel((float) (loserIntensity - 0.3));
                }
            }
        }
    }

    /**
     * Awards the Winning Player for their victory
     */
    public void awardWin() {
        // The number of plays this Player made

        // Checks that the Game was of normal length
        if (!(winnerPlayer.getPlays() < Math.floor(0.4 * avgPlays))) {
            // Adds exp to this User
            this.winnerAccount.setExpInLevel(this.winnerAccount.getExpInLevel() + 1);

            // Checks if this user is at a threshold
            if (this.winnerAccount.getExpInLevel() >= this.winnerAccount.getNextLevelThreshold()) {
                increaseLevel(this.winnerAccount);
            }
        // Awards the winner for a quick win if the game was short
        } else {
            awardQuickWin();
        }
    }

    /**
     * Awards the Winning Player for playing really well
     */
    private void awardQuickWin()  {
        // The number of plays this Player made

            // Adds exp to this User
            this.winnerAccount.setExpInLevel(this.winnerAccount.getExpInLevel() + 2);
            // Checks if this user is at a threshold
            if (this.winnerAccount.getExpInLevel() >= this.winnerAccount.getNextLevelThreshold()) {
                increaseLevel(this.winnerAccount);
            }

    }

    /**
     * Awards all Players for playing competitively
     */
    public void awardLongGame ()  {

        // Checks that the game was considered long (*2 because avgPlays is based on one Player)
        if ((avgPlays * 2) < Math.ceil(1.25 * totalPlays/2)) {
            // Goes through the Player List to award them one point
            for (Player player : players) {
                // Adds exp to this User
                player.getAccount().setExpInLevel(player.getAccount().getExpInLevel() + 1);

                // Checks if this user is at a threshold
                if (player.getAccount().getExpInLevel() >= player.getAccount().getNextLevelThreshold()) {
                    increaseLevel(player.getAccount());
                }
            }
        }
    }

    /**
     * Awards all Players for participating in a full Game
     */
    public void awardParticipation() {
        // Goes through the Player List to award them one point
        for (Player player : players) {
            // Adds exp to this User
            player.getAccount().setExpInLevel(player.getAccount().getExpInLevel() + 1);

            // Checks if this user is at a threshold
            if (player.getAccount().getExpInLevel() >= player.getAccount().getNextLevelThreshold()) {
                increaseLevel(player.getAccount());
            }
        }
    }

    /**
     * Increases the Level of the user if they pass the Points Threshold of their next Level
     *
     * @param user The given User Account to level up
     */
    public void increaseLevel(UserAccount user)  {
        // increase player level
        int expNewLevel = user.getNextLevelThreshold() - user.getExpInLevel();
        double d = 1.2 * user.getNextLevelThreshold();
        int nextLevelThreshold = (int)Math.round(d);
        user.setLevel(user.getLevel() + 1);
        user.setNextLevelThreshold(nextLevelThreshold);
        user.setExpInLevel(expNewLevel);

        // TODO: they can probably increase more than 1 level at once so add it
    }
}