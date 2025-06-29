package com.services;

import com.network.HTTPHandler;
import com.network.responses.LoginResponse;
import com.network.responses.RegistrationResponse;
import com.utils.error_handling.Dialog;
import com.utils.error_handling.Logging;

/**
 * Handles user authentication and account management operations.
 * This service provides methods for user login, guest access, registration,
 * and password recovery functionality.
 *
 * @authors Clement Luo
 * @date May 17, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class LoginService {

    private final ValidationService validationService;
    private final TokenService tokenService;

    public LoginService(ValidationService validationService, TokenService tokenService) {
        this.validationService = validationService;
        this.tokenService = tokenService;
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
        
        // Call HTTPHandler to perform the actual authentication
        try {
            LoginResponse response = HTTPHandler.login(username, password);
            Logging.info("Login response - Status: " + response.getStatusCode() + ", Body: " + response.getResponseBody());
            
            // Check if login was successful based on HTTP status code
            if (response.isSuccess()) {
                // Store JWT token if present in response
                if (response.hasJWTToken()) {
                    tokenService.setToken(response.getJWTToken());
                    Logging.info("JWT token stored for user: '" + username + "'");
                } else {
                    Logging.warning("Login successful but no JWT token received for user: '" + username + "'");
                }
                
                Logging.info("Login successful for user: '" + username + "'");
                return true;
            } else {
                Logging.warning("Login failed - Status: " + response.getStatusCode() + ", Response: " + response.getResponseBody());
                Dialog.showErrorCompact("Login Error", "Invalid username or password: " + response.getResponseBody());
                return false;
            }
        } catch (Exception e) {
            Logging.error("Login failed with exception: " + e.getMessage(), e);
            Dialog.showErrorCompact("Login Error", "Failed to connect to server. Please try again.");
            return false;
        }
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
     * Handles all validation internally and shows appropriate dialogs.
     *
     * @param username Desired username for the new account
     * @param password Password for the new account
     * @param fullName Full name of the user
     * @param dateOfBirth Date of birth of the user
     * @param confirmPassword Password confirmation
     * @return true if registration is successful, false otherwise
     */
    public boolean register(String username, String password, String fullName, String dateOfBirth, String confirmPassword) {
        Logging.info("Registration attempt initiated - Username: '" + username + "', Full Name: '" + fullName + "'");
        
        // Validate registration data using ValidationService
        ValidationService.ValidationResult validation = validationService.validateRegistrationData(
            fullName, username, password, confirmPassword, dateOfBirth);
        
        if (!validation.isValid()) {
            Logging.warning("Registration failed: " + validation.getErrorMessage());
            Dialog.showErrorCompact(validation.getErrorTitle(), validation.getErrorMessage());
            return false;
        }
        
        // Call HTTPHandler to perform the actual registration
        try {
            RegistrationResponse response = HTTPHandler.register(username, password, fullName, dateOfBirth);
            Logging.info("Registration response - Status: " + response.getStatusCode() + ", Body: " + response.getResponseBody());
            
            // Check if registration was successful based on HTTP status code
            if (response.isSuccess()) {
                Logging.info("Registration successful for user: '" + username + "'");
                return true;
            } else {
                Logging.warning("Registration failed - Status: " + response.getStatusCode() + ", Response: " + response.getResponseBody());
                Dialog.showErrorCompact("Registration Error", "Failed to register account: " + response.getResponseBody());
                return false;
            }
        } catch (Exception e) {
            Logging.error("Registration failed with exception: " + e.getMessage(), e);
            Dialog.showErrorCompact("Registration Error", "Failed to register account. Please try again.");
            return false;
        }
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
     * Logs out the current user by clearing the JWT token.
     * Handles all cleanup internally and shows appropriate dialogs.
     *
     * @return true if logout was successful, false otherwise
     */
    public boolean logout() {
        Logging.info("Logout initiated for user: '" + tokenService.getCurrentUsername() + "'");
        
        try {
            // Clear the JWT token
            tokenService.clearToken();
            
            Logging.info("Logout successful");
            return true;
        } catch (Exception e) {
            Logging.error("Logout failed with exception: " + e.getMessage(), e);
            Dialog.showErrorCompact("Logout Error", "Failed to logout properly. Please try again.");
            return false;
        }
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
