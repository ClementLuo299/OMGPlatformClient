package com.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * ScreenManager is responsible for managing screen transitions in the application.
 * It uses a single scene with content swapping for performance, reducing the need to rebuild scenes
 * on every navigation. It also handles caching of screens and preloading essential screens for smoother transitions.
 */
public class ScreenManager {
    private static ScreenManager instance;  // Singleton instance of ScreenManager
    private BorderPane mainContainer;  // Main container that holds the content (used for swapping scenes)
    private Scene mainScene;  // The main scene that will be used for displaying the UI
    private Stage mainStage;  // The primary stage for the JavaFX application
    private Map<String, Parent> screenCache = new HashMap<>();  // Cache to store loaded screens by their FXML paths

    // Paths to the FXML files for various screens
    public static final String OPENING_SCREEN = "fxml/Opening.fxml";
    public static final String LOGIN_SCREEN = "fxml/Login.fxml";
    public static final String DASHBOARD_SCREEN = "fxml/Dashboard.fxml";
    public static final String GAME_LIBRARY_SCREEN = "fxml/GameLibrary.fxml";
    public static final String LEADERBOARD_SCREEN = "fxml/Leaderboard.fxml";
    public static final String SETTINGS_SCREEN = "fxml/Setting.fxml";
    public static final String REGISTER_SCREEN = "fxml/Register.fxml";
    public static final String GAME_LOBBY_SCREEN = "fxml/GameLobby.fxml";

    // Paths to the corresponding CSS files for the screens
    public static final String OPENING_CSS = "css/opening.css";
    public static final String LOGIN_CSS = "css/login.css";
    public static final String DASHBOARD_CSS = "css/dashboard.css";
    public static final String GAME_LIBRARY_CSS = "css/library.css";
    public static final String LEADERBOARD_CSS = "css/leaderboard.css";
    public static final String SETTINGS_CSS = "css/setting.css";
    public static final String REGISTER_CSS = "css/register.css";
    public static final String GAME_LOBBY_CSS = "css/game_lobby.css";

    // Private constructor to ensure singleton pattern
    private ScreenManager() {
        mainContainer = new BorderPane();
        mainScene = new Scene(mainContainer, 1400, 800);  // Set the scene size to 1400x800
    }

    /**
     * Returns the singleton instance of the ScreenManager.
     * If the instance doesn't exist, a new one is created.
     *
     * @return The singleton instance of ScreenManager.
     */
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    /**
     * Initializes the ScreenManager with the primary stage.
     * This method sets up the main scene and configures the title of the stage.
     *
     * @param stage The primary stage from the JavaFX application.
     */
    public void initialize(Stage stage) {
        this.mainStage = stage;
        stage.setScene(mainScene);  // Set the main scene
        stage.setTitle("OMG Platform");  // Set the title of the application window
    }

    /**
     * Sets the CSS stylesheet for the current scene transition.
     * It clears the existing stylesheets and applies the specified one.
     *
     * @param cssPath The path to the CSS file to be applied.
     */
    private void setCssStylesheet(String cssPath) {
        mainScene.getStylesheets().clear();  // Clear existing stylesheets
        if (cssPath != null) {
            String cssUrl = getClass().getClassLoader().getResource(cssPath).toExternalForm();
            mainScene.getStylesheets().add(cssUrl);  // Add the new CSS file
        }
    }

    /**
     * Navigates to a new screen by loading the specified FXML file.
     * If the screen is already cached, it reuses the cached version.
     * This method also applies the appropriate CSS based on the theme.
     *
     * @param fxmlPath The path to the FXML file for the screen.
     * @param cssPath The path to the CSS file for the screen.
     * @return The controller instance of the loaded screen.
     */
    public Object navigateTo(String fxmlPath, String cssPath) {
        try {
            Parent root;
            Object controller = null;

            // Special case for the game lobby: always reload it
            if (fxmlPath.equals(GAME_LOBBY_SCREEN)) {
                return reloadAndNavigateTo(fxmlPath, cssPath);
            }

            // If the screen is not cached, load it
            if (!screenCache.containsKey(fxmlPath)) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
                root = loader.load();
                controller = loader.getController();
                screenCache.put(fxmlPath, root);  // Cache the loaded screen
            } else {
                root = screenCache.get(fxmlPath);  // Use cached version
            }

            // Apply the appropriate CSS based on the theme
            if (cssPath != null) {
                if (ThemeManager.getInstance().isDarkTheme()) {
                    setCssStylesheet("css/dark-theme.css");
                } else {
                    setCssStylesheet(cssPath);
                }
            }

            // Set the loaded screen as the center of the main container
            mainContainer.setCenter(root);

            return controller;  // Return the controller of the loaded screen
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not navigate to screen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Reloads the specified screen and navigates to it, bypassing the cache.
     * This method is useful for screens that need to be reloaded, such as the game lobby.
     *
     * @param fxmlPath The path to the FXML file for the screen.
     * @param cssPath The path to the CSS file for the screen.
     * @return The controller instance of the loaded screen.
     */
    public Object reloadAndNavigateTo(String fxmlPath, String cssPath) {
        try {
            // Remove the screen from cache to force reload
            screenCache.remove(fxmlPath);

            // Reload the screen and its controller
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();

            // Cache the screen unless it's a game screen that needs to be reloaded every time
            /*
            if (!fxmlPath.equals(GAME_LOBBY_SCREEN) &&
                    !fxmlPath.equals(TICTACTOE_SCREEN) &&
                    !fxmlPath.equals(CONNECTFOUR_SCREEN) &&
                    !fxmlPath.equals(CHECKERS_SCREEN) &&
                    !fxmlPath.equals(WHIST_SCREEN)) {
                screenCache.put(fxmlPath, root);
            }
             */

            // Apply the appropriate CSS
            if (ThemeManager.getInstance().isDarkTheme()) {
                setCssStylesheet("css/dark-theme.css");
            } else {
                setCssStylesheet(cssPath);
            }

            mainContainer.setCenter(root);

            return controller;  // Return the controller of the reloaded screen
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not navigate to screen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Shows an alert dialog to the user.
     * This is typically used to display error or warning messages during navigation.
     *
     * @param type The type of alert (ERROR, WARNING, INFORMATION, etc.).
     * @param title The title of the alert dialog.
     * @param message The content/message of the alert.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header text
        alert.setContentText(message);  // Set the content message
        alert.showAndWait();  // Show the alert and wait for user response
    }

    /**
     * Preloads commonly used screens for faster navigation later.
     * This method preloads screens like the Dashboard and Game Library, which are accessed frequently.
     */
    public void preloadCommonScreens() {
        try {
            // Preload the Dashboard screen
            if (!screenCache.containsKey(DASHBOARD_SCREEN)) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(DASHBOARD_SCREEN));
                Parent root = loader.load();
                screenCache.put(DASHBOARD_SCREEN, root);
            }

            // Preload the Game Library screen
            if (!screenCache.containsKey(GAME_LIBRARY_SCREEN)) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(GAME_LIBRARY_SCREEN));
                Parent root = loader.load();
                screenCache.put(GAME_LIBRARY_SCREEN, root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to preload screens: " + e.getMessage());
        }
    }
}
