package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.ServiceManager;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manages GUI state and logic for the dashboard screen
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class DashboardViewModel {

    // ==================== PROPERTIES ====================
    
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty playerLevel = new SimpleStringProperty();
    private final StringProperty totalGames = new SimpleStringProperty();
    private final StringProperty winRate = new SimpleStringProperty();
    private final StringProperty currentRank = new SimpleStringProperty();
    private final StringProperty bestGame = new SimpleStringProperty();
    private final ObservableList<String> activityList = FXCollections.observableArrayList();
    private final BooleanProperty isGuest = new SimpleBooleanProperty(false);

    // ==================== DEPENDENCIES ====================
    
    private final ServiceManager serviceManager;
    private final ScreenManager screenManager;

    // ==================== CONSTRUCTOR ====================
    
    public DashboardViewModel(ServiceManager serviceManager, ScreenManager screenManager) {
        this.serviceManager = serviceManager;
        this.screenManager = screenManager;
        
        // Initialize with default values
        initializeDefaultData();
    }

    // ==================== PROPERTY ACCESSORS ====================
    
    public StringProperty usernameProperty() { return username; }
    public StringProperty playerLevelProperty() { return playerLevel; }
    public StringProperty totalGamesProperty() { return totalGames; }
    public StringProperty winRateProperty() { return winRate; }
    public StringProperty currentRankProperty() { return currentRank; }
    public StringProperty bestGameProperty() { return bestGame; }
    public ObservableList<String> getActivityList() { return activityList; }
    public BooleanProperty isGuestProperty() { return isGuest; }

    // ==================== PUBLIC ACTION HANDLERS ====================
    
    /**
     * Handles navigation to games library
     */
    public void handleGamesNavigation() {
        Logging.info("Games button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.GAME_LIBRARY);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open game library: " + e.getMessage(), e);
        }
    }

    /**
     * Handles navigation to leaderboard
     */
    public void handleLeaderboardNavigation() {
        Logging.info("Leaderboard button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.LEADERBOARD);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open leaderboard: " + e.getMessage(), e);
        }
    }

    /**
     * Handles navigation to settings
     */
    public void handleSettingsNavigation() {
        Logging.info("Settings button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.SETTINGS);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open settings: " + e.getMessage(), e);
        }
    }

    /**
     * Handles sign out action
     */
    public void handleSignOut() {
        Logging.info("Sign out button clicked");
        
        try {
            // TODO: Implement proper session cleanup
            screenManager.navigateTo(ScreenRegistry.LOGIN);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not sign out: " + e.getMessage(), e);
        }
    }



    /**
     * Handles Checkers game launch
     */
    public void handlePlayCheckers() {
        Logging.info("Play Checkers button clicked");
        
        try {
            // TODO: Implement Checkers game launch
            Dialog.showInfo("Info", "Checkers game launch not implemented yet");
        } catch (Exception e) {
            Dialog.showError("Error", "Could not launch Checkers: " + e.getMessage(), e);
        }
    }

    /**
     * Handles Whist game launch
     */
    public void handlePlayWhist() {
        Logging.info("Play Whist button clicked");
        
        try {
            // TODO: Implement Whist game launch
            Dialog.showInfo("Info", "Whist game launch not implemented yet");
        } catch (Exception e) {
            Dialog.showError("Error", "Could not launch Whist: " + e.getMessage(), e);
        }
    }

    /**
     * Handles Tic Tac Toe game launch
     */
    public void handlePlayTicTacToe() {
        Logging.info("Play Tic Tac Toe button clicked");
        
        try {
            // TODO: Implement Tic Tac Toe game launch
            Dialog.showInfo("Info", "Tic Tac Toe game launch not implemented yet");
        } catch (Exception e) {
            Dialog.showError("Error", "Could not launch Tic Tac Toe: " + e.getMessage(), e);
        }
    }

    // ==================== PUBLIC METHODS ====================
    
    /**
     * Sets the current user information
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.username.set(username);
        this.isGuest.set(isGuest);
        
        if (isGuest) {
            // Set guest-specific data
            playerLevel.set("Guest");
            totalGames.set("0");
            winRate.set("0%");
            currentRank.set("--");
            bestGame.set("--");
            
            // Clear and set guest activity
            activityList.clear();
            activityList.addAll(
                "Welcome to the platform as a guest!",
                "Note: Your progress will not be saved."
            );
        } else {
            // Set registered user data
            playerLevel.set("Level 10"); // TODO: Get from user service
            totalGames.set("42"); // TODO: Get from user service
            winRate.set("64%"); // TODO: Get from user service
            currentRank.set("#156"); // TODO: Get from user service
            bestGame.set("Tic Tac Toe"); // TODO: Get from user service
            
            // Set sample activity for registered users
            activityList.clear();
            activityList.addAll(
                "Played Tic Tac Toe - Won",
                "Played Checkers - Lost",
                "Played Whist - Won",
                "Reached Level 12 in Tic Tac Toe"
            );
        }
        
        Logging.info("Dashboard user set: " + username + " (Guest: " + isGuest + ")");
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Initializes default data for the dashboard
     */
    private void initializeDefaultData() {
        username.set("PlayerName");
        playerLevel.set("Level 10");
        totalGames.set("42");
        winRate.set("64%");
        currentRank.set("#156");
        bestGame.set("Tic Tac Toe");
        
        activityList.addAll(
            "Played Tic Tac Toe - Won",
            "Played Checkers - Lost",
            "Played Whist - Won",
            "Reached Level 12 in Tic Tac Toe"
        );
    }
}
