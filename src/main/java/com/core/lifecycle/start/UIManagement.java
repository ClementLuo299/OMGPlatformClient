package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.config.Screens;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenTemplate;
import com.services.AlertService;
import com.services.LoginService;
import com.viewmodels.LoginViewModel;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @authors Clement Luo
 * @date May 24, 2025
 */
public class UIManagement {

    /**
     * Initializes the UI and navigates to the initial screen.
     */
    public static void initialize() throws Exception {
        System.out.println("Initializing UI...");

        // Get and validate initial screen
        ScreenTemplate initialScreen = GUIConfig.getInitialScreen();
        if (initialScreen == null) {
            throw new IllegalStateException("Initial screen not configured");
        }
        System.out.println("Initial screen: " + initialScreen.getFxmlPath());

        try {
            // Navigate to initial screen
            ScreenManager manager = ScreenManager.getInstance();
            if (manager == null) {
                throw new IllegalStateException("Screen manager not initialized");
            }

            manager.navigateTo(initialScreen);
            System.out.println("Successfully navigated to initial screen");
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
        if (stage == null) {
            throw new IllegalArgumentException("Stage cannot be null");
        }

        System.out.println("Configuring primary stage...");
        stage.setTitle(GUIConfig.getAppTitle());
        stage.setWidth(GUIConfig.getWindowWidth());
        stage.setHeight(GUIConfig.getWindowHeight());
        stage.setMinWidth(GUIConfig.getMinWindowWidth());
        stage.setMinHeight(GUIConfig.getMinWindowHeight());
        stage.centerOnScreen(); // Center the window
        System.out.println("Stage configuration complete");
    }
}
