package com.gui_controllers;

import com.viewmodels.GameLobbyViewModel;
import com.utils.error_handling.Logging;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Controller for the Game Lobby screen, handling UI interactions and binding to ViewModel.
 * Manages the display of game lobby features and navigation between screens.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels, Jason Bakajika
 * @date April 2, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class GameLobbyController {

    // ==================== FXML INJECTIONS ====================
    @FXML private Button backButton;
    @FXML private Label statusLabel;
    @FXML private ImageView playerAvatar;
    @FXML private Label playerName;
    @FXML private Button quickPlayButton;
    @FXML private Label queueStatusLabel;
    @FXML private ProgressBar queueProgressBar;
    @FXML private TextField playerIdField;
    @FXML private Button searchPlayerButton;
    @FXML private Button invitePlayerButton;
    @FXML private Label playerSearchResult;
    @FXML private ListView<String> publicMatchesList;
    @FXML private Button refreshMatchesButton;
    @FXML private TextField matchIdField;
    @FXML private Button searchMatchButton;
    @FXML private Button joinMatchButton;
    @FXML private Label matchSearchResult;
    @FXML private Button cancelButton;
    @FXML private Button viewRulesButton;
    @FXML private Label gameTitle;
    @FXML private VBox mainContainer;

    // ==================== DEPENDENCIES ====================
    private GameLobbyViewModel viewModel;

    // ==================== INITIALIZATION ====================
    @FXML
    public void initialize() {
        Logging.info("Initializing GameLobbyController");
        setupEventHandlers();
        Logging.info("GameLobbyController initialized successfully");
    }

    /**
     * Set the ViewModel for this controller.
     */
    public void setViewModel(GameLobbyViewModel viewModel) {
        Logging.info("Setting ViewModel for GameLobbyController");
        this.viewModel = viewModel;
        bindToViewModel();
    }

    // ==================== PRIVATE SETUP METHODS ====================
    private void setupEventHandlers() {
        if (backButton != null) {
            backButton.setOnAction(e -> {
                Logging.info("Back button clicked");
                viewModel.navigateBack();
            });
        }
        if (cancelButton != null) {
            cancelButton.setOnAction(e -> {
                Logging.info("Cancel button clicked");
                viewModel.navigateBack();
            });
        }
        if (quickPlayButton != null) {
            quickPlayButton.setOnAction(e -> {
                Logging.info("Quick Play button clicked");
                viewModel.toggleQueue();
            });
        }
        if (searchPlayerButton != null) {
            searchPlayerButton.setOnAction(e -> {
                Logging.info("Search Player button clicked");
                viewModel.searchPlayer(playerIdField.getText());
            });
        }
        if (invitePlayerButton != null) {
            invitePlayerButton.setOnAction(e -> {
                Logging.info("Invite Player button clicked");
                viewModel.invitePlayer();
            });
        }
        if (refreshMatchesButton != null) {
            refreshMatchesButton.setOnAction(e -> {
                Logging.info("Refresh Matches button clicked");
                viewModel.refreshPublicMatches();
            });
        }
        if (searchMatchButton != null) {
            searchMatchButton.setOnAction(e -> {
                Logging.info("Search Match button clicked");
                viewModel.searchMatch(matchIdField.getText());
            });
        }
        if (joinMatchButton != null) {
            joinMatchButton.setOnAction(e -> {
                Logging.info("Join Match button clicked");
                viewModel.joinMatch();
            });
        }
        if (viewRulesButton != null) {
            viewRulesButton.setOnAction(e -> {
                Logging.info("View Rules button clicked");
                viewModel.viewRules();
            });
        }
    }

    private void bindToViewModel() {
        if (viewModel == null) {
            Logging.warning("Cannot bind to null ViewModel");
            return;
        }
        Logging.info("Binding controller to ViewModel");
        // Bindings for UI properties
        if (gameTitle != null) gameTitle.textProperty().bind(viewModel.gameNameProperty().concat(" - Game Lobby"));
        if (playerName != null) playerName.textProperty().bind(viewModel.playerNameProperty());
        if (queueStatusLabel != null) queueStatusLabel.textProperty().bind(viewModel.queueStatusProperty());
        if (playerSearchResult != null) playerSearchResult.textProperty().bind(viewModel.playerSearchResultProperty());
        if (matchSearchResult != null) matchSearchResult.textProperty().bind(viewModel.matchSearchResultProperty());
        if (queueProgressBar != null) queueProgressBar.progressProperty().bind(viewModel.queueProgressProperty());
        if (publicMatchesList != null) publicMatchesList.setItems(viewModel.getPublicMatches());
        // Enable/disable buttons based on state (example)
        if (invitePlayerButton != null) invitePlayerButton.disableProperty().bind(viewModel.playerSearchResultProperty().isNotEqualTo("Player found: " + playerIdField.getText()));
        if (joinMatchButton != null) joinMatchButton.disableProperty().bind(viewModel.matchSearchResultProperty().isNotEqualTo("Match found: " + viewModel.getGameName() + " - " + matchIdField.getText()));
    }
} 