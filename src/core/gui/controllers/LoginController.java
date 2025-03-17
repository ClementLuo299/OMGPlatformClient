package core.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import core.gui.GUIManager;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button createAccountButton;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private CheckBox rememberMe;

    @FXML
    public void initialize() {

    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Email or Password cannot be empty.");
            return;
        }

        // Simulated Login - No Game Logic
        if (email.equals("admin@example.com") && password.equals("password123")) {
            System.out.println("Login Successful!");
            showAlert("Login Successful", "GUI is working! Game logic is disabled.");
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }


    private void showForgotPasswordAlert() {
        showAlert("Forgot Password", "Password recovery is not implemented yet.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
