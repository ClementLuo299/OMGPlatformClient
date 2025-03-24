package gui;

import core.networking.DatabaseStub;
import core.networking.IO.DatabaseIOHandler;
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

    //Database IO Handler
    private DatabaseIOHandler db = DatabaseIOHandler.getInstance();

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

        if (db.CheckAccountExists(username)) {
            System.out.println("Login successful!");
            switchToDashboard(event);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private void openRegisterScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/register.css").toExternalForm());
            
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleForgotPassword(ActionEvent event) {
        // Implement forgot password functionality
    }

    private void switchToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}