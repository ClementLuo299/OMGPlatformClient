package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.Logging;
import com.utils.error_handling.enums.ErrorCategory;
import com.utils.error_handling.enums.ErrorSeverity;

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
     * Initializes the UI, navigates to the initial screen, and configures the stage.
     * This method is called directly by LifecycleManager.
     */
    public static void initialize(Stage stage) throws Exception {
        Logging.info("Initializing UI...");
        
        try {
            // Configure the stage first (before showing any content)
            configureStage(stage);
            
            // Get initial screen
            ScreenLoadable initialScreen = GUIConfig.INITIAL_SCREEN;

            // Navigate to initial screen
            ScreenManager manager = ScreenManager.getInstance();
            manager.navigateTo(initialScreen);
            
            // Show the stage explicitly
            stage.show();
            
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
    private static void configureStage(Stage stage) {
        try {
            stage.setTitle(GUIConfig.APP_TITLE);
            stage.setWidth(GUIConfig.WINDOW_WIDTH);
            stage.setHeight(GUIConfig.WINDOW_HEIGHT);
            stage.setMinWidth(GUIConfig.MIN_WINDOW_WIDTH);
            stage.setMinHeight(GUIConfig.MIN_WINDOW_HEIGHT);
            stage.centerOnScreen(); // Center the window
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, "Failed to configure application stage", 
                                              ErrorCategory.CONFIGURATION, ErrorSeverity.MEDIUM);
        }
    }
}
