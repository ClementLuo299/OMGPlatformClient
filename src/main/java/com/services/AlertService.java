package com.services;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Handles GUI alerts
 *
 * @authors Clement Luo
 * @date May 18, 2025
 */
public class AlertService {

    /**
     * Display error message
     *
     * @param title The title of the alert
     * @param message The message of the alert
     */
    public void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Display info message
     *
     * @param title The title of the alert
     * @param message The message of the alert
     */
    public void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}