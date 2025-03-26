package main.java.gui;

import main.java.networking.IO.DatabaseIOHandler;

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
import javafx.scene.control.Alert;

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

    //Database IO Handler
    private DatabaseIOHandler db = DatabaseIOHandler.getInstance();

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

        if (db.isAccountExists(username)) {
            showAlert("Registration Error", "Username already exists");
            return;
        }

        // Register the account
        db.RegisterAccount(username, password);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}