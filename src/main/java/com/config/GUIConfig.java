package com.config;

import com.core.screens.ScreenLoadable;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Configuration class for the JavaFX GUI window and initial scene settings.
 * This class provides centralized configuration for window dimensions,
 * application title, and initial screen settings. All configurations
 * are immutable and accessed through static methods.
 *
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
@UtilityClass
public class GUIConfig {
    
    // Application window settings
    public static final String APP_TITLE = "OMG Platform";
    public static final int WINDOW_WIDTH = 1500;
    public static final int WINDOW_HEIGHT = 800;
    public static final int MIN_WINDOW_WIDTH = 800;
    public static final int MIN_WINDOW_HEIGHT = 600;

    // Main Scene settings
    public static final int MAIN_SCENE_WIDTH = 1400;
    public static final int MAIN_SCENE_HEIGHT = 800;

    // Screen management settings
    public static final ScreenLoadable INITIAL_SCREEN = ScreenRegistry.LOGIN;
    public static final int SCREEN_CACHE_SIZE = 10;

    // Preloaded screens - empty by default
    public static final List<ScreenLoadable> PRELOAD_SCREENS = List.of(
            //ScreenRegistry.DASHBOARD,
            //ScreenRegistry.GAME_LIBRARY
    );
}
