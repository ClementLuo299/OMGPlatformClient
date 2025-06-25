package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

import javafx.stage.Stage;

/**
 * Manages the initialization and configuration of the application's UI.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class UIManagement {

    /**
     * The primary stage for UI configuration.
     */
    private static Stage primaryStage;

    /**
     * Initializes the UI and navigates to the initial screen.
     * This method is called directly by LifecycleManager.
     */
    public static void initialize(Stage stage) throws Exception {
        primaryStage = stage;
        Logging.info("Initializing UI...");
        
        try {
            // Get initial screen
            ScreenLoadable initialScreen = GUIConfig.getInitialScreen();

            // Navigate to initial screen
            ScreenManager manager = ScreenManager.getInstance();
            manager.navigateTo(initialScreen);
            
            Logging.info("UI initialized successfully");
            
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Failed to navigate to initial screen", 
                                           ErrorCategory.CONFIGURATION, ErrorSeverity.HIGH);
            throw e; // Re-throw to let LifecycleManager handle it
        }
    }

    /**
     * Configures the primary stage with title, dimensions, and constraints.
     *
     * @param stage The primary stage of the application.
     */
    public static void configureStage(Stage stage) {
        try {
            stage.setTitle(GUIConfig.getAppTitle());
            stage.setWidth(GUIConfig.getWindowWidth());
            stage.setHeight(GUIConfig.getWindowHeight());
            stage.setMinWidth(GUIConfig.getMinWindowWidth());
            stage.setMinHeight(GUIConfig.getMinWindowHeight());
            stage.centerOnScreen(); // Center the window
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, "Failed to configure application stage", 
                                              ErrorCategory.CONFIGURATION, ErrorSeverity.MEDIUM);
        }
    }
}
