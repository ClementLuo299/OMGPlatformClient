package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;

import javafx.stage.Stage;

/**
 * Manages the initialization and configuration of the application's UI.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited May 31, 2025
 * @since 1.0
 */
public class UIManagement {

    /**
     * Initializes the UI and navigates to the initial screen.
     */
    public static void initialize() throws Exception {
        try {
            // Get initial screen
            ScreenLoadable initialScreen = GUIConfig.getInitialScreen();

            // Navigate to initial screen
            ScreenManager manager = ScreenManager.getInstance();

            manager.navigateTo(initialScreen);
        } catch (Exception e) {
            System.err.println("Failed to navigate to initial screen");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Configures the primary stage with title, dimensions, and constraints.
     *
     * @param stage The primary stage of the application.
     */
    public static void configureStage(Stage stage) {
        stage.setTitle(GUIConfig.getAppTitle());
        stage.setWidth(GUIConfig.getWindowWidth());
        stage.setHeight(GUIConfig.getWindowHeight());
        stage.setMinWidth(GUIConfig.getMinWindowWidth());
        stage.setMinHeight(GUIConfig.getMinWindowHeight());
        stage.centerOnScreen(); // Center the window
    }
}
