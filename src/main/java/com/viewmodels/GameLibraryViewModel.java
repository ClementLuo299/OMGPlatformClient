package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Logging;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * ViewModel for the Game Library screen, managing UI state and business logic.
 * Handles game filtering, navigation, and user interactions.
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class GameLibraryViewModel {
    
    // ==================== PROPERTIES ====================
    
    private final StringProperty selectedFilter = new SimpleStringProperty("all");
    private final BooleanProperty isGuest = new SimpleBooleanProperty(false);
    
    // ==================== DEPENDENCIES ====================
    
    private ScreenManager screenManager; // Lazy initialization to avoid circular dependencies
    
    // ==================== CONSTRUCTOR ====================
    
    public GameLibraryViewModel() {
        Logging.info("Initializing GameLibraryViewModel");
        Logging.info("GameLibraryViewModel initialized successfully");
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Gets the ScreenManager instance with lazy initialization.
     */
    private ScreenManager getScreenManager() {
        if (screenManager == null) {
            try {
                screenManager = ScreenManager.getInstance();
            } catch (IllegalStateException e) {
                // ScreenManager not yet initialized (during preloading)
                Logging.warning("ScreenManager not yet initialized, navigation will be delayed");
                return null;
            }
        }
        return screenManager;
    }
    
    // ==================== PROPERTY ACCESSORS ====================
    
    public String getSelectedFilter() { return selectedFilter.get(); }
    public void setSelectedFilter(String filter) { 
        Logging.info("Setting filter to: " + filter);
        selectedFilter.set(filter); 
    }
    public StringProperty selectedFilterProperty() { return selectedFilter; }
    
    public boolean isGuest() { return isGuest.get(); }
    public void setGuest(boolean guest) { 
        Logging.info("Setting guest status to: " + guest);
        isGuest.set(guest); 
    }
    public BooleanProperty isGuestProperty() { return isGuest; }
    
    // ==================== NAVIGATION ACTIONS ====================
    
    /**
     * Navigate back to the dashboard screen.
     */
    public void navigateToDashboard() {
        try {
            Logging.info("Navigating to dashboard from game library");
            ScreenManager sm = getScreenManager();
            if (sm != null) {
                sm.navigateTo(ScreenRegistry.DASHBOARD);
                Logging.info("Successfully navigated to dashboard");
            } else {
                Logging.warning("Navigation delayed - ScreenManager not yet initialized");
            }
        } catch (Exception e) {
            Logging.error("Failed to navigate to dashboard: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Navigate to the leaderboard screen.
     */
    public void navigateToLeaderboard() {
        try {
            Logging.info("Navigating to leaderboard from game library");
            ScreenManager sm = getScreenManager();
            if (sm != null) {
                sm.navigateTo(ScreenRegistry.LEADERBOARD);
                Logging.info("Successfully navigated to leaderboard");
            } else {
                Logging.warning("Navigation delayed - ScreenManager not yet initialized");
            }
        } catch (Exception e) {
            Logging.error("Failed to navigate to leaderboard: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Navigate to the settings screen (disabled for guests).
     */
    public void navigateToSettings() {
        if (isGuest()) {
            Logging.info("Settings access denied for guest user");
            return;
        }
        
        try {
            Logging.info("Navigating to settings from game library");
            ScreenManager sm = getScreenManager();
            if (sm != null) {
                sm.navigateTo(ScreenRegistry.SETTINGS);
                Logging.info("Successfully navigated to settings");
            } else {
                Logging.warning("Navigation delayed - ScreenManager not yet initialized");
            }
        } catch (Exception e) {
            Logging.error("Failed to navigate to settings: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Sign out and return to login screen.
     */
    public void signOut() {
        try {
            Logging.info("Signing out from game library");
            ScreenManager sm = getScreenManager();
            if (sm != null) {
                sm.navigateTo(ScreenRegistry.LOGIN);
                Logging.info("Successfully signed out");
            } else {
                Logging.warning("Navigation delayed - ScreenManager not yet initialized");
            }
        } catch (Exception e) {
            Logging.error("Failed to sign out: " + e.getMessage(), e);
            throw e;
        }
    }
    
    // ==================== GAME ACTIONS ====================
    
    /**
     * Launch Tic Tac Toe game.
     */
    public void launchTicTacToe() {
        try {
            Logging.info("Launching Tic Tac Toe from game library");
            // TODO: Add GAME_LOBBY to ScreenRegistry
            Logging.info("Tic Tac Toe launch not yet implemented");
        } catch (Exception e) {
            Logging.error("Failed to launch Tic Tac Toe: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Launch Connect 4 game.
     */
    public void launchConnect4() {
        try {
            Logging.info("Launching Connect 4 from game library");
            // TODO: Add GAME_LOBBY to ScreenRegistry
            Logging.info("Connect 4 launch not yet implemented");
        } catch (Exception e) {
            Logging.error("Failed to launch Connect 4: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Launch Checkers game.
     */
    public void launchCheckers() {
        try {
            Logging.info("Launching Checkers from game library");
            // TODO: Add GAME_LOBBY to ScreenRegistry
            Logging.info("Checkers launch not yet implemented");
        } catch (Exception e) {
            Logging.error("Failed to launch Checkers: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Launch Whist game.
     */
    public void launchWhist() {
        try {
            Logging.info("Launching Whist from game library");
            // TODO: Add GAME_LOBBY to ScreenRegistry
            Logging.info("Whist launch not yet implemented");
        } catch (Exception e) {
            Logging.error("Failed to launch Whist: " + e.getMessage(), e);
            throw e;
        }
    }
    
    // ==================== FILTER ACTIONS ====================
    
    /**
     * Show all games (clear filter).
     */
    public void showAllGames() {
        Logging.info("Filtering to show all games");
        setSelectedFilter("all");
    }
    
    /**
     * Filter to show only card games.
     */
    public void filterCardGames() {
        Logging.info("Filtering to show card games");
        setSelectedFilter("card");
    }
    
    /**
     * Filter to show only strategy games.
     */
    public void filterStrategyGames() {
        Logging.info("Filtering to show strategy games");
        setSelectedFilter("strategy");
    }
    
    /**
     * Filter to show only classic games.
     */
    public void filterClassicGames() {
        Logging.info("Filtering to show classic games");
        setSelectedFilter("classic");
    }
}
