package gamelogic;
public class Player {

    // The name of the player (e.g., "Alice" or "Player 1")
    private final String name;

    // A unique numeric identifier for the player (e.g., 1 or 2)
    private final int playerId;

    /**
     * Constructs a new Player object with the given name and ID.
     *
     * @param name     The display name of the player.
     * @param playerId A unique ID for the player (1 for Player 1, 2 for Player 2).
     */
    public Player(String name, int playerId) {
        this.name = name;
        this.playerId = playerId;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name assigned to the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique ID of the player.
     *
     * @return The numeric ID of the player (used for board logic).
     */
    public int getPlayerId() {
        return playerId;
    }

