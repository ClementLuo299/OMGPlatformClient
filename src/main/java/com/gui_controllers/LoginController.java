package com.gui_controllers;

import com.viewmodels.LoginViewModel;
import com.utils.error_handling.Logging;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * GUI controller for login screen - delegates all logic to ViewModel
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class LoginController {

    @FXML private VBox mainForm;
    
    // Regular login form
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Button loginButton;
    @FXML private Button createAccountButton;

    // Guest login form
    @FXML private TextField guestUsernameField;
    @FXML private Button guestLoginButton;

    private LoginViewModel viewModel;

    @FXML
    public void initialize() {
        Logging.info("LoginController initialized");
        if (rememberMe != null) {
            Logging.info("RememberMe checkbox found and initialized");
        } else {
            Logging.warning("RememberMe checkbox is null during initialization");
        }
    }

    /**
     * Sets the view model and binds UI components
     */
    public void setViewModel(LoginViewModel viewModel) {
        this.viewModel = viewModel;
        if (viewModel != null) {
            bindViewModel();
        }
    }

    /**
     * Binds UI components to the view model
     */
    private void bindViewModel() {
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());
        viewModel.guestUsernameProperty().bindBidirectional(guestUsernameField.textProperty());
        viewModel.rememberMeProperty().bindBidirectional(rememberMe.selectedProperty());
    }

    // Event handlers - delegate to ViewModel
    @FXML private void onGuestLoginClicked() { viewModel.handleGuestLogin(); }
    @FXML private void onLoginClicked() { viewModel.handleLogin(); }
    @FXML private void onCreateAccountClicked() { viewModel.handleCreateAccount(); }
    @FXML private void onForgotPasswordClicked() { viewModel.handleForgotPassword(); }
    @FXML private void onRememberMeClicked() { 
        Logging.info("RememberMe checkbox clicked, selected: " + rememberMe.isSelected());
    }
}