package com.gui_controllers;

import com.viewmodels.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * GUI controller for login screen - delegates all logic to ViewModel
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class LoginController {

    @FXML private VBox mainForm;
    @FXML private TextField usernameField, guestUsernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Button loginButton, createAccountButton, guestLoginButton;

    /**
     * Sets the view model and binds all UI components
     */
    public void setViewModel(LoginViewModel viewModel) {
        if (viewModel == null) {
            return;
        }
        
        // Bind properties and actions
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());
        viewModel.guestUsernameProperty().bindBidirectional(guestUsernameField.textProperty());
        viewModel.rememberMeProperty().bindBidirectional(rememberMe.selectedProperty());
        
        // Set up action handlers
        loginButton.setOnAction(e -> viewModel.handleLogin());
        guestLoginButton.setOnAction(e -> viewModel.handleGuestLogin());
        createAccountButton.setOnAction(e -> viewModel.handleCreateAccount());
        forgotPasswordLink.setOnAction(e -> viewModel.handleForgotPassword());
        
        // Ensure text fields lose focus when clicking outside
        mainForm.setOnMouseClicked(e -> mainForm.requestFocus());
        
        // Set up field focus logging
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> viewModel.onFieldFocus("Username", newVal));
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> viewModel.onFieldFocus("Password", newVal));
        guestUsernameField.focusedProperty().addListener((obs, oldVal, newVal) -> viewModel.onFieldFocus("Guest username", newVal));
    }
}