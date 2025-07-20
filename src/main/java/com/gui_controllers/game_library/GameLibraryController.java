package com.gui_controllers.game_library;

import com.game.GameModule;
import com.game.GameOptions;
import com.game.enums.GameDifficulty;
import com.game.enums.GameMode;
import com.services.GameDiscoveryService;
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
 * Enhanced Game Library Controller with advanced filtering, search, and dynamic game discovery.
 * Combines the best features of both simple and dynamic game library controllers.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 27, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class GameLibraryController {

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
    private GridPane gamesGrid;
    
    @FXML
    private ListView<?> availableMatchesList;
    
    // Dynamic game cards container
    @FXML
    private TilePane gameCardsContainer;
    
    // Enhanced UI components
    @FXML
    private ProgressIndicator loadingIndicator;
    
    @FXML
    private Label statusLabel;
    
    // Advanced filter controls
    @FXML
    private ComboBox<String> categoryFilter;
    
    @FXML
    private ComboBox<GameDifficulty> difficultyFilter;
    
    @FXML
    private ComboBox<GameMode> modeFilter;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button refreshButton;
    
    // Legacy filter buttons (kept for backward compatibility)
    @FXML
    private Button allGamesFilterBtn;
    
    @FXML
    private Button cardGamesFilterBtn;
    
    @FXML
    private Button strategyGamesFilterBtn;
    
    @FXML
    private Button classicGamesFilterBtn;
    
    // ==================== DEPENDENCIES ====================
    
    private GameLibraryViewModel viewModel;
    private GameDiscoveryService gameDiscoveryService;
    private GameDiscoveryService discoveryService;
    private GameLauncherService gameLauncher;
    private ObservableList<GameModule> allGames;
    private FilteredList<GameModule> filteredGames;
    
    // ==================== INITIALIZATION ====================
    
    @FXML
    public void initialize() {
        Logging.info("🎮 Initializing Enhanced GameLibraryController - Instance: " + System.identityHashCode(this));
        
        // Initialize game services
        initializeGameServices();
        
        // Set up UI bindings and event handlers
        setupNavigationButtons();
        setupAdvancedFilterControls();
        setupSearchField();
        setupRefreshButton();
        setupLegacyFilterButtons();
        setupUIState();
        
        // Initialize game list
        initializeGameList();
        
        // Set up game cards from existing registry
        setupGameCards();
        
        // Start game discovery
        discoverGames();
        
        Logging.info("✅ Enhanced GameLibraryController initialized successfully - Instance: " + System.identityHashCode(this));
    }
    
    /**
     * Set the ViewModel for this controller.
     */
    public void setViewModel(GameLibraryViewModel viewModel) {
        Logging.info("Setting ViewModel for GameLibraryController");
        this.viewModel = viewModel;
        bindToViewModel();
    }
    
    // ==================== PRIVATE SETUP METHODS ====================
    
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
     * Initialize game services and registry.
     */
    private void initializeGameServices() {
        Logging.info("Initializing game services");
        
        // Initialize game discovery service and launcher
        gameDiscoveryService = GameDiscoveryService.getInstance();
        discoveryService = GameDiscoveryService.getInstance();
        gameLauncher = GameLauncherService.getInstance();
        
        // Initialize discovery service
        gameDiscoveryService.initialize();
        discoveryService.initialize();
        
        Logging.info("Game services initialized successfully");
    }
    
    /**
     * Set up dynamic game cards from discovered games.
     */
    private void setupGameCards() {
        Logging.info("🎮 Setting up dynamic game cards");
        
        try {
            // Check if gameCardsContainer is available
            if (gameCardsContainer == null) {
                Logging.error("❌ gameCardsContainer is null - cannot set up game cards");
                return;
            }
            
            Logging.info("📦 gameCardsContainer found: " + gameCardsContainer.getClass().getSimpleName());
            
            // Get all discovered games from discovery service
            List<GameModule> games = gameDiscoveryService.getAllDiscoveredGames();
            
            Logging.info("🎮 Found " + games.size() + " games to display");
            
            // Clear existing cards
            gameCardsContainer.getChildren().clear();
            Logging.info("🧹 Cleared existing game cards");
            
            // Create game cards for each discovered game
            for (GameModule game : games) {
                Logging.info("🎯 Creating card for: " + game.getGameName() + " (ID: " + game.getGameId() + ")");
                try {
                    GameLibraryCard gameCard = new GameLibraryCard(game, () -> handleGamePlay(game));
                    gameCardsContainer.getChildren().add(gameCard);
                    Logging.info("✅ Added card for: " + game.getGameName());
                } catch (Exception e) {
                    Logging.error("❌ Failed to create card for " + game.getGameName() + ": " + e.getMessage(), e);
                }
            }
            
            Logging.info("✅ Created " + gameCardsContainer.getChildren().size() + " game cards total");
            
            // Check if the container is visible
            Logging.info("👁️ gameCardsContainer visible: " + gameCardsContainer.isVisible());
            Logging.info("👁️ gameCardsContainer managed: " + gameCardsContainer.isManaged());
            
        } catch (Exception e) {
            Logging.error("❌ Error setting up game cards: " + e.getMessage(), e);
        }
    }
    
    /**
     * Set up advanced filter controls.
     */
    private void setupAdvancedFilterControls() {
        Logging.info("Setting up advanced filter controls");
        
        if (categoryFilter != null) {
            categoryFilter.getItems().addAll("All Categories", "Classic", "Strategy", "Puzzle", "Card", "Arcade", "Adventure", "Simulation");
            categoryFilter.setValue("All Categories");
            categoryFilter.setOnAction(event -> applyFilters());
        }
        
        if (difficultyFilter != null) {
            difficultyFilter.getItems().addAll(GameDifficulty.values());
            difficultyFilter.setValue(GameDifficulty.EASY);
            difficultyFilter.setOnAction(event -> applyFilters());
        }
        
        if (modeFilter != null) {
            modeFilter.getItems().addAll(GameMode.values());
            modeFilter.setValue(GameMode.LOCAL_MULTIPLAYER);
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
     * Set up legacy filter button event handlers.
     */
    private void setupLegacyFilterButtons() {
        Logging.info("Setting up legacy filter buttons");
        
        if (allGamesFilterBtn != null) {
            allGamesFilterBtn.setOnAction(event -> {
                Logging.info("All games filter clicked");
                viewModel.showAllGames();
            });
            // Set as default selection
            allGamesFilterBtn.getStyleClass().add("selected");
        }
        
        if (cardGamesFilterBtn != null) {
            cardGamesFilterBtn.setOnAction(event -> {
                Logging.info("Card games filter clicked");
                viewModel.filterCardGames();
            });
        }
        
        if (strategyGamesFilterBtn != null) {
            strategyGamesFilterBtn.setOnAction(event -> {
                Logging.info("Strategy games filter clicked");
                viewModel.filterStrategyGames();
            });
        }
        
        if (classicGamesFilterBtn != null) {
            classicGamesFilterBtn.setOnAction(event -> {
                Logging.info("Classic games filter clicked");
                viewModel.filterClassicGames();
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
                
                // Update the game display
                updateGameDisplay();
                
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
        Logging.info("🔄 Updating game display...");
        
        if (gameCardsContainer == null) {
            Logging.error("❌ gameCardsContainer is null - cannot update display");
            return;
        }
        
        Logging.info("📊 Filtered games count: " + (filteredGames != null ? filteredGames.size() : "null"));
        
        gameCardsContainer.getChildren().clear();
        
        if (filteredGames != null) {
            for (GameModule game : filteredGames) {
                Logging.info("🎮 Creating card for game: " + game.getGameName());
                GameLibraryCard gameCard = new GameLibraryCard(game, () -> handleGamePlay(game));
                gameCardsContainer.getChildren().add(gameCard);
            }
        }
        
        Logging.info("✅ Updated game display with " + (filteredGames != null ? filteredGames.size() : 0) + " games");
    }
    
    /**
     * Set up UI state bindings to ViewModel.
     */
    private void setupUIState() {
        Logging.info("Setting up UI state bindings");
        
        // Bind guest status to settings button
        if (settingsBtn != null && viewModel != null) {
            settingsBtn.disableProperty().bind(viewModel.isGuestProperty());
        }
    }
    
    /**
     * Bind controller to ViewModel properties.
     */
    private void bindToViewModel() {
        if (viewModel == null) {
            Logging.warning("Cannot bind to null ViewModel");
            return;
        }
        
        Logging.info("Binding controller to ViewModel");
        
        // Bind filter selection to UI state
        viewModel.selectedFilterProperty().addListener((observable, oldValue, newValue) -> {
            Logging.info("Filter changed from " + oldValue + " to " + newValue);
            updateFilterSelection(newValue);
        });
        
        // Bind guest status to settings button
        if (settingsBtn != null) {
            settingsBtn.disableProperty().bind(viewModel.isGuestProperty());
        }
    }
    
    /**
     * Update the visual selection state of filter buttons.
     */
    private void updateFilterSelection(String selectedFilter) {
        Logging.info("Updating filter selection to: " + selectedFilter);
        
        // Clear all selections
        clearFilterSelections();
        
        // Set new selection based on filter
        switch (selectedFilter) {
            case "all":
                if (allGamesFilterBtn != null) {
                    allGamesFilterBtn.getStyleClass().add("selected");
                }
                break;
            case "card":
                if (cardGamesFilterBtn != null) {
                    cardGamesFilterBtn.getStyleClass().add("selected");
                }
                break;
            case "strategy":
                if (strategyGamesFilterBtn != null) {
                    strategyGamesFilterBtn.getStyleClass().add("selected");
                }
                break;
            case "classic":
                if (classicGamesFilterBtn != null) {
                    classicGamesFilterBtn.getStyleClass().add("selected");
                }
                break;
        }
    }
    
    /**
     * Clear all filter button selections.
     */
    private void clearFilterSelections() {
        if (allGamesFilterBtn != null) {
            allGamesFilterBtn.getStyleClass().remove("selected");
        }
        if (cardGamesFilterBtn != null) {
            cardGamesFilterBtn.getStyleClass().remove("selected");
        }
        if (strategyGamesFilterBtn != null) {
            strategyGamesFilterBtn.getStyleClass().remove("selected");
        }
        if (classicGamesFilterBtn != null) {
            classicGamesFilterBtn.getStyleClass().remove("selected");
        }
    }
    
    /**
     * Refresh the game library display.
     * Can be called manually to reload games.
     */
    public void refreshGameLibrary() {
        Logging.info("🔄 Refreshing game library");
        setupGameCards();
    }
    
    /**
     * Handle game play action for a specific game.
     * 
     * @param game The game module to launch
     */
    private void handleGamePlay(GameModule game) {
        Logging.info("🎮 Play button clicked for game: " + game.getGameName());
        
        try {
            // Create game options
            GameOptions gameOptions = new GameOptions();
            gameOptions.setOption("launchTime", System.currentTimeMillis());
            
            // Launch the game using the integrated method
            boolean success = gameLauncher.launchGameIntegrated(
                game.getGameId(), 
                GameMode.LOCAL_MULTIPLAYER, 
                game.getMinPlayers(), 
                gameOptions
            );
            
            if (success) {
                Logging.info("✅ Game launched successfully: " + game.getGameName());
            } else {
                Logging.error("❌ Failed to launch game: " + game.getGameName());
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error launching game: " + e.getMessage(), e);
        }
    }
    
    /**
     * Launch a game with the specified parameters.
     * 
     * @param gameId The game ID to launch
     * @param gameMode The game mode
     * @param playerCount Number of players
     */
    private void launchGame(String gameId, GameMode gameMode, int playerCount) {
        Logging.info("🚀 Launching game: " + gameId + " with mode: " + gameMode.getDisplayName());
        
        try {
            // Create game options
            GameOptions gameOptions = new GameOptions();
            gameOptions.setOption("launchTime", System.currentTimeMillis());
            
            // Get the current stage
            Stage currentStage = (Stage) mainContainer.getScene().getWindow();
            
            // Launch the game
            Scene gameScene = gameLauncher.launchGame(gameId, currentStage, gameMode, playerCount, gameOptions);
            
            if (gameScene != null) {
                Logging.info("✅ Game launched successfully: " + gameId);
                // The game scene is now set on the stage
            } else {
                Logging.error("❌ Failed to launch game: " + gameId);
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error launching game: " + e.getMessage(), e);
        }
    }
} 