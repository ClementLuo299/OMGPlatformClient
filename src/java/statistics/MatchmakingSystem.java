package statistics;

import networking.sessions.GameSession;
import gamelogic.Game;
import gamelogic.GameType;
import networking.accounts.UserAccount;
import statistics.MatchmakingException;
import java.util.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles full matchmaking lifecycle including queue management,
 * skill-based matching, and error handling
 *
 * @authors Irith Irith,
 * @date April 10, 2025
 */
public class MatchmakingSystem {
    // ATTRIBUTES

    // The handler for matchmaking queue operations
    private final MatchmakingHandler matchmakingHandler;
    // The map of active games by GameType
    private final Map<GameType, List<Game>> activeGames;
    // The maximum allowed players in matchmaking queue
    private static final int MAX_QUEUE_SIZE = 100;

    // CONSTRUCTOR

    /**
     * Instantiates the Matchmaking System with empty queues and game lists
     */
    public MatchmakingSystem() {
        this.matchmakingHandler = new MatchmakingHandler(new LinkedHashMap<>());
        this.activeGames = new HashMap<>();
        initializeActiveGames();
    }

    // INITIALIZATION

    /**
     * Creates empty game lists for all GameType values
     */
    private void initializeActiveGames() {
        for (GameType type : GameType.values()) {
            activeGames.put(type, new ArrayList<>());
        }
    }

    // PUBLIC METHODS

    /**
     * Adds a Player to matchmaking queue or existing game session
     *
     * @param player The UserAccount joining matchmaking
     * @param gameType The GameType to queue for
     * @throws MatchmakingException If queue is full or matchmaking fails
     */
    public void joinMatchmaking(UserAccount player, GameType gameType) throws MatchmakingException {
        // Check queue capacity
        if (matchmakingHandler.getQueueSizeAll() >= MAX_QUEUE_SIZE) {
            throw new MatchmakingException("Matchmaking queue is full");
        }

        // Try to find existing game first
        Game existingGame = findExistingGame(player, gameType);
        if (existingGame != null) {
            joinExistingGame(player, existingGame);
        } else {
            // Add to queue and attempt matchmaking
            matchmakingHandler.joinQueue(player, gameType);
            attemptMatchmaking(gameType);
        }
    }

    // PRIVATE METHODS

    /**
     * Searches for existing games with available slots
     *
     * @param player The UserAccount looking for a game
     * @param gameType The GameType to search for
     * @return Game with available slot, or null if none found
     */
    private Game findExistingGame(UserAccount player, GameType gameType) {
        // Check all games of specified type
        for (Game game : activeGames.get(gameType)) {
            if (game.getPlayers().size() < 2) {
                return game;
            }
        }
        return null;
    }

    /**
     * Adds player to an existing game session
     *
     * @param player The UserAccount to add
     * @param game The Game to join
     */
    private void joinExistingGame(UserAccount player, Game game) {
        //game.getPlayers().add(player);
        // Start game if both players are present
        if (game.getPlayers().size() == 2) {
            game.startGame();
        }
    }

    /**
     * Attempts primary skill-based matchmaking
     *
     * @param gameType The GameType to matchmake for
     * @throws MatchmakingException If no matches found
     */
    private void attemptMatchmaking(GameType gameType) throws MatchmakingException {
        try {
            matchmakingHandler.findMatch();
        } catch (Exception e) {
            // Fallback to alternative matching if primary fails
            alternativeMatchmaking(gameType);
        }
    }

    /**
     * Fallback matchmaking with broader skill range matching
     *
     * @param gameType The GameType to matchmake for
     * @throws MatchmakingException If no candidates available
     */
    private void alternativeMatchmaking(GameType gameType) throws MatchmakingException {
        // Get all queued players for this game type
        List<UserAccount> candidates = new ArrayList<>(
                //matchmakingHandler.getSearchingUsers().keySet()
        );

        if (candidates.size() >= 2) {
            // Create game with first 2 candidates
            createGame(candidates.subList(0, 2), gameType);
        } else {
            throw new MatchmakingException("No available opponents found");
        }
    }

    /**
     * Creates new game session and removes players from queue
     *
     * @param players The List of UserAccounts to match
     * @param gameType The GameType for the new game
     */
    private void createGame(List<UserAccount> players, GameType gameType) {
        // Create new game with default 10 average plays
        //Game newGame = new Game(gameType, players, 10);
        //activeGames.get(gameType).add(newGame);

        // Clean up matchmaking queue
        //players.forEach(player -> matchmakingHandler.getSearchingUsers().remove(player));

        // Start the game session
        //newGame.startGame();
    }
}