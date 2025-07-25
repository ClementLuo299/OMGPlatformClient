package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.ServiceManager;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;
import javafx.beans.property.*;

/**
 * Manages GUI state and logic for the registration screen
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class RegisterViewModel {

    // ==================== PROPERTIES ====================
    
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty confirmPassword = new SimpleStringProperty();
    private final StringProperty dateOfBirth = new SimpleStringProperty();

    // ==================== DEPENDENCIES ====================
    
    private final ServiceManager serviceManager;
    private final ScreenManager screenManager;

    // ==================== CONSTRUCTOR ====================
    
    public RegisterViewModel(ServiceManager serviceManager, ScreenManager screenManager) {
        this.serviceManager = serviceManager;
        this.screenManager = screenManager;
    }

    // ==================== PROPERTY ACCESSORS ====================
    
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty confirmPasswordProperty() { return confirmPassword; }
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }

    // ==================== PUBLIC ACTION HANDLERS ====================
    
    /**
     * Handles user registration attempt
     */
    public void handleRegistration() {
        Logging.info("Create account button clicked - Synchronous registration");
        
        // Get values from UI components
        String fullNameValue = fullName.get();
        String usernameValue = username.get();
        String passwordValue = password.get();
        String confirmPasswordValue = confirmPassword.get();
        String dobValue = dateOfBirth.get();
        
        // Perform synchronous registration
        boolean success = serviceManager.getLoginService().register(usernameValue, passwordValue, fullNameValue, dobValue, confirmPasswordValue);
        
        if (success) {
            // Registration successful, navigate to dashboard
            try {
                screenManager.navigateTo(ScreenRegistry.DASHBOARD);
            } catch (Exception e) {
                Logging.error("Could not navigate to dashboard: " + e.getMessage(), e);
            }
        }
        // If registration fails, LoginService will show error dialog automatically
    }

    /**
     * Handles navigation back to login screen
     */
    public void navigateToLogin() {
        Logging.info("Back to login button clicked");
        
        try {
            screenManager.navigateTo(ScreenRegistry.LOGIN);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not return to login screen: " + e.getMessage(), e);
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
     * Handles date picker change events
     */
    public void onDatePickerChange(String fieldName, String selectedDate) {
        Logging.info(fieldName + " date picker changed to: " + selectedDate);
    }
}
