package main.java.gamelogic;

public abstract class AbstractGame implements IGame {

    // all game boards will be stored as a String
    protected String board;

    // player 1 will always go first
    protected Player player1;
    protected Player player2;

    public String getBoard() {
        return board;
    }

    public boolean playerQuit(){
        return !player1.isOnline() || !player2.isOnline();
    }

    public boolean gameIsOver() {
        return drew() || getWinner() != null || playerQuit();
    }

    public Player getLoser(){
        Player winner = getWinner();
        if(player1==winner){
            return player2;
        } else if(player2==winner){
            return player1;
        }
        return null;
    }

}
