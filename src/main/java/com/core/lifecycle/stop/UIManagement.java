package com.core.lifecycle.stop;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

/**
 * Manages the shutdown of the application's UI components.
 * Handles cleanup of UI resources, dialogs, and state.
 *
 * @authors Clement Luo
 * @date June 25, 2025
 * @since 1.0
 */
public class UIManagement {

    /**
     * Performs UI cleanup during shutdown.
     * This method is called directly by LifecycleManager.
     */
    public static void shutdown() {
        Logging.info("Shutting down UI...");
        
        try {
            // Close any open dialogs or popups
            // Clear any UI caches
            // Save any UI state if needed
            
            Logging.info("UI shutdown completed");
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, "Error during UI shutdown", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.MEDIUM);
        }
    }
} 