package gui;

import networking.IO.DatabaseIOHandler;

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
    
    // Database IO Handler
    private DatabaseIOHandler db = DatabaseIOHandler.getInstance();

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
        gamesBtn.setOnAction(event -> openGameLibrary());
        settingsBtn.setOnAction(event -> openSettings());
        signOutBtn.setOnAction(event -> signOut());
    }
    
    /**
     * Set the currently logged in user
     * @param username The username of the logged in user
     */
    public void setCurrentUser(String username) {
        this.currentUsername = username;
        
        // Get the user's full name from the database
        String fullName = db.getUserFullName(username);
        
        // If we have a full name, display it; otherwise, use the username
        if (fullName != null && !fullName.isEmpty()) {
            usernameLabel.setText(fullName);
        } else {
            usernameLabel.setText(username);
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
            showAlert("Error", "Could not sign out: " + e.getMessage());
        }
    }
    
    @FXML
    private void openGameLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/GameLibrary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            
            // Add the library.css stylesheet directly
            String cssPath = getClass().getClassLoader().getResource("css/library.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            
            Stage stage = (Stage) gamesBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open game library: " + e.getMessage());
        }
    }

    @FXML
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Setting.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/setting.css").toExternalForm());
            
            Stage stage = (Stage) settingsBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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
