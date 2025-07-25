package com.services;

import com.game.GameModule;
import com.game.enums.GameDifficulty;
import com.game.enums.GameMode;
import com.game.sourcing.GameSource;
import com.game.sourcing.LocalGameSource;
import com.game.sourcing.RemoteGameSource;
import com.utils.error_handling.Logging;
import com.utils.error_handling.SafeExecute;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for dynamically discovering and loading game modules.
 * Supports both local game discovery and remote game fetching from servers.
 * 
 * @authors Clement Luo
 * @date July 18, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public class GameSearchService {
    
    private static GameSearchService instance;
    private final Map<String, GameModule> discoveredGames;
    private final List<GameSource> gameSources;
    
    private GameSearchService() {
        this.discoveredGames = new ConcurrentHashMap<>();
        this.gameSources = new ArrayList<>();
    }
    
    /**
     * Gets the singleton instance of GameDiscoveryService.
     * @return The GameDiscoveryService instance
     */
    public static synchronized GameSearchService getInstance() {
        if (instance == null) {
            instance = new GameSearchService();
        }
        return instance;
    }
    
    /**
     * Initializes the discovery service with default game sources.
     */
    public void initialize() {
        Logging.info("🔍 Initializing Game Discovery Service...");
        

        
        // Add local game source (for both compiled and uncompiled modules)
        addGameSource(new LocalGameSource());
        
        // Add remote game source (if configured)
        addGameSource(new RemoteGameSource());
        
        Logging.info("✅ Game Discovery Service initialized with " + gameSources.size() + " sources");
    }
    
    /**
     * Adds a game source for discovery.
     * @param source The game source to add
     */
    public void addGameSource(GameSource source) {
        if (source != null) {
            gameSources.add(source);
            Logging.info("➕ Added game source: " + source.getName());
        }
    }
    
    /**
     * Discovers all available games from all sources.
     * @return CompletableFuture that completes when all games are discovered
     */
    public CompletableFuture<List<GameModule>> discoverAllGames() {
        Logging.info("🔍 Starting game discovery from " + gameSources.size() + " sources");
        
        List<CompletableFuture<List<GameModule>>> futures = new ArrayList<>();
        
        // Discover games from each source
        for (GameSource source : gameSources) {
            CompletableFuture<List<GameModule>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Logging.info("🔍 Discovering games from: " + source.getName());
                    List<GameModule> games = source.discoverGames();
                    Logging.info("✅ Discovered " + games.size() + " games from " + source.getName());
                    return games;
                } catch (Exception e) {
                    Logging.error("❌ Failed to discover games from " + source.getName() + ": " + e.getMessage(), e);
                    return new ArrayList<GameModule>();
                }
            });
            futures.add(future);
        }
        
        // Combine all results
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<GameModule> allGames = new ArrayList<>();
                for (CompletableFuture<List<GameModule>> future : futures) {
                    try {
                        allGames.addAll(future.get());
                    } catch (Exception e) {
                        Logging.error("❌ Error getting games from source: " + e.getMessage(), e);
                    }
                }
                
                // Register discovered games
                for (GameModule game : allGames) {
                    discoveredGames.put(game.getGameId(), game);
                }
                
                Logging.info("🎮 Total games discovered: " + allGames.size());
                return allGames;
            });
    }
    
    /**
     * Gets all discovered games.
     * @return List of all discovered game modules
     */
    public List<GameModule> getAllDiscoveredGames() {
        return new ArrayList<>(discoveredGames.values());
    }
    
    /**
     * Gets a specific discovered game by ID.
     * @param gameId The game ID
     * @return The game module, or null if not found
     */
    public GameModule getDiscoveredGame(String gameId) {
        return discoveredGames.get(gameId);
    }
    
    /**
     * Refreshes the game discovery (useful for checking for new games).
     * @return CompletableFuture that completes when refresh is done
     */
    public CompletableFuture<List<GameModule>> refreshGames() {
        Logging.info("🔄 Refreshing game discovery...");
        discoveredGames.clear();
        return discoverAllGames();
    }
    
    /**
     * Gets games by category.
     * @param category The game category
     * @return List of games in the specified category
     */
    public List<GameModule> getGamesByCategory(String category) {
        return discoveredGames.values().stream()
            .filter(game -> category.equalsIgnoreCase(game.getGameCategory()))
            .toList();
    }
    
    /**
     * Gets games by difficulty.
     * @param difficulty The game difficulty
     * @return List of games with the specified difficulty
     */
    public List<GameModule> getGamesByDifficulty(GameDifficulty difficulty) {
        return discoveredGames.values().stream()
            .filter(game -> difficulty == game.getDifficulty())
            .toList();
    }
    
    /**
     * Gets games that support a specific game mode.
     * @param gameMode The game mode
     * @return List of games that support the specified mode
     */
    public List<GameModule> getGamesByMode(GameMode gameMode) {
        return discoveredGames.values().stream()
            .filter(game -> {
                switch (gameMode) {
                    case SINGLE_PLAYER: return game.supportsSinglePlayer();
                    case LOCAL_MULTIPLAYER: return game.supportsLocalMultiplayer();
                    case ONLINE_MULTIPLAYER: return game.supportsOnlineMultiplayer();
                    default: return false;
                }
            })
            .toList();
    }
} 