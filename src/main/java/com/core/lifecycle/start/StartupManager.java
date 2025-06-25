package com.core.lifecycle.start;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages the startup process of the application.
 * Handles initialization of resources, services, and components.
 * 
 * @authors Clement Luo
 * @date June 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class StartupManager {

    /**
     * List of startup tasks to execute during application startup.
     */
    private static final List<StartupTask> startupTasks = new ArrayList<>();

    /**
     * Flag to track if startup has been initiated.
     */
    private static volatile boolean isStarting = false;

    /**
     * Flag to track if startup has been completed.
     */
    private static volatile boolean isStarted = false;

    /**
     * Maximum time to wait for startup tasks to complete (in milliseconds).
     */
    private static final long STARTUP_TIMEOUT_MS = 15000; // 15 seconds

    /**
     * Registers a startup task to be executed during application startup.
     * Only allows registration if startup hasn't started yet.
     * 
     * @param task the startup task to register
     */
    public static void registerStartupTask(StartupTask task) {
        // Prevent registration during startup to avoid race conditions
        if (!isStarting) {
            startupTasks.add(task);
        }
    }

    /**
     * Performs the complete startup process.
     * Executes all registered startup tasks in order with timeout protection.
     * Prevents multiple startup attempts.
     */
    public static void startup() {
        // Prevent multiple startup attempts
        if (isStarting || isStarted) {
            Logging.warning("Startup already in progress or completed");
            return;
        }

        // Mark startup as started
        isStarting = true;
        Logging.info("Starting application startup process...");

        try {
            // Execute startup tasks
            executeStartupTasks();
            
            // Final startup operations
            performFinalStartup();
            
            // Mark startup as completed
            isStarted = true;
            Logging.info("Application startup completed successfully");
            
        } catch (Exception e) {
            // Handle any unexpected errors during startup process
            ErrorHandler.handleCriticalError(e, "Error during startup process", 
                                           ErrorCategory.SYSTEM, ErrorSeverity.CRITICAL);
        }
    }

    /**
     * Executes all registered startup tasks.
     * Each task runs with timeout protection and error handling.
     * Stops execution if any critical task fails.
     */
    private static void executeStartupTasks() {
        // Check if there are any tasks to execute
        if (startupTasks.isEmpty()) {
            Logging.info("No startup tasks registered");
            return;
        }

        Logging.info("Executing " + startupTasks.size() + " startup tasks...");

        // Execute each task with individual error handling
        for (int i = 0; i < startupTasks.size(); i++) {
            StartupTask task = startupTasks.get(i);
            try {
                Logging.info("Executing startup task " + (i + 1) + ": " + task.getName());
                
                // Execute task with timeout - wraps checked exceptions
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        task.execute();
                    } catch (Exception e) {
                        // Convert checked exception to runtime for CompletableFuture
                        throw new RuntimeException(e);
                    }
                });
                // Wait for task completion with timeout
                future.get(STARTUP_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                
                Logging.info("Startup task completed: " + task.getName());
                
            } catch (Exception e) {
                // Startup failures are critical - fail fast
                ErrorHandler.handleCriticalError(e, 
                    "Failed to execute startup task: " + task.getName(), 
                    ErrorCategory.SYSTEM, ErrorSeverity.CRITICAL);
                throw new RuntimeException("Startup failed at task: " + task.getName(), e);
            }
        }
    }

    /**
     * Performs final startup operations.
     * Handles any post-initialization setup and validation.
     */
    private static void performFinalStartup() {
        Logging.info("Performing final startup operations...");
        
        try {
            // Validate all systems are ready
            // validateSystemReadiness();
            
            // Initialize any remaining components
            // initializeRemainingComponents();
            
            Logging.info("Final startup operations completed");
            
        } catch (Exception e) {
            // Handle startup validation errors
            ErrorHandler.handleCriticalError(e, "Error during final startup operations", 
                                           ErrorCategory.SYSTEM, ErrorSeverity.CRITICAL);
        }
    }

    /**
     * Checks if the application is currently starting up.
     * 
     * @return true if startup is in progress
     */
    public static boolean isStarting() { return isStarting; }

    /**
     * Checks if the application startup has been completed.
     * 
     * @return true if startup is completed
     */
    public static boolean isStarted() { return isStarted; }

    /**
     * Clears all registered startup tasks.
     * Useful for testing or resetting the startup manager.
     * Should be used carefully in production.
     */
    public static void clearStartupTasks() { startupTasks.clear(); }

    /**
     * Gets the number of registered startup tasks.
     * Useful for monitoring and debugging startup process.
     * 
     * @return the number of startup tasks
     */
    public static int getStartupTaskCount() { return startupTasks.size();}
} 