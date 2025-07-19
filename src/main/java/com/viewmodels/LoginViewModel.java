package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.ServiceManager;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;
import javafx.beans.property.*;

/**
 * Manages GUI state and logic for the login screen
 *
 * @authors Clement Luo
 * @date May 17, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class LoginViewModel {

    // ==================== PROPERTIES ====================
    
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty guestUsername = new SimpleStringProperty();
    private final BooleanProperty rememberMe = new SimpleBooleanProperty(false);

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
    public BooleanProperty rememberMeProperty() { return rememberMe; }

    // ==================== PUBLIC ACTION HANDLERS ====================
    
    /**
     * Handles regular user login attempt
     */
    public void handleLogin() {
        Logging.info("Login button clicked - Synchronous authentication");
        
        // Get values from UI components
        String usernameValue = username.get();
        String passwordValue = password.get();
        
        // Perform synchronous authentication
        boolean success = serviceManager.getLoginService().login(usernameValue, passwordValue);
        
        if (success) {
            // Login successful, navigate to dashboard
            navigateToDashboard();
        }
        // If login fails, LoginService will show error dialog automatically
    }

    /**
     * Handles guest login attempt
     */
    public void handleGuestLogin() {
        Logging.info("Guest login button clicked - Synchronous guest authentication");
        
        // Get values from UI components
        String guestUsernameValue = guestUsername.get();
        
        // Perform synchronous guest authentication
        boolean success = serviceManager.getLoginService().guestLogin(guestUsernameValue);
        
        if (success) {
            // Guest login successful, navigate to dashboard
            navigateToDashboard();
        }
        // If guest login fails, LoginService will show error dialog automatically
    }

    /**
     * Handles navigation to registration screen
     */
    public void handleCreateAccount() {
        Logging.info("Create account button clicked");
        
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
        Logging.info("Forgot password button clicked");
        
        Dialog.showInfo("Info", "Forgot password functionality not implemented yet");
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Navigates to the dashboard screen
     */
    private void navigateToDashboard() {
        try {
            screenManager.navigateTo(ScreenRegistry.DASHBOARD);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open dashboard: " + e.getMessage(), e);
        }
    }

    // ==================== UI LOGGING METHODS ====================
    
    /**
     * Handles field focus events
     */
    public void onFieldFocus(String fieldName, boolean focused) {
        Logging.info(fieldName + " field " + (focused ? "focused" : "lost focus"));
    }
    
    /**
     * Handles checkbox change events
     */
    public void onCheckboxChange(String checkboxName, boolean selected) {
        Logging.info(checkboxName + " checkbox " + (selected ? "selected" : "deselected"));
    }
}
