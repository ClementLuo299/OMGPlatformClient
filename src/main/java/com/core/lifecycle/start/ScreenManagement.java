package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.config.ScreenManagementConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;
import com.utils.error_handling.Logging;
import javafx.stage.Stage;

/**
 * Boots up and configures the application's screen manager.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenManagement {

    /**
     * Initializes the ScreenManager before any startup tasks run.
     * This ensures ScreenManager is fully ready when UIManagement tries to navigate.
     * 
     * @param primaryStage the primary stage
     */
    public static void initializeScreenManager(Stage primaryStage) {
        Logging.info("Initializing ScreenManager...");
        
        // Build configuration with caching settings and preloaded screens
        ScreenManagementConfig config = buildScreenConfig();
        
        // Initialize the ScreenManager singleton with stage and config
        ScreenManager.initializeInstance(primaryStage, config);
        
        Logging.info("ScreenManager initialized successfully");
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
