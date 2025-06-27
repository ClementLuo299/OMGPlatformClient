package com.gui_controllers;

import com.viewmodels.SettingViewModel;
import com.utils.error_handling.Logging;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller for the Settings screen, handling UI interactions and binding to ViewModel.
 * Manages the display of user settings and navigation between screens.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class SettingController {
    // ==================== FXML INJECTIONS ====================
    @FXML private VBox sidebar;
    @FXML private Button dashboardBtn;
    @FXML private Button gamesBtn;
    @FXML private Button leaderboardBtn;
    @FXML private Button settingsBtn;
    @FXML private Button signOutBtn;
    @FXML private ImageView profileAvatar;
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button changePasswordBtn;
    @FXML private HBox toggleSwitch;
    @FXML private Pane toggleThumb;
    @FXML private Pane chatToggleThumb;

    // ==================== DEPENDENCIES ====================
    private SettingViewModel viewModel;

    // ==================== INITIALIZATION ====================
    @FXML
    public void initialize() {
        Logging.info("Initializing SettingController");
        setupNavigationButtons();
        setupSettingsHandlers();
        Logging.info("SettingController initialized successfully");
    }

    /**
     * Set the ViewModel for this controller.
     */
    public void setViewModel(SettingViewModel viewModel) {
        Logging.info("Setting ViewModel for SettingController");
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
                viewModel.navigateToGameLibrary();
            });
        }
        if (leaderboardBtn != null) {
            leaderboardBtn.setOnAction(event -> {
                Logging.info("Leaderboard button clicked");
                viewModel.navigateToLeaderboard();
            });
        }
        if (settingsBtn != null) {
            settingsBtn.getStyleClass().add("selected");
        }
        if (signOutBtn != null) {
            signOutBtn.setOnAction(event -> {
                Logging.info("Sign out button clicked");
                viewModel.signOut();
            });
        }
    }

    private void setupSettingsHandlers() {
        if (changePasswordBtn != null) {
            changePasswordBtn.setOnAction(event -> {
                Logging.info("Change Password button clicked");
                if (viewModel.isEditingPassword()) {
                    viewModel.savePassword();
                } else {
                    viewModel.togglePasswordEditing();
                }
            });
        }
        if (toggleSwitch != null) {
            toggleSwitch.setOnMouseClicked(event -> {
                Logging.info("Theme toggle clicked");
                viewModel.toggleDarkTheme();
            });
        }
        if (chatToggleThumb != null) {
            HBox chatToggle = (HBox) chatToggleThumb.getParent();
            chatToggle.setOnMouseClicked(event -> {
                Logging.info("Chat toggle clicked");
                viewModel.toggleChatEnabled();
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
        if (usernameField != null) usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        if (nameField != null) nameField.textProperty().bindBidirectional(viewModel.fullNameProperty());
        if (passwordField != null) passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        if (changePasswordBtn != null) changePasswordBtn.disableProperty().bind(viewModel.isEditingPasswordProperty().not());
        // Theme and chat toggles can be bound to their respective properties if needed
    }
} 