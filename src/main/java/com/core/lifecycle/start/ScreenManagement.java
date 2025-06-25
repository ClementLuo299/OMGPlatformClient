package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.config.ScreenManagementConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;

import javafx.stage.Stage;

/**
 * Boots up and configures the application's screen manager.
 * Registers itself as a startup task for coordinated initialization.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenManagement {

    /**
     * The primary stage for screen management.
     */
    private static Stage primaryStage;

    /**
     * Initializes the screen manager.
     * This method is called directly by LifecycleManager for backward compatibility.
     */
    public static void initialize(Stage stage) throws Exception {
        primaryStage = stage;
        ScreenManagementConfig config = buildScreenConfig();
        ScreenManager.initializeInstance(primaryStage, config);
    }

    /**
     * Registers screen management as a startup task.
     * Should be called before startup begins.
     * 
     * @param stage the primary stage
     */
    public static void registerAsStartupTask(Stage stage) {
        primaryStage = stage;
        StartupManager.registerStartupTask(new StartupTask() {
            @Override
            public void execute() throws Exception {
                ScreenManagementConfig config = buildScreenConfig();
                ScreenManager.initializeInstance(primaryStage, config);
            }
            
            @Override
            public String getName() {
                return "ScreenManagement";
            }
        });
    }

    /**
     * Builds and returns the `ScreenConfig` object for the application.
     *
     * @return ScreenConfig configured with preloaded screens and caching.
     */
    private static ScreenManagementConfig buildScreenConfig() {
        // Set caching config
        ScreenManagementConfig.Builder builder = new ScreenManagementConfig.Builder()
                .setCacheSize(GUIConfig.getScreenCacheSize())
                .setEnableCaching(GUIConfig.isEnableCaching());

        // Add preloaded screens from config
        for(ScreenLoadable screen : GUIConfig.getPreloadScreens()) {
            builder.addPreloadScreen(screen);
        }

        // Return built config
        return builder.build();
    }
}
