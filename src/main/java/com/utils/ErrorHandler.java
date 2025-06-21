package com.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Error handling utility for the application.
 * Provides methods for handling errors with various levels of logging and user feedback.
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
     * Logger instance used for logging errors and warnings.
     */
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());

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

    //region ==================== LOGGING METHODS ====================

    /**
     * Logs a debug message. Only visible when debug logging is enabled.
     *
     * @param message the debug message to log
     */
    public static void debug(String message) { logger.fine(message); }

    /**
     * Logs a debug message with an associated throwable.
     *
     * @param message the debug message to log
     * @param throwable the throwable to log
     */
    public static void debug(String message, Throwable throwable) { logger.log(Level.FINE, message, throwable); }

    /**
     * Logs an informational message.
     *
     * @param message the info message to log
     */
    public static void info(String message) { logger.info(message); }

    /**
     * Logs a warning message.
     *
     * @param message the warning message to log
     */
    public static void warning(String message) { logger.warning(message); }

    /**
     * Logs a warning message with an associated throwable.
     *
     * @param message the warning message to log
     * @param throwable the throwable to log
     */
    public static void warning(String message, Throwable throwable) { logger.log(Level.WARNING, message, throwable); }

    /**
     * Logs an error message.
     *
     * @param message the error message to log
     */
    public static void error(String message) { logger.severe(message); }

    /**
     * Logs an error message with an associated throwable.
     *
     * @param message the error message to log
     * @param throwable the throwable to log
     */
    public static void error(String message, Throwable throwable) { logger.log(Level.SEVERE, message, throwable); }

    //endregion

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
            logger.warning("Ignoring non-critical error during shutdown: " + userMessage);
            return;
        }

        logError(Level.WARNING, throwable, userMessage);
        showAlert("Warning", userMessage, throwable, Alert.AlertType.WARNING);
    }

    /**
     * Handles a critical error that requires immediate application termination.
     * This method exits the application.
     *
     * @param throwable the exception that occurred
     * @param userMessage a user-friendly message describing the error
     */
    public static void handleCriticalError(Throwable throwable, String userMessage) {
        logError(Level.SEVERE, throwable, "FATAL: " + userMessage);
        showAlertAndExit("Fatal Error", userMessage, throwable);
    }

    //endregion

    //region ==================== SAFE EXECUTION METHODS ====================
    
    /**
     * Executes a task safely, handling any exceptions that occur.
     * If an exception occurs, it's handled as a non-critical error and null is returned.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     * @return the result of the task, or null if an exception occurred
     */
    public static <T> T safeExecute(Callable<T> task, String errorMessage) {
        try {
            return task.call();
        } catch (Exception e) {
            handleNonCriticalError(e, errorMessage);
            return null;
        }
    }

    /**
     * Executes a task safely, handling any exceptions that occur.
     * If an exception occurs, it's handled as a non-critical error.
     *
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     */
    public static void safeExecute(Runnable task, String errorMessage) {
        safeExecute(() -> {
            task.run();
            return null;
        }, errorMessage);
    }

    /**
     * Executes a task safely with a default value if the task fails.
     * If an exception occurs, it's handled as a non-critical error and the default value is returned.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     * @param defaultValue the default value to return if the task fails
     * @return the result of the task, or the default value if an exception occurred
     */
    public static <T> T safeExecuteWithDefault(Callable<T> task, String errorMessage, T defaultValue) {
        try {
            return task.call();
        } catch (Exception e) {
            handleNonCriticalError(e, errorMessage);
            return defaultValue;
        }
    }

    /**
     * Executes a task safely with a default supplier if the task fails.
     * If an exception occurs, it's handled as a non-critical error and the default supplier is called.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     * @param defaultSupplier the supplier to call if the task fails
     * @return the result of the task, or the result of the default supplier if an exception occurred
     */
    public static <T> T safeExecuteWithDefault(Callable<T> task, String errorMessage, Supplier<T> defaultSupplier) {
        try {
            return task.call();
        } catch (Exception e) {
            handleNonCriticalError(e, errorMessage);
            return defaultSupplier.get();
        }
    }

    /**
     * Executes a task and rethrows any checked exceptions as runtime exceptions.
     * This is useful for converting checked exceptions to unchecked ones.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @return the result of the task
     * @throws RuntimeException if the task throws any exception
     */
    public static <T> T unchecked(ThrowingSupplier<T> task) {
        try {
            return task.get();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    //region ==================== ALERT AND DIALOG UTILITIES ====================
    /**
     * Shows a confirmation dialog and returns the user's choice.
     * Must be called on the JavaFX Application Thread.
     *
     * @param title the dialog title
     * @param message the confirmation message
     * @return true if the user confirmed, false otherwise
     * @throws IllegalStateException if not called on the JavaFX Application Thread
     */
    public static boolean showConfirmation(String title, String message) {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Must be called on the JavaFX Application Thread");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows a confirmation dialog with custom buttons and returns the user's choice.
     * Must be called on the JavaFX Application Thread.
     *
     * @param title the dialog title
     * @param message the confirmation message
     * @param confirmText the text for the confirm button
     * @param cancelText the text for the cancel button
     * @return true if the user confirmed, false otherwise
     * @throws IllegalStateException if not called on the JavaFX Application Thread
     */
    public static boolean showConfirmation(String title, String message, String confirmText, String cancelText) {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Must be called on the JavaFX Application Thread");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType confirmButton = new ButtonType(confirmText);
        ButtonType cancelButton = new ButtonType(cancelText);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }
    //endregion

    //region ==================== PRIVATE UTILITY METHODS ====================
    /**
     * Shows an alert dialog with the specified parameters.
     * Handles thread safety by ensuring the dialog is shown on the JavaFX Application Thread.
     */
    private static void showAlert(String title, String header, Throwable e, Alert.AlertType type) {
        if (Platform.isFxApplicationThread()) {
            showAlertOnFxThread(title, header, e, type, false);
        } else {
            Platform.runLater(() -> showAlertOnFxThread(title, header, e, type, false));
        }
    }

    /**
     * Shows an alert dialog and exits the application after the user acknowledges it.
     * Handles thread safety by ensuring the dialog is shown on the JavaFX Application Thread.
     */
    private static void showAlertAndExit(String title, String header, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            showAlertOnFxThread(title, header, e, Alert.AlertType.ERROR, true);
        } else {
            Platform.runLater(() -> showAlertOnFxThread(title, header, e, Alert.AlertType.ERROR, true));
        }
    }

    /**
     * Shows an alert dialog on the JavaFX Application Thread.
     * This method must be called on the JavaFX Application Thread.
     */
    private static void showAlertOnFxThread(String title, String header, Throwable e, Alert.AlertType type, boolean exitAfter) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(e != null ? e.getMessage() : "No error details available");

        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(textArea, 0, 0);

            alert.getDialogPane().setExpandableContent(expContent);
        }

        if (exitAfter) {
            alert.setOnCloseRequest(event -> safelyExit());
        }

        alert.showAndWait();
        
        if (exitAfter) {
            safelyExit();
        }
    }

    /**
     * Logs an error with the specified level and notifies the global error handler.
     */
    private static void logError(Level level, Throwable e, String message) {
        logger.log(level, message, e);
    }

    /**
     * Safely exits the application, ensuring proper cleanup.
     */
    private static void safelyExit() {
        if (isShuttingDown.compareAndSet(false, true)) {
            try {
                logger.info("Initiating application shutdown...");
                Platform.exit();
            } finally {
                System.exit(1);
            }
        }
    }

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

    //region ==================== FUNCTIONAL INTERFACES ====================
    /**
     * A functional interface for suppliers that may throw checked exceptions.
     * This allows for cleaner exception handling in lambda expressions.
     *
     * @param <T> the type of the result
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        /**
         * Gets a result, potentially throwing an exception.
         *
         * @return the result
         * @throws Exception if an error occurs
         */
        T get() throws Exception;
    }
    //endregion
}