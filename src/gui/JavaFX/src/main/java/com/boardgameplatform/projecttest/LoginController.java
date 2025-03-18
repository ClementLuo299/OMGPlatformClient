package com.boardgameplatform.projecttest;

import core.networking.DatabaseStub;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

    private static final DatabaseStub database = new DatabaseStub();

    @FXML
    public void initialize() {
        // Initialize your components here if needed

        // Set up event handlers
        loginButton.setOnAction(this::handleLogin);
        createAccountButton.setOnAction(this::openRegisterScreen);
        forgotPasswordLink.setOnAction(this::handleForgotPassword);
    }

    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            // Show an error message
            System.out.println("Please enter both email and password");
            return;
        }

        if (database.validateAccountPassword(username, password)) {
            System.out.println("Login successful!");
            switchToDashboard(event);
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

    private void switchToDashboard(ActionEvent event) {
        try {
            // Load Dashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boardgameplatform/projecttest/Dashboard.fxml"));
            Parent dashboardView = loader.load();

            // Pass user data to DashboardController
            DashboardController dashboardController = loader.getController();


            // Set up the new scene
            Scene dashboardScene = new Scene(dashboardView);
            dashboardScene.getStylesheets().add(getClass().getResource("/com/boardgameplatform/projecttest/dashboard.css").toExternalForm());

            // Get the current stage
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Switch to the Dashboard scene
            stage.setTitle("OMG Platform - Dashboard");
            stage.setScene(dashboardScene);
            stage.show();

            System.out.println("Switched to Dashboard screen!");

        } catch (Exception e) {
            System.out.println("Error loading Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

}