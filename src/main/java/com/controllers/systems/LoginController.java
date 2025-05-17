package com.controllers.systems;

import com.core.ScreenManager;
import com.core.Services;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * GUI controller for login screen
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 */
public class LoginController {

    @FXML
    private TextField usernameField; // Username text field

    @FXML
    private PasswordField passwordField; // Password text field

    @FXML
    private CheckBox rememberMe; // "Remember Me" checkbox

    @FXML
    private Hyperlink forgotPasswordLink; // "Forgot Password" link

    @FXML
    private Button loginButton; // Login button

    @FXML
    private Button createAccountButton; // Create account button
    
    @FXML
    private TextField guestUsernameField; // Guest username field
    
    @FXML
    private Button guestLoginButton; // Guest login button
    
    // Screen manager for navigation
    private ScreenManager screenManager = ScreenManager.getInstance();

    // View model
    private final LoginViewModel viewModel = new LoginViewModel();

    @FXML
    public void initialize() {
        //Bind UI components to view model
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());

        // Set up event handlers
        loginButton.setOnAction(this::loginButtonPressed);
        createAccountButton.setOnAction(this::createAccountButtonPressed);
        forgotPasswordLink.setOnAction(this::forgotPasswordButtonPressed);
        guestLoginButton.setOnAction(this::guestLoginButtonPressed);
    }

    private void loginButtonPressed(ActionEvent event) {
        viewModel.login();

        if (Services.db().verifyCredentials(username, password)) {
            System.out.println("Login successful!");
            switchToDashboard(username, false);
        } else {
            showAlert("Login Error", "Invalid username or password");
        }
    }
    
    private void guestLoginButtonPressed(ActionEvent event) {
        String guestUsername = guestUsernameField.getText();
        
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

    private void createAccountButtonPressed(ActionEvent event) {
        try {
            // Use ScreenManager to navigate to register screen
            screenManager.navigateTo(ScreenManager.REGISTER_SCREEN, ScreenManager.REGISTER_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open register screen: " + e.getMessage());
        }
    }

    private void forgotPasswordButtonPressed(ActionEvent event) {
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