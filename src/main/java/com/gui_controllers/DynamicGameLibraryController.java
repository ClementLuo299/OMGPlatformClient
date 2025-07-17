package com.gui_controllers;

import com.games.GameModule;
import com.games.GameDiscoveryService;
import com.games.GameOptions;
import com.services.GameLauncherService;
import com.viewmodels.GameLibraryViewModel;
import com.utils.error_handling.Logging;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Dynamic Game Library Controller that automatically discovers and displays games.
 * No manual GUI modifications required when adding new games.
 *
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class DynamicGameLibraryController {

    // ==================== FXML INJECTIONS ====================
    
    @FXML
    private BorderPane mainContainer;
    
    // Navigation buttons
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
    
    // Layout containers
    @FXML
    private VBox sidebar;
    
    @FXML
    private ScrollPane gamesScrollPane;
    
    @FXML
    private VBox gamesContainer;
    
    @FXML
    private ProgressIndicator loadingIndicator;
    
    @FXML
    private Label statusLabel;
    
    // Filter controls
    @FXML
    private ComboBox<String> categoryFilter;
    
    @FXML
    private ComboBox<GameModule.GameDifficulty> difficultyFilter;
    
    @FXML
    private ComboBox<GameModule.GameMode> modeFilter;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button refreshButton;
    
    // ==================== DEPENDENCIES ====================
    
    private GameLibraryViewModel viewModel;
    private GameDiscoveryService discoveryService;
    private GameLauncherService gameLauncher;
    private ObservableList<GameModule> allGames;
    private FilteredList<GameModule> filteredGames;
    
    // ==================== INITIALIZATION ====================
    
    @FXML
    public void initialize() {
        Logging.info("Initializing Dynamic Game Library Controller");
        
        // Initialize services
        initializeServices();
        
        // Set up UI components
        setupNavigationButtons();
        setupFilterControls();
        setupSearchField();
        setupRefreshButton();
        
        // Initialize game list
        initializeGameList();
        
        // Start game discovery
        discoverGames();
        
        Logging.info("Dynamic Game Library Controller initialized successfully");
    }
    
    /**
     * Set the ViewModel for this controller.
     */
    public void setViewModel(GameLibraryViewModel viewModel) {
        Logging.info("Setting ViewModel for Dynamic Game Library Controller");
        this.viewModel = viewModel;
        bindToViewModel();
    }
    
    // ==================== PRIVATE SETUP METHODS ====================
    
    /**
     * Initialize game services.
     */
    private void initializeServices() {
        Logging.info("Initializing game services");
        
        discoveryService = GameDiscoveryService.getInstance();
        discoveryService.initialize();
        
        gameLauncher = GameLauncherService.getInstance();
        
        Logging.info("Game services initialized successfully");
    }
    
    /**
     * Set up navigation button event handlers.
     */
    private void setupNavigationButtons() {
        Logging.info("Setting up navigation buttons");
        
        if (dashboardBtn != null) {
            dashboardBtn.setOnAction(event -> {
                Logging.info("Dashboard button clicked");
                viewModel.navigateToDashboard();
            });
        }
        
        if (leaderboardBtn != null) {
            leaderboardBtn.setOnAction(event -> {
                Logging.info("Leaderboard button clicked");
                viewModel.navigateToLeaderboard();
            });
        }
        
        if (settingsBtn != null) {
            settingsBtn.setOnAction(event -> {
                Logging.info("Settings button clicked");
                viewModel.navigateToSettings();
            });
        }
        
        if (signOutBtn != null) {
            signOutBtn.setOnAction(event -> {
                Logging.info("Sign out button clicked");
                viewModel.signOut();
            });
        }
        
        // Mark current button as selected
        if (gamesBtn != null) {
            gamesBtn.getStyleClass().add("selected");
        }
    }
    
    /**
     * Set up filter controls.
     */
    private void setupFilterControls() {
        if (categoryFilter != null) {
            categoryFilter.getItems().addAll("All Categories", "Classic", "Strategy", "Puzzle", "Card", "Arcade");
            categoryFilter.setValue("All Categories");
            categoryFilter.setOnAction(event -> applyFilters());
        }
        
        if (difficultyFilter != null) {
            difficultyFilter.getItems().addAll(GameModule.GameDifficulty.values());
            difficultyFilter.setValue(GameModule.GameDifficulty.EASY);
            difficultyFilter.setOnAction(event -> applyFilters());
        }
        
        if (modeFilter != null) {
            modeFilter.getItems().addAll(GameModule.GameMode.values());
            modeFilter.setValue(GameModule.GameMode.LOCAL_MULTIPLAYER);
            modeFilter.setOnAction(event -> applyFilters());
        }
    }
    
    /**
     * Set up search field.
     */
    private void setupSearchField() {
        if (searchField != null) {
            searchField.setPromptText("Search games...");
            searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        }
    }
    
    /**
     * Set up refresh button.
     */
    private void setupRefreshButton() {
        if (refreshButton != null) {
            refreshButton.setOnAction(event -> {
                Logging.info("Refresh button clicked");
                discoverGames();
            });
        }
    }
    
    /**
     * Initialize the game list.
     */
    private void initializeGameList() {
        allGames = FXCollections.observableArrayList();
        filteredGames = new FilteredList<>(allGames, game -> true);
        
        // Bind filtered games to the UI
        filteredGames.addListener((javafx.collections.ListChangeListener.Change<? extends GameModule> change) -> {
            updateGameDisplay();
        });
    }
    
    /**
     * Discover games from all sources.
     */
    private void discoverGames() {
        Logging.info("Starting game discovery");
        
        // Show loading indicator
        if (loadingIndicator != null) {
            loadingIndicator.setVisible(true);
        }
        
        if (statusLabel != null) {
            statusLabel.setText("Discovering games...");
        }
        
        // Start discovery
        CompletableFuture<List<GameModule>> discoveryFuture = discoveryService.discoverAllGames();
        
        discoveryFuture.thenAccept(games -> {
            Logging.info("Game discovery completed: " + games.size() + " games found");
            
            // Update UI on JavaFX thread
            javafx.application.Platform.runLater(() -> {
                allGames.clear();
                allGames.addAll(games);
                
                // Hide loading indicator
                if (loadingIndicator != null) {
                    loadingIndicator.setVisible(false);
                }
                
                if (statusLabel != null) {
                    statusLabel.setText(games.size() + " games available");
                }
                
                Logging.info("Game list updated with " + games.size() + " games");
            });
        }).exceptionally(throwable -> {
            Logging.error("Game discovery failed: " + throwable.getMessage(), throwable);
            
            javafx.application.Platform.runLater(() -> {
                if (loadingIndicator != null) {
                    loadingIndicator.setVisible(false);
                }
                
                if (statusLabel != null) {
                    statusLabel.setText("Failed to load games");
                }
            });
            
            return null;
        });
    }
    
    /**
     * Apply current filters to the game list.
     */
    private void applyFilters() {
        if (filteredGames == null) return;
        
        filteredGames.setPredicate(game -> {
            // Category filter
            if (categoryFilter != null && categoryFilter.getValue() != null && 
                !"All Categories".equals(categoryFilter.getValue())) {
                if (!categoryFilter.getValue().equals(game.getGameCategory())) {
                    return false;
                }
            }
            
            // Difficulty filter
            if (difficultyFilter != null && difficultyFilter.getValue() != null) {
                if (difficultyFilter.getValue() != game.getDifficulty()) {
                    return false;
                }
            }
            
            // Mode filter
            if (modeFilter != null && modeFilter.getValue() != null) {
                switch (modeFilter.getValue()) {
                    case SINGLE_PLAYER:
                        if (!game.supportsSinglePlayer()) return false;
                        break;
                    case LOCAL_MULTIPLAYER:
                        if (!game.supportsLocalMultiplayer()) return false;
                        break;
                    case ONLINE_MULTIPLAYER:
                        if (!game.supportsOnlineMultiplayer()) return false;
                        break;
                }
            }
            
            // Search filter
            if (searchField != null && searchField.getText() != null && !searchField.getText().trim().isEmpty()) {
                String searchTerm = searchField.getText().toLowerCase();
                return game.getGameName().toLowerCase().contains(searchTerm) ||
                       game.getGameDescription().toLowerCase().contains(searchTerm) ||
                       game.getGameCategory().toLowerCase().contains(searchTerm);
            }
            
            return true;
        });
    }
    
    /**
     * Update the game display with current filtered games.
     */
    private void updateGameDisplay() {
        if (gamesContainer == null) return;
        
        gamesContainer.getChildren().clear();
        
        for (GameModule game : filteredGames) {
            VBox gameCard = createGameCard(game);
            gamesContainer.getChildren().add(gameCard);
        }
        
        Logging.info("Updated game display with " + filteredGames.size() + " games");
    }
    
    /**
     * Create a game card for display.
     */
    private VBox createGameCard(GameModule game) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("game-card");
        card.setPadding(new Insets(15));
        card.setMinWidth(200);
        card.setMaxWidth(250);
        
        // Game icon
        ImageView icon = new ImageView();
        try {
            Image gameIcon = new Image(getClass().getResourceAsStream(game.getGameIconPath()));
            icon.setImage(gameIcon);
        } catch (Exception e) {
            // Use default icon if game icon not found
            Logging.warning("Game icon not found for " + game.getGameId() + ": " + e.getMessage());
        }
        icon.setFitWidth(60);
        icon.setFitHeight(60);
        
        // Game name
        Text gameName = new Text(game.getGameName());
        gameName.getStyleClass().add("game-name");
        
        // Game description
        Text description = new Text(game.getGameDescription());
        description.getStyleClass().add("game-description");
        description.setWrappingWidth(200);
        
        // Game info
        HBox infoBox = new HBox(10);
        infoBox.setAlignment(Pos.CENTER);
        
        Label playersLabel = new Label(game.getMinPlayers() + "-" + game.getMaxPlayers() + " players");
        Label durationLabel = new Label(game.getEstimatedDuration() + " min");
        Label difficultyLabel = new Label(game.getDifficulty().getDisplayName());
        
        infoBox.getChildren().addAll(playersLabel, durationLabel, difficultyLabel);
        
        // Play button
        Button playButton = new Button("Play");
        playButton.getStyleClass().add("play-button");
        playButton.setOnAction(event -> launchGame(game));
        
        card.getChildren().addAll(icon, gameName, description, infoBox, playButton);
        card.setAlignment(Pos.CENTER);
        
        return card;
    }
    
    /**
     * Launch a game.
     */
    private void launchGame(GameModule game) {
        Logging.info("🚀 Launching game: " + game.getGameName());
        
        try {
            // Create game options
            GameOptions gameOptions = new GameOptions();
            gameOptions.setOption("launchTime", System.currentTimeMillis());
            
            // Get the current stage
            Stage currentStage = (Stage) mainContainer.getScene().getWindow();
            
            // Launch the game
            Scene gameScene = gameLauncher.launchGame(
                game.getGameId(), 
                currentStage, 
                GameModule.GameMode.LOCAL_MULTIPLAYER, 
                game.getMinPlayers(), 
                gameOptions
            );
            
            if (gameScene != null) {
                Logging.info("✅ Game launched successfully: " + game.getGameName());
            } else {
                Logging.error("❌ Failed to launch game: " + game.getGameName());
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error launching game: " + e.getMessage(), e);
        }
    }
    
    /**
     * Bind to the ViewModel.
     */
    private void bindToViewModel() {
        // Add any ViewModel bindings here if needed
        Logging.info("ViewModel binding completed");
    }
} 