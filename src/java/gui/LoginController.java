package gui;

import networking.Backend;

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

    //Backend
    private Backend backend = Backend.getInstance();

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
            showAlert("Login Error", "Please enter both username and password");
            return;
        }

        if (db.verifyCredentials(username, password)) {
            System.out.println("Login successful!");
            switchToDashboard(event);
        } else {
            showAlert("Login Error", "Invalid username or password");
        }
    }

    private void openRegisterScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/register.css").toExternalForm());
            
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open register screen: " + e.getMessage());
        }
    }

    private void handleForgotPassword(ActionEvent event) {
        // Implement forgot password functionality
        showAlert("Info", "Forgot password functionality not implemented yet");
    }

    private void switchToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/dashboard.css").toExternalForm());
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open dashboard: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}