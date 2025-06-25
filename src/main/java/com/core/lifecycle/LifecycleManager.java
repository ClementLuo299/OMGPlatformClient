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
     * Current state of the application lifecycle.
     */
    private static volatile LifecycleState currentState = LifecycleState.INITIALIZING;

    /**
     * Flag to track if the application has been initialized.
     */
    private static volatile boolean isInitialized = false;

    /**
     * Initializes the application and starts the primary stage.
     * Uses the new StartupManager for coordinated startup.
     * 
     * @param primaryStage the primary JavaFX stage
     */
    public static void start(Stage primaryStage) {
        try {
            // Update state to starting
            updateState(LifecycleState.STARTING, "Application startup initiated");
            
            // Register startup tasks
            registerStartupTasks(primaryStage);
            
            // Execute startup using StartupManager
            StartupManager.startup();
            
            // Configure and show the stage
            UIManagement.configureStage(primaryStage);
            primaryStage.show();
            
            // Update state to running
            updateState(LifecycleState.RUNNING, "Application started successfully");
            isInitialized = true;
            
            Logging.info("Application lifecycle started successfully");
            
        } catch (Exception e) {
            updateState(LifecycleState.ERROR, "Critical error during startup", e);
            ErrorHandler.handleCriticalError(e, "Critical error occurred during startup", 
                                           ErrorCategory.SYSTEM, ErrorSeverity.CRITICAL);
        }
    }

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
     * Performs cleanup and saves necessary data before exiting the application.
     */
    public static void stop() {
        try {
            // Update state to shutting down
            updateState(LifecycleState.SHUTTING_DOWN, "Application shutdown initiated");
            
            // Perform shutdown using ShutdownManager
            ShutdownManager.shutdown();
            
            // Update state to stopped
            updateState(LifecycleState.STOPPED, "Application shutdown completed");
            
            Logging.info("Application lifecycle stopped successfully");
            
        } catch (Exception e) {
            updateState(LifecycleState.ERROR, "Error during shutdown", e);
            ErrorHandler.handleNonCriticalError(e, "Error occurred during shutdown", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.HIGH);
        }
    }

    /**
     * Updates the current lifecycle state and logs the event.
     * 
     * @param newState the new lifecycle state
     * @param message the state change message
     */
    private static void updateState(LifecycleState newState, String message) {
        updateState(newState, message, null);
    }

    /**
     * Updates the current lifecycle state and logs the event with an error.
     * 
     * @param newState the new lifecycle state
     * @param message the state change message
     * @param error the error that occurred (can be null)
     */
    private static void updateState(LifecycleState newState, String message, Throwable error) {
        LifecycleState oldState = currentState;
        currentState = newState;
        
        LifecycleEvent event = new LifecycleEvent(newState, message, error);
        Logging.info("Lifecycle state changed: " + oldState + " -> " + newState + " - " + message);
        
        if (error != null) {
            Logging.error("Lifecycle error: " + message, error);
        }
    }

    /**
     * Gets the current lifecycle state.
     * 
     * @return the current lifecycle state
     */
    public static LifecycleState getCurrentState() {
        return currentState;
    }

    /**
     * Checks if the application is currently running.
     * 
     * @return true if the application is running
     */
    public static boolean isRunning() {
        return currentState == LifecycleState.RUNNING;
    }

    /**
     * Checks if the application has been initialized.
     * 
     * @return true if the application is initialized
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Checks if the application is shutting down.
     * 
     * @return true if the application is shutting down
     */
    public static boolean isShuttingDown() {
        return currentState == LifecycleState.SHUTTING_DOWN || ShutdownManager.isShuttingDown();
    }

    /**
     * Checks if the application is in an error state.
     * 
     * @return true if the application is in an error state
     */
    public static boolean isInErrorState() {
        return currentState == LifecycleState.ERROR;
    }
}