package gui;

import networking.IO.DatabaseIOHandler;

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
    
    @FXML
    private TextField usernameField1; // Guest username field
    
    @FXML
    private Button loginButton1; // Guest login button

    //Database IO Handler
    private DatabaseIOHandler db = DatabaseIOHandler.getInstance();

    @FXML
    public void initialize() {
        // Initialize your components here if needed

        // Set up event handlers
        loginButton.setOnAction(this::handleLogin);
        createAccountButton.setOnAction(this::openRegisterScreen);
        forgotPasswordLink.setOnAction(this::handleForgotPassword);
        
        // Set up guest login handler
        loginButton1.setOnAction(this::handleGuestLogin);
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
            switchToDashboard(event, username, false);
        } else {
            showAlert("Login Error", "Invalid username or password");
        }
    }
    
    private void handleGuestLogin(ActionEvent event) {
        String guestUsername = usernameField1.getText();
        
        // Validate guest username
        if (guestUsername.isEmpty()) {
            showAlert("Guest Login Error", "Please enter a guest username");
            return;
        }
        
        // Check if username already exists in database
        if (db.isAccountExists(guestUsername)) {
            showAlert("Guest Login Error", "This username is already taken. Please choose another one.");
            return;
        }
        
        System.out.println("Guest login successful!");
        switchToDashboard(event, guestUsername, true);
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

    private void switchToDashboard(ActionEvent event, String username, boolean isGuest) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/dashboard.css").toExternalForm());
            
            // Get the controller and set the current user
            DashboardController dashboardController = loader.getController();
            dashboardController.setCurrentUser(username, isGuest);
            
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