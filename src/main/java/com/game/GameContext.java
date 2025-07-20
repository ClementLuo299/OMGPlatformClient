package com.game;

/**
 * Context class for passing game information between screens.
 * Used to transfer game data from the game library to the game lobby.
 *
 * @author Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class GameContext {
    
    private static GameContext instance;
    private GameModule currentGame;
    
    private GameContext() {
        // Private constructor for singleton
    }
    
    /**
     * Get the singleton instance of GameContext.
     */
    public static GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }
    
    /**
     * Set the current game for the context.
     * 
     * @param game The game module to set as current
     */
    public void setCurrentGame(GameModule game) {
        this.currentGame = game;
    }
    
    /**
     * Get the current game from the context.
     * 
     * @return The current game module, or null if not set
     */
    public GameModule getCurrentGame() {
        return currentGame;
    }
    
    /**
     * Clear the current game context.
     */
    public void clearCurrentGame() {
        this.currentGame = null;
    }
    
    /**
     * Check if there is a current game set.
     * 
     * @return true if a game is set, false otherwise
     */
    public boolean hasCurrentGame() {
        return currentGame != null;
    }
} 