package com;

import com.core.lifecycle.LifecycleManager;
import com.utils.ErrorHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point of the application.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 * @edited June 20, 2025
 * @since 1.0
 */
public class MainApplication extends Application {
    
    static {
        // Configure global error handling
        configureGlobalErrorHandling();
    }
    
    private static void configureGlobalErrorHandling() {
        // Set up global uncaught exception handler for all threads
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ErrorHandler.error("Uncaught exception in thread: " + thread.getName());
            ErrorHandler.handleCriticalError(
                new RuntimeException("Unhandled exception in thread: " + thread.getName(), throwable),
                "An unexpected error occurred"
            );
        });
        
        // Set up a global error handler for the application
        ErrorHandler.setGlobalErrorHandler(throwable -> {
            // Here you could add additional error reporting, like sending crash reports
            ErrorHandler.error("Global error handler caught: " + throwable.getMessage());
        });
        
        // Set up error recovery strategy (example)
        ErrorHandler.setErrorRecoveryStrategy(throwable -> {
            // Add custom recovery logic based on exception type
            return false; // Return false to let the application handle it normally
        });
    }

    @Override
    public void start(Stage primaryStage) {
        // Use safeExecute to wrap the entire startup process
        ErrorHandler.safeExecute(() -> {
            validateStage(primaryStage);
            configurePrimaryStage(primaryStage);
            initializeApplication(primaryStage);
            return null;
        }, "Failed to start the application");
    }
    
    private void validateStage(Stage primaryStage) {
        if (primaryStage == null) {
            throw new IllegalArgumentException("Primary stage cannot be null");
        }
    }
    
    private void configurePrimaryStage(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            ErrorHandler.info("Application shutdown requested by user");
            Platform.exit();
            System.exit(0);
        });
    }
    
    private void initializeApplication(Stage primaryStage) throws Exception {
        try {
            ErrorHandler.info("Starting application initialization...");
            LifecycleManager.start(primaryStage);
            ErrorHandler.info("Application initialization completed successfully");
        } catch (Exception e) {
            ErrorHandler.error("Error during application initialization: " + e.getMessage());
            throw e; // Re-throw to be handled by safeExecute
        }
    }

    @Override
    public void stop() {
        ErrorHandler.safeExecute(() -> {
            ErrorHandler.info("Initiating application shutdown...");
            LifecycleManager.stop();
            ErrorHandler.info("Application shutdown completed successfully");
            return null;
        }, "Error during application shutdown");
    }

    public static void main(String[] args) {
        // Set up main thread uncaught exception handler
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            ErrorHandler.error("Uncaught exception in main thread: " + throwable.getMessage());
            ErrorHandler.handleCriticalError(
                new RuntimeException("Unhandled exception in main thread", throwable),
                "A critical error occurred"
            );
        });
        
        // Execute the application with error handling
        ErrorHandler.safeExecute(() -> {
            ErrorHandler.info("Launching application...");
            launch(args);
            return null;
        }, "Fatal error during application startup");
    }
}
