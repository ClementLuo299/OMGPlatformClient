package com.models.accounts;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a Connect Four player profile. This class extends AbstractGameProfile
 * and provides functionality specific to managing game records, player status, and rankings
 * in Connect Four.
 *
 * @authors Clement Luo
 * @date April 10, 2025
 */
public class ConnectFourProfile extends AbstractGameProfile {

    /**
     * A list to store all game records for the player.
     */
    private List<GameRecord> gameRecords = new ArrayList<>();

    /**
     * Constructs a new ConnectFourProfile with default values.
     */
    public ConnectFourProfile() {
        super();
    }

    /**
     * Updates the game history with details about the last game, including the opponent, result, and score.
     *
     * @param opponent The name of the opponent.
     * @param result The result of the game (e.g., win, lose, draw).
     * @param score The score achieved in the game.
     */
    @Override
    public void updateGameHistory(String opponent, String result, int score) {
        setLastOpponent(opponent);
        setLastGameResult(result);
        setLastGameScore(score);

        // Update total games and scores
        setTotalGamesPlayed(getTotalGamesPlayed() + 1);
        setTotalScore(getTotalScore() + score);
    }

    /**
     * Updates the game history, including the opponent, result, and cumulative score.
     * This method includes an additional calculation for score accumulation.
     *
     * @param opponent The name of the opponent.
     * @param result The result of the game (e.g., win, lose, draw).
     * @param score The score achieved in the game.
     */
    public void updateGameHistoryReal(String opponent, String result, int score) {
        setLastOpponent(opponent);
        setLastGameResult(result);
        setLastGameScore(getTotalScore() + score);

        // Update total games and scores
        setTotalGamesPlayed(getTotalGamesPlayed() + 1);
        setTotalScore(getTotalScore() + score);
    }

    /**
     * Updates the player's status, including the total number of games played, total score, and win rate.
     *
     * @param totalGames The total number of games played.
     * @param score The total score accumulated by the player.
     * @param winRate The player's win rate as a percentage.
     */
    @Override
    public void updatePlayerStatus(int totalGames, int score, double winRate) {
        setTotalGamesPlayed(totalGames);
        setTotalScore(score);
        setWinRate(winRate);
    }

    /**
     * Updates the player's win/loss record and recalculates the win rate.
     *
     * @param wins The total number of wins.
     * @param losses The total number of losses.
     */
    @Override
    public void updateWinLoseRecord(int wins, int losses) {
        setTotalWins(wins);
        setTotalLosses(losses);

        // Update win rate calculation
        if (wins + losses > 0) {
            setWinRate((double) wins / (wins + losses) * 100);
        } else {
            setWinRate(0.0);
        }
    }

    /**
     * Updates the player's rankings based on their score and win rate.
     *
     * @param scoreRank The rank based on score.
     * @param winRateRank The rank based on win rate.
     */
    @Override
    public void updateRanking(int scoreRank, int winRateRank) {
        setScoreRank(scoreRank);
        setWinRateRank(winRateRank);
    }

    /**
     * Retrieves all game records stored for the player.
     *
     * @return A list of all game records.
     */
    public List<GameRecord> getAllGameRecords() {
        return gameRecords;
    }

    /**
     * Adds a new game record manually to the player's game history.
     *
     * @param record The GameRecord object representing the game's details.
     */
    public void addGameRecord(GameRecord record) {
        gameRecords.add(record);

        setLastGameScore(record.getScore());
    }

    /**
     * Prints all game records to the console. This method is primarily for debugging purposes.
     */
    public void printGameRecords() {
        for (GameRecord record : gameRecords) {
            System.out.println(record);
        }
    }
}
