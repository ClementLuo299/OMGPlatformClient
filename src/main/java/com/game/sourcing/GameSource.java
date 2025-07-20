package com.game.sourcing;

import java.util.List;

import com.game.GameModule;

/**
 * Interface for game sources that can discover games.
 * Implementations of this interface provide different ways to discover
 * and load game modules (e.g., from local files, remote servers, source code).
 *
 * @authors Clement Luo
 * @date July 19, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public interface GameSource {
    
    /**
     * Gets the name of this game source.
     * @return The source name
     */
    String getName();
    
    /**
     * Discovers games from this source.
     * @return List of discovered game modules
     */
    List<GameModule> discoverGames();
    
    /**
     * Checks if this source is available.
     * @return true if the source is available
     */
    boolean isAvailable();
} 