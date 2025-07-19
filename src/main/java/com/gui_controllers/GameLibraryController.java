package com.gui_controllers;

import com.games.GameModule;
import com.games.GameRegistry;
import com.games.GameOptions;
import com.services.GameLauncherService;
import com.viewmodels.GameLibraryViewModel;
import com.gui_controllers.GameLibraryCard;
import com.utils.error_handling.Logging;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the Game Library screen, handling UI interactions and binding to ViewModel.
 * Manages the display of available games and navigation between screens.
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
    
    // Filter buttons
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
    private GameRegistry gameRegistry;
    private GameLauncherService gameLauncher;
    
    // ==================== INITIALIZATION ====================
    
    @FXML
    public void initialize() {
        Logging.info("Initializing GameLibraryController");
        
        // Initialize game services
        initializeGameServices();
        
        // Set up UI bindings and event handlers
        setupNavigationButtons();
        setupFilterButtons();
        setupUIState();
        
        // Game cards will be set up asynchronously after discovery completes
        
        Logging.info("GameLibraryController initialized successfully");
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
        
        // Initialize game registry
        gameRegistry = GameRegistry.getInstance();
        gameLauncher = GameLauncherService.getInstance();
        
        // Initialize registry and wait for games to be discovered
        gameRegistry.initialize();
        
        // Wait a bit for async discovery to complete, then set up game cards
        new Thread(() -> {
            try {
                // Wait for discovery to complete
                Thread.sleep(2000);
                
                // Update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    setupGameCards();
                });
                
            } catch (InterruptedException e) {
                Logging.error("❌ Interrupted while waiting for game discovery", e);
            }
        }).start();
        
        Logging.info("Game services initialized successfully");
    }
    
    /**
     * Set up dynamic game cards from discovered games.
     */
    private void setupGameCards() {
        Logging.info("Setting up dynamic game cards");
        
        try {
            // Get all discovered games from registry
            List<GameModule> games = gameRegistry.getAllGames();
            
            Logging.info("🎮 Found " + games.size() + " games to display");
            
            // Clear existing cards
            gameCardsContainer.getChildren().clear();
            
            // Create game cards for each discovered game
            for (GameModule game : games) {
                Logging.info("🎯 Creating card for: " + game.getGameName() + " (ID: " + game.getGameId() + ")");
                GameLibraryCard gameCard = new GameLibraryCard(game, () -> handleGamePlay(game));
                gameCardsContainer.getChildren().add(gameCard);
            }
            
            Logging.info("✅ Created " + gameCardsContainer.getChildren().size() + " game cards");
            
        } catch (Exception e) {
            Logging.error("❌ Error setting up game cards: " + e.getMessage(), e);
        }
    }
    
    /**
     * Set up filter button event handlers.
     */
    private void setupFilterButtons() {
        Logging.info("Setting up filter buttons");
        
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
            
            // Get the current stage
            Stage currentStage = (Stage) mainContainer.getScene().getWindow();
            
            // Launch the game with default settings
            Scene gameScene = gameLauncher.launchGame(
                game.getGameId(), 
                currentStage, 
                GameModule.GameMode.LOCAL_MULTIPLAYER, 
                game.getMinPlayers(), 
                gameOptions
            );
            
            if (gameScene != null) {
                Logging.info("✅ Game launched successfully: " + game.getGameName());
                // The game scene is now set on the stage
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
    private void launchGame(String gameId, GameModule.GameMode gameMode, int playerCount) {
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