package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

import java.io.IOException;

public class GameLibraryController {
    
    @FXML
    private Button dashboardBtn;
    
    @FXML
    private Button gamesBtn;
    
    @FXML
    private Button leaderboardBtn;
    
    @FXML
    private Button settingsBtn;
    
    @FXML
    private Button signOutBtn;
    
    @FXML
    private VBox sidebar;
    
    @FXML
    private GridPane gamesGrid;
    
    @FXML
    private ListView<?> availableMatchesList;
    
    // Game card buttons
    @FXML
    private Button connect4Button;
    
    @FXML
    private Button checkersButton;
    
    @FXML
    private Button whistButton;
    
    // User info
    private String currentUsername;
    private boolean isGuest;
    
    // Add ScreenManager instance at the beginning of the class
    private ScreenManager screenManager = ScreenManager.getInstance();
    
    @FXML
    public void initialize() {
        // Set up button event handlers
        dashboardBtn.setOnAction(event -> backToDashboard());
        gamesBtn.getStyleClass().add("selected"); // Mark current button as selected
        leaderboardBtn.setOnAction(event -> openLeaderboard());
        signOutBtn.setOnAction(event -> signOut());
        settingsBtn.setOnAction(event -> openSettings());
        
        // Populate the games grid
        populateGamesGrid();
        
        // Populate active matches
        populateActiveMatches();
    }
    
    /**
     * Set the current user for this controller
     * @param username The username of the current user
     * @param isGuest Whether this user is a guest
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.currentUsername = username;
        this.isGuest = isGuest;
        
        // Disable settings button for guest users
        if (isGuest) {
            settingsBtn.setDisable(true);
        }
    }
    
    private void populateGamesGrid() {
        // This will be implemented to dynamically populate the games grid
        // For now, we leave it empty as a placeholder since we've added static cards
    }
    
    private void populateActiveMatches() {
        // This will be implemented to dynamically populate the active matches list
        // For now, we leave it empty as a placeholder
    }
    
    @FXML
    private void backToDashboard() {
        try {
            // Use the ScreenManager to navigate
            DashboardController controller = (DashboardController)
                    screenManager.navigateTo(ScreenManager.DASHBOARD_SCREEN, ScreenManager.DASHBOARD_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not return to dashboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void openLeaderboard() {
        try {
            // Use the ScreenManager to navigate
            LeaderboardController controller = (LeaderboardController)
                    screenManager.navigateTo(ScreenManager.LEADERBOARD_SCREEN, ScreenManager.LEADERBOARD_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not open leaderboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void signOut() {
        try {
            // Use the ScreenManager to navigate to login
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not sign out: " + e.getMessage());
        }
    }
    
    @FXML
    private void openSettings() {
        // Don't allow guests to access settings
        if (isGuest) {
            showAlert(AlertType.INFORMATION, "Not Available", "Settings are not available for guest users");
            return;
        }
        
        try {
            // Use the ScreenManager to navigate
            SettingController controller = (SettingController)
                    screenManager.navigateTo(ScreenManager.SETTINGS_SCREEN, ScreenManager.SETTINGS_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not open settings: " + e.getMessage());
        }
    }
    
    // Game launch methods
    
    @FXML
    private void playConnect4() {
        showAlert(AlertType.INFORMATION, "Game Launch", "Connect 4 game will launch here");
        // TODO: Implement actual game launch
    }
    
    @FXML
    private void playCheckers() {
        showAlert(AlertType.INFORMATION, "Game Launch", "Checkers game will launch here");
        // TODO: Implement actual game launch
    }
    
    @FXML
    private void playWhist() {
        showAlert(AlertType.INFORMATION, "Game Launch", "Whist Card Game will launch here");
        // TODO: Implement actual game launch
    }
    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 