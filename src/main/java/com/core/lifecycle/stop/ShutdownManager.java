package com.core.lifecycle.stop;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages the shutdown process of the application.
 * Handles cleanup of resources, saving data, and graceful termination.
 * 
 * @authors Clement Luo
 * @date June 24, 2025
 * @edited June 24, 2025
 * @since 1.0
 */
public class ShutdownManager {

    /**
     * List of shutdown tasks to execute during application shutdown.
     */
    private static final List<ShutdownTask> shutdownTasks = new ArrayList<>();

    /**
     * Flag to track if shutdown has been initiated.
     */
    private static volatile boolean isShuttingDown = false;

    /**
     * Maximum time to wait for shutdown tasks to complete (in milliseconds).
     */
    private static final long SHUTDOWN_TIMEOUT_MS = 10000; // 10 seconds

    /**
     * Registers a shutdown task to be executed during application shutdown.
     * Only allows registration if shutdown hasn't started yet.
     * 
     * @param task the shutdown task to register
     */
    public static void registerShutdownTask(ShutdownTask task) {
        // Prevent registration during shutdown to avoid race conditions
        if (!isShuttingDown) {
            shutdownTasks.add(task);
        }
    }

    /**
     * Performs the complete shutdown process.
     * Executes all registered shutdown tasks in order with timeout protection.
     * Prevents multiple shutdown attempts.
     */
    public static void shutdown() {
        // Prevent multiple shutdown attempts
        if (isShuttingDown) {
            Logging.warning("Shutdown already in progress");
            return;
        }

        // Mark shutdown as started
        isShuttingDown = true;
        Logging.info("Starting application shutdown process...");

        try {
            // Execute shutdown tasks
            executeShutdownTasks();
            
            // Final cleanup
            performFinalCleanup();
            
            Logging.info("Application shutdown completed successfully");
            
        } catch (Exception e) {
            // Handle any unexpected errors during shutdown process
            ErrorHandler.handleNonCriticalError(e, "Error during shutdown process", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.HIGH);
        }
    }

    /**
     * Executes all registered shutdown tasks.
     * Each task runs with timeout protection and error handling.
     * Continues execution even if individual tasks fail.
     */
    private static void executeShutdownTasks() {
        // Check if there are any tasks to execute
        if (shutdownTasks.isEmpty()) {
            Logging.info("No shutdown tasks registered");
            return;
        }

        Logging.info("Executing " + shutdownTasks.size() + " shutdown tasks...");

        // Execute each task with individual error handling
        for (int i = 0; i < shutdownTasks.size(); i++) {
            ShutdownTask task = shutdownTasks.get(i);
            try {
                Logging.info("Executing shutdown task " + (i + 1) + ": " + task.getName());
                
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
                future.get(SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                
                Logging.info("Shutdown task completed: " + task.getName());
                
            } catch (Exception e) {
                // Log task failure but continue with other tasks
                ErrorHandler.handleNonCriticalError(e, 
                    "Failed to execute shutdown task: " + task.getName(), 
                    ErrorCategory.SYSTEM, ErrorSeverity.MEDIUM);
            }
        }
    }

    /**
     * Performs final cleanup operations.
     * Handles data saving, cache clearing, and connection closing.
     * Placeholder for actual cleanup implementations.
     */
    private static void performFinalCleanup() {
        Logging.info("Performing final cleanup...");
        
        try {
            // Save any pending data
            // Services.db().saveDBData();
            
            // Clear any caches
            // CacheManager.clearAll();
            
            // Close any open connections
            // ConnectionManager.closeAll();
            
            Logging.info("Final cleanup completed");
            
        } catch (Exception e) {
            // Handle cleanup errors but don't fail shutdown
            ErrorHandler.handleNonCriticalError(e, "Error during final cleanup", 
                                              ErrorCategory.SYSTEM, ErrorSeverity.MEDIUM);
        }
    }

    /**
     * Checks if the application is currently shutting down.
     * Used by other components to avoid starting new operations during shutdown.
     * 
     * @return true if shutdown is in progress
     */
    public static boolean isShuttingDown() { return isShuttingDown; }

    /**
     * Clears all registered shutdown tasks.
     * Useful for testing or resetting the shutdown manager.
     * Should be used carefully in production.
     */
    public static void clearShutdownTasks() { shutdownTasks.clear(); }

    /**
     * Gets the number of registered shutdown tasks.
     * Useful for monitoring and debugging shutdown process.
     * 
     * @return the number of shutdown tasks
     */
    public static int getShutdownTaskCount() { return shutdownTasks.size(); }
} 