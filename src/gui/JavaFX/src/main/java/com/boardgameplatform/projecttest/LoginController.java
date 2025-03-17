package com.boardgameplatform.projecttest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

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
    public void initialize() {
        // Initialize your components here if needed

        // Set up event handlers
        loginButton.setOnAction(this::handleLogin);
        createAccountButton.setOnAction(this::openRegisterScreen);
        forgotPasswordLink.setOnAction(this::handleForgotPassword);
    }

    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (email.isEmpty() || password.isEmpty()) {
            // Show an error message
            System.out.println("Please enter both email and password");
            return;
        }

        // For testing - in a real app, check against a database
        if (email.equals("admin@example.com") && password.equals("password")) {
            System.out.println("Login successful!");
            // Here you would navigate to the main game screen
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private void openRegisterScreen(ActionEvent event) {
        try {
            // Load the Register.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boardgameplatform/projecttest/Register.fxml"));
            Parent registerView = loader.load();

            // Set up the new scene for Register screen
            Scene registerScene = new Scene(registerView);
            registerScene.getStylesheets().add(getClass().getResource("/com/boardgameplatform/projecttest/register.css").toExternalForm());

            // Get the current stage (the window)
            Stage stage = (Stage) createAccountButton.getScene().getWindow();

            // Set the new scene to the stage (this will display the Register screen)
            stage.setTitle("OMG Platform");
            stage.setScene(registerScene);

        } catch (Exception e) {
            System.out.println("Error loading registration screen: " + e.getMessage());
            e.printStackTrace();  // To print the full stack trace for debugging
        }
    }

    private void handleForgotPassword(ActionEvent event) {
        // Implement forgot password functionality
        System.out.println("Forgot password link clicked");
    }
}