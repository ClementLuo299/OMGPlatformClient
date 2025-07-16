package com.core;

import com.services.LoginService;
import com.services.TokenService;
import com.services.ValidationService;
import com.services.LocalStorageService;
import com.services.WebSocketService;

/**
 * Central service manager for the application.
 * 
 * This class provides access to core services that are shared across the application.
 * - Manages core shared services like `LoginService`, `ValidationService`, `TokenService`, and `LocalStorageService`.
 * - Ensures services are properly initialized before use.
 * - Use `getInstance()` to get the singleton instance, then call service methods.
 * 
 * Services managed:
 * - `LoginService`: Handles user authentication and login operations.
 * - `ValidationService`: Handles input validation for forms and user inputs.
 * - `TokenService`: Manages JWT tokens for user authentication.
 * - `LocalStorageService`: Handles secure local data storage on the client's computer.
 * - `WebSocketService`: Handles WebSocket connections and real-time messaging.
 * 
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 29, 2025
 * @since 1.0
 */
public class ServiceManager {

    private static ServiceManager instance;
    private LoginService loginService;
    private ValidationService validationService;
    private TokenService tokenService;
    private LocalStorageService localStorageService;
    private WebSocketService webSocketService;

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
     * Gets the TokenService instance.
     *
     * @return the TokenService instance
     */
    public TokenService getTokenService() {
        return tokenService;
    }

    /**
     * Gets the LocalStorageService instance.
     *
     * @return the LocalStorageService instance
     */
    public LocalStorageService getLocalStorageService() {
        return localStorageService;
    }

    /**
     * Gets the WebSocketService instance.
     *
     * @return the WebSocketService instance
     */
    public WebSocketService getWebSocketService() {
        return webSocketService;
    }

    /**
     * Initializes all services. Called automatically during construction.
     */
    private void initializeServices() {
        validationService = new ValidationService();
        tokenService = new TokenService();
        localStorageService = LocalStorageService.getInstance();
        webSocketService = new WebSocketService();
        loginService = new LoginService(validationService, tokenService);
    }
}