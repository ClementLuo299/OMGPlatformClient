package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.ServiceManager;
import com.core.screens.ScreenManager;
//import com.gui_controllers.DashboardController;
import com.services.LoginService;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;
import javafx.beans.property.*;

/**
 * Manages GUI state and logic for the login screen
 *
 * @authors Clement Luo
 * @date May 17, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class LoginViewModel {

    // ==================== PROPERTIES ====================
    
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty guestUsername = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    // ==================== DEPENDENCIES ====================
    
    private final ServiceManager serviceManager;
    private final ScreenManager screenManager;

    // ==================== CONSTRUCTOR ====================
    
    public LoginViewModel(ServiceManager serviceManager, ScreenManager screenManager) {
        this.serviceManager = serviceManager;
        this.screenManager = screenManager;
    }

    // ==================== PROPERTY ACCESSORS ====================
    
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty guestUsernameProperty() { return guestUsername; }
    public StringProperty messageProperty() { return message; }

    // ==================== PUBLIC ACTION HANDLERS ====================
    
    /**
     * Handles regular user login attempt
     */
    public void handleLogin(boolean rememberMeValue) {
        String usernameValue = username.get();
        String passwordValue = password.get();
        
        if (usernameValue == null || usernameValue.trim().isEmpty()) {
            showError("Login Error", "Username is required");
            return;
        }
        
        if (passwordValue == null || passwordValue.trim().isEmpty()) {
            showError("Login Error", "Password is required");
            return;
        }
        
        LoginService loginService = serviceManager.getLoginService();
        if (loginService.login(usernameValue, passwordValue)) {
            message.set("Login successful!");
            Logging.info("User logged in successfully: " + usernameValue + " (Remember me: " + rememberMeValue + ")");
            
            // Handle remember me functionality
            if (rememberMeValue) {
                Logging.info("Remember me enabled for user: " + usernameValue);
                // TODO: Implement remember me functionality (save credentials securely)
            }
            
            navigateToDashboard(usernameValue, false);
        } else {
            message.set("Login failed!");
            showError("Login Error", "Invalid username or password");
        }
    }

    /**
     * Handles guest login attempt
     */
    public void handleGuestLogin() {
        String guestUsernameValue = guestUsername.get();
        
        if (guestUsernameValue == null || guestUsernameValue.trim().isEmpty()) {
            showError("Guest Login Error", "Guest username is required");
            return;
        }
        
        LoginService loginService = serviceManager.getLoginService();
        if (loginService.guestLogin(guestUsernameValue)) {
            message.set("Guest login successful!");
            Logging.info("Guest logged in successfully: " + guestUsernameValue);
            navigateToDashboard(guestUsernameValue, true);
        } else {
            message.set("Guest login failed!");
            showError("Guest Login Error", "Guest login failed");
        }
    }

    /**
     * Handles navigation to registration screen
     */
    public void handleCreateAccount() {
        try {
            screenManager.navigateTo(ScreenRegistry.REGISTER);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open registration screen: " + e.getMessage(), e);
        }
    }

    /**
     * Handles forgot password functionality
     */
    public void handleForgotPassword() {
        showInfo("Info", "Forgot password functionality not implemented yet");
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Navigates to the dashboard screen
     */
    private void navigateToDashboard(String username, boolean isGuest) {
        try {
            // TODO: Navigate to dashboard when available
            Logging.info("Would navigate to dashboard for user: " + username + " (guest: " + isGuest + ")");
            showInfo("Success", "Login successful! Dashboard navigation not implemented yet.");
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open dashboard: " + e.getMessage(), e);
        }
    }

    /**
     * Shows an error dialog
     */
    private void showError(String title, String message) {
        Dialog.showError(title, message, null);
    }

    /**
     * Shows an info dialog
     */
    private void showInfo(String title, String message) {
        Dialog.showInfo(title, message);
    }

}
