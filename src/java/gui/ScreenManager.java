package gui;

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
 * ScreenManager handles efficient screen transitions using a single scene with content swapping.
 * This approach improves performance by avoiding complete scene rebuilds on every navigation.
 */
public class ScreenManager {
    private static ScreenManager instance;
    private BorderPane mainContainer;
    private Scene mainScene;
    private Stage mainStage;
    private Map<String, Parent> screenCache = new HashMap<>();
    
    // Screen paths
    public static final String LOGIN_SCREEN = "fxml/Login.fxml";
    public static final String DASHBOARD_SCREEN = "fxml/Dashboard.fxml";
    public static final String GAME_LIBRARY_SCREEN = "fxml/GameLibrary.fxml";
    public static final String LEADERBOARD_SCREEN = "fxml/Leaderboard.fxml";
    public static final String SETTINGS_SCREEN = "fxml/Setting.fxml";
    public static final String REGISTER_SCREEN = "fxml/Register.fxml";
    public static final String TICTACTOE_SCREEN = "fxml/TicTacToe.fxml";
    public static final String CONNECTFOUR_SCREEN = "fxml/ConnectFour.fxml";
    public static final String GAME_LOBBY_SCREEN = "fxml/GameLobby.fxml";
    
    // CSS paths
    public static final String LOGIN_CSS = "css/login.css";
    public static final String DASHBOARD_CSS = "css/dashboard.css";
    public static final String GAME_LIBRARY_CSS = "css/library.css";
    public static final String LEADERBOARD_CSS = "css/leaderboard.css";
    public static final String SETTINGS_CSS = "css/setting.css";
    public static final String REGISTER_CSS = "css/register.css";
    public static final String TICTACTOE_CSS = "css/tictactoe.css";
    public static final String CONNECTFOUR_CSS = "css/connectfour.css";
    public static final String GAME_LOBBY_CSS = "css/game_lobby.css";
    
    private ScreenManager() {
        mainContainer = new BorderPane();
        mainScene = new Scene(mainContainer, 1400, 800);
    }
    
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
    
    /**
     * Initializes the screen manager with a primary stage.
     * @param stage The primary stage from the JavaFX application
     */
    public void initialize(Stage stage) {
        this.mainStage = stage;
        stage.setScene(mainScene);
        stage.setTitle("OMG Platform");
    }
    
    /**
     * Sets the scene's stylesheet for the current transition
     * @param cssPath Path to the CSS file
     */
    private void setCssStylesheet(String cssPath) {
        mainScene.getStylesheets().clear();
        String cssUrl = getClass().getClassLoader().getResource(cssPath).toExternalForm();
        mainScene.getStylesheets().add(cssUrl);
    }
    
    /**
     * Navigates to a screen, loading it if not already cached
     * @param fxmlPath The path to the FXML file
     * @param cssPath The path to the CSS file
     * @return The controller instance for the loaded screen
     */
    public Object navigateTo(String fxmlPath, String cssPath) {
        try {
            Parent root;
            Object controller = null;
            
            // Special case for the game lobby - always reload it
            // This fixes the issue with game switching
            if (fxmlPath.equals(GAME_LOBBY_SCREEN)) {
                return reloadAndNavigateTo(fxmlPath, cssPath);
            }
            
            if (!screenCache.containsKey(fxmlPath)) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
                root = loader.load();
                controller = loader.getController();
                screenCache.put(fxmlPath, root);
            } else {
                root = screenCache.get(fxmlPath);
                // We don't have the controller here when loading from cache
            }
            
            setCssStylesheet(cssPath);
            mainContainer.setCenter(root);
            
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not navigate to screen: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Reloads a screen fresh (not from cache) and navigates to it
     * @param fxmlPath The path to the FXML file
     * @param cssPath The path to the CSS file
     * @return The controller instance for the loaded screen
     */
    public Object reloadAndNavigateTo(String fxmlPath, String cssPath) {
        try {
            // Remove from cache
            screenCache.remove(fxmlPath);
            
            // Load fresh
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();
            
            // Only cache screens that aren't game-specific
            if (!fxmlPath.equals(GAME_LOBBY_SCREEN) && 
                !fxmlPath.equals(TICTACTOE_SCREEN) && 
                !fxmlPath.equals(CONNECTFOUR_SCREEN)) {
                screenCache.put(fxmlPath, root);
            }
            
            setCssStylesheet(cssPath);
            mainContainer.setCenter(root);
            
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not navigate to screen: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Shows an alert dialog
     * @param type The alert type
     * @param title The alert title
     * @param message The alert message
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Preloads common screens for faster navigation
     * Only loads essential screens like Dashboard and Game Library
     */
    public void preloadCommonScreens() {
        try {
            if (!screenCache.containsKey(DASHBOARD_SCREEN)) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(DASHBOARD_SCREEN));
                Parent root = loader.load();
                screenCache.put(DASHBOARD_SCREEN, root);
            }
            
            if (!screenCache.containsKey(GAME_LIBRARY_SCREEN)) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(GAME_LIBRARY_SCREEN));
                Parent root = loader.load();
                screenCache.put(GAME_LIBRARY_SCREEN, root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Non-fatal, just log the error
        }
    }
} 