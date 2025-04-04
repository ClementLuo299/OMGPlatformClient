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
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    
    @FXML
    private BorderPane mainContainer;
    
    // Game card buttons
    @FXML
    private Button ticTacToeButton;
    
    @FXML
    private Button connect4Button;
    
    @FXML
    private Button checkersButton;
    
    @FXML
    private Button whistButton;
    
    // Filter buttons
    @FXML
    private Button allGamesFilterBtn;
    
    @FXML
    private Button cardGamesFilterBtn;
    
    @FXML
    private Button strategyGamesFilterBtn;
    
    @FXML
    private Button classicGamesFilterBtn;
    
    // Game cards
    @FXML
    private VBox ticTacToeCard;
    
    @FXML
    private VBox connect4Card;
    
    @FXML
    private VBox checkersCard;
    
    @FXML
    private VBox whistCard;
    
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
        
        // Set up filter button event handlers
        setupFilterButtons();
        
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
    private void playTicTacToe() {
        try {
            GameLobbyController controller = (GameLobbyController)
                    screenManager.navigateTo(ScreenManager.GAME_LOBBY_SCREEN, ScreenManager.GAME_LOBBY_CSS);
            
            if (controller != null) {
                controller.setGame("Tic Tac Toe");
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Game Launch Error", "Could not launch Tic Tac Toe: " + e.getMessage());
        }
    }
    
    @FXML
    private void playConnect4() {
        try {
            GameLobbyController controller = (GameLobbyController)
                    screenManager.navigateTo(ScreenManager.GAME_LOBBY_SCREEN, ScreenManager.GAME_LOBBY_CSS);
            
            if (controller != null) {
                controller.setGame("Connect 4");
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Game Launch Error", "Could not launch Connect 4: " + e.getMessage());
        }
    }
    
    @FXML
    private void playCheckers() {
        showAlert(Alert.AlertType.INFORMATION, "Game Under Development", 
                 "The Checkers game is currently under development.\nPlease check back later!");
    }
    
    @FXML
    private void playWhist() {
        showAlert(Alert.AlertType.INFORMATION, "Game Under Development", 
                 "The Whist Card Game is currently under development.\nPlease check back later!");
    }
    
    /**
     * Set up the filter buttons with their event handlers
     */
    private void setupFilterButtons() {
        // Locate filter buttons if they aren't injected properly
        if (allGamesFilterBtn == null) {
            allGamesFilterBtn = findButton("All Games", "filter-button");
            cardGamesFilterBtn = findButton("Card Games", "filter-button");
            strategyGamesFilterBtn = findButton("Strategy Games", "filter-button");
            classicGamesFilterBtn = findButton("Classic Games", "filter-button");
        }
        
        // Locate game cards if they aren't injected properly
        if (ticTacToeCard == null) {
            ticTacToeCard = findGameCard("Tic Tac Toe");
            connect4Card = findGameCard("Connect 4");
            checkersCard = findGameCard("Checkers");
            whistCard = findGameCard("Whist Card Game");
        }
        
        // Set up filter button handlers
        if (allGamesFilterBtn != null) {
            allGamesFilterBtn.setOnAction(event -> showAllGames());
            selectFilter(allGamesFilterBtn); // Default selection
        }
        
        if (cardGamesFilterBtn != null) {
            cardGamesFilterBtn.setOnAction(event -> filterGames("card"));
        }
        
        if (strategyGamesFilterBtn != null) {
            strategyGamesFilterBtn.setOnAction(event -> filterGames("strategy"));
        }
        
        if (classicGamesFilterBtn != null) {
            classicGamesFilterBtn.setOnAction(event -> filterGames("classic"));
        }
    }
    
    /**
     * Helper method to find a button by text and style class
     */
    private Button findButton(String text, String styleClass) {
        for (javafx.scene.Node node : findNodesByType(mainContainer, javafx.scene.control.Button.class)) {
            Button button = (Button) node;
            if (button.getText().equals(text) && button.getStyleClass().contains(styleClass)) {
                return button;
            }
        }
        return null;
    }
    
    /**
     * Helper method to find a game card by title
     */
    private VBox findGameCard(String title) {
        for (javafx.scene.Node node : findNodesByType(mainContainer, javafx.scene.layout.VBox.class)) {
            VBox vbox = (VBox) node;
            if (vbox.getStyleClass().contains("game-card")) {
                for (javafx.scene.Node child : vbox.getChildren()) {
                    if (child instanceof Label && ((Label) child).getText().equals(title) && 
                            ((Label) child).getStyleClass().contains("game-title")) {
                        return vbox;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Helper method to find nodes by type
     */
    private <T> List<javafx.scene.Node> findNodesByType(javafx.scene.Parent root, Class<T> type) {
        List<javafx.scene.Node> nodes = new ArrayList<>();
        findNodesByType(root, type, nodes);
        return nodes;
    }
    
    /**
     * Recursive helper for findNodesByType
     */
    private <T> void findNodesByType(javafx.scene.Parent parent, Class<T> type, List<javafx.scene.Node> results) {
        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
            if (type.isInstance(node)) {
                results.add(node);
            }
            if (node instanceof javafx.scene.Parent) {
                findNodesByType((javafx.scene.Parent) node, type, results);
            }
        }
    }
    
    /**
     * Select a filter button and deselect others
     */
    private void selectFilter(Button selectedButton) {
        List<Button> filterButtons = Arrays.asList(
            allGamesFilterBtn, cardGamesFilterBtn, strategyGamesFilterBtn, classicGamesFilterBtn
        );
        
        for (Button button : filterButtons) {
            if (button != null) {
                if (button == selectedButton) {
                    if (!button.getStyleClass().contains("selected")) {
                        button.getStyleClass().add("selected");
                    }
                } else {
                    button.getStyleClass().remove("selected");
                }
            }
        }
    }
    
    /**
     * Show all games
     */
    private void showAllGames() {
        selectFilter(allGamesFilterBtn);
        
        // Show all game cards
        showGameCard(ticTacToeCard, true);
        showGameCard(connect4Card, true);
        showGameCard(checkersCard, true);
        showGameCard(whistCard, true);
    }
    
    /**
     * Filter games by category
     */
    private void filterGames(String category) {
        switch (category) {
            case "card":
                selectFilter(cardGamesFilterBtn);
                showGameCard(ticTacToeCard, false);
                showGameCard(connect4Card, false);
                showGameCard(checkersCard, false);
                showGameCard(whistCard, true);
                break;
            case "strategy":
                selectFilter(strategyGamesFilterBtn);
                showGameCard(ticTacToeCard, false);
                showGameCard(connect4Card, true);
                showGameCard(checkersCard, true);
                showGameCard(whistCard, false);
                break;
            case "classic":
                selectFilter(classicGamesFilterBtn);
                showGameCard(ticTacToeCard, true);
                showGameCard(connect4Card, false);
                showGameCard(checkersCard, false);
                showGameCard(whistCard, false);
                break;
        }
    }
    
    /**
     * Show or hide a game card
     */
    private void showGameCard(VBox card, boolean show) {
        if (card != null) {
            card.setVisible(show);
            card.setManaged(show);
        }
    }
    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 