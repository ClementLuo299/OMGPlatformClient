package com.gui_controllers;

import com.viewmodels.LoginViewModel;
import com.core.ScreenManager;
import com.core.Services;
import com.core.ViewModelInjectable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * GUI controller for login screen
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 */
public class LoginController implements ViewModelInjectable<LoginViewModel> {

    @FXML private TextField usernameField; // Username text field

    @FXML private PasswordField passwordField; // Password text field

    @FXML private CheckBox rememberMe; // "Remember Me" checkbox

    @FXML private Hyperlink forgotPasswordLink; // "Forgot Password" link

    @FXML private Button loginButton; // Login button

    @FXML private Button createAccountButton; // Create account button

    @FXML private TextField guestUsernameField; // Guest username field

    @FXML private Button guestLoginButton; // Guest login button

    private ScreenManager screenManager = ScreenManager.getInstance();  // Screen manager for navigation

    private LoginViewModel viewModel;  // View model

    @FXML
    public void initialize() {}

    /**
     *
     */
    public void setViewModel(LoginViewModel viewModel) {
        this.viewModel = viewModel;
        bindViewModel();
    }

    /**
     *
     */
    private void bindViewModel() {
        if(viewModel == null) return;

        //Bind UI components to view model
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());
        viewModel.guestUsernameProperty().bindBidirectional(guestUsernameField.textProperty());
    }

    @FXML
    private void onGuestLoginClicked() {
        viewModel.guestLogin(
                username -> {
                    System.out.println("Guest login successful!");
                    switchToDashboard(username, true);
                },
                errorMessage -> showAlert("Guest Login Error", errorMessage)
        );
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