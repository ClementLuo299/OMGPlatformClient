package com.core.lifecycle;

import com.core.lifecycle.start.ServiceManagement;
import com.core.lifecycle.start.UIManagement;
import com.core.lifecycle.stop.ShutdownManager;
import com.core.lifecycle.start.ScreenManagement;
import com.core.lifecycle.start.StartupManager;
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
     * Registers all startup tasks with the StartupManager.
     * 
     * @param primaryStage the primary stage
     */
    private static void registerStartupTasks(Stage primaryStage) {
        // Register startup tasks in order
        ScreenManagement.registerAsStartupTask(primaryStage);
        ServiceManagement.registerAsStartupTask();
        UIManagement.registerAsStartupTask(primaryStage);
    }

    /**
     * Initializes the application and starts the primary stage.
     * Uses the new StartupManager for coordinated startup.
     * 
     * @param primaryStage the primary JavaFX stage
     */
    public static void start(Stage primaryStage) {
        try {
            Logging.info("Application startup initiated");
            
            // Register startup tasks
            registerStartupTasks(primaryStage);
            
            // Execute startup using StartupManager
            StartupManager.startup();
            
            // Configure and show the stage
            UIManagement.configureStage(primaryStage);
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
            
            // Perform shutdown using ShutdownManager
            ShutdownManager.shutdown();
            
            Logging.info("Application shutdown completed");
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, "Error occurred during shutdown", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.HIGH);
        }
    }
}