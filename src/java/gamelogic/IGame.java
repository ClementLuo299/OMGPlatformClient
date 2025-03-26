<<<<<<<< HEAD:src/java/gamelogic/IGame.java
<<<<<<<< HEAD:src/old/gamelogic/IGame.java
========
package java.gamelogic;

>>>>>>>> gamelogic:src/java/gamelogic/IGame.java
========
>>>>>>>> matchmaking:src/old/gamelogic/IGame.java
public interface IGame {

    public Player getWinner();

    public Player getLoser();

    //public Player getTurn();

    public String getBoard();

    public boolean drew();

    public boolean playerQuit();

    public boolean gameIsOver();


}
