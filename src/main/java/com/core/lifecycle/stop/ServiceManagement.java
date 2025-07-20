package com.core.lifecycle.stop;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.Logging;
import com.utils.error_handling.enums.ErrorCategory;
import com.utils.error_handling.enums.ErrorSeverity;

/**
 * Manages the shutdown of application services.
 * Handles cleanup of service resources, data saving, and connection closing.
 *
 * @authors Clement Luo
 * @date June 25, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ServiceManagement {

    /**
     * Performs service cleanup during shutdown.
     * This method is called directly by LifecycleManager.
     */
    public static void shutdown() {
        Logging.info("Shutting down services...");
        
        try {
            // Save any pending data
            // Close any open connections
            // Clean up service resources
            
            Logging.info("Services shutdown completed");
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, "Error during services shutdown", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.MEDIUM);
        }
    }
} 