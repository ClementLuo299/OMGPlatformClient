package java.gamelogic;

public interface IGame {

    public Player getWinner();

    public Player getLoser();

    //public Player getTurn();

    public String getBoard();

    public boolean drew();

    public boolean playerQuit();

    public boolean gameIsOver();


}
