package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.ServiceManager;
import com.core.screens.ScreenManager;
import com.game.GameModule;
import com.game.GameOptions;
import com.game.GameState;
import com.game.enums.GameDifficulty;
import com.game.enums.GameMode;
import com.services.GameSearchService;
import com.gui_controllers.dashboard.GameCard;

import javafx.scene.Scene;
import javafx.stage.Stage;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * Manages GUI state and logic for the dashboard screen
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class DashboardViewModel {

    // ==================== PROPERTIES ====================
    
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty playerLevel = new SimpleStringProperty();
    private final StringProperty totalGames = new SimpleStringProperty();
    private final StringProperty winRate = new SimpleStringProperty();
    private final StringProperty currentRank = new SimpleStringProperty();
    private final StringProperty bestGame = new SimpleStringProperty();
    private final ObservableList<String> activityList = FXCollections.observableArrayList();
    private final ObservableList<GameCard> gamesList = FXCollections.observableArrayList();
    private final BooleanProperty isGuest = new SimpleBooleanProperty(false);

    // ==================== DEPENDENCIES ====================
    
    private final ServiceManager serviceManager;
    private final ScreenManager screenManager;

    // ==================== CONSTRUCTOR ====================
    
    public DashboardViewModel(ServiceManager serviceManager, ScreenManager screenManager) {
        this.serviceManager = serviceManager;
        this.screenManager = screenManager;
        
        // Initialize with default values
        initializeDefaultData();
        
        // Load games from registry
        loadGamesFromRegistry();
    }

    // ==================== PROPERTY ACCESSORS ====================
    
    public StringProperty usernameProperty() { return username; }
    public StringProperty playerLevelProperty() { return playerLevel; }
    public StringProperty totalGamesProperty() { return totalGames; }
    public StringProperty winRateProperty() { return winRate; }
    public StringProperty currentRankProperty() { return currentRank; }
    public StringProperty bestGameProperty() { return bestGame; }
    public ObservableList<String> getActivityList() { return activityList; }
    public ObservableList<GameCard> getGamesList() { return gamesList; }
    public BooleanProperty isGuestProperty() { return isGuest; }

    // ==================== PUBLIC ACTION HANDLERS ====================
    
    /**
     * Handles navigation to games library
     */
    public void handleGamesNavigation() {
        Logging.info("Games button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.GAME_LIBRARY);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open game library: " + e.getMessage(), e);
        }
    }

    /**
     * Handles navigation to leaderboard
     */
    public void handleLeaderboardNavigation() {
        Logging.info("Leaderboard button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.LEADERBOARD);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open leaderboard: " + e.getMessage(), e);
        }
    }

    /**
     * Handles navigation to settings
     */
    public void handleSettingsNavigation() {
        Logging.info("Settings button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.SETTINGS);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open settings: " + e.getMessage(), e);
        }
    }

    /**
     * Handles sign out action
     */
    public void handleSignOut() {
        Logging.info("Sign out button clicked");
        
        try {
            // TODO: Implement proper session cleanup
            screenManager.navigateTo(ScreenRegistry.LOGIN);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not sign out: " + e.getMessage(), e);
        }
    }





    // ==================== PUBLIC METHODS ====================
    
    /**
     * Sets the current user information
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.username.set(username);
        this.isGuest.set(isGuest);
        
        if (isGuest) {
            // Set guest-specific data
            playerLevel.set("Guest");
            totalGames.set("0");
            winRate.set("0%");
            currentRank.set("--");
            bestGame.set("--");
            
            // Clear and set guest activity
            activityList.clear();
            activityList.addAll(
                "Welcome to the platform as a guest!",
                "Note: Your progress will not be saved."
            );
        } else {
            // Set registered user data
            playerLevel.set("Level 10"); // TODO: Get from user service
            totalGames.set("42"); // TODO: Get from user service
            winRate.set("64%"); // TODO: Get from user service
            currentRank.set("#156"); // TODO: Get from user service
            bestGame.set("Tic Tac Toe"); // TODO: Get from user service
            
            // Set sample activity for registered users
            activityList.clear();
            activityList.addAll(
                "Played Tic Tac Toe - Won",
                "Played Checkers - Lost",
                "Played Whist - Won",
                "Reached Level 12 in Tic Tac Toe"
            );
        }
        
        Logging.info("Dashboard user set: " + username + " (Guest: " + isGuest + ")");
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Initializes default data for the dashboard
     */
    private void initializeDefaultData() {
        username.set("PlayerName");
        playerLevel.set("Level 10");
        totalGames.set("42");
        winRate.set("64%");
        currentRank.set("#156");
        bestGame.set("Tic Tac Toe");
        
        activityList.addAll(
            "Played Tic Tac Toe - Won",
            "Played Checkers - Lost",
            "Played Whist - Won",
            "Reached Level 12 in Tic Tac Toe"
        );
    }
    
    /**
     * Loads games from the GameDiscoveryService and creates GameCard components
     */
    private void loadGamesFromRegistry() {
        try {
            GameSearchService discoveryService = GameSearchService.getInstance();
            List<GameModule> availableGames = discoveryService.getAllDiscoveredGames();
            
            gamesList.clear();
            
            for (GameModule game : availableGames) {
                GameCard gameCard = new GameCard(game, () -> handleGamePlay(game));
                gamesList.add(gameCard);
            }
            
            // If no games found, add a demo game to show the functionality
            if (gamesList.isEmpty()) {
                addDemoGame();
            }
            
            Logging.info("🎮 Loaded " + gamesList.size() + " games for dashboard");
        } catch (Exception e) {
            Logging.error("❌ Failed to load games from registry: " + e.getMessage(), e);
        }
    }
    
    /**
     * Adds a demo game to show the dynamic loading functionality
     */
    private void addDemoGame() {
        try {
            // Create a demo game module
            GameModule demoGame = new GameModule() {
                @Override
                public String getGameId() { return "demo-game"; }
                
                @Override
                public String getGameName() { return "Demo Game"; }
                
                @Override
                public String getGameDescription() { return "A demo game to show dynamic loading"; }
                
                @Override
                public int getMinPlayers() { return 1; }
                
                @Override
                public int getMaxPlayers() { return 4; }
                
                @Override
                public int getEstimatedDuration() { return 10; }
                
                @Override
                public GameDifficulty getDifficulty() { return GameDifficulty.EASY; }
                
                @Override
                public String getGameCategory() { return "Demo"; }
                
                @Override
                public boolean supportsOnlineMultiplayer() { return true; }
                
                @Override
                public boolean supportsLocalMultiplayer() { return true; }
                
                @Override
                public boolean supportsSinglePlayer() { return true; }
                
                @Override
                public Scene launchGame(Stage primaryStage, GameMode gameMode, int playerCount, GameOptions gameOptions) {
                    return null; // Demo only
                }
                
                @Override
                public String getGameIconPath() { return "/icons/games/default_game_icon.png"; }
                
                @Override
                public String getGameFxmlPath() { return "/fxml/demo.fxml"; }
                
                @Override
                public String getGameCssPath() { return "/css/demo.css"; }
                
                @Override
                public void onGameClose() { }
                
                @Override
                public GameState getGameState() { return new GameState(getGameId(), getGameName(), GameMode.SINGLE_PLAYER, 1, new GameOptions()); }
                
                @Override
                public void loadGameState(GameState gameState) { }
            };
            
            GameCard demoCard = new GameCard(demoGame, () -> handleGamePlay(demoGame));
            gamesList.add(demoCard);
            
            Logging.info("🎮 Added demo game to dashboard");
        } catch (Exception e) {
            Logging.error("❌ Failed to add demo game: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handles game play action for a specific game
     */
    private void handleGamePlay(GameModule game) {
        Logging.info("🎮 Play button clicked for game: " + game.getGameName());
        
        try {
            // TODO: Implement game launch logic
            Dialog.showInfo("Game Launch", "Launching " + game.getGameName() + "...");
        } catch (Exception e) {
            Dialog.showError("Error", "Could not launch " + game.getGameName() + ": " + e.getMessage(), e);
        }
    }
}
