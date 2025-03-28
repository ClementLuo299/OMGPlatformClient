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
    
    @FXML
    public void initialize() {
        // Set up button event handlers
        dashboardBtn.setOnAction(event -> backToDashboard());
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/dashboard.css").toExternalForm());
            
            // Pass current user information
            DashboardController dashboardController = loader.getController();
            dashboardController.setCurrentUser(currentUsername, isGuest);
            
            Stage stage = (Stage) dashboardBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not return to dashboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void signOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/login.css").toExternalForm());
            
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Setting.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/setting.css").toExternalForm());
            
            // Pass the current user to settings
            SettingController settingController = loader.getController();
            settingController.setCurrentUser(currentUsername);
            
            Stage stage = (Stage) settingsBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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