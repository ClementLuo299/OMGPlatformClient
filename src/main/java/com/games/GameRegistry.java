package com.games;

import com.games.modules.Connect4Module;
import com.games.modules.TicTacToeModule;
import com.services.GameLauncherService;
import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for automatically discovering and registering game modules.
 * Provides a centralized way to manage all available games.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class GameRegistry {
    
    private static GameRegistry instance;
    private final List<GameModule> availableGames;
    private final GameLauncherService gameLauncher;
    
    private GameRegistry() {
        this.availableGames = new ArrayList<>();
        this.gameLauncher = GameLauncherService.getInstance();
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
     * Initializes the game registry and registers all available games.
     */
    public void initialize() {
        Logging.info("🎮 Initializing Game Registry...");
        
        // Register all available game modules
        registerGameModules();
        
        Logging.info("✅ Game Registry initialized with " + availableGames.size() + " games");
    }
    
    /**
     * Registers all available game modules.
     */
    private void registerGameModules() {
        // Register TicTacToe
        registerGame(new TicTacToeModule());
        
        // Register Connect4
        registerGame(new Connect4Module());
        
        // TODO: Add more games here as they are developed
        // registerGame(new CheckersModule());
        // registerGame(new ChessModule());
        // registerGame(new BattleshipModule());
        // registerGame(new SnakeModule());
        // registerGame(new TetrisModule());
    }
    
    /**
     * Registers a game module.
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
    public List<GameModule> getGamesByDifficulty(GameModule.GameDifficulty difficulty) {
        List<GameModule> filteredGames = new ArrayList<>();
        
        for (GameModule game : availableGames) {
            if (game.getDifficulty() == difficulty) {
                filteredGames.add(game);
            }
        }
        
        return filteredGames;
    }
    
    /**
     * Gets games that support a specific game mode.
     * @param gameMode The game mode to filter by
     * @return List of games that support the specified mode
     */
    public List<GameModule> getGamesByMode(GameModule.GameMode gameMode) {
        List<GameModule> filteredGames = new ArrayList<>();
        
        for (GameModule game : availableGames) {
            boolean supportsMode = false;
            
            switch (gameMode) {
                case SINGLE_PLAYER:
                    supportsMode = game.supportsSinglePlayer();
                    break;
                case LOCAL_MULTIPLAYER:
                    supportsMode = game.supportsLocalMultiplayer();
                    break;
                case ONLINE_MULTIPLAYER:
                    supportsMode = game.supportsOnlineMultiplayer();
                    break;
            }
            
            if (supportsMode) {
                filteredGames.add(game);
            }
        }
        
        return filteredGames;
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
        return getGamesByMode(GameModule.GameMode.ONLINE_MULTIPLAYER);
    }
    
    /**
     * Gets games that support local multiplayer.
     * @return List of games that support local multiplayer
     */
    public List<GameModule> getLocalMultiplayerGames() {
        return getGamesByMode(GameModule.GameMode.LOCAL_MULTIPLAYER);
    }
    
    /**
     * Gets games that support single player.
     * @return List of games that support single player
     */
    public List<GameModule> getSinglePlayerGames() {
        return getGamesByMode(GameModule.GameMode.SINGLE_PLAYER);
    }
    
    /**
     * Gets a summary of all available games.
     * @return Summary string of all games
     */
    public String getGamesSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("🎮 Available Games (").append(getGameCount()).append("):\n");
        
        for (GameModule game : availableGames) {
            summary.append("  • ").append(game.getGameName())
                   .append(" (").append(game.getGameId()).append(")")
                   .append(" - ").append(game.getGameDescription())
                   .append(" [").append(game.getMinPlayers()).append("-").append(game.getMaxPlayers()).append(" players")
                   .append(", ").append(game.getDifficulty().getDisplayName())
                   .append(", ").append(game.getEstimatedDuration()).append(" min]\n");
        }
        
        return summary.toString();
    }
} 