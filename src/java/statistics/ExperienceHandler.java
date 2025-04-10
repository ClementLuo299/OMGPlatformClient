package statistics;

import gamelogic.Player;

import java.util.List;

/**
 * Handles the awarding of Points to Players at the conclusion of a Game
 *
 * @authors Dylan Shiels
 * @date March 18, 2025
 */
public class ExperienceHandler {
    // ATTRIBUTES

    // The list of players being given experience
    List<Player> players;
    // The Winner of the game
    Player winner;
    // The average plays for this Game
    int avgPlays;
    // The total number of moves, from both players, in each game
    int totalPlays;


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
        this.winner = winner;
        this.avgPlays = avgPlays;
    }


    // GETTERS



    // SETTERS



    // METHODS


    public void awardWin() {
        if (!(avgPlays < Math.floor(0.4 * totalPlays/2))) {
            this.winner.setExpInLevel(this.winner.getExpInLevel() + 1);
            if (this.winner.getExpInLevel() >= this.winner.getNextLevelThreshold()) {
                increaseLevel(this.winner);
            }
        }  else  {
            awardQuickWin();
        }
    }

    private void awardQuickWin()  {
        if (avgPlays < Math.floor(0.4 * totalPlays/2)) {
            this.winner.setExpInLevel(this.winner.getExpInLevel() + 2);
            if (this.winner.getExpInLevel() >= this.winner.getNextLevelThreshold()) {
                increaseLevel(this.winner);
            }
        }
    }

    public void awardLongGame ()  {
        if (avgPlays < Math.ceil(1.25 * totalPlays/2)) {
            this.winner.setExpInLevel(this.winner.getExpInLevel() + 2);
            if (this.winner.getExpInLevel() >= this.winner.getNextLevelThreshold()) {
                increaseLevel(this.winner);
            }
        }
    }

    public void awardParticipation() {
        // Goes through the Player List to award them one point
        for (int i = 0; i < players.size(); i++)  {
            players.get(i).setExpInLevel(players.get(i).getExpInLevel() + 1);
            if (players.get(i).getExpInLevel() >= players.get(i).getNextLevelThreshold())  {
                increaseLevel(players.get(i));
            }
        }

    }

    public void increaseLevel  (Player p)  {
        // increase player level
        int expNewLevel = p.getNextLevelThreshold() - p.getExpInLevel();
        double d = 1.2 * p.getNextLevelThreshold();
        int nextLevelThreshold = (int)Math.round(d);
        p.setLevel(p.getLevel() + 1);
        p.setNextLevelThreshold(nextLevelThreshold);
        p.setExpInLevel(expNewLevel);

        // TODO: they can probably increase more than 1 level at once so add it
    }




}