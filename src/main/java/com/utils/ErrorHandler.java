package com.utils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A comprehensive error handling utility for consistent error management across the application.
 * Provides methods for handling both critical and non-critical errors with various levels of logging,
 * user feedback, and recovery options.
 *
 * @authors Clement Luo,
 * @date May 22, 2025
 * @edited June 20, 2025
 * @since 1.0
 */
public final class ErrorHandler {

    /**
     * Logger instance used for all error and debug logging within this class.
     * 
     * <p>Logging levels used:
     * <ul>
     *   <li>SEVERE: For critical errors that affect application stability
     *   <li>WARNING: For non-critical issues that might need attention
     *   <li>INFO: For important application events and state changes
     *   <li>FINE/FINER/FINEST: For detailed debugging information
     * </ul>
     */
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());

    /**
     * Global error handler that will be notified of all errors processed by this ErrorHandler.
     * Can be set using {@link #setGlobalErrorHandler(Consumer)} to implement custom error
     * reporting or monitoring. The handler will be called on the same thread that encountered
     * the error, so it should be non-blocking and thread-safe.
     */
    private static Consumer<Throwable> globalErrorHandler = null;
    
    /**
     * Strategy for recovering from errors before they cause the application to exit.
     * When set, this function will be called for critical errors before the application
     * terminates. If it returns true, the application will continue running; if false,
     * the application will exit. This allows for custom recovery logic based on the
     * specific error that occurred.
     */
    private static Function<Throwable, Boolean> errorRecoveryStrategy = null;

    private ErrorHandler() {
        // Private constructor to prevent instantiation
    }

    // Logging methods with different severity levels
    public static void debug(String message) {
        logger.fine(message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void error(String message) {
        logger.severe(message);
    }

    // Core error handling methods
    public static void handleNonCriticalError(Throwable e, String userMessage) {
        logError(Level.WARNING, e, userMessage);
        showAlert("Warning", userMessage, e, Alert.AlertType.WARNING);
        notifyGlobalHandler(e);
    }

    public static void handleCriticalError(Throwable e, String userMessage) {
        logError(Level.SEVERE, e, userMessage);
        boolean shouldExit = true;

        if (errorRecoveryStrategy != null) {
            shouldExit = !errorRecoveryStrategy.apply(e);
        }

        if (shouldExit) {
            showAlertAndExit("Critical Error", userMessage, e);
        }
    }

    public static <T> T safeExecute(Callable<T> task, String errorMessage) {
        try {
            return task.call();
        } catch (Exception e) {
            handleNonCriticalError(e, errorMessage);
            return null;
        }
    }

    public static void safeExecute(Runnable task, String errorMessage) {
        safeExecute(() -> {
            task.run();
            return null;
        }, errorMessage);
    }

    public static void setGlobalErrorHandler(Consumer<Throwable> handler) {
        globalErrorHandler = handler;
    }

    public static void setErrorRecoveryStrategy(Function<Throwable, Boolean> strategy) {
        errorRecoveryStrategy = strategy;
    }

    // Alert and dialog utilities
    private static void showAlert(String title, String header, Throwable e, Alert.AlertType type) {
        if (Platform.isFxApplicationThread()) {
            showAlertOnFxThread(title, header, e, type, false);
        } else {
            Platform.runLater(() -> showAlertOnFxThread(title, header, e, type, false));
        }
    }

    private static void showAlertAndExit(String title, String header, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            showAlertOnFxThread(title, header, e, Alert.AlertType.ERROR, true);
        } else {
            Platform.runLater(() -> showAlertOnFxThread(title, header, e, Alert.AlertType.ERROR, true));
        }
    }

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

    // Utility methods
    private static void logError(Level level, Throwable e, String message) {
        logger.log(level, message, e);
        notifyGlobalHandler(e);
    }

    private static void notifyGlobalHandler(Throwable e) {
        if (globalErrorHandler != null) {
            try {
                globalErrorHandler.accept(e);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error in global error handler", ex);
            }
        }
    }

    private static void safelyExit() {
        try {
            Platform.exit();
        } finally {
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception> RuntimeException rethrow(E exception) throws E {
        throw exception;
    }

    public static <T> T unchecked(ThrowingSupplier<T> task) {
        try {
            return task.get();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}