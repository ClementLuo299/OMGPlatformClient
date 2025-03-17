package com.boardgameplatform.projecttest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // Initialize your components here if needed

        // Set up event handlers
        createAccountButton.setOnAction(this::handleRegistration);
        loginButton.setOnAction(this::backToLogin);
    }

    private void handleRegistration(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        LocalDate dob = dobPicker.getValue();

        // Simple validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob == null) {
            System.out.println("Please fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords don't match");
            return;
        }

        // In a real app, save to database here
        System.out.println("Registration successful!");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Date of Birth: " + dob);

        // Navigate back to login screen after successful registration
        backToLogin(event);
    }

    private void backToLogin(ActionEvent event) {
        try {
            // Load the login FXML
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/boardgameplatform/projecttest/Login.fxml"));
            Scene loginScene = new Scene(loginView);
            loginScene.getStylesheets().add(getClass().getResource("/com/boardgameplatform/projecttest/login.css").toExternalForm());

            // Get the stage
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Set the new scene
            stage.setTitle("OMG Platform");
            stage.setScene(loginScene);

        } catch (Exception e) {
            System.out.println("Error loading login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}