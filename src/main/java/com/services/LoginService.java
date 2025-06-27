package com.services;

import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;

/**
 * Handles user authentication and account management operations.
 * This service provides methods for user login, guest access, registration,
 * and password recovery functionality.
 *
 * @authors Clement Luo
 * @date May 17, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class LoginService {

    private final ValidationService validationService;

    public LoginService(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Authenticates a user with the provided credentials.
     * Handles all validation internally and shows appropriate dialogs.
     *
     * @param username The user's login name
     * @param password The user's password
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(String username, String password) {
        Logging.info("Login attempt initiated - Username: '" + username + "'");
        
        // Validate credentials using ValidationService
        ValidationService.ValidationResult validation = validationService.validateLoginCredentials(username, password);
        if (!validation.isValid()) {
            Logging.warning("Login failed: " + validation.getErrorMessage());
            Dialog.showErrorCompact(validation.getErrorTitle(), validation.getErrorMessage());
            return false;
        }
        
        // TODO: Implement actual authentication logic
        // For demonstration, simulate successful login
        Logging.info("Login successful for user: '" + username + "'");
        return true;
    }

    /**
     * Provides guest access to the system with limited functionality.
     * Handles all validation internally and shows appropriate dialogs.
     *
     * @param username Desired guest username
     * @return true if guest access is granted, false otherwise
     */
    public boolean guestLogin(String username) {
        Logging.info("Guest login attempt initiated - Username: '" + username + "'");
        
        // Validate guest username using ValidationService
        ValidationService.ValidationResult validation = validationService.validateGuestCredentials(username);
        if (!validation.isValid()) {
            Logging.warning("Guest login failed: " + validation.getErrorMessage());
            Dialog.showErrorCompact(validation.getErrorTitle(), validation.getErrorMessage());
            return false;
        }
        
        // TODO: Implement actual guest login logic
        // For demonstration, simulate successful guest login
        Logging.info("Guest login successful for user: '" + username + "'");
        return true;
    }

    /**
     * Registers a new user account in the system.
     *
     * @param username Desired username for the new account
     * @return true if registration is successful, false if username is taken
     */
    public boolean register(String username) {
        return false;
    }

    /**
     * Initiates the password recovery process for a user.
     *
     * @param username Username of the account to recover
     * @return true if the recovery process started successfully, false if user not found
     */
    public boolean forgotPassword(String username) {
        return false;
    }
}
