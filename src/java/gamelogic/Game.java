package gamelogic;

import java.util.List;

/**
 * Handles the Creation, Initialization, and Ending of Games
 *
 * @authors Scott Brown, Dylan Shiels
 * @date April 1, 2025
 */
public class Game {
    // ATTRIBUTES

    // The type of Game this Game is
    private final GameType gameType;
    // The list of players for this game
    private final List<Player> players;
    // The average number of turns/rounds this game takes to complete
    private final int avgPlays;
    // The player whose turn it is
    private Player turnHolder;
    // The player who won the game
    private Player winner;


    // CONSTRUCTOR

    /**
     * Instantiates a Game with a specified Game Type, List of participating Players, and the average Plays required to win
     *
     * @param gameType The given Game Type for this Game
     * @param players The given List of Players for this Game
     * @param avgPlays The given integer Average Plays for this Game
     */
    public Game(GameType gameType, List<Player> players, int avgPlays) {
        this.gameType = gameType;
        this.players = players;
        this.avgPlays = avgPlays;
    }


    // GETTERS

    /**
     * Gets the Player whose turn it is
     *
     * @return The turn holding Player
     */
    public Player getTurnHolder() {
        return turnHolder;
    }


    // SETTERS

    /**
     * Gives the turn to a specified Player
     *
     * @param nextPlayer The given Player to give the next Turn to
     */
    public void setTurnHolder(Player nextPlayer) {
        this.turnHolder = nextPlayer;
    }

    /**
     * Sets the Winning Player of this Game
     *
     * @param winningPlayer The given Winning Player for this Game
     */
    public void setWinner(Player winningPlayer) {
        this.winner = winningPlayer;
    }


    // METHODS

    /**
     * Starts the Game, setting up the Players for play
     */
    public void startGame() {
        // TODO: Add startup logic for games
    }

    /**
     * Removes a Player from the Players List
     *
     * @param quittingPlayer The given Player to remove from the Game
     */
    public void quitGame(Player quittingPlayer) {
        // Checks through the list to find the player to remove
        for (int i = 0; i < players.size(); i++) {
            // The current player being checked
            Player checkedPlayer = players.get(i);

            // Removes the checked player if they match the player to remove
            if (checkedPlayer == quittingPlayer) {
                players.remove(i);
                return;
            }
        }
    }

    /**
     * Ends the game, awarding Players points and placing them back into the Matchmaking menu
     */
    public void endGame() {
        // Awards players with experience points
        this.awardPoints();

        // TODO: Add logic to announce ending state of the game (Win, Loss, Draw)

        // TODO: Add logic to send both players back to the matchmaking menu
    }

    /**
     * Awards Experience Points to Players based on their Performance
     */
    public void awardPoints() {
        // TODO: Add logic to award points to players using avgPlays variable
    }
}
