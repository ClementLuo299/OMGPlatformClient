package com.core;

/**
 * Defines all available screens in the application
 */
public enum Screen {
    LOGIN("fxml/Login.fxml", "css/login.css"),
    DASHBOARD("fxml/Dashboard.fxml", "css/dashboard.css"),
    GAME_LIBRARY("fxml/GameLibrary.fxml", "css/library.css"),
    LEADERBOARD("fxml/Leaderboard.fxml", "css/leaderboard.css"),
    SETTINGS("fxml/Setting.fxml", "css/setting.css"),
    REGISTER("fxml/Register.fxml", "css/register.css"),
    GAME_LOBBY("fxml/GameLobby.fxml", "css/game_lobby.css", false); // false means don't cache

    private final String fxmlPath;
    private final String cssPath;
    private final boolean cacheable;

    Screen(String fxmlPath, String cssPath) {
        this(fxmlPath, cssPath, true);
    }

    Screen(String fxmlPath, String cssPath, boolean cacheable) {
        this.fxmlPath = fxmlPath;
        this.cssPath = cssPath;
        this.cacheable = cacheable;
    }

    public String getFxmlPath() { return fxmlPath; }
    public String getCssPath() { return cssPath; }
    public boolean isCacheable() { return cacheable; }
}