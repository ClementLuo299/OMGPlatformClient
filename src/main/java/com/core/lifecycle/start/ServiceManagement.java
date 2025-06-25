package com.core.lifecycle.start;

import com.core.ServiceManager;
import com.utils.error_handling.Logging;

/**
 * Manages the initialization of application services.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ServiceManagement {

    /**
     * Initializes all application services.
     * This method is called directly by LifecycleManager.
     */
    public static void initialize() {
        Logging.info("Initializing application services...");
        
        // Initialize ServiceManager
        ServiceManager.initializeCoreServices();
        
        Logging.info("Application services initialized successfully");
    }
}
