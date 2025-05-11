package com.controllers.systems;

import com.core.ScreenManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.core.Services;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;
    
    @FXML
    private TextField usernameField1; // Guest username field
    
    @FXML
    private Button loginButton1; // Guest login button
    
    // Screen manager for navigation
    private ScreenManager screenManager = ScreenManager.getInstance();

    @FXML
    public void initialize() {
        // Initialize your components here if needed

        // Set up event handlers
        loginButton.setOnAction(this::handleLogin);
        createAccountButton.setOnAction(this::openRegisterScreen);
        forgotPasswordLink.setOnAction(this::handleForgotPassword);
        
        // Set up guest login handler
        loginButton1.setOnAction(this::handleGuestLogin);
    }

    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both username and password");
            return;
        }

        if (Services.db().verifyCredentials(username, password)) {
            System.out.println("Login successful!");
            switchToDashboard(username, false);
        } else {
            showAlert("Login Error", "Invalid username or password");
        }
    }
    
    private void handleGuestLogin(ActionEvent event) {
        String guestUsername = usernameField1.getText();
        
        // Validate guest username
        if (guestUsername.isEmpty()) {
            showAlert("Guest Login Error", "Please enter a guest username");
            return;
        }
        
        // Check if username already exists in database
        if (Services.db().isAccountExists(guestUsername)) {
            showAlert("Guest Login Error", "This username is already taken. Please choose another one.");
            return;
        }
        
        System.out.println("Guest login successful!");
        switchToDashboard(guestUsername, true);
    }

    private void openRegisterScreen(ActionEvent event) {
        try {
            // Use ScreenManager to navigate to register screen
            screenManager.navigateTo(ScreenManager.REGISTER_SCREEN, ScreenManager.REGISTER_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open register screen: " + e.getMessage());
        }
    }

    private void handleForgotPassword(ActionEvent event) {
        // Implement forgot password functionality
        showAlert("Info", "Forgot password functionality not implemented yet");
    }

    private void switchToDashboard(String username, boolean isGuest) {
        try {
            // Always reload dashboard to ensure it's fresh for the new user
            DashboardController controller = (DashboardController)
                    screenManager.reloadAndNavigateTo(ScreenManager.DASHBOARD_SCREEN, ScreenManager.DASHBOARD_CSS);
            
            // Set the current user in the controller
            if (controller != null) {
                controller.setCurrentUser(username, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open dashboard: " + e.getMessage());
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