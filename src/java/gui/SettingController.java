package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private TextField emailField;
    
    // Toggle Elements
    @FXML
    private HBox toggleSwitch;
    @FXML
    private Pane toggleThumb;
    @FXML
    private Pane chatToggleThumb;
    
    private boolean isDarkTheme = false;
    private boolean isChatEnabled = true;

    @FXML
    public void initialize() {
        // Set up button event handlers
        dashboardBtn.setOnAction(event -> navigateToDashboard());
        gamesBtn.setOnAction(event -> navigateToGameLibrary());
        signOutBtn.setOnAction(event -> signOut());
        
        // Set up toggle handlers
        setupToggleHandlers();
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
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/dashboard.css").toExternalForm());
            
            Stage stage = (Stage) dashboardBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not navigate to dashboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void navigateToGameLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/GameLibrary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/library.css").toExternalForm());
            
            Stage stage = (Stage) gamesBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not navigate to game library: " + e.getMessage());
        }
    }
    
    @FXML
    private void signOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/login.css").toExternalForm());
            
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not sign out: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 