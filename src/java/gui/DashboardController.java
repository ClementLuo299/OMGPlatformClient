package gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import networking.IO.DatabaseIOHandler;
import gui.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.application.Platform;

import java.io.IOException;

public class DashboardController {
    @FXML
    private VBox sidebar;
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
    private ListView<String> activityList;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label totalGames;
    @FXML
    private Label winRate;
    @FXML
    private Label currentRank;
    @FXML
    private Label bestGame;
    
    // Current logged-in username
    private String currentUsername;
    
    // Guest status flag
    private boolean isGuest;
    
    // Database IO Handler
    private DatabaseIOHandler db = DatabaseIOHandler.getInstance();

    // ScreenManager instance
    private ScreenManager screenManager = ScreenManager.getInstance();



    @FXML
    public void initialize() {
        // Initialize activity list with some sample data
        activityList.getItems().addAll(
            "Played Connect 4 - Won",
            "Played Checkers - Lost",
            "Played Whist - Won",
            "Reached Level 12 in Connect 4"
        );

        // Set initial stats
        totalGames.setText("42");
        winRate.setText("64%");
        currentRank.setText("#156");
        bestGame.setText("Connect 4");
        
        // Set button actions
        dashboardBtn.getStyleClass().add("selected"); // Mark current button as selected
        gamesBtn.setOnAction(event -> openGameLibrary());
        leaderboardBtn.setOnAction(event -> openLeaderboard());
        settingsBtn.setOnAction(event -> openSettings());
        signOutBtn.setOnAction(event -> signOut());
    }
    
    /**
     * Set the currently logged in user
     * @param username The username of the logged in user
     * @param isGuest Whether the user is a guest
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.currentUsername = username;
        this.isGuest = isGuest;
        
        if (isGuest) {
            // For guest users, display username with "Guest" prefix
            usernameLabel.setText("Guest: " + username);
            
            // Disable settings for guest users
            settingsBtn.setDisable(true);
            
            // Clear activity list for guest users
            activityList.getItems().clear();
            activityList.getItems().add("Welcome to the platform as a guest!");
            activityList.getItems().add("Note: Your progress will not be saved.");
            
            // Reset stats for guest users
            totalGames.setText("0");
            winRate.setText("0%");
            currentRank.setText("--");
            bestGame.setText("--");
        } else {
            // For registered users, always display the username in the top corner
            usernameLabel.setText(username);
        }
    }

    @FXML
    private void signOut() {
        try {
            // Use ScreenManager to navigate to login screen
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not sign out: " + e.getMessage());
        }
    }
    
    @FXML
    private void openGameLibrary() {
        try {
            // Use ScreenManager to navigate to game library
            GameLibraryController controller = (GameLibraryController)
                    screenManager.navigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open game library: " + e.getMessage());
        }
    }
    
    @FXML
    private void openLeaderboard() {
        try {
            // Use ScreenManager to navigate to leaderboard
            LeaderboardController controller = (LeaderboardController)
                    screenManager.navigateTo(ScreenManager.LEADERBOARD_SCREEN, ScreenManager.LEADERBOARD_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not open leaderboard: " + e.getMessage());
        }
    }

    @FXML
    private void openSettings() {
        // Don't allow guests to access settings
        if (isGuest) {
            showAlert("Not Available", "Settings are not available for guest users");
            return;
        }
        
        try {
            // Use ScreenManager to navigate to settings
            SettingController controller = (SettingController)
                    screenManager.navigateTo(ScreenManager.SETTINGS_SCREEN, ScreenManager.SETTINGS_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open settings: " + e.getMessage());
        }
    }

    @FXML
    private void playConnect4() {
        // TODO: Implement Connect 4 game launch
        showAlert("Info", "Connect 4 game launch not implemented yet");
    }

    @FXML
    private void playCheckers() {
        // TODO: Implement Checkers game launch
        showAlert("Info", "Checkers game launch not implemented yet");
    }

    @FXML
    private void playWhist() {
        // TODO: Implement Whist game launch
        showAlert("Info", "Whist game launch not implemented yet");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
