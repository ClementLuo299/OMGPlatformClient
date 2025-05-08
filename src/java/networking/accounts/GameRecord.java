package networking.accounts;

import gamelogic.GameType;

/**
 * A class representing a record of a single game, including details about the game type,
 * opponent, result, and score.
 */
public class GameRecord {

    private GameType game; // The name of the game
    private UserAccount player1;
    private UserAccount player2;
    private int winner; // 1 for player 1, 2 for player 2
    private int score; // The score achieved in the game

    /**
     * Constructs a GameRecord object with the specified details.
     *
     * @param game The name of the game.
     * @param score The score achieved in the game.
     */
    public GameRecord(GameType game, UserAccount p1, UserAccount p2, int winner, int score) {
        this.game = game;
        this.player1 = p1;
        this.player2 = p2;
        this.winner = winner;
        this.score = score;
    }



    /**
     * Gets the result of the game.
     *
     * @return The result of the game (e.g., "Win", "Lose", "Draw").
     */
    public int getWinner() {
        return winner;
    }

    /**
     * Gets the score achieved in the game.
     *
     * @return The score of the game.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the name of the game.
     *
     * @return The name of the game.
     */
    public GameType getGame() {
        return game;
    }

    public UserAccount getP1(){
        return player1;
    }

    public UserAccount getP2(){
        return player2;
    }

    /**
     * Returns a string representation of the game record, including the game, opponent, result, and score.
     *
     * @return A formatted string representation of the game record.
     */
    @Override
    public String toString() {
        return "Game = " + game + "\n" +
                "Player 1 = " + player1 + "\n" +
                "Player 2 = " + player2 + "\n" +
                "Result = Player " + winner + " wins!\n" +
                "Score = " + score;
    }
}