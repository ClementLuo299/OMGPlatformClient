package com.controllers.statistics;

import com.core.ScreenManager;
import com.controllers.GameLibraryController;
import com.controllers.systems.DashboardController;
import com.controllers.systems.SettingController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for the Leaderboard screen.
 * Handles displaying global and game-specific leaderboards.
 */
public class LeaderboardController {

    // Sidebar Navigation
    @FXML private Button dashboardBtn;
    @FXML private Button gamesBtn;
    @FXML private Button leaderboardBtn;
    @FXML private Button settingsBtn;
    @FXML private Button signOutBtn;
    @FXML private VBox sidebar;

    // Leaderboard Components
    @FXML private TabPane leaderboardTabs;
    
    // Tables in Global Tab
    @FXML private TableView<LeaderboardEntry> accountLevelTable;
    @FXML private TableView<LeaderboardEntry> mostWinsTable;
    @FXML private TableView<LeaderboardEntry> mostGamesTable;
    
    // Tables in Connect4 Tab
    @FXML private TableView<LeaderboardEntry> c4LevelTable;
    @FXML private TableView<LeaderboardEntry> c4WinsTable;
    @FXML private TableView<LeaderboardEntry> c4BestTable;
    @FXML private TableView<LeaderboardEntry> connect4MostGamesTable;
    
    // Tables in Checkers Tab
    @FXML private TableView<LeaderboardEntry> checkersLevelTable;
    @FXML private TableView<LeaderboardEntry> checkersWinsTable;
    @FXML private TableView<LeaderboardEntry> checkersBestTable;
    @FXML private TableView<LeaderboardEntry> checkersMostGamesTable;
    
    // Tables in Whist Tab
    @FXML private TableView<LeaderboardEntry> whistLevelTable;
    @FXML private TableView<LeaderboardEntry> whistWinsTable;
    @FXML private TableView<LeaderboardEntry> whistBestTable;
    @FXML private TableView<LeaderboardEntry> whistMostGamesTable;
    
    // com.network.IO.User info
    private String currentUsername;
    private boolean isGuest;

    // Add ScreenManager instance at the beginning of the class
    private ScreenManager screenManager = ScreenManager.getInstance();

    /**
     * Initialize the controller.
     * Sets up event handlers and populates leaderboard tables.
     */
    @FXML
    public void initialize() {
        // Set up event handlers for navigation buttons
        dashboardBtn.setOnAction(event -> navigateToDashboard());
        gamesBtn.setOnAction(event -> navigateToGames());
        leaderboardBtn.getStyleClass().add("selected");  // Mark current button as selected
        signOutBtn.setOnAction(event -> signOut());
        settingsBtn.setOnAction(event -> navigateToSettings());
        
        // Initialize all tables
        initializeAllTables();
        
        // Populate tables with sample data for now
        populateLeaderboardTables();
    }
    
    /**
     * Set the current user.
     * 
     * @param username The username of the current user
     * @param isGuest Whether the user is a guest
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.currentUsername = username;
        this.isGuest = isGuest;
        
        // Disable settings for guest users
        if (isGuest) {
            settingsBtn.setDisable(true);
        }
    }
    
    /**
     * Initialize all leaderboard tables with columns.
     */
    private void initializeAllTables() {
        // Initialize global tables
        initializeGlobalTables();
        
        // Initialize game-specific tables
        initializeGameTables();
    }
    
    /**
     * Initialize global leaderboard tables
     */
    private void initializeGlobalTables() {
        // Account Level table - simple table with rank, player, level
        initializeTable(accountLevelTable, "level");
        
        // Most Wins table - simple table with rank, player, wins
        initializeTable(mostWinsTable, "value");
        
        // Most Games table - simple table with rank, player, games
        initializeTable(mostGamesTable, "games");
    }
    
    /**
     * Initialize game-specific leaderboard tables
     */
    private void initializeGameTables() {
        // Connect4 tables
        initializeTable(c4LevelTable, "level"); 
        initializeTable(c4WinsTable, "value");
        initializeTable(c4BestTable, "win-percent");
        initializeWideTable(connect4MostGamesTable);
        
        // Checkers tables
        initializeTable(checkersLevelTable, "level");
        initializeTable(checkersWinsTable, "value");
        initializeTable(checkersBestTable, "win-percent");
        initializeWideTable(checkersMostGamesTable);
        
        // Whist tables
        initializeTable(whistLevelTable, "level");
        initializeTable(whistWinsTable, "value");
        initializeTable(whistBestTable, "win-percent");
        initializeWideTable(whistMostGamesTable);
    }
    
