package com.gui_controllers;

import com.viewmodels.DashboardViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * GUI controller for dashboard screen - delegates all logic to ViewModel
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels, Jason Bakajika, Zaman Dogar
 * @date March 18, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class DashboardController {

    // ==================== FXML INJECTIONS ====================
    
    // Sidebar navigation
    @FXML private VBox sidebar;
    @FXML private Button dashboardBtn;
    @FXML private Button gamesBtn;
    @FXML private Button leaderboardBtn;
    @FXML private Button settingsBtn;
    @FXML private Button signOutBtn;
    
    // Main content area
    @FXML private Label usernameLabel;
    @FXML private Label playerLevel;
    @FXML private Label totalGames;
    @FXML private Label winRate;
    @FXML private Label currentRank;
    @FXML private Label bestGame;
    @FXML private ListView<String> activityList;
    
    // Dynamic games list
    @FXML private ListView<GameCard> gamesListView;

    /**
     * Sets the view model and binds all UI components
     */
    public void setViewModel(DashboardViewModel viewModel) {
        if (viewModel == null) {
            return;
        }
        
        // Bind properties
        bindProperties(viewModel);
        
        // Set up action handlers
        setupActionHandlers(viewModel);
        
        // Set up navigation button styling
        setupNavigationStyling();
        
        // Initialize activity list
        initializeActivityList(viewModel);
        
        // Initialize games list
        initializeGamesList(viewModel);
    }
    
    /**
     * Binds ViewModel properties to UI components
     */
    private void bindProperties(DashboardViewModel viewModel) {
        usernameLabel.textProperty().bind(viewModel.usernameProperty());
        playerLevel.textProperty().bind(viewModel.playerLevelProperty());
        totalGames.textProperty().bind(viewModel.totalGamesProperty());
        winRate.textProperty().bind(viewModel.winRateProperty());
        currentRank.textProperty().bind(viewModel.currentRankProperty());
        bestGame.textProperty().bind(viewModel.bestGameProperty());
    }
    
    /**
     * Sets up action handlers for buttons
     */
    private void setupActionHandlers(DashboardViewModel viewModel) {
        // Navigation buttons
        gamesBtn.setOnAction(e -> viewModel.handleGamesNavigation());
        leaderboardBtn.setOnAction(e -> viewModel.handleLeaderboardNavigation());
        settingsBtn.setOnAction(e -> viewModel.handleSettingsNavigation());
        signOutBtn.setOnAction(e -> viewModel.handleSignOut());
    }
    
    /**
     * Sets up navigation button styling
     */
    private void setupNavigationStyling() {
        // Mark dashboard button as selected by default
        dashboardBtn.getStyleClass().add("selected");
        
        // Remove selected class from other buttons
        gamesBtn.getStyleClass().remove("selected");
        leaderboardBtn.getStyleClass().remove("selected");
        settingsBtn.getStyleClass().remove("selected");
    }
    
    /**
     * Initializes the activity list with data from ViewModel
     */
    private void initializeActivityList(DashboardViewModel viewModel) {
        activityList.setItems(viewModel.getActivityList());
    }
    
    /**
     * Initializes the games list with dynamic game cards
     */
    private void initializeGamesList(DashboardViewModel viewModel) {
        gamesListView.setItems(viewModel.getGamesList());
        
        // Set cell factory to create GameCard components
        gamesListView.setCellFactory(listView -> new ListCell<GameCard>() {
            @Override
            protected void updateItem(GameCard item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });
    }
}
