package com.gui_controllers;

import com.viewmodels.LeaderboardViewModel;
import com.utils.error_handling.Logging;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Controller for the Leaderboard screen, handling UI interactions and binding to ViewModel.
 * Manages the display of leaderboard data and navigation between screens.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 28, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class LeaderboardController {
    // ==================== FXML INJECTIONS ====================
    @FXML private Button dashboardBtn;
    @FXML private Button gamesBtn;
    @FXML private Button leaderboardBtn;
    @FXML private Button settingsBtn;
    @FXML private Button signOutBtn;
    @FXML private VBox sidebar;
    @FXML private TabPane leaderboardTabs;
    // ... (add FXML TableView fields for each leaderboard table as needed)

    // ==================== DEPENDENCIES ====================
    private LeaderboardViewModel viewModel;

    // ==================== INITIALIZATION ====================
    @FXML
    public void initialize() {
        Logging.info("Initializing LeaderboardController");
        setupNavigationButtons();
        Logging.info("LeaderboardController initialized successfully");
    }

    /**
     * Set the ViewModel for this controller.
     */
    public void setViewModel(LeaderboardViewModel viewModel) {
        Logging.info("Setting ViewModel for LeaderboardController");
        this.viewModel = viewModel;
        bindToViewModel();
    }

    // ==================== PRIVATE SETUP METHODS ====================
    private void setupNavigationButtons() {
        if (dashboardBtn != null) {
            dashboardBtn.setOnAction(event -> {
                Logging.info("Dashboard button clicked");
                viewModel.navigateToDashboard();
            });
        }
        if (gamesBtn != null) {
            gamesBtn.setOnAction(event -> {
                Logging.info("Games button clicked");
                viewModel.navigateToGames();
            });
        }
        if (leaderboardBtn != null) {
            leaderboardBtn.getStyleClass().add("selected");
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
    }

    private void bindToViewModel() {
        if (viewModel == null) {
            Logging.warning("Cannot bind to null ViewModel");
            return;
        }
        Logging.info("Binding controller to ViewModel");
        // Example: Bind guest status to settings button
        if (settingsBtn != null) {
            settingsBtn.disableProperty().bind(viewModel.isGuestProperty());
        }
        // Example: Bind leaderboard data to tables (add as needed)
        // accountLevelTable.setItems(viewModel.getGlobalLevelData());
        // mostWinsTable.setItems(viewModel.getGlobalWinsData());
        // mostGamesTable.setItems(viewModel.getGlobalGamesData());
    }
} 