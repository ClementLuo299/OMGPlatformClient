package com.core;

import com.services.LoginService;
import com.services.ValidationService;

/**
 * Central service manager for the application.
 * 
 * This class provides access to core services that are shared across the application.
 * - Manages core shared services like `LoginService` and `ValidationService`.
 * - Ensures services are properly initialized before use.
 * - Use `getInstance()` to get the singleton instance, then call service methods.
 * 
 * Services managed:
 * - `LoginService`: Handles user authentication and login operations.
 * - `ValidationService`: Handles input validation for forms and user inputs.
 * 
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class ServiceManager {

    private static ServiceManager instance;
    private LoginService loginService;
    private ValidationService validationService;

    private ServiceManager() {
        initializeServices();
    }

    /**
     * Gets the singleton instance of ServiceManager.
     *
     * @return the ServiceManager instance
     */
    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    /**
     * Gets the LoginService instance.
     *
     * @return the LoginService instance
     */
    public LoginService getLoginService() {
        return loginService;
    }

    /**
     * Gets the ValidationService instance.
     *
     * @return the ValidationService instance
     */
    public ValidationService getValidationService() {
        return validationService;
    }

    /**
     * Initializes all services. Called automatically during construction.
     */
    private void initializeServices() {
        validationService = new ValidationService();
        loginService = new LoginService(validationService);
    }
}