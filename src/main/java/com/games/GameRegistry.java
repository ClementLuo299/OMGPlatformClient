package com.games;

import com.games.enums.GameDifficulty;
import com.games.enums.GameMode;
import com.services.GameLauncherService;
import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Registry for automatically discovering and registering game modules.
 * Now uses GameDiscoveryService for dynamic game discovery.
 *
 * @authors Clement Luo
 * @date July 18, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class GameRegistry {
    
    private static GameRegistry instance;
    private final List<GameModule> availableGames;
    private final GameLauncherService gameLauncher;
    private final GameDiscoveryService discoveryService;
    
    private GameRegistry() {
        this.availableGames = new ArrayList<>();
        this.gameLauncher = GameLauncherService.getInstance();
        this.discoveryService = GameDiscoveryService.getInstance();
    }
    
    /**
     * Gets the singleton instance of GameRegistry.
     * @return The GameRegistry instance
     */
    public static synchronized GameRegistry getInstance() {
        if (instance == null) {
            instance = new GameRegistry();
        }
        return instance;
    }
    
    /**
     * Initializes the game registry and discovers all available games.
     */
    public void initialize() {
        Logging.info("🎮 Initializing Game Registry...");
        
        // Initialize discovery service
        discoveryService.initialize();
        
        // Discover games asynchronously
        CompletableFuture<List<GameModule>> discoveryFuture = discoveryService.discoverAllGames();
        
        discoveryFuture.thenAccept(games -> {
            availableGames.clear();
            availableGames.addAll(games);
            Logging.info("✅ Game Registry initialized with " + availableGames.size() + " games");
        }).exceptionally(throwable -> {
            Logging.error("❌ Failed to initialize Game Registry: " + throwable.getMessage(), throwable);
            return null;
        });
    }
    
    /**
     * Registers a game module (now handled by discovery service).
     * @param gameModule The game module to register
     */
    public void registerGame(GameModule gameModule) {
        if (gameModule == null) {
            Logging.warning("⚠️ Attempted to register null game module");
            return;
        }
        
        String gameId = gameModule.getGameId();
        if (gameId == null || gameId.trim().isEmpty()) {
            Logging.warning("⚠️ Game module has null or empty game ID");
            return;
        }
        
        // Check if game is already registered
        if (gameLauncher.isGameRegistered(gameId)) {
            Logging.warning("⚠️ Game already registered: " + gameId);
            return;
        }
        
        // Register with the game launcher
        gameLauncher.registerGame(gameModule);
        
        // Add to available games list
        availableGames.add(gameModule);
        
        Logging.info("🎮 Registered game: " + gameModule.getGameName() + " (ID: " + gameId + ")");
    }
    
    /**
     * Unregisters a game module.
     * @param gameId The game ID to unregister
     */
    public void unregisterGame(String gameId) {
        GameModule game = gameLauncher.getGame(gameId);
        if (game != null) {
            gameLauncher.unregisterGame(gameId);
            availableGames.remove(game);
            Logging.info("🗑️ Unregistered game: " + game.getGameName() + " (ID: " + gameId + ")");
        }
    }
    
    /**
     * Gets all available games.
     * @return List of all available game modules
     */
    public List<GameModule> getAllGames() {
        return new ArrayList<>(availableGames);
    }
    
    /**
     * Gets games by difficulty level.
     * @param difficulty The difficulty level to filter by
     * @return List of games with the specified difficulty
     */
    public List<GameModule> getGamesByDifficulty(GameDifficulty difficulty) {
        return discoveryService.getGamesByDifficulty(difficulty);
    }
    
    /**
     * Gets games that support a specific game mode.
     * @param gameMode The game mode to filter by
     * @return List of games that support the specified mode
     */
    public List<GameModule> getGamesByMode(GameMode gameMode) {
        return discoveryService.getGamesByMode(gameMode);
    }
    
    /**
     * Gets games by player count.
     * @param playerCount The number of players
     * @return List of games that support the specified player count
     */
    public List<GameModule> getGamesByPlayerCount(int playerCount) {
        List<GameModule> filteredGames = new ArrayList<>();
        
        for (GameModule game : availableGames) {
            if (playerCount >= game.getMinPlayers() && playerCount <= game.getMaxPlayers()) {
                filteredGames.add(game);
            }
        }
        
        return filteredGames;
    }
    
    /**
     * Gets games by estimated duration.
     * @param maxDuration Maximum duration in minutes
     * @return List of games with duration less than or equal to maxDuration
     */
    public List<GameModule> getGamesByDuration(int maxDuration) {
        List<GameModule> filteredGames = new ArrayList<>();
        
        for (GameModule game : availableGames) {
            if (game.getEstimatedDuration() <= maxDuration) {
                filteredGames.add(game);
            }
        }
        
        return filteredGames;
    }
    
    /**
     * Gets a specific game by ID.
     * @param gameId The game ID
     * @return The game module, or null if not found
     */
    public GameModule getGame(String gameId) {
        return gameLauncher.getGame(gameId);
    }
    
    /**
     * Gets the number of available games.
     * @return Number of available games
     */
    public int getGameCount() {
        return availableGames.size();
    }
    
    /**
     * Gets games that support online multiplayer.
     * @return List of games that support online multiplayer
     */
    public List<GameModule> getOnlineMultiplayerGames() {
        return getGamesByMode(GameMode.ONLINE_MULTIPLAYER);
    }
    
    /**
     * Gets games that support local multiplayer.
     * @return List of games that support local multiplayer
     */
    public List<GameModule> getLocalMultiplayerGames() {
        return getGamesByMode(GameMode.LOCAL_MULTIPLAYER);
    }
    
    /**
     * Gets games that support single player.
     * @return List of games that support single player
     */
    public List<GameModule> getSinglePlayerGames() {
        return getGamesByMode(GameMode.SINGLE_PLAYER);
    }
    
    /**
     * Gets a summary of all available games.
     * @return Summary string of all games
     */
    public String getGamesSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("🎮 Game Registry Summary:\n");
        summary.append("Total Games: ").append(getGameCount()).append("\n");
        
        // Group by category
        summary.append("\n📂 By Category:\n");
        for (String category : List.of("Classic", "Strategy", "Puzzle", "Card", "Arcade")) {
            List<GameModule> categoryGames = discoveryService.getGamesByCategory(category);
            if (!categoryGames.isEmpty()) {
                summary.append("  ").append(category).append(": ").append(categoryGames.size()).append(" games\n");
            }
        }
        
        // Group by difficulty
        summary.append("\n🎯 By Difficulty:\n");
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            List<GameModule> difficultyGames = getGamesByDifficulty(difficulty);
            if (!difficultyGames.isEmpty()) {
                summary.append("  ").append(difficulty.getDisplayName()).append(": ").append(difficultyGames.size()).append(" games\n");
            }
        }
        
        // Group by mode
        summary.append("\n🎲 By Game Mode:\n");
        for (GameMode mode : GameMode.values()) {
            List<GameModule> modeGames = getGamesByMode(mode);
            if (!modeGames.isEmpty()) {
                summary.append("  ").append(mode.getDisplayName()).append(": ").append(modeGames.size()).append(" games\n");
            }
        }
        
        return summary.toString();
    }
    
    /**
     * Refreshes the game registry by rediscovering games.
     * @return CompletableFuture that completes when refresh is done
     */
    public CompletableFuture<List<GameModule>> refreshGames() {
        Logging.info("🔄 Refreshing Game Registry...");
        return discoveryService.refreshGames().thenApply(games -> {
            availableGames.clear();
            availableGames.addAll(games);
            Logging.info("✅ Game Registry refreshed with " + availableGames.size() + " games");
            return games;
        });
    }
} 