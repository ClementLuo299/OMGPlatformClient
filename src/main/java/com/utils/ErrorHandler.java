package com.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.logging.Logger;

/**
 * A utility class for managing errors consistently across the application.
 * This class provides methods to handle both critical and non-critical errors,
 * log them appropriately, and communicate meaningful feedback to the user.
 *
 * @authors Clement Luo,
 * @date May 22, 2025
 */
public class ErrorHandler {

    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());

    /**
     * Generic handler for non-critical errors.
     */
    public static void handleNonCriticalError(Exception e, String userMessage) {
        logger.warning("Non-critical error: " + e.getMessage());
        showAlert(userMessage, e.getMessage(), Alert.AlertType.WARNING);
    }

    /**
     * Generic handler for critical errors.
     * Exits the application after informing the user.
     */
    public static void handleCriticalError(Exception e, String userMessage) {
        logger.severe("Critical error: " + e.getMessage());
        showAlert(userMessage, e.getMessage(), Alert.AlertType.ERROR);
        safelyExit();
    }

    private static void showAlert(String header, String content, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    private static void safelyExit() {
        Platform.exit();
        System.exit(1);
    }
}