package gamelogic.whist;

/**
 * Contains all the possible stages of Whist gameplay
 *
 * @authors Dylan Shiels, Sameer Askar
 * @date April 6, 2025
 */
public enum StageType {
    DEAL("Dealing"),
    DRAFT("Revealing Trump"),
    DUEL("Dueling");

    private final String displayName;

    StageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
