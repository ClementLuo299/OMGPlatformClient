package com.gui_controllers;

import com.viewmodels.LoginViewModel;
import com.core.ViewModelInjectable;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * GUI controller for login screen
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 * @edited June 1, 2025
 * @since 1.0
 */
public class LoginController implements ViewModelInjectable<LoginViewModel> {

    public VBox mainForm;
    //Regular login form
    @FXML private TextField usernameField; // Username text field
    @FXML private PasswordField passwordField; // Password text field
    @FXML private CheckBox rememberMe; // "Remember Me" checkbox
    @FXML private Hyperlink forgotPasswordLink; // "Forgot Password" link
    @FXML private Button loginButton; // Login button
    @FXML private Button createAccountButton; // Create account button

    //Guest login form
    @FXML private TextField guestUsernameField; // Guest username field
    @FXML private Button guestLoginButton; // Guest login button

    private LoginViewModel viewModel;  // View model

    @FXML
    public void initialize() {}

    /**
     * Sets the viewmodel for the page
     *
     * @param viewModel the viewmodel
     */
    public void setViewModel(LoginViewModel viewModel) {
        this.viewModel = viewModel;
        bindViewModel();
    }

    /**
     * Binds UI parts to the viewmodel
     */
    private void bindViewModel() {
        //Stop if there is no viewmodel
        if(viewModel == null) return;

        //Bind UI components to view model
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());
        viewModel.guestUsernameProperty().bindBidirectional(guestUsernameField.textProperty());
    }

    /**
     * Connect event handlers
     */
    @FXML private void onGuestLoginClicked() { viewModel.handleGuestLogin(); }
    @FXML private void onLoginClicked() { viewModel.handleLogin(); }
    @FXML private void onCreateAccountClicked() { viewModel.handleCreateAccount(); }
    @FXML private void onForgotPasswordClicked() { viewModel.handleForgotPassword(); }
}