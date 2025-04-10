package gui.controllers.systems;

import gui.ScreenManager;
import gui.controllers.games.GameLibraryController;
import gui.controllers.statistics.LeaderboardController;
import networking.IO.DatabaseIOHandler;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SettingController {
    @FXML
    private VBox sidebar;
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

    // Profile Information Fields
    @FXML
    private ImageView profileAvatar;
    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button changePasswordBtn;

    // Toggle Elements
    @FXML
    private HBox toggleSwitch;
    @FXML
    private Pane toggleThumb;
    @FXML
    private Pane chatToggleThumb;

    private boolean isDarkTheme = false;
    private boolean isChatEnabled = true;
    private boolean isEditingPassword = false;

    // Current user info
    private String currentUsername;

    // Database IO Handler
    private DatabaseIOHandler db = DatabaseIOHandler.getInstance();

    // ScreenManager instance
    private ScreenManager screenManager = ScreenManager.getInstance();

    @FXML
    public void initialize() {
        // Set up button event handlers
        dashboardBtn.setOnAction(event -> navigateToDashboard());
        gamesBtn.setOnAction(event -> navigateToGameLibrary());
        leaderboardBtn.setOnAction(event -> navigateToLeaderboard());
        settingsBtn.getStyleClass().add("selected"); // Mark current button as selected
        signOutBtn.setOnAction(event -> signOut());
        changePasswordBtn.setOnAction(event -> handlePasswordChange());

        // Set up toggle handlers
        setupToggleHandlers();
    }

    /**
     * Set the current user for this controller
     * @param username The username of the current user
     */
    public void setCurrentUser(String username) {
        this.currentUsername = username;

        // Set the username field
        usernameField.setText(username);

        // Try to get and set the full name from database
        String fullName = db.getUserFullName(username);
        if (fullName != null && !fullName.isEmpty()) {
            nameField.setText(fullName);
        }
    }

    /**
     * Handle password change button click
     */
    private void handlePasswordChange() {
        if (!isEditingPassword) {
            // Enable password editing
            passwordField.setDisable(false);
            passwordField.setPromptText("Enter new password");
            changePasswordBtn.setText("Save Password");
            isEditingPassword = true;
        } else {
            // Save new password
            String newPassword = passwordField.getText();
            if (newPassword == null || newPassword.trim().isEmpty()) {
                showAlert(AlertType.WARNING, "Warning", "Password cannot be empty");
                return;
            }

            // In a real implementation, validate password complexity

            // Update password in database
            boolean success = db.updatePassword(currentUsername, "current_password", newPassword);
            if (success) {
                showAlert(AlertType.INFORMATION, "Success", "Password updated successfully");

                // Reset UI
                passwordField.clear();
                passwordField.setDisable(true);
                passwordField.setPromptText("********");
                changePasswordBtn.setText("Change Password");
                isEditingPassword = false;
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update password");
            }
        }
    }

    private void setupToggleHandlers() {
        // Theme toggle
        HBox themeToggle = (HBox) toggleThumb.getParent();
        themeToggle.setOnMouseClicked(event -> {
            isDarkTheme = !isDarkTheme;
            updateThemeToggle();
            // In a real implementation, this would apply theme changes
        });

        // Chat toggle
        HBox chatToggle = (HBox) chatToggleThumb.getParent();
        chatToggle.setOnMouseClicked(event -> {
            isChatEnabled = !isChatEnabled;
            updateChatToggle();
            // In a real implementation, this would update chat settings
        });

        // Initialize toggle positions
        updateThemeToggle();
        updateChatToggle();
    }

    private void updateThemeToggle() {
        HBox toggleSwitch = (HBox) toggleThumb.getParent();
        Scene scene = toggleSwitch.getScene(); // Get the current scene

        if (scene != null) {
            if (isDarkTheme) {
                toggleThumb.setTranslateX(20);
                toggleSwitch.getStyleClass().add("active");

                // Apply dark theme
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            } else {
                toggleThumb.setTranslateX(0);
                toggleSwitch.getStyleClass().remove("active");

                // Apply light theme
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("/css/setting.css").toExternalForm());
            }
        }
    }
    private void updateChatToggle() {
        HBox toggleSwitch = (HBox) chatToggleThumb.getParent();
        if (isChatEnabled) {
            chatToggleThumb.setTranslateX(20);
            toggleSwitch.getStyleClass().add("active");
        } else {
            chatToggleThumb.setTranslateX(0);
            toggleSwitch.getStyleClass().remove("active");
        }
    }

    @FXML
    private void navigateToDashboard() {
        try {
            // Use the ScreenManager to navigate
            DashboardController controller = (DashboardController)
                    screenManager.navigateTo(ScreenManager.DASHBOARD_SCREEN, ScreenManager.DASHBOARD_CSS);

            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, false); // Not a guest since settings are for registered users
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not navigate to dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToGameLibrary() {
        try {
            // Use the ScreenManager to navigate
            GameLibraryController controller = (GameLibraryController)
                    screenManager.navigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);

            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, false); // Not a guest since settings are for registered users
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not navigate to game library: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToLeaderboard() {
        try {
            // Use the ScreenManager to navigate
            LeaderboardController controller = (LeaderboardController)
                    screenManager.navigateTo(ScreenManager.LEADERBOARD_SCREEN, ScreenManager.LEADERBOARD_CSS);

            // Set current user in the controller if we got one back
            if (controller != null) {
                controller.setCurrentUser(currentUsername, false); // Not a guest since settings are for registered users
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not navigate to leaderboard: " + e.getMessage());
        }
    }

    @FXML
    private void signOut() {
        try {
            // Use the ScreenManager to navigate to login
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not sign out: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}