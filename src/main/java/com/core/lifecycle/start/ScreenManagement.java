package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.config.ScreenManagementConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenTemplate;

import javafx.stage.Stage;

/**
 *
 * @authors Clement Luo
 * @date May 24, 2025
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
        for(ScreenTemplate screen : GUIConfig.getPreloadScreens()) {
            builder.addPreloadScreen(screen);
        }

        return builder.build();
    }
}
