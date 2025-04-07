package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller for the universal game lobby screen that provides game queuing
 * and player searching options for any game in the platform.
 */
public class GameLobbyController implements Initializable {
    
    // Header controls
    @FXML private Button backButton;
    @FXML private Text gameTitle;
    @FXML private Label statusLabel;
    
    // Player info
    @FXML private ImageView playerAvatar;
    @FXML private Label playerName;
    
    // Quick play controls
    @FXML private Button quickPlayButton;
    @FXML private Label queueStatusLabel;
    @FXML private ProgressBar queueProgressBar;
    
    // Player search
    @FXML private TextField playerIdField;
    @FXML private Button searchPlayerButton;
    @FXML private Button invitePlayerButton;
    @FXML private Label playerSearchResult;
    
    // Public matches
    @FXML private ListView<String> publicMatchesList;
    @FXML private Button refreshMatchesButton;
    
    // Match search
    @FXML private TextField matchIdField;
    @FXML private Button searchMatchButton;
    @FXML private Button joinMatchButton;
    @FXML private Label matchSearchResult;
    
    // Bottom bar
    @FXML private Button cancelButton;
    @FXML private Button viewRulesButton;
    
    // State variables
    private Timeline queueTimeline;
    private final Random random = new Random();
    private boolean inQueue = false;
    private String gameName = "Game";
    private String currentUsername;
    private boolean isGuest;
    
    // Reference to the ScreenManager
    private ScreenManager screenManager = ScreenManager.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup player avatar with default color tint
        try {
            // This would be replaced with actual code to fetch player name from database
            playerName.setText(getPlayerNameFromDatabase());
        } catch (Exception e) {
            System.err.println("Error setting up player avatar: " + e.getMessage());
        }
        
        // Populate the matches list with sample data for the selected game
        refreshPublicMatches();
    }
    
    /**
     * Set the current game name and update the UI accordingly
     * @param gameName The name of the currently selected game
     */
    public void setGame(String gameName) {
        // Store the game name
        this.gameName = gameName;
        
        // Update UI elements
        gameTitle.setText(gameName + " - Game Lobby");
        statusLabel.setText(gameName + " Lobby");
        
        // Clear any previous game-specific data
        clearPreviousGameData();
        
        // Refresh the matches list with game-specific data
        refreshPublicMatches();
    }
    
    /**
     * Clear any previous game-specific data to ensure a fresh state
     */
    private void clearPreviousGameData() {
        // Reset UI elements
        queueStatusLabel.setText("Not in queue");
        queueProgressBar.setVisible(false);
        playerSearchResult.setText("");
        matchSearchResult.setText("");
        
        // Reset button states
        invitePlayerButton.setDisable(true);
        joinMatchButton.setDisable(true);
        
        // Clear search fields
        playerIdField.clear();
        matchIdField.clear();
    }
    
    /**
     * Set the current user for this controller
     * @param username The username of the current user
     * @param isGuest Whether this user is a guest
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.currentUsername = username;
        this.isGuest = isGuest;
        
        // Update the player name if we have one
        if (username != null && !username.isEmpty()) {
            playerName.setText(username);
        }
    }
    
    /**
     * Simulate fetching player name from database
     */
    private String getPlayerNameFromDatabase() {
        // This would be replaced with actual database lookup
        return "Player_" + (1000 + random.nextInt(9000));
    }
    
    /**
     * Refresh the list of public matches for the current game
     */
    private void refreshPublicMatches() {
        // Generate sample matches for the currently selected game
        String[] sampleMatches = {
            gameName + " Match #" + (1000 + random.nextInt(9000)) + " - Standard - 1/2 players (Waiting)",
            gameName + " Match #" + (1000 + random.nextInt(9000)) + " - Quick Play - 2/2 players (In Progress)",
            gameName + " Match #" + (1000 + random.nextInt(9000)) + " - Ranked - 1/2 players (Waiting)",
            gameName + " Match #" + (1000 + random.nextInt(9000)) + " - Standard - 2/2 players (In Progress)"
        };
        
        ObservableList<String> matches = FXCollections.observableArrayList(sampleMatches);
        publicMatchesList.setItems(matches);
    }
    
    /**
     * Handle quick play button click
     */
    @FXML
    private void onQuickPlayClicked() {
        if (inQueue) {
            // Cancel queue
            stopQueue();
        } else {
            // Enter queue
            startQueue();
        }
    }
    
    /**
     * Start the game queue process
     */
    private void startQueue() {
        inQueue = true;
        quickPlayButton.setText("Cancel Queue");
        queueStatusLabel.setText("Searching for opponent...");
        queueProgressBar.setVisible(true);
        queueProgressBar.setProgress(-1.0); // Indeterminate progress
        
        // Create a timeline that will "find" a match after a random delay
        int queueTimeInSeconds = 3 + random.nextInt(4); // 3-7 seconds
        
        queueTimeline = new Timeline(
            new KeyFrame(Duration.seconds(queueTimeInSeconds), event -> {
                // "Found" a match
                queueStatusLabel.setText("Opponent found! Starting game...");
                
                // Start the game after a brief delay
                Timeline startGameTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), e -> {
                        startGame();
                    })
                );
                startGameTimeline.play();
            })
        );
        
        queueTimeline.play();
    }
    
    /**
     * Stop the queue process
     */
    private void stopQueue() {
        if (queueTimeline != null) {
            queueTimeline.stop();
        }
        
        inQueue = false;
        quickPlayButton.setText("Queue for Game");
        queueStatusLabel.setText("Not in queue");
        queueProgressBar.setVisible(false);
    }
    
    /**
     * Start the selected game
     */
    private void startGame() {
        try {
            // Different game start logic based on the game name
            if (gameName.contains("Tic Tac Toe")) {
                TicTacToeController controller = (TicTacToeController)
                    screenManager.navigateTo(ScreenManager.TICTACTOE_SCREEN, ScreenManager.TICTACTOE_CSS);
                
                // Set the match ID for the game if controller supports it
                if (controller != null) {
                    try {
                        controller.setMatchId("M" + (10000 + random.nextInt(90000)));
                    } catch (Exception e) {
                        System.err.println("Warning: Could not set match ID: " + e.getMessage());
                    }
                }
            } else if (gameName.contains("Connect 4")) {
                ConnectFourController controller = (ConnectFourController)
                    screenManager.navigateTo(ScreenManager.CONNECTFOUR_SCREEN, ScreenManager.CONNECTFOUR_CSS);
                
                // Set the match ID for the game if controller supports it
                if (controller != null) {
                    try {
                        controller.setMatchId("M" + (10000 + random.nextInt(90000)));
                    } catch (Exception e) {
                        System.err.println("Warning: Could not set match ID: " + e.getMessage());
                    }
                }
            } else if (gameName.contains("Checkers")) {
                CheckersGameController controller = (CheckersGameController)
                    screenManager.navigateTo(ScreenManager.CHECKERS_SCREEN, ScreenManager.CHECKERS_CSS);
                
                // Set the match ID for the game if controller supports it
                if (controller != null) {
                    try {
                        controller.setMatchId("M" + (10000 + random.nextInt(90000)));
                    } catch (Exception e) {
                        System.err.println("Warning: Could not set match ID: " + e.getMessage());
                    }
                }
            } else {
                // For other games that are not yet implemented
                stopQueue(); // Make sure to stop the queue animation
                showAlert(Alert.AlertType.INFORMATION, "Game Not Available", 
                    "The " + gameName + " game is not yet available in this version. Please try another game.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Game Start Error", 
                "There was an error starting the game: " + e.getMessage());
            stopQueue(); // Make sure to stop the queue animation in case of error
        }
    }
    
    /**
     * Handle search player button click
     */
    @FXML
    private void onSearchPlayerClicked() {
        String playerId = playerIdField.getText().trim();
        
        if (playerId.isEmpty()) {
            playerSearchResult.setText("Please enter a Player ID");
            return;
        }
        
        // Simulate player search
        if (random.nextBoolean()) {
            // Player found
            playerSearchResult.setText("Player found: Player_" + playerId);
            invitePlayerButton.setDisable(false);
        } else {
            // Player not found
            playerSearchResult.setText("Player not found");
            invitePlayerButton.setDisable(true);
        }
    }
    
    /**
     * Handle invite player button click
     */
    @FXML
    private void onInvitePlayerClicked() {
        playerSearchResult.setText("Invitation sent! Waiting for response...");
        
        // Simulate invitation response after a delay
        Timeline responseTimeline = new Timeline(
            new KeyFrame(Duration.seconds(2), event -> {
                if (random.nextBoolean()) {
                    playerSearchResult.setText("Invitation accepted! Starting game...");
                    
                    // Start the game after a brief delay
                    Timeline startGameTimeline = new Timeline(
                        new KeyFrame(Duration.seconds(1.5), e -> {
                            startGame();
                        })
                    );
                    startGameTimeline.play();
                } else {
                    playerSearchResult.setText("Invitation declined");
                }
            })
        );
        
        responseTimeline.play();
    }
    
    /**
     * Handle refresh matches button click
     */
    @FXML
    private void onRefreshMatchesClicked() {
        refreshPublicMatches();
    }
    
    /**
     * Handle search match button click
     */
    @FXML
    private void onSearchMatchClicked() {
        String matchId = matchIdField.getText().trim();
        
        if (matchId.isEmpty()) {
            matchSearchResult.setText("Please enter a Match ID");
            return;
        }
        
        // Simulate match search
        if (random.nextBoolean()) {
            // Match found
            matchSearchResult.setText("Match found: " + gameName + " - " + matchId);
            joinMatchButton.setDisable(false);
        } else {
            // Match not found
            matchSearchResult.setText("Match not found");
            joinMatchButton.setDisable(true);
        }
    }
    
    /**
     * Handle join match button click
     */
    @FXML
    private void onJoinMatchClicked() {
        matchSearchResult.setText("Joining match...");
        
        // Start the game after a brief delay
        Timeline startGameTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1.5), e -> {
                startGame();
            })
        );
        startGameTimeline.play();
    }
    
    /**
     * Handle back button click
     */
    @FXML
    private void onBackButtonClicked() {
        try {
            // Stop any active queue
            if (inQueue) {
                stopQueue();
            }
            
            // Reset game selection (this is important to fix the issue)
            this.gameName = "Game"; // Reset the game name
            
            // Navigate back to game library
            GameLibraryController controller = (GameLibraryController)
                screenManager.reloadAndNavigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null && currentUsername != null) {
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not navigate back to Game Library: " + e.getMessage());
        }
    }
    
    /**
     * Handle cancel button click
     */
    @FXML
    private void onCancelButtonClicked() {
        // Same as back button for now
        onBackButtonClicked();
    }
    
    /**
     * Handle view rules button click
     */
    @FXML
    private void onViewRulesClicked() {
        // Show game rules (would be replaced with actual game-specific rules)
        String rules = "Game Rules for " + gameName + ":\n\n";

        if (gameName.contains("Tic Tac Toe")) {
            rules += "1. The game is played on a 3x3 grid.\n" +
                    "2. Players take turns placing X or O in empty cells.\n" +
                    "3. The first player to get three of their marks in a row (horizontally, vertically, or diagonally) wins.\n" +
                    "4. If all cells are filled and no player has won, the game is a draw.";
        } else if (gameName.contains("Connect 4")) {
            rules += "1. The game is played on a 7×6 grid.\n" +
                    "2. Players take turns dropping their colored discs from the top into any of the seven columns.\n" +
                    "3. The disc falls to the lowest available space in the column.\n" +
                    "4. The first player to connect 4 of their discs horizontally, vertically, or diagonally wins.\n" +
                    "5. If the board fills up with no four-in-a-row, the game is a draw.";
        } else if (gameName.contains("Checkers")) {
            rules += "1. Checkers is played on an 8x8 board using only the dark squares.\n" +
                    "2. Each player starts with 12 pieces placed on the dark squares of the three rows closest to them.\n" +
                    "3. Pieces move diagonally forward to an adjacent empty square.\n" +
                    "4. If an opponent's piece is diagonally adjacent and the square immediately beyond it is empty, that piece can be captured by jumping over it.\n" +
                    "5. Multiple captures in a single turn are allowed if available.\n" +
                    "6. When a piece reaches the farthest row from its starting position, it becomes a king and can move both forward and backward.\n" +
                    "7. The game ends when a player cannot make any legal moves, at which point the opponent is declared the winner.";
        } else {
            rules += "Rules for this game will be available soon.";
        }
        
        showAlert(Alert.AlertType.INFORMATION, gameName + " Rules", rules);
    }
    
    /**
     * Shows an alert dialog
     * @param type The alert type
     * @param title The alert title
     * @param message The alert message
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 