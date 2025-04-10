package statistics;
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


    public int awardPoint() {
        // Checks if the Player is at a levelling boundary



        return 1;
    }

    public void awardParticipation(List<Player> players) {
        // Goes through the Player List to award them one point
    }


}