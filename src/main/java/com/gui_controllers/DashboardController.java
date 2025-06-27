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
 * @edited June 26, 2025
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
    
    // Game play buttons
    @FXML private Button playConnect4Btn;
    @FXML private Button playCheckersBtn;
    @FXML private Button playWhistBtn;
    @FXML private Button playTicTacToeBtn;

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
        
        // Game play buttons
        playConnect4Btn.setOnAction(e -> viewModel.handlePlayConnect4());
        playCheckersBtn.setOnAction(e -> viewModel.handlePlayCheckers());
        playWhistBtn.setOnAction(e -> viewModel.handlePlayWhist());
        playTicTacToeBtn.setOnAction(e -> viewModel.handlePlayTicTacToe());
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
}
