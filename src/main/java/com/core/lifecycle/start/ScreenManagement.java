package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.config.ScreenManagementConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenLoadable;

import javafx.stage.Stage;

/**
 * Boots up and configures the application's screen manager.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited May 31, 2025
 * @since 1.0
 */
public class ScreenManagement {

    /**
     * Initializes the screen manager.
     */
    public static void initialize(Stage primaryStage) throws Exception {
        ScreenManagementConfig config = buildScreenConfig();
        ScreenManager.initializeInstance(primaryStage, config);
    }

    /**
     * Builds and returns the `ScreenConfig` object for the application.
     *
     * @return ScreenConfig configured with preloaded screens and caching.
     */
    private static ScreenManagementConfig buildScreenConfig() {
        //Set caching config
        ScreenManagementConfig.Builder builder = new ScreenManagementConfig.Builder()
                .setCacheSize(GUIConfig.getScreenCacheSize())
                .setEnableCaching(GUIConfig.isEnableCaching());

        //Add preloaded screens from config
        for(ScreenLoadable screen : GUIConfig.getPreloadScreens()) {
            builder.addPreloadScreen(screen);
        }

        //Return built config
        return builder.build();
    }
}
