package main.java.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        usernameLabel.setText("PlayerName");
        totalGames.setText("42");
        winRate.setText("64%");
        currentRank.setText("#156");
        bestGame.setText("Connect 4");
    }

    @FXML
    private void signOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void playConnect4() {
        // TODO: Implement Connect 4 game launch
    }

    @FXML
    private void playCheckers() {
        // TODO: Implement Checkers game launch
    }

    @FXML
    private void playWhist() {
        // TODO: Implement Whist game launch
    }
}
