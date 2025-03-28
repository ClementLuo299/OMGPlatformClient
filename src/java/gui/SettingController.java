package gui;

import networking.IO.DatabaseIOHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import javafx.stage.Stage;

import java.io.IOException;

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
            boolean success = db.updatePassword(currentUsername, newPassword);
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
        if (isDarkTheme) {
            toggleThumb.setTranslateX(20);
            toggleSwitch.getStyleClass().add("active");
        } else {
            toggleThumb.setTranslateX(0);
            toggleSwitch.getStyleClass().remove("active");
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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/dashboard.css").toExternalForm());
            
            // Pass the current user to the dashboard
            DashboardController dashboardController = loader.getController();
            dashboardController.setCurrentUser(currentUsername, false); // Not a guest since settings are for registered users
            
            Stage stage = (Stage) dashboardBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not navigate to dashboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void navigateToGameLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/GameLibrary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/library.css").toExternalForm());
            
            // Pass the current user to the game library
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setCurrentUser(currentUsername, false); // Not a guest since settings are for registered users
            
            Stage stage = (Stage) gamesBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not navigate to game library: " + e.getMessage());
        }
    }
    
    @FXML
    private void navigateToLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Leaderboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/leaderboard.css").toExternalForm());
            
            // Pass current user information
            LeaderboardController leaderboardController = loader.getController();
            leaderboardController.setCurrentUser(currentUsername, false); // Not a guest since settings are for registered users
            
            Stage stage = (Stage) leaderboardBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not navigate to leaderboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void signOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/login.css").toExternalForm());
            
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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