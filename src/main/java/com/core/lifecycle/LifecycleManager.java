package com.core.lifecycle;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

import javafx.stage.Stage;

/**
 * Manages the lifecycle of the application, handling initialization, startup,
 * and cleanup processes. This class is responsible for configuring core components,
 * managing application screens, and ensuring proper shutdown procedures.
 * 
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class LifecycleManager {

    /**
     * Initializes the application and starts the primary stage.
     * 
     * @param primaryStage the primary JavaFX stage
     */
    public static void start(Stage primaryStage) {
        try {
            Logging.info("Application startup initiated");
            
            // Initialize core components in order
            com.core.lifecycle.start.ScreenManagement.initialize(primaryStage);
            com.core.lifecycle.start.ServiceManagement.initialize();
            com.core.lifecycle.start.UIManagement.initialize(primaryStage);
            
            // Show the stage
            primaryStage.show();
            
            Logging.info("Application started successfully");
            
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Critical error occurred during startup", 
                                           ErrorCategory.SYSTEM, ErrorSeverity.CRITICAL);
        }
    }

    /**
     * Performs cleanup and saves necessary data before exiting the application.
     */
    public static void stop() {
        try {
            Logging.info("Application shutdown initiated");
            
            // Perform shutdown operations in reverse order using fully qualified names
            com.core.lifecycle.stop.UIManagement.shutdown();
            com.core.lifecycle.stop.ServiceManagement.shutdown();
            com.core.lifecycle.stop.ScreenManagement.shutdown();
            
            Logging.info("Application shutdown completed");
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, "Error occurred during shutdown", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.HIGH);
        }
    }
}