    /**
     * Initialize a single table with standard columns.
     * 
     * @param table The table to initialize
     * @param valueType The type of value column to create ("level", "value", "games", "win-percent")
     */
    private void initializeTable(TableView<LeaderboardEntry> table, String valueType) {
        // Create columns
        TableColumn<LeaderboardEntry, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rankCol.getStyleClass().add("rank-column");
        
        TableColumn<LeaderboardEntry, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        playerCol.getStyleClass().add("player-column");
        
        TableColumn<LeaderboardEntry, Integer> valueCol = new TableColumn<>(getColumnHeaderForType(valueType));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.getStyleClass().add(getStyleClassForType(valueType));
        
        // Add columns to table
        table.getColumns().addAll(rankCol, playerCol, valueCol);
        
        // Set table properties
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("leaderboard-table");
    }
    
    /**
     * Initialize a wide table for Most Games Played with multiple columns
     * 
     * @param table The table to initialize
     */
    private void initializeWideTable(TableView<LeaderboardEntry> table) {
        // For wide tables, we don't replace the default columns - they're already specified in FXML
        // Instead, we just set the table properties
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Clear all columns first since they're defined in FXML
        table.getColumns().clear();
        
        // Create columns
        TableColumn<LeaderboardEntry, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rankCol.getStyleClass().add("rank-column");
        
        TableColumn<LeaderboardEntry, String> playerCol = new TableColumn<>("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        playerCol.getStyleClass().add("player-column");
        
        TableColumn<LeaderboardEntry, Integer> gamesCol = new TableColumn<>("Games");
        gamesCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        gamesCol.getStyleClass().add("games-column");
        
        // Add columns to table - in the future we would add the win % and level columns too
        table.getColumns().addAll(rankCol, playerCol, gamesCol);
    }
    
    /**
     * Get the appropriate column header based on the value type
     * 
     * @param valueType The type of value column
     * @return The header text for that column
     */
    private String getColumnHeaderForType(String valueType) {
        switch(valueType) {
            case "level": return "Level";
            case "games": return "Games";
            case "win-percent": return "Win %";
            default: return "Wins";
        }
    }
    
    /**
     * Get the appropriate style class based on the value type
     * 
     * @param valueType The type of value column
     * @return The style class for that column
     */
    private String getStyleClassForType(String valueType) {
        switch(valueType) {
            case "level": return "level-column";
            case "games": return "games-column";
            case "win-percent": return "win-percent-column";
            default: return "value-column";
        }
    }
    
    /**
     * Populate all leaderboard tables with sample data.
     * In a real application, this would fetch data from a database or service.
     */
    private void populateLeaderboardTables() {
        // Sample data for demonstration
        ObservableList<LeaderboardEntry> globalLevelData = FXCollections.observableArrayList(
            new LeaderboardEntry(1, "JohnMaster", 42),
            new LeaderboardEntry(2, "GameQueen", 38),
            new LeaderboardEntry(3, "ProPlayer99", 35),
            new LeaderboardEntry(4, "GameWizard", 32),
            new LeaderboardEntry(5, "ChampionX", 29)
        );
        
        ObservableList<LeaderboardEntry> globalWinsData = FXCollections.observableArrayList(
            new LeaderboardEntry(1, "GameQueen", 156),
            new LeaderboardEntry(2, "JohnMaster", 142),
            new LeaderboardEntry(3, "ChampionX", 121),
            new LeaderboardEntry(4, "ProPlayer99", 103),
            new LeaderboardEntry(5, "BoardGamePro", 89)
        );
        
        ObservableList<LeaderboardEntry> globalGamesData = FXCollections.observableArrayList(
            new LeaderboardEntry(1, "GameQueen", 230),
            new LeaderboardEntry(2, "JohnMaster", 215),
            new LeaderboardEntry(3, "BoardGamePro", 198),
            new LeaderboardEntry(4, "ProPlayer99", 185),
            new LeaderboardEntry(5, "ChampionX", 170)
        );
        
        // Populate global tables
        accountLevelTable.setItems(globalLevelData);
        mostWinsTable.setItems(globalWinsData);
        mostGamesTable.setItems(globalGamesData);
        
        // Create sample data for Connect4
        ObservableList<LeaderboardEntry> connect4LevelData = FXCollections.observableArrayList(
            new LeaderboardEntry(1, "Connect4Pro", 26),
            new LeaderboardEntry(2, "JohnMaster", 24),
            new LeaderboardEntry(3, "GameQueen", 22),
            new LeaderboardEntry(4, "FourInARow", 21),
            new LeaderboardEntry(5, "ChampionX", 19)
        );
        
        // Populate Connect4 tables
        c4LevelTable.setItems(connect4LevelData);
        c4WinsTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "Connect4Pro", 78),
            new LeaderboardEntry(2, "GameQueen", 65),
            new LeaderboardEntry(3, "FourInARow", 58),
            new LeaderboardEntry(4, "JohnMaster", 52),
            new LeaderboardEntry(5, "ChampionX", 45)
        ));
        c4BestTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "Connect4Pro", 87),
            new LeaderboardEntry(2, "FourInARow", 82),
            new LeaderboardEntry(3, "GameQueen", 79),
            new LeaderboardEntry(4, "ChampionX", 74),
            new LeaderboardEntry(5, "JohnMaster", 72)
        ));
        connect4MostGamesTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "Connect4Pro", 112),
            new LeaderboardEntry(2, "GameQueen", 98),
            new LeaderboardEntry(3, "JohnMaster", 87),
            new LeaderboardEntry(4, "FourInARow", 78),
            new LeaderboardEntry(5, "BoardGamePro", 65)
        ));
        
        // Sample data for Checkers
        checkersLevelTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "CheckersMaster", 31),
            new LeaderboardEntry(2, "JohnMaster", 29),
            new LeaderboardEntry(3, "GameQueen", 27),
            new LeaderboardEntry(4, "ChampionX", 24),
            new LeaderboardEntry(5, "BoardGamePro", 22)
        ));
        checkersWinsTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "CheckersMaster", 95),
            new LeaderboardEntry(2, "GameQueen", 82),
            new LeaderboardEntry(3, "JohnMaster", 76),
            new LeaderboardEntry(4, "ChampionX", 61),
            new LeaderboardEntry(5, "BoardGamePro", 58)
        ));
        checkersBestTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "CheckersMaster", 92),
            new LeaderboardEntry(2, "GameQueen", 87),
            new LeaderboardEntry(3, "JohnMaster", 83),
            new LeaderboardEntry(4, "ChampionX", 78),
            new LeaderboardEntry(5, "BoardGamePro", 75)
        ));
        checkersMostGamesTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "CheckersMaster", 134),
            new LeaderboardEntry(2, "GameQueen", 121),
            new LeaderboardEntry(3, "JohnMaster", 104),
            new LeaderboardEntry(4, "BoardGamePro", 93),
            new LeaderboardEntry(5, "ChampionX", 85)
        ));
        
        // Sample data for Whist
        whistLevelTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "WhistPro", 28),
            new LeaderboardEntry(2, "CardShark", 26),
            new LeaderboardEntry(3, "GameQueen", 24),
            new LeaderboardEntry(4, "JohnMaster", 22),
            new LeaderboardEntry(5, "ChampionX", 20)
        ));
        whistWinsTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "WhistPro", 87),
            new LeaderboardEntry(2, "CardShark", 74),
            new LeaderboardEntry(3, "GameQueen", 69),
            new LeaderboardEntry(4, "JohnMaster", 62),
            new LeaderboardEntry(5, "ChampionX", 58)
        ));
        whistBestTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "WhistPro", 91),
            new LeaderboardEntry(2, "CardShark", 86),
            new LeaderboardEntry(3, "GameQueen", 82),
            new LeaderboardEntry(4, "JohnMaster", 78),
            new LeaderboardEntry(5, "ChampionX", 73)
        ));
        whistMostGamesTable.setItems(FXCollections.observableArrayList(
            new LeaderboardEntry(1, "WhistPro", 105),
            new LeaderboardEntry(2, "CardShark", 97),
            new LeaderboardEntry(3, "GameQueen", 89),
            new LeaderboardEntry(4, "JohnMaster", 76),
            new LeaderboardEntry(5, "ChampionX", 68)
        ));
    }

    /**
     * Navigate to the Dashboard screen.
     */
    private void navigateToDashboard() {
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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not navigate to dashboard: " + e.getMessage());
        }
    }

    /**
     * Navigate to the Games screen.
     */
    private void navigateToGames() {
        try {
            // Use the ScreenManager to navigate
            GameLibraryController controller = (GameLibraryController)
                    screenManager.navigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);
            
            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not navigate to game library: " + e.getMessage());
        }
    }

    /**
     * Navigate to the Settings screen.
     */
    private void navigateToSettings() {
        // Don't allow guests to access settings
        if (isGuest) {
            showAlert(Alert.AlertType.INFORMATION, "Not Available", "Settings are not available for guest users");
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
            showAlert(Alert.AlertType.ERROR, "Error loading Settings", e.getMessage());
        }
    }

    /**
     * Sign out the current user and return to the login screen.
     */
    private void signOut() {
        try {
            // Use the ScreenManager to navigate to login
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error logging out", e.getMessage());
        }
    }

    /**
     * Show an alert dialog.
     * 
     * @param alertType The type of alert
     * @param title The alert title
     * @param message The message to display
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Inner class representing a leaderboard entry.
     */
    public static class LeaderboardEntry {
        private final int rank;
        private final String playerName;
        private final int value;
        
        public LeaderboardEntry(int rank, String playerName, int value) {
            this.rank = rank;
            this.playerName = playerName;
            this.value = value;
        }
        
        public int getRank() {
            return rank;
        }
        
        public String getPlayerName() {
            return playerName;
        }
        
        public int getValue() {
            return value;
        }
    }
} 