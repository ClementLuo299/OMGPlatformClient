package com.utils.error_handling;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Dialog utility for the application.
 * Provides methods for displaying dialogs to the user.
 * 
 * @authors Clement Luo
 * @date June 22, 2025
 * @edited June 22, 2025
 * @since 1.0
 * @version 1.0
 */
public final class Dialog {

    /**
     * Tells the dialog utility that the application is shutting down.
     * Stops the application from showing dialogs during shutdown.
     */
    private static final AtomicBoolean isShuttingDown = new AtomicBoolean(false);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Dialog() { throw new UnsupportedOperationException("Utility class cannot be instantiated"); }

    //region ==================== ALERT DIALOGS ====================

    /**
     * Shows a warning dialog with the specified parameters.
     * Handles thread safety by ensuring the dialog is shown on the JavaFX Application Thread.
     *
     * @param title the dialog title
     * @param message the warning message
     * @param throwable the throwable to display (can be null)
     */
    public static void showWarning(String title, String message, Throwable throwable) {
        if (isShuttingDown.get()) {
            AppLogger.warning("Ignoring warning dialog during shutdown: " + message);
            return;
        }
        showAlert(title, message, throwable, Alert.AlertType.WARNING);
    }

    /**
     * Shows an error dialog with the specified parameters.
     * Handles thread safety by ensuring the dialog is shown on the JavaFX Application Thread.
     *
     * @param title the dialog title
     * @param message the error message
     * @param throwable the throwable to display (can be null)
     */
    public static void showError(String title, String message, Throwable throwable) {
        if (isShuttingDown.get()) {
            AppLogger.warning("Ignoring error dialog during shutdown: " + message);
            return;
        }
        showAlert(title, message, throwable, Alert.AlertType.ERROR);
    }

    /**
     * Shows an error dialog and exits the application after the user acknowledges it.
     * Handles thread safety by ensuring the dialog is shown on the JavaFX Application Thread.
     *
     * @param title the dialog title
     * @param message the error message
     * @param throwable the throwable to display (can be null)
     */
    public static void showErrorAndExit(String title, String message, Throwable throwable) {
        showAlertAndExit(title, message, throwable);
    }

    /**
     * Shows an information dialog with the specified parameters.
     * Handles thread safety by ensuring the dialog is shown on the JavaFX Application Thread.
     *
     * @param title the dialog title
     * @param message the information message
     */
    public static void showInfo(String title, String message) {
        if (isShuttingDown.get()) {
            AppLogger.warning("Ignoring info dialog during shutdown: " + message);
            return;
        }
        showAlert(title, message, null, Alert.AlertType.INFORMATION);
    }

    //endregion

    //region ==================== CONFIRMATION DIALOGS ====================

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
     * Safely exits the application, ensuring proper cleanup.
     */
    private static void safelyExit() {
        if (isShuttingDown.compareAndSet(false, true)) {
            try {
                AppLogger.info("Initiating application shutdown...");
                Platform.exit();
            } finally {
                System.exit(1);
            }
        }
    }

    //endregion

    //region ==================== SHUTDOWN MANAGEMENT ====================

    /**
     * Marks the application as shutting down to prevent dialogs during shutdown.
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
}
