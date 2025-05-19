package com.core;

import com.services.AlertService;
import com.services.LoginService;

/**
 * A utility class for managing and providing centralized access to shared services
 * within the application. Facilitates lazy initialization and singleton-like
 * behavior for service instances.
 *
 * Features:
 * - Manages core shared services like `LoginService` and `AlertService`.
 * - Supports lazy initialization to optimize resource usage.
 * - Provides a single access point for global service instances.
 *
 * Usage:
 * - Call `getLoginService()` or `getAlertService()` to retrieve and use services as needed.
 * - Use `initializeCoreServices()` at application startup for eager initialization of all services.
 *
 * Associated Services:
 * - `LoginService`: Handles user authentication and session management.
 * - `AlertService`: Provides tools for displaying alerts across the application.
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class ServiceManager {

    private static LoginService loginService;
    private static AlertService alertService;

    public static LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService(); // Lazy initialization
        }
        return loginService;
    }

    public static AlertService getAlertService() {
        if (alertService == null) {
            alertService = new AlertService();
        }
        return alertService;
    }

    public static void initializeCoreServices() {
        // Initialize other shared services here, if needed
        loginService = new LoginService();
        alertService = new AlertService();
    }
}