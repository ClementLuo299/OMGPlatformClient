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

    /**
     * Compares this player with another object for equality.
     * Two players are considered equal if their player IDs are the same.
     *
     * @param obj The object to compare with.
     * @return true if the object is a Player and has the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Same object
        if (!(obj instanceof Player)) return false; // Different type
        Player other = (Player) obj;
        return this.playerId == other.playerId; // Compare by ID
    }
    /**
     * Generates a hash code for the player object.
     * Important for using Player as a key in data structures like HashMaps.
     *
     * @return A hash code based on the player's ID.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(playerId);
    }

    /**
     * Returns a user-friendly string representation of the player.
     *
     * @return A string like "Alice (Player 1)" or "Player 2 (Player 2)"
     */
    @Override
    public String toString() {
        return name + " (Player " + playerId + ")";
    }
}