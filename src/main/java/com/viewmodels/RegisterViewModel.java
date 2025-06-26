package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.ServiceManager;
import com.core.screens.ScreenManager;
import com.network.IO.DatabaseIOHandler;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Manages GUI state and logic for the registration screen
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class RegisterViewModel {

    // ==================== PROPERTIES ====================
    
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty confirmPassword = new SimpleStringProperty();
    private final StringProperty dateOfBirth = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

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
    public StringProperty messageProperty() { return message; }

    // ==================== PUBLIC ACTION HANDLERS ====================
    
    /**
     * Handles user registration attempt
     */
    public void handleRegistration() {
        String fullNameValue = fullName.get();
        String usernameValue = username.get();
        String passwordValue = password.get();
        String confirmPasswordValue = confirmPassword.get();
        String dobValue = dateOfBirth.get();
        
        // Validate all required fields
        if (fullNameValue == null || fullNameValue.trim().isEmpty()) {
            showError("Registration Error", "Full name is required");
            return;
        }
        
        if (usernameValue == null || usernameValue.trim().isEmpty()) {
            showError("Registration Error", "Username is required");
            return;
        }
        
        if (passwordValue == null || passwordValue.trim().isEmpty()) {
            showError("Registration Error", "Password is required");
            return;
        }
        
        if (confirmPasswordValue == null || confirmPasswordValue.trim().isEmpty()) {
            showError("Registration Error", "Password confirmation is required");
            return;
        }
        
        if (dobValue == null || dobValue.trim().isEmpty()) {
            showError("Registration Error", "Date of birth is required");
            return;
        }
        
        // Validate password confirmation
        if (!passwordValue.equals(confirmPasswordValue)) {
            showError("Registration Error", "Passwords do not match");
            return;
        }
        
        // Validate password strength (basic validation)
        if (passwordValue.length() < 6) {
            showError("Registration Error", "Password must be at least 6 characters long");
            return;
        }
        
        // Validate date of birth format
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobValue, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            showError("Registration Error", "Invalid date of birth format");
            return;
        }
        
        // Check if user is old enough (13+ years old)
        LocalDate minDate = LocalDate.now().minusYears(13);
        if (dob.isAfter(minDate)) {
            showError("Registration Error", "You must be at least 13 years old to register");
            return;
        }
        
        // Attempt to register the account
        try {
            DatabaseIOHandler dbHandler = new DatabaseIOHandler();
            
            // Check if username already exists
            if (dbHandler.isAccountExists(usernameValue)) {
                showError("Registration Error", "Username already exists");
                return;
            }
            
            // Register the account
            String dobString = dob.format(DateTimeFormatter.ISO_LOCAL_DATE);
            dbHandler.RegisterAccount(usernameValue, passwordValue, fullNameValue, dobString);
            
            message.set("Account created successfully!");
            Logging.info("New account registered successfully: " + usernameValue);
            showInfo("Success", "Account created successfully! You can now log in.");
            
            // Navigate back to login screen
            navigateToLogin();
            
        } catch (Exception e) {
            Logging.error("Failed to register account: " + usernameValue, e);
            Dialog.showError("Registration Error", "Failed to create account: " + e.getMessage(), e);
        }
    }

    /**
     * Handles navigation back to login screen
     */
    public void handleBackToLogin() {
        try {
            navigateToLogin();
        } catch (Exception e) {
            Dialog.showError("Error", "Could not return to login screen: " + e.getMessage(), e);
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Navigates to the login screen
     */
    private void navigateToLogin() {
        try {
            screenManager.navigateTo(ScreenRegistry.LOGIN);
        } catch (Exception e) {
            Dialog.showError("Error", "Could not open login screen: " + e.getMessage(), e);
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
