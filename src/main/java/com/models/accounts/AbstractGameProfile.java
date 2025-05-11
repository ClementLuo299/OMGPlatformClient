package com.models.accounts;

//import gamelogic.GameType;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a game profile, storing player information such as game history,
 * player status, win/loss records, and rankings. This class includes abstract methods for
 * subclasses to implement game-specific updates.
 */
public abstract class AbstractGameProfile {

    // Various private variables being used by the functions
    // Game history fields
    private String lastOpponent;
    private String lastGameResult;
    private int lastGameScore;
    // Player status fields
    private int totalGamesPlayed;
    private int totalScore;
    private double winRate;

    // Win/Lose record fields
    private int totalWins;
    private int totalLosses;

    // Ranking fields
    private int scoreRank;
    private int winRateRank;

    // List to store all game records
    private List<GameRecord> gameRecords = new ArrayList<>();

    /**
     * Adds a new game record to the player's history and updates the last game details.
     *
     * @param gameType The type of game
     * @param player1 The first player account
     * @param player2 The second player account
     * @param winner The winner (1 for player1, 2 for player2)
     * @param score The score of the game
     */
    public void addGameRecord(String gameType, UserAccount player1, UserAccount player2, int winner, int score) {
        GameRecord newRecord = new GameRecord("type", player1, player2, winner, score);
        this.gameRecords.add(newRecord);

        // Update the last game details
        setLastOpponent(winner == 1 ? player2.getUsername() : player1.getUsername());
        setLastGameResult(winner == 1 ? "Win" : "Loss");
        setLastGameScore(score);
    }

    /**
     * Fetches a copy of all game records stored in the profile.
     *
     * @return A list containing all game records.
     */
    public List<GameRecord> getAllGameRecords() {
        return new ArrayList<>(this.gameRecords); // Return a copy of the list to preserve encapsulation
    }

    /**
     * Constructs an empty game profile with default values.
     */
    public AbstractGameProfile() {
        // Initialize with default values if needed
        this.gameRecords = new ArrayList<>();
    }

    // Abstract methods to be implemented by subclasses (specific to each game)
    /**
     * Updates the game history with a new game.
     *
     * @param opponent The opponent's name.
     * @param result The result of the game (win, loss, draw, etc).
     * @param score The score achieved in the game.
     */
    public abstract void updateGameHistory(String opponent, String result, int score);

    /**
     * Updates the player's status.
     *
     * @param totalGames The total number of games played.
     * @param score The total score.
     * @param winRate The win rate of the player.
     */
    public abstract void updatePlayerStatus(int totalGames, int score, double winRate);

    /**
     * Updates the win/loss record of the player.
     *
     * @param wins The total number of wins.
     * @param losses The total number of losses.
     */
    public abstract void updateWinLoseRecord(int wins, int losses);

    /**
     * Updates the player's ranking based on score and win rate.
     *
     * @param scoreRank The rank based on score.
     * @param winRateRank The rank based on win rate.
     */
    public abstract void updateRanking(int scoreRank, int winRateRank);

    // Getters and setters for each field

    // Game history getters/setters
    public String getLastOpponent() {
        return lastOpponent;
    }

    public void setLastOpponent(String lastOpponent) {
        this.lastOpponent = lastOpponent;
    }

    public String getLastGameResult() {
        return lastGameResult;
    }

    public void setLastGameResult(String lastGameResult) {
        this.lastGameResult = lastGameResult;
    }

    public int getLastGameScore() {
        return lastGameScore;
    }

    public void setLastGameScore(int lastGameScore) {
        this.lastGameScore = lastGameScore;
    }

    // Player status getters/setters
    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    // Win/Lose record getters/setters
    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    // Ranking getters/setters
    public int getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    public int getWinRateRank() {
        return winRateRank;
    }

    public void setWinRateRank(int winRateRank) {
        this.winRateRank = winRateRank;
    }


}
