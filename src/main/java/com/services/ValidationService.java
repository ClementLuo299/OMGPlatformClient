package com.services;

import com.config.ValidationConfig;

/**
 * Handles input validation for various forms and user inputs.
 * This service provides centralized validation logic that can be reused
 * across different parts of the application.
 *
 * @authors Clement Luo
 * @date June 26, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class ValidationService {

    /**
     * Result of a validation operation
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final String errorTitle;
        private final String errorMessage;

        public ValidationResult(boolean isValid, String errorTitle, String errorMessage) {
            this.isValid = isValid;
            this.errorTitle = errorTitle;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() { return isValid; }
        public String getErrorTitle() { return errorTitle; }
        public String getErrorMessage() { return errorMessage; }

        public static ValidationResult success() {
            return new ValidationResult(true, null, null);
        }

        public static ValidationResult error(String title, String message) {
            return new ValidationResult(false, title, message);
        }
    }

    /**
     * Validates that a string is not null or empty
     *
     * @param value The string to validate
     * @param fieldName The name of the field for error messages
     * @return ValidationResult with validation status
     */
    public ValidationResult validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return ValidationResult.error("Validation Error", fieldName + " is required");
        }
        return ValidationResult.success();
    }

    /**
     * Validates user login credentials
     *
     * @param username The user's login name
     * @param password The user's password
     * @return ValidationResult with validation status and error details
     */
    public ValidationResult validateLoginCredentials(String username, String password) {
        // Validate username
        ValidationResult usernameValidation = validateRequired(username, "Username");
        if (!usernameValidation.isValid()) {
            return ValidationResult.error("Login Error", usernameValidation.getErrorMessage());
        }
        
        // Validate password
        ValidationResult passwordValidation = validateRequired(password, "Password");
        if (!passwordValidation.isValid()) {
            return ValidationResult.error("Login Error", passwordValidation.getErrorMessage());
        }
        
        return ValidationResult.success();
    }

    /**
     * Validates guest login credentials
     *
     * @param username Desired guest username
     * @return ValidationResult with validation status and error details
     */
    public ValidationResult validateGuestCredentials(String username) {
        ValidationResult usernameValidation = validateRequired(username, "Guest username");
        if (!usernameValidation.isValid()) {
            return ValidationResult.error("Guest Login Error", usernameValidation.getErrorMessage());
        }
        
        return ValidationResult.success();
    }

    /**
     * Validates registration form data
     *
     * @param fullName User's full name
     * @param username Desired username
     * @param password Password
     * @param confirmPassword Password confirmation
     * @param dateOfBirth Date of birth
     * @return ValidationResult with validation status and error details
     */
    public ValidationResult validateRegistrationData(String fullName, String username, 
                                                   String password, String confirmPassword, 
                                                   String dateOfBirth) {
        // Validate required fields
        ValidationResult fullNameValidation = validateRequired(fullName, "Full name");
        if (!fullNameValidation.isValid()) {
            return ValidationResult.error("Registration Error", fullNameValidation.getErrorMessage());
        }

        ValidationResult usernameValidation = validateRequired(username, "Username");
        if (!usernameValidation.isValid()) {
            return ValidationResult.error("Registration Error", usernameValidation.getErrorMessage());
        }

        ValidationResult passwordValidation = validateRequired(password, "Password");
        if (!passwordValidation.isValid()) {
            return ValidationResult.error("Registration Error", passwordValidation.getErrorMessage());
        }

        ValidationResult confirmPasswordValidation = validateRequired(confirmPassword, "Password confirmation");
        if (!confirmPasswordValidation.isValid()) {
            return ValidationResult.error("Registration Error", confirmPasswordValidation.getErrorMessage());
        }

        ValidationResult dobValidation = validateRequired(dateOfBirth, "Date of birth");
        if (!dobValidation.isValid()) {
            return ValidationResult.error("Registration Error", dobValidation.getErrorMessage());
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            return ValidationResult.error("Registration Error", "Passwords do not match");
        }

        // Validate password strength (basic validation)
        if (password.length() < ValidationConfig.MIN_PASSWORD_LENGTH) {
            return ValidationResult.error("Registration Error", 
                "Password must be at least " + ValidationConfig.MIN_PASSWORD_LENGTH + " characters long");
        }

        return ValidationResult.success();
    }

    /**
     * Validates email format
     *
     * @param email Email address to validate
     * @return ValidationResult with validation status
     */
    public ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ValidationResult.error("Validation Error", "Email is required");
        }

        // Basic email validation regex
        if (!email.matches(ValidationConfig.EMAIL_PATTERN)) {
            return ValidationResult.error("Validation Error", "Invalid email format");
        }

        return ValidationResult.success();
    }

    /**
     * Validates username format and length
     *
     * @param username Username to validate
     * @return ValidationResult with validation status
     */
    public ValidationResult validateUsername(String username) {
        ValidationResult requiredValidation = validateRequired(username, "Username");
        if (!requiredValidation.isValid()) {
            return requiredValidation;
        }

        // Username length validation
        if (username.length() < ValidationConfig.MIN_USERNAME_LENGTH) {
            return ValidationResult.error("Validation Error", 
                "Username must be at least " + ValidationConfig.MIN_USERNAME_LENGTH + " characters long");
        }

        if (username.length() > ValidationConfig.MAX_USERNAME_LENGTH) {
            return ValidationResult.error("Validation Error", 
                "Username must be no more than " + ValidationConfig.MAX_USERNAME_LENGTH + " characters long");
        }

        // Username format validation (alphanumeric and underscores only)
        if (!username.matches(ValidationConfig.USERNAME_PATTERN)) {
            return ValidationResult.error("Validation Error", "Username can only contain letters, numbers, and underscores");
        }

        return ValidationResult.success();
    }
} 