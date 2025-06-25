package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.core.screens.ScreenCacheConfig;
import com.core.screens.ScreenManager;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

import javafx.stage.Stage;

/**
 * Manages the initialization of screen management components.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenManagement {

    /**
     * Initializes the ScreenManager with configuration.
     * This method is called directly by LifecycleManager.
     *
     * @param primaryStage the primary stage
     */
    public static void initialize(Stage primaryStage) {
        Logging.info("Initializing ScreenManager...");
        
        try {
            ScreenCacheConfig config = buildScreenConfig();
            ScreenManager.initializeInstance(primaryStage, config);
            Logging.info("ScreenManager initialized successfully");
            
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Failed to initialize ScreenManager", 
                                           ErrorCategory.SYSTEM, ErrorSeverity.HIGH);
        }
    }

    /**
     * Builds and returns the `ScreenCacheConfig` object for the application.
     *
     * @return ScreenCacheConfig configured with preloaded screens.
     */
    private static ScreenCacheConfig buildScreenConfig() {
        // Create builder for screen cache configuration
        ScreenCacheConfig.Builder builder = new ScreenCacheConfig.Builder();

        // Add preloaded screens from config
        for (var screen : GUIConfig.PRELOAD_SCREENS) {
            builder.addPreloadScreen(screen);
        }

        return builder.build();
    }
}
