package com.utils.error_handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging utility for the application.
 * Provides methods for logging messages with various levels.
 * 
 * @authors Clement Luo
 * @date June 22, 2025
 * @edited June 23, 2025
 * @since 1.0
 * @version 1.0
 */
public final class LogUtils {

    /**
     * SLF4J Logger instance used for all logging operations.
     * This logger is configured to use the LogUtils class name as the logger name,
     * which helps in identifying the source of log messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * Private constructor to prevent instantiation of this utility class.
     * 
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private LogUtils() { throw new UnsupportedOperationException("Utility class cannot be instantiated"); }
    
    //region ==================== MESSAGE LOGGING METHODS ====================

    /**
     * Logs a trace message with the most detailed level of debugging information.
     *
     * @param message the trace message to log
     */
    public static void trace(String message) { logger.trace(message); }
    
    /**
     * Logs a trace message with an associated throwable for detailed debugging.
     *
     * @param message the trace message to log
     * @param throwable the throwable to log 
     */
    public static void trace(String message, Throwable throwable) { logger.trace(message, throwable); }

    /**
     * Logs a debug message for development and debugging purposes.
     *
     * @param message the debug message to log
     */
    public static void debug(String message) { logger.debug(message); }

    /**
     * Logs a debug message with an associated throwable for debugging with exception details.
     *
     * @param message the debug message to log
     * @param throwable the throwable to log (can be null)
     */
    public static void debug(String message, Throwable throwable) { logger.debug(message, throwable); }

    /**
     * Logs an informational message about normal application operation.
     *
     * @param message the informational message to log
     */
    public static void info(String message) { logger.info(message); }

    /**
     * Logs a warning message for potential problems that don't stop execution.
     *
     * @param message the warning message to log
     */
    public static void warning(String message) { logger.warn(message); }

    /**
     * Logs a warning message with an associated throwable.
     *
     * @param message the warning message to log
     * @param throwable the throwable to log
     */
    public static void warning(String message, Throwable throwable) { logger.warn(message, throwable); }

    /**
     * Logs an error message for serious problems that may affect functionality.
     *
     * @param message the error message to log
     */
    public static void error(String message) { logger.error(message); }

    /**
     * Logs an error message with an associated throwable.
     *
     * @param message the error message to log
     * @param throwable the throwable to log
     */
    public static void error(String message, Throwable throwable) { logger.error(message, throwable); }

    /**
     * Logs a fatal message.
     *
     * @param message the fatal message to log
     */
    public static void fatal(String message) { logger.error("FATAL: " + message); }
    
    /**
     * Logs a fatal message with an associated throwable.
     *
     * @param message the fatal message to log
     * @param throwable the throwable to log
     */
    public static void fatal(String message, Throwable throwable) { logger.error("FATAL: " + message, throwable); }

    /**
     * Logs with context information (useful for debugging).
     * 
     * @param message the message to log
     * @param context the context to log
     */
    public static void debugWithContext(String message, String context) { logger.debug("[{}] {}", context, message); }

    //endregion

    //region ==================== METHOD LOGGING METHODS ====================

    /**
     * Logs method entry/exit for debugging.
     * 
     * @param methodName the name of the method to log
     */
    public static void enterMethod(String methodName) {
        logger.debug("Entering method: {}", methodName);
    }

    /**
     * Logs method exit for debugging.
     * 
     * @param methodName the name of the method to log
     */
    public static void exitMethod(String methodName) {
        logger.debug("Exiting method: {}", methodName);
    }

    //endregion

    //region ==================== PERFORMANCE LOGGING METHODS ====================

    /**
     * Logs performance metrics.
     * 
     * @param operation the name of the operation to log
     * @param durationMs the duration of the operation in milliseconds
     */
    public static void logPerformance(String operation, long durationMs) {
        logger.info("Performance: {} took {}ms", operation, durationMs);
    }

    /**
     * Logs an object state for debugging.
     * 
     * @param objectName the name of the object
     * @param state the state information
     */
    public static void logObjectState(String objectName, String state) {
        logger.debug("OBJECT: {} - {}", objectName, state);
    }

    //endregion

    //region ==================== BUSINESS AND SECURITY EVENTS ====================

    /**
     * Logs a business event.
     * 
     * @param event the name of the event to log
     * @param details the details of the event to log
     */
    public static void logBusinessEvent(String event, String details) {
        logger.info("BUSINESS: {} - {}", event, details);
    }

    /**
     * Logs a security event.
     * 
     * @param event the name of the event to log
     * @param user the user associated with the event
     */
    public static void logSecurityEvent(String event, String user) {
        logger.warn("SECURITY: {} by user: {}", event, user);
    }

    /**
     * Logs a validation error.
     * 
     * @param field the field that failed validation
     * @param value the value that failed validation
     * @param reason the reason for the validation failure
     */
    public static void logValidationError(String field, String value, String reason) {
        logger.warn("VALIDATION: Field '{}' with value '{}' failed: {}", field, value, reason);
    }

    //endregion

    //region ==================== API EVENTS ====================

    /**
     * Logs an API call.
     * 
     * @param endpoint the endpoint of the API call
     * @param method the method of the API call
     * @param statusCode the status code of the API call
     */
    public static void logApiCall(String endpoint, String method, int statusCode) {
        logger.info("API: {} {} -> {}", method, endpoint, statusCode);
    }

    /**
     * Logs an API call with duration.
     * 
     * @param endpoint the endpoint of the API call
     * @param method the method of the API call
     * @param statusCode the status code of the API call
     * @param durationMs the duration of the API call in milliseconds
     */
    public static void logApiCall(String endpoint, String method, int statusCode, long durationMs) {
        logger.info("API: {} {} -> {} ({}ms)", method, endpoint, statusCode, durationMs);
    }

    //endregion

    //region ==================== USER AND FILE OPERATIONS ====================

    /**
     * Logs a user action.
     * 
     * @param userId the user ID
     * @param action the action performed
     * @param details additional details about the action
     */
    public static void logUserAction(String userId, String action, String details) {
        logger.info("USER: {} performed {} - {}", userId, action, details);
    }

    /**
     * Logs a file operation.
     * 
     * @param operation the file operation (READ, WRITE, DELETE)
     * @param filePath the file path
     * @param success whether the operation was successful
     */
    public static void logFileOperation(String operation, String filePath, boolean success) {
        String status = success ? "SUCCESS" : "FAILED";
        logger.debug("FILE: {} {} - {}", operation, filePath, status);
    }

    //endregion
}
