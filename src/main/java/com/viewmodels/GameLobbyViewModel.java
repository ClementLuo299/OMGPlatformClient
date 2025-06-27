package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Logging;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the Game Lobby screen, managing UI state and business logic.
 * Handles game queuing, player search, match management, and navigation.
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class GameLobbyViewModel {
    
    // ==================== PROPERTIES ====================
    
    private final StringProperty gameName = new SimpleStringProperty("Game");
    private final StringProperty playerName = new SimpleStringProperty();
    private final StringProperty queueStatus = new SimpleStringProperty("Not in queue");
    private final StringProperty playerSearchResult = new SimpleStringProperty("");
    private final StringProperty matchSearchResult = new SimpleStringProperty("");
    private final BooleanProperty inQueue = new SimpleBooleanProperty(false);
    private final BooleanProperty isGuest = new SimpleBooleanProperty(false);
    private final DoubleProperty queueProgress = new SimpleDoubleProperty(0.0);
    private final ObservableList<String> publicMatches = FXCollections.observableArrayList();
    
    // ==================== DEPENDENCIES ====================
    
    private final ScreenManager screenManager;
    
    // ==================== CONSTRUCTOR ====================
    
    public GameLobbyViewModel() {
        Logging.info("Initializing GameLobbyViewModel");
        this.screenManager = ScreenManager.getInstance();
        initializeDefaultData();
        Logging.info("GameLobbyViewModel initialized successfully");
    }
    
    // ==================== PROPERTY ACCESSORS ====================
    
    public String getGameName() { return gameName.get(); }
    public void setGameName(String gameName) { 
        Logging.info("Setting game name to: " + gameName);
        this.gameName.set(gameName); 
    }
    public StringProperty gameNameProperty() { return gameName; }
    
    public String getPlayerName() { return playerName.get(); }
    public void setPlayerName(String playerName) { 
        Logging.info("Setting player name to: " + playerName);
        this.playerName.set(playerName); 
    }
    public StringProperty playerNameProperty() { return playerName; }
    
    public String getQueueStatus() { return queueStatus.get(); }
    public void setQueueStatus(String status) { 
        Logging.info("Setting queue status to: " + status);
        queueStatus.set(status); 
    }
    public StringProperty queueStatusProperty() { return queueStatus; }
    
    public String getPlayerSearchResult() { return playerSearchResult.get(); }
    public void setPlayerSearchResult(String result) { 
        Logging.info("Setting player search result to: " + result);
        playerSearchResult.set(result); 
    }
    public StringProperty playerSearchResultProperty() { return playerSearchResult; }
    
    public String getMatchSearchResult() { return matchSearchResult.get(); }
    public void setMatchSearchResult(String result) { 
        Logging.info("Setting match search result to: " + result);
        matchSearchResult.set(result); 
    }
    public StringProperty matchSearchResultProperty() { return matchSearchResult; }
    
    public boolean isInQueue() { return inQueue.get(); }
    public void setInQueue(boolean inQueue) { 
        Logging.info("Setting in queue to: " + inQueue);
        this.inQueue.set(inQueue); 
    }
    public BooleanProperty inQueueProperty() { return inQueue; }
    
    public boolean isGuest() { return isGuest.get(); }
    public void setGuest(boolean guest) { 
        Logging.info("Setting guest status to: " + guest);
        isGuest.set(guest); 
    }
    public BooleanProperty isGuestProperty() { return isGuest; }
    
    public double getQueueProgress() { return queueProgress.get(); }
    public void setQueueProgress(double progress) { 
        Logging.info("Setting queue progress to: " + progress);
        queueProgress.set(progress); 
    }
    public DoubleProperty queueProgressProperty() { return queueProgress; }
    
    public ObservableList<String> getPublicMatches() { return publicMatches; }
    
    // ==================== NAVIGATION ACTIONS ====================
    
    /**
     * Navigate back to the game library.
     */
    public void navigateBack() {
        try {
            Logging.info("Navigating back to game library from lobby");
            screenManager.navigateTo(ScreenRegistry.GAME_LIBRARY);
            Logging.info("Successfully navigated back to game library");
        } catch (Exception e) {
            Logging.error("Failed to navigate back to game library: " + e.getMessage(), e);
            throw e;
        }
    }
    
    // ==================== QUEUE ACTIONS ====================
    
    /**
     * Start the game queue process.
     */
    public void startQueue() {
        try {
            Logging.info("Starting queue for game: " + getGameName());
            setInQueue(true);
            setQueueStatus("Searching for opponent...");
            setQueueProgress(-1.0); // Indeterminate progress
            Logging.info("Queue started successfully");
        } catch (Exception e) {
            Logging.error("Failed to start queue: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Stop the queue process.
     */
    public void stopQueue() {
        try {
            Logging.info("Stopping queue for game: " + getGameName());
            setInQueue(false);
            setQueueStatus("Not in queue");
            setQueueProgress(0.0);
            Logging.info("Queue stopped successfully");
        } catch (Exception e) {
            Logging.error("Failed to stop queue: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Toggle queue state (start if not in queue, stop if in queue).
     */
    public void toggleQueue() {
        if (isInQueue()) {
            stopQueue();
        } else {
            startQueue();
        }
    }
    
    // ==================== PLAYER SEARCH ACTIONS ====================
    
    /**
     * Search for a player by ID.
     */
    public void searchPlayer(String playerId) {
        try {
            Logging.info("Searching for player: " + playerId);
            
            if (playerId == null || playerId.trim().isEmpty()) {
                setPlayerSearchResult("Please enter a Player ID");
                return;
            }
            
            // TODO: Implement actual player search logic
            boolean playerFound = Math.random() > 0.5; // Simulate search result
            
            if (playerFound) {
                setPlayerSearchResult("Player found: Player_" + playerId);
                Logging.info("Player found: " + playerId);
            } else {
                setPlayerSearchResult("Player not found");
                Logging.info("Player not found: " + playerId);
            }
        } catch (Exception e) {
            Logging.error("Failed to search for player: " + e.getMessage(), e);
            setPlayerSearchResult("Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Send invitation to a player.
     */
    public void invitePlayer() {
        try {
            Logging.info("Sending player invitation");
            setPlayerSearchResult("Invitation sent! Waiting for response...");
            
            // TODO: Implement actual invitation logic
            Logging.info("Player invitation sent");
        } catch (Exception e) {
            Logging.error("Failed to send player invitation: " + e.getMessage(), e);
            setPlayerSearchResult("Failed to send invitation: " + e.getMessage());
        }
    }
    
    // ==================== MATCH SEARCH ACTIONS ====================
    
    /**
     * Search for a match by ID.
     */
    public void searchMatch(String matchId) {
        try {
            Logging.info("Searching for match: " + matchId);
            
            if (matchId == null || matchId.trim().isEmpty()) {
                setMatchSearchResult("Please enter a Match ID");
                return;
            }
            
            // TODO: Implement actual match search logic
            boolean matchFound = Math.random() > 0.5; // Simulate search result
            
            if (matchFound) {
                setMatchSearchResult("Match found: " + getGameName() + " - " + matchId);
                Logging.info("Match found: " + matchId);
            } else {
                setMatchSearchResult("Match not found");
                Logging.info("Match not found: " + matchId);
            }
        } catch (Exception e) {
            Logging.error("Failed to search for match: " + e.getMessage(), e);
            setMatchSearchResult("Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Join a specific match.
     */
    public void joinMatch() {
        try {
            Logging.info("Joining match");
            setMatchSearchResult("Joining match...");
            
            // TODO: Implement actual match joining logic
            Logging.info("Match join initiated");
        } catch (Exception e) {
            Logging.error("Failed to join match: " + e.getMessage(), e);
            setMatchSearchResult("Failed to join match: " + e.getMessage());
        }
    }
    
    // ==================== MATCH MANAGEMENT ACTIONS ====================
    
    /**
     * Refresh the list of public matches.
     */
    public void refreshPublicMatches() {
        try {
            Logging.info("Refreshing public matches for game: " + getGameName());
            
            // TODO: Implement actual match fetching logic
            publicMatches.clear();
            publicMatches.addAll(
                getGameName() + " Match #" + (1000 + (int)(Math.random() * 9000)) + " - Standard - 1/2 players (Waiting)",
                getGameName() + " Match #" + (1000 + (int)(Math.random() * 9000)) + " - Quick Play - 2/2 players (In Progress)",
                getGameName() + " Match #" + (1000 + (int)(Math.random() * 9000)) + " - Ranked - 1/2 players (Waiting)",
                getGameName() + " Match #" + (1000 + (int)(Math.random() * 9000)) + " - Standard - 2/2 players (In Progress)"
            );
            
            Logging.info("Public matches refreshed successfully");
        } catch (Exception e) {
            Logging.error("Failed to refresh public matches: " + e.getMessage(), e);
        }
    }
    
    /**
     * Start the selected game.
     */
    public void startGame() {
        try {
            Logging.info("Starting game: " + getGameName());
            
            // TODO: Implement actual game start logic
            Logging.info("Game start initiated");
        } catch (Exception e) {
            Logging.error("Failed to start game: " + e.getMessage(), e);
            throw e;
        }
    }
    
    // ==================== UTILITY ACTIONS ====================
    
    /**
     * View game rules.
     */
    public void viewRules() {
        try {
            Logging.info("Viewing rules for game: " + getGameName());
            
            // TODO: Implement actual rules display logic
            Logging.info("Rules display initiated");
        } catch (Exception e) {
            Logging.error("Failed to view rules: " + e.getMessage(), e);
        }
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Initialize default data for the lobby.
     */
    private void initializeDefaultData() {
        Logging.info("Initializing default lobby data");
        
        setGameName("Game");
        setPlayerName("Player_" + (1000 + (int)(Math.random() * 9000)));
        setQueueStatus("Not in queue");
        setInQueue(false);
        setQueueProgress(0.0);
        setGuest(false);
        
        // Initialize public matches
        refreshPublicMatches();
        
        Logging.info("Default lobby data initialized");
    }
} 