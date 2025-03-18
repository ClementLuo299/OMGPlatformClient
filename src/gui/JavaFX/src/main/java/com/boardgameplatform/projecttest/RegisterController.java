package com.boardgameplatform.projecttest;

import core.networking.DatabaseStub;
import core.networking.accounts.UserAccountRegistration;
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
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

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

    private static final DatabaseStub database = new DatabaseStub();
    private final UserAccountRegistration registration = new UserAccountRegistration(database);

    @FXML
    public void initialize() {
        // Initialize your components here if needed

        // Set up event handlers
        createAccountButton.setOnAction(this::handleRegistration);
        loginButton.setOnAction(this::backToLogin);
    }

    private void handleRegistration(ActionEvent event) {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        LocalDate dob = dobPicker.getValue();

        // Simple validation
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob == null) {
            System.out.println("Please fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords don't match");
            return;
        }

        boolean success = registration.registerUser(fullName, username, password, confirmPassword, dob.toString());
        if (success) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Registration failed.");
        }

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