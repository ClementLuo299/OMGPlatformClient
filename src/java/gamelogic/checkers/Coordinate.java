package gamelogic.checkers;

public class Coordinate {
    /**
     * Class to hold coordinates for hashmap of checkers that is used for tracking the board and where pieces are
     * Author: Sameer Askar
     */
    private int x;
    private int y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

}
