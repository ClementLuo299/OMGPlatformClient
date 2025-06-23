package com.utils.error_handling;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Core error handling utility for the application.
 * Provides methods for handling critical and non-critical errors with user feedback.
 * 
 * @authors Clement Luo
 * @date May 22, 2025
 * @edited June 21, 2025
 * @since 1.0
 * @version 1.0
 */
public final class ErrorHandler {

    //region ==================== CONSTANTS AND FIELDS ====================

    /**
     * Tells the error handler that the application is shutting down.
     * Stops the application from handling errors during shutdown.
     */
    private static final AtomicBoolean isShuttingDown = new AtomicBoolean(false);

    //endregion

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ErrorHandler() { throw new UnsupportedOperationException("Utility class cannot be instantiated"); }

    //region ==================== CORE ERROR HANDLING METHODS ====================

    /**
     * Handles a non-critical error that doesn't require application termination.
     * Logs the error, shows a warning dialog to the user.
     *
     * @param throwable the exception that occurred
     * @param userMessage a user-friendly message describing the error
     */
    public static void handleNonCriticalError(Throwable throwable, String userMessage) {
        if (isShuttingDown.get()) {
            AppLogger.warning("Ignoring non-critical error during shutdown: " + userMessage);
            return;
        }

        AppLogger.warning(userMessage, throwable);
        Dialog.showWarning("Warning", userMessage, throwable);
    }

    /**
     * Handles a critical error that requires immediate application termination.
     * This method exits the application.
     *
     * @param throwable the exception that occurred
     * @param userMessage a user-friendly message describing the error
     */
    public static void handleCriticalError(Throwable throwable, String userMessage) {
        AppLogger.fatal(userMessage, throwable);
        Dialog.showErrorAndExit("Fatal Error", userMessage, throwable);
    }

    //endregion

    //region ==================== SHUTDOWN MANAGEMENT ====================

    /**
     * Marks the application as shutting down to prevent error handling during shutdown.
     */
    public static void markShuttingDown() {
        isShuttingDown.set(true);
    }

    /**
     * Checks if the application is currently shutting down.
     *
     * @return true if the application is shutting down, false otherwise
     */
    public static boolean isShuttingDown() {
        return isShuttingDown.get();
    }

    //endregion

    //region ==================== UTILITY METHODS ====================

    /**
     * Rethrows a checked exception as an unchecked exception.
     * This is a utility method for converting checked exceptions to unchecked ones.
     *
     * @param <E> the type of the exception
     * @param exception the exception to rethrow
     * @return never returns, always throws
     * @throws E the exception that was passed in
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception> RuntimeException rethrow(E exception) throws E {
        throw exception;
    }

    //endregion
}