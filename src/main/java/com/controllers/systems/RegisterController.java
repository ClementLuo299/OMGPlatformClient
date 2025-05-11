package com.controllers.systems;

import com.core.ScreenManager;
import com.core.Services;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    // Add ScreenManager instance at the beginning of the class
    private ScreenManager screenManager = ScreenManager.getInstance();

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

        // Validation
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob == null) {
            showAlert("Registration Error", "Please fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Registration Error", "Passwords don't match");
            return;
        }

        if (Services.db().isAccountExists(username)) {
            showAlert("Registration Error", "Username already exists");
            return;
        }

        // Format the date of birth as a string
        String dobString = dob.format(DateTimeFormatter.ISO_LOCAL_DATE); // Format: YYYY-MM-DD

        // Register the account with full name and date of birth
        Services.db().RegisterAccount(username, password, fullName, dobString);
        System.out.println("Account created successfully!");

        // Navigate back to login screen
        backToLogin(event);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void backToLogin(ActionEvent event) {
        try {
            // Use the ScreenManager to navigate to login
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not return to login screen: " + e.getMessage());
        }
    }
}