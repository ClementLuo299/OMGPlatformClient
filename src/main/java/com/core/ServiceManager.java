package com.core;

import com.services.LoginService;

/**
 * Central service manager for the application.
 * 
 * This class provides access to core services that are shared across the application.
 * - Manages core shared services like `LoginService`.
 * - Ensures services are properly initialized before use.
 * - Call `getLoginService()` to retrieve and use services as needed.
 * 
 * Services managed:
 * - `LoginService`: Handles user authentication and login operations.
 * 
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ServiceManager {

    private static LoginService loginService;

    /**
     * Gets the LoginService instance, initializing it if necessary.
     *
     * @return the LoginService instance
     */
    public static LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    /**
     * Initializes all services. Call this during application startup.
     */
    public static void initializeServices() {
        loginService = new LoginService();
    }
}