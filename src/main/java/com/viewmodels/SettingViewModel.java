package com.viewmodels;

import com.config.ScreenRegistry;
import com.core.screens.ScreenManager;
import com.utils.error_handling.Logging;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel for the Settings screen, managing UI state and business logic.
 * Handles user profile settings, theme preferences, and navigation.
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class SettingViewModel {
    
    // ==================== PROPERTIES ====================
    
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final BooleanProperty isDarkTheme = new SimpleBooleanProperty(false);
    private final BooleanProperty isChatEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty isEditingPassword = new SimpleBooleanProperty(false);
    
    // ==================== DEPENDENCIES ====================
    
    private final ScreenManager screenManager;
    
    // ==================== CONSTRUCTOR ====================
    
    public SettingViewModel() {
        Logging.info("Initializing SettingViewModel");
        this.screenManager = ScreenManager.getInstance();
        Logging.info("SettingViewModel initialized successfully");
    }
    
    // ==================== PROPERTY ACCESSORS ====================
    
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { 
        Logging.info("Setting username to: " + username);
        this.username.set(username); 
    }
    public StringProperty usernameProperty() { return username; }
    
    public String getFullName() { return fullName.get(); }
    public void setFullName(String fullName) { 
        Logging.info("Setting full name to: " + fullName);
        this.fullName.set(fullName); 
    }
    public StringProperty fullNameProperty() { return fullName; }
    
    public String getPassword() { return password.get(); }
    public void setPassword(String password) { 
        Logging.info("Setting password (length: " + (password != null ? password.length() : 0) + ")");
        this.password.set(password); 
    }
    public StringProperty passwordProperty() { return password; }
    
    public boolean isDarkTheme() { return isDarkTheme.get(); }
    public void setDarkTheme(boolean darkTheme) { 
        Logging.info("Setting dark theme to: " + darkTheme);
        isDarkTheme.set(darkTheme); 
    }
    public BooleanProperty isDarkThemeProperty() { return isDarkTheme; }
    
    public boolean isChatEnabled() { return isChatEnabled.get(); }
    public void setChatEnabled(boolean chatEnabled) { 
        Logging.info("Setting chat enabled to: " + chatEnabled);
        isChatEnabled.set(chatEnabled); 
    }
    public BooleanProperty isChatEnabledProperty() { return isChatEnabled; }
    
    public boolean isEditingPassword() { return isEditingPassword.get(); }
    public void setEditingPassword(boolean editingPassword) { 
        Logging.info("Setting editing password to: " + editingPassword);
        isEditingPassword.set(editingPassword); 
    }
    public BooleanProperty isEditingPasswordProperty() { return isEditingPassword; }
    
    // ==================== NAVIGATION ACTIONS ====================
    
    /**
     * Navigate to the dashboard screen.
     */
    public void navigateToDashboard() {
        try {
            Logging.info("Navigating to dashboard from settings");
            screenManager.navigateTo(ScreenRegistry.DASHBOARD);
            Logging.info("Successfully navigated to dashboard");
        } catch (Exception e) {
            Logging.error("Failed to navigate to dashboard: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Navigate to the game library screen.
     */
    public void navigateToGameLibrary() {
        try {
            Logging.info("Navigating to game library from settings");
            screenManager.navigateTo(ScreenRegistry.GAME_LIBRARY);
            Logging.info("Successfully navigated to game library");
        } catch (Exception e) {
            Logging.error("Failed to navigate to game library: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Navigate to the leaderboard screen.
     */
    public void navigateToLeaderboard() {
        try {
            Logging.info("Navigating to leaderboard from settings");
            // TODO: Add LEADERBOARD to ScreenRegistry
            Logging.info("Leaderboard navigation not yet implemented");
        } catch (Exception e) {
            Logging.error("Failed to navigate to leaderboard: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Sign out and return to login screen.
     */
    public void signOut() {
        try {
            Logging.info("Signing out from settings");
            screenManager.navigateTo(ScreenRegistry.LOGIN);
            Logging.info("Successfully signed out");
        } catch (Exception e) {
            Logging.error("Failed to sign out: " + e.getMessage(), e);
            throw e;
        }
    }
    
    // ==================== SETTINGS ACTIONS ====================
    
    /**
     * Toggle password editing mode.
     */
    public void togglePasswordEditing() {
        if (isEditingPassword()) {
            Logging.info("Disabling password editing mode");
            setEditingPassword(false);
        } else {
            Logging.info("Enabling password editing mode");
            setEditingPassword(true);
        }
    }
    
    /**
     * Save the new password.
     */
    public void savePassword() {
        try {
            Logging.info("Saving new password");
            
            String newPassword = getPassword();
            if (newPassword == null || newPassword.trim().isEmpty()) {
                Logging.warning("Password cannot be empty");
                // TODO: Show warning dialog
                return;
            }
            
            // TODO: Implement actual password update logic
            Logging.info("Password saved successfully");
            setEditingPassword(false);
            setPassword(""); // Clear password field
            
        } catch (Exception e) {
            Logging.error("Failed to save password: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Toggle dark theme setting.
     */
    public void toggleDarkTheme() {
        boolean newTheme = !isDarkTheme();
        Logging.info("Toggling dark theme to: " + newTheme);
        setDarkTheme(newTheme);
        
        // TODO: Implement actual theme application logic
    }
    
    /**
     * Toggle chat enabled setting.
     */
    public void toggleChatEnabled() {
        boolean newChatSetting = !isChatEnabled();
        Logging.info("Toggling chat enabled to: " + newChatSetting);
        setChatEnabled(newChatSetting);
        
        // TODO: Implement actual chat setting update logic
    }
    
    /**
     * Load user profile data.
     */
    public void loadUserProfile() {
        try {
            Logging.info("Loading user profile for: " + getUsername());
            
            // TODO: Implement actual profile loading logic
            // For now, set some default values
            if (getFullName() == null || getFullName().isEmpty()) {
                setFullName("User " + getUsername());
            }
            
            Logging.info("User profile loaded successfully");
        } catch (Exception e) {
            Logging.error("Failed to load user profile: " + e.getMessage(), e);
        }
    }
} 