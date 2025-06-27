package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Logging;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the Leaderboard screen, managing UI state and business logic.
 * Handles leaderboard data, navigation, and user interactions.
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class LeaderboardViewModel {
    // ==================== PROPERTIES ====================
    private final BooleanProperty isGuest = new SimpleBooleanProperty(false);
    // Example leaderboard data (replace with real data source)
    private final ObservableList<Object> globalLevelData = FXCollections.observableArrayList();
    private final ObservableList<Object> globalWinsData = FXCollections.observableArrayList();
    private final ObservableList<Object> globalGamesData = FXCollections.observableArrayList();
    // ... add more ObservableLists for each leaderboard table as needed

    // ==================== DEPENDENCIES ====================
    private ScreenManager screenManager; // Lazy initialization to avoid circular dependencies

    // ==================== CONSTRUCTOR ====================
    public LeaderboardViewModel() {
        Logging.info("Initializing LeaderboardViewModel");
        Logging.info("LeaderboardViewModel initialized successfully");
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
    public boolean isGuest() { return isGuest.get(); }
    public void setGuest(boolean guest) { isGuest.set(guest); }
    public BooleanProperty isGuestProperty() { return isGuest; }
    public ObservableList<Object> getGlobalLevelData() { return globalLevelData; }
    public ObservableList<Object> getGlobalWinsData() { return globalWinsData; }
    public ObservableList<Object> getGlobalGamesData() { return globalGamesData; }
    // ... add more getters for each leaderboard table as needed

    // ==================== NAVIGATION ACTIONS ====================
    public void navigateToDashboard() {
        try {
            Logging.info("Navigating to dashboard from leaderboard");
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
    public void navigateToGames() {
        try {
            Logging.info("Navigating to games from leaderboard");
            ScreenManager sm = getScreenManager();
            if (sm != null) {
                sm.navigateTo(ScreenRegistry.GAME_LIBRARY);
                Logging.info("Successfully navigated to games");
            } else {
                Logging.warning("Navigation delayed - ScreenManager not yet initialized");
            }
        } catch (Exception e) {
            Logging.error("Failed to navigate to games: " + e.getMessage(), e);
            throw e;
        }
    }
    public void navigateToSettings() {
        if (isGuest()) {
            Logging.info("Settings access denied for guest user");
            return;
        }
        try {
            Logging.info("Navigating to settings from leaderboard");
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
    public void signOut() {
        try {
            Logging.info("Signing out from leaderboard");
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
    // ==================== DATA ACTIONS ====================
    // Add methods to load/populate leaderboard data as needed
} 