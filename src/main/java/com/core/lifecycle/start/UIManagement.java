package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;

import javafx.stage.Stage;

/**
 * Manages the initialization and configuration of the application's UI.
 * Registers itself as a startup task for coordinated initialization.
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
     * This method is called directly by LifecycleManager for backward compatibility.
     */
    public static void initialize(Stage stage) throws Exception {
        primaryStage = stage;
        try {
            // Get initial screen
            ScreenLoadable initialScreen = GUIConfig.getInitialScreen();

            // Navigate to initial screen
            ScreenManager manager = ScreenManager.getInstance();
            manager.navigateTo(initialScreen);
            
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Failed to navigate to initial screen", 
                                           ErrorCategory.CONFIGURATION, ErrorSeverity.HIGH);
            throw e; // Re-throw to let LifecycleManager handle it
        }
    }

    /**
     * Registers UI management as a startup task.
     * Should be called before startup begins.
     * 
     * @param stage the primary stage
     */
    public static void registerAsStartupTask(Stage stage) {
        primaryStage = stage;
        StartupManager.registerStartupTask(new StartupTask() {
            @Override
            public void execute() throws Exception {
                // Get initial screen
                ScreenLoadable initialScreen = GUIConfig.getInitialScreen();

                // Navigate to initial screen
                ScreenManager manager = ScreenManager.getInstance();
                manager.navigateTo(initialScreen);
            }
            
            @Override
            public String getName() {
                return "UIManagement";
            }
        });
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
