package com.core;

/**
 * Defines all available screens in the application and their associated resources.
 * This enum provides a centralized registry of screens with their FXML layouts
 * and CSS styling files. It also controls whether individual screens can be cached.
 *
 * @author Clement Luo
 * @date May 18, 2025
 */
public enum Screen {
    /** Opening screen - Initial splash screen with animated start button */
    OPENING("fxml/Opening.fxml", "css/opening.css"),

    /** Login screen - Entry point for user authentication */
    LOGIN("fxml/Login.fxml", "css/login.css"),

    /** Dashboard screen - Main user interface after login */
    DASHBOARD("fxml/Dashboard.fxml", "css/dashboard.css"),

    /** Game library screen - Shows available games */
    GAME_LIBRARY("fxml/GameLibrary.fxml", "css/library.css"),

    /** Leaderboard screen - Displays player rankings */
    LEADERBOARD("fxml/Leaderboard.fxml", "css/leaderboard.css"),

    /** Settings screen - User preferences and configuration */
    SETTINGS("fxml/Setting.fxml", "css/setting.css"),

    /** Registration screen - New user account creation */
    REGISTER("fxml/Register.fxml", "css/register.css"),

    /** Game lobby screen - Pre-game setup and matchmaking (not cached) */
    GAME_LOBBY("fxml/GameLobby.fxml", "css/game_lobby.css", false);


    /** Path to the FXML layout file for this screen */
    private final String fxmlPath;

    /** Path to the CSS stylesheet for this screen */
    private final String cssPath;

    /** Flag indicating whether this screen can be cached */
    private final boolean cacheable;


    /**
     * Creates a new screen with the specified FXML and CSS paths.
     * Screen will be cacheable by default.
     *
     * @param fxmlPath Path to the screen's FXML layout file
     * @param cssPath Path to the screen's CSS stylesheet
     */
    Screen(String fxmlPath, String cssPath) {
        this(fxmlPath, cssPath, true);
    }

    /**
     * Creates a new screen with the specified FXML and CSS paths,
     * and caching behavior.
     *
     * @param fxmlPath Path to the screen's FXML layout file
     * @param cssPath Path to the screen's CSS stylesheet
     * @param cacheable Whether this screen can be cached in memory
     */
    Screen(String fxmlPath, String cssPath, boolean cacheable) {
        this.fxmlPath = fxmlPath;
        this.cssPath = cssPath;
        this.cacheable = cacheable;
    }

    /** GETTERS */
    public String getFxmlPath() { return fxmlPath; }
    public String getCssPath() { return cssPath; }
    public boolean isCacheable() { return cacheable; }
}