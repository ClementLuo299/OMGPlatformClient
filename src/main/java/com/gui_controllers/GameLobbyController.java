package com.gui_controllers;

import com.game.GameModule;
import com.game.GameOptions;
import com.game.GameContext;
import com.game.enums.GameMode;
import com.game.GameManager;
import com.viewmodels.GameLobbyViewModel;
import com.utils.error_handling.Logging;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

/**
 * Controller for the Game Lobby screen.
 * Handles match creation, joining, and game configuration before actual gameplay.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class GameLobbyController implements Initializable {

    // ==================== FXML INJECTIONS ====================
    
    @FXML
    private BorderPane mainContainer;
    
    // Navigation buttons
    @FXML
    private Button backButton;
    
    @FXML
    private Button refreshButton;
    
    // Game information
    @FXML
    private Label gameTitleLabel;
    
    @FXML
    private Label gameDescriptionLabel;
    
    @FXML
    private ImageView gameIconImageView;
    
    // Match creation panel
    @FXML
    private VBox createMatchPanel;
    
    @FXML
    private ComboBox<GameMode> gameModeComboBox;
    
    @FXML
    private Spinner<Integer> playerCountSpinner;
    
    @FXML
    private Button createMatchButton;
    
    @FXML
    private Button quickPlayButton;
    
    // Available matches panel
    @FXML
    private VBox availableMatchesPanel;
    
    @FXML
    private ListView<MatchInfo> availableMatchesListView;
    
    @FXML
    private Label noMatchesLabel;
    
    // Game options panel
    @FXML
    private VBox gameOptionsPanel;
    
    @FXML
    private TextField matchNameField;
    
    @FXML
    private CheckBox privateMatchCheckBox;
    
    @FXML
    private Spinner<Integer> timeLimitSpinner;
    
    @FXML
    private ComboBox<String> difficultyComboBox;
    
    // ==================== DEPENDENCIES ====================
    
    private GameLobbyViewModel viewModel;
    private GameManager gameManager;
    private GameModule currentGame;
    private ObservableList<MatchInfo> availableMatches;
    
    // ==================== INITIALIZATION ====================
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logging.info("🎮 Initializing GameLobbyController");
        
        // Initialize dependencies
        gameManager = GameManager.getInstance();
        
        // Set up UI components
        setupUI();
        setupEventHandlers();
        setupBindings();
        
        // Load game from context
        loadGameFromContext();
        
        Logging.info("✅ GameLobbyController initialized successfully");
    }
    
    /**
     * Set the ViewModel for this controller.
     */
    public void setViewModel(GameLobbyViewModel viewModel) {
        this.viewModel = viewModel;
        bindToViewModel();
    }
    
    /**
     * Set the current game for this lobby.
     */
    public void setCurrentGame(GameModule game) {
        this.currentGame = game;
        updateGameInfo();
        loadAvailableMatches();
    }
    
    // ==================== PRIVATE SETUP METHODS ====================
    
    private void setupUI() {
        // Set up game mode combo box
        gameModeComboBox.setItems(FXCollections.observableArrayList(GameMode.values()));
        gameModeComboBox.getSelectionModel().selectFirst();
        
        // Set up player count spinner
        playerCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 2));
        
        // Set up time limit spinner (in minutes)
        timeLimitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 30));
        
        // Set up difficulty combo box
        difficultyComboBox.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
        difficultyComboBox.getSelectionModel().selectFirst();
        
        // Set up available matches list
        availableMatches = FXCollections.observableArrayList();
        availableMatchesListView.setItems(availableMatches);
        availableMatchesListView.setCellFactory(param -> new MatchInfoListCell());
        
        // Set up match name field
        matchNameField.setText("My Match");
        
        // Initially hide no matches label
        noMatchesLabel.setVisible(false);
    }
    
    private void setupEventHandlers() {
        // Back button
        if (backButton != null) {
            backButton.setOnAction(event -> {
                Logging.info("Back button clicked");
                navigateBack();
            });
        }
        
        // Refresh button
        if (refreshButton != null) {
            refreshButton.setOnAction(event -> {
                Logging.info("Refresh button clicked");
                refreshMatches();
            });
        }
        
        // Create match button
        if (createMatchButton != null) {
            createMatchButton.setOnAction(event -> {
                Logging.info("Create match button clicked");
                createMatch();
            });
        }
        
        // Quick play button
        if (quickPlayButton != null) {
            quickPlayButton.setOnAction(event -> {
                Logging.info("Quick play button clicked");
                quickPlay();
            });
        }
        
        // Available matches list
        if (availableMatchesListView != null) {
            availableMatchesListView.setOnMouseClicked(event -> {
                MatchInfo selectedMatch = availableMatchesListView.getSelectionModel().getSelectedItem();
                if (selectedMatch != null) {
                    Logging.info("Selected match: " + selectedMatch.getMatchName());
                    joinMatch(selectedMatch);
                }
            });
        }
    }
    
    private void setupBindings() {
        // Bind game mode changes to update player count limits
        if (gameModeComboBox != null && playerCountSpinner != null) {
            gameModeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                updatePlayerCountLimits(newVal);
            });
        }
    }
    
    private void bindToViewModel() {
        if (viewModel != null) {
            // Bind to view model properties
            Logging.info("Binding GameLobbyController to ViewModel");
        }
    }
    
    /**
     * Load the current game from the game context.
     */
    private void loadGameFromContext() {
        GameContext context = GameContext.getInstance();
        if (context.hasCurrentGame()) {
            GameModule game = context.getCurrentGame();
            setCurrentGame(game);
            Logging.info("✅ Loaded game from context: " + game.getGameName());
        } else {
            Logging.warning("⚠️ No game found in context, lobby will be empty");
            // TODO: Handle case where no game is in context
        }
    }
    
    // ==================== UI UPDATE METHODS ====================
    
    private void updateGameInfo() {
        if (currentGame != null) {
            // Update game title and description
            if (gameTitleLabel != null) {
                gameTitleLabel.setText(currentGame.getGameName());
            }
            
            if (gameDescriptionLabel != null) {
                gameDescriptionLabel.setText(currentGame.getGameDescription());
            }
            
            // Update player count limits based on game
            updatePlayerCountLimits(gameModeComboBox.getValue());
            
            Logging.info("Updated game info for: " + currentGame.getGameName());
        }
    }
    
    private void updatePlayerCountLimits(GameMode gameMode) {
        if (currentGame != null && playerCountSpinner != null) {
            int minPlayers = currentGame.getMinPlayers();
            int maxPlayers = currentGame.getMaxPlayers();
            
            // Adjust based on game mode
            if (gameMode == GameMode.SINGLE_PLAYER) {
                maxPlayers = 1;
            } else if (gameMode == GameMode.LOCAL_MULTIPLAYER) {
                // Keep original limits for local multiplayer
            } else if (gameMode == GameMode.ONLINE_MULTIPLAYER) {
                // Could have different limits for online
            }
            
            SpinnerValueFactory.IntegerSpinnerValueFactory factory = 
                (SpinnerValueFactory.IntegerSpinnerValueFactory) playerCountSpinner.getValueFactory();
            factory.setMin(minPlayers);
            factory.setMax(maxPlayers);
            
            // Ensure current value is within bounds
            int currentValue = factory.getValue();
            if (currentValue < minPlayers) {
                factory.setValue(minPlayers);
            } else if (currentValue > maxPlayers) {
                factory.setValue(maxPlayers);
            }
            
            Logging.info("Updated player count limits: " + minPlayers + "-" + maxPlayers + " for mode: " + gameMode);
        }
    }
    
    private void loadAvailableMatches() {
        // TODO: Implement actual match discovery
        // For now, create some dummy matches
        availableMatches.clear();
        
        // Add dummy matches
        availableMatches.add(new MatchInfo("Cool Match", "player1", GameMode.LOCAL_MULTIPLAYER, 2, 4, false));
        availableMatches.add(new MatchInfo("Epic Battle", "player2", GameMode.ONLINE_MULTIPLAYER, 3, 6, true));
        availableMatches.add(new MatchInfo("Quick Game", "player3", GameMode.SINGLE_PLAYER, 1, 2, false));
        
        updateMatchesDisplay();
        
        Logging.info("Loaded " + availableMatches.size() + " available matches");
    }
    
    private void updateMatchesDisplay() {
        if (availableMatches.isEmpty()) {
            noMatchesLabel.setVisible(true);
            availableMatchesListView.setVisible(false);
        } else {
            noMatchesLabel.setVisible(false);
            availableMatchesListView.setVisible(true);
        }
    }
    
    // ==================== ACTION METHODS ====================
    
    private void navigateBack() {
        try {
            if (viewModel != null) {
                viewModel.navigateBack();
            } else {
                // Fallback navigation
                Logging.warning("ViewModel not set, using fallback navigation");
            }
        } catch (Exception e) {
            Logging.error("Error navigating back: " + e.getMessage(), e);
        }
    }
    
    private void refreshMatches() {
        Logging.info("Refreshing available matches");
        loadAvailableMatches();
    }
    
    private void createMatch() {
        if (currentGame == null) {
            Logging.error("No current game set for match creation");
            return;
        }
        
        try {
            // Get match configuration
            String matchName = matchNameField.getText();
            GameMode gameMode = gameModeComboBox.getValue();
            int playerCount = playerCountSpinner.getValue();
            boolean isPrivate = privateMatchCheckBox.isSelected();
            int timeLimit = timeLimitSpinner.getValue();
            String difficulty = difficultyComboBox.getValue();
            
            // Create game options
            GameOptions gameOptions = new GameOptions();
            gameOptions.setOption("matchName", matchName);
            gameOptions.setOption("timeLimit", timeLimit);
            gameOptions.setOption("difficulty", difficulty);
            gameOptions.setOption("isPrivate", isPrivate);
            
            Logging.info("Creating match: " + matchName + " with " + playerCount + 
                        " players, mode: " + gameMode.getDisplayName());
            
            // Launch the game
            boolean success = gameManager.launchGameIntegrated(
                currentGame.getGameId(),
                gameMode,
                playerCount,
                gameOptions
            );
            
            if (success) {
                Logging.info("✅ Match created and game launched successfully");
            } else {
                Logging.error("❌ Failed to create match");
                showErrorDialog("Failed to create match");
            }
            
        } catch (Exception e) {
            Logging.error("Error creating match: " + e.getMessage(), e);
            showErrorDialog("Error creating match: " + e.getMessage());
        }
    }
    
    private void quickPlay() {
        if (currentGame == null) {
            Logging.error("No current game set for quick play");
            return;
        }
        
        try {
            // Use default settings for quick play
            GameMode gameMode = GameMode.LOCAL_MULTIPLAYER;
            int playerCount = currentGame.getMinPlayers();
            
            GameOptions gameOptions = new GameOptions();
            gameOptions.setOption("matchName", "Quick Play");
            gameOptions.setOption("timeLimit", 30);
            gameOptions.setOption("difficulty", "Medium");
            gameOptions.setOption("isPrivate", false);
            
            Logging.info("Starting quick play with " + playerCount + " players");
            
            // Launch the game
            boolean success = gameManager.launchGameIntegrated(
                currentGame.getGameId(),
                gameMode,
                playerCount,
                gameOptions
            );
            
            if (success) {
                Logging.info("✅ Quick play started successfully");
            } else {
                Logging.error("❌ Failed to start quick play");
                showErrorDialog("Failed to start quick play");
            }
            
        } catch (Exception e) {
            Logging.error("Error starting quick play: " + e.getMessage(), e);
            showErrorDialog("Error starting quick play: " + e.getMessage());
        }
    }
    
    private void joinMatch(MatchInfo match) {
        Logging.info("Joining match: " + match.getMatchName());
        
        // TODO: Implement actual match joining logic
        // For now, just show a message
        showInfoDialog("Joining Match", "Joining match: " + match.getMatchName());
        
        // In the future, this would:
        // 1. Connect to the match server
        // 2. Join the specific match
        // 3. Launch the game with match-specific settings
    }
    
    // ==================== UTILITY METHODS ====================
    
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // ==================== INNER CLASSES ====================
    
    /**
     * Represents information about an available match.
     */
    public static class MatchInfo {
        private String matchName;
        private String hostName;
        private GameMode gameMode;
        private int currentPlayers;
        private int maxPlayers;
        private boolean isPrivate;
        
        public MatchInfo(String matchName, String hostName, GameMode gameMode, 
                        int currentPlayers, int maxPlayers, boolean isPrivate) {
            this.matchName = matchName;
            this.hostName = hostName;
            this.gameMode = gameMode;
            this.currentPlayers = currentPlayers;
            this.maxPlayers = maxPlayers;
            this.isPrivate = isPrivate;
        }
        
        // Getters
        public String getMatchName() { return matchName; }
        public String getHostName() { return hostName; }
        public GameMode getGameMode() { return gameMode; }
        public int getCurrentPlayers() { return currentPlayers; }
        public int getMaxPlayers() { return maxPlayers; }
        public boolean isPrivate() { return isPrivate; }
        
        @Override
        public String toString() {
            return matchName + " (" + currentPlayers + "/" + maxPlayers + ") - " + hostName;
        }
    }
    
    /**
     * Custom list cell for displaying match information.
     */
    private class MatchInfoListCell extends ListCell<MatchInfo> {
        @Override
        protected void updateItem(MatchInfo match, boolean empty) {
            super.updateItem(match, empty);
            
            if (empty || match == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox content = new VBox(5);
                content.setPadding(new Insets(10));
                
                Label nameLabel = new Label(match.getMatchName());
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                
                Label detailsLabel = new Label(match.getHostName() + " • " + 
                                             match.getGameMode().getDisplayName() + " • " +
                                             match.getCurrentPlayers() + "/" + match.getMaxPlayers() + " players");
                detailsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                
                if (match.isPrivate()) {
                    Label privateLabel = new Label("🔒 Private");
                    privateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: orange;");
                    content.getChildren().addAll(nameLabel, detailsLabel, privateLabel);
                } else {
                    content.getChildren().addAll(nameLabel, detailsLabel);
                }
                
                setGraphic(content);
                setText(null);
            }
        }
    }
} 