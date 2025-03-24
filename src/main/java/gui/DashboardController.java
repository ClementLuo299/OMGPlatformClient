package gui;

import core.networking.IO.DatabaseIOHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private Label username;

    @FXML
    private TextField searchBar;

    @FXML
    private Button dashboardButton, gameLibraryButton, leaderboardButton, profileButton, settingsButton;

    // User information variables (passed from LoginController)
    private String loggedInUsername;
    private String mostPlayedGame = "Connect 4";
    private int totalGamesPlayed = 128;
    private int totalWins = 64;
    private int gameLevel = 3;

    /**
     * Initialize Dashboard with user data
     */
    @FXML
    public void initialize() {
        System.out.println(" Dashboard Loaded!");

        // Set the username (default until login data is passed)
        username.setText(loggedInUsername != null ? loggedInUsername : "Guest");

        // Debug logs
        System.out.println("User: " + loggedInUsername);
        System.out.println("Most Played Game: " + mostPlayedGame);
        System.out.println("Total Games Played: " + totalGamesPlayed);
        System.out.println("Total Wins: " + totalWins);
    }

    /**
     * Method to set user details from LoginController
     */
    public void setUserData(String username) {
        this.loggedInUsername = username;
        this.username.setText(username);
    }

    /**
     * Handles button clicks for navigation
     */
    @FXML
    private void handleNavigation(ActionEvent event) {
        if (event.getSource() == gameLibraryButton) {
            System.out.println("📚 Switching to Game Library...");
            switchScene("GameLibrary.fxml");
        } else if (event.getSource() == leaderboardButton) {
            System.out.println("🏆 Switching to Leaderboard...");
            switchScene("Leaderboard.fxml");
        } else if (event.getSource() == profileButton) {
            System.out.println("👤 Switching to Profile...");
            switchScene("Profile.fxml");
        } else if (event.getSource() == settingsButton) {
            System.out.println("⚙ Switching to Settings...");
            switchScene("Settings.fxml");
        }
    }

    /**
     * Helper function to switch scenes
     */
    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent view = loader.load();
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());

            // Get the current stage
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
