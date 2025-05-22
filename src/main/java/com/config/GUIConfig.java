package com.config;

import com.core.screens.ScreenView;

import java.util.List;

/**
 * Configuration class for the JavaFX GUI window and initial scene settings.
 * This class provides centralized configuration for window dimensions,
 * application title, and initial screen settings. All configurations
 * are immutable and accessed through static methods.
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class GUIConfig {
    /** Application window title */
    private static final String APP_TITLE = "OMG Platform";

    /** Default window width (in pixels) */
    private static final int WINDOW_WIDTH = 1500;

    /** Default window height (in pixels) */
    private static final int WINDOW_HEIGHT = 800;

    /** Minimum allowed window width (in pixels) */
    private static final int MIN_WINDOW_WIDTH = 800;

    /** Minimum allowed window height (in pixels) */
    private static final int MIN_WINDOW_HEIGHT = 600;

    /** Initial screen */
    private static final ScreenView INITIAL_SCREEN_VIEW = ScreenView.LOGIN;

    private static final int SCREEN_CACHE_SIZE = 10;

    private static final boolean ENABLE_CACHING = true;

    public static final List<ScreenView> PRELOAD_SCREEN_VIEWS = List.of(
            ScreenView.DASHBOARD,
            ScreenView.GAME_LIBRARY
    );

    /** GETTERS */
    public static String getAppTitle() { return APP_TITLE; }
    public static int getWindowWidth() { return WINDOW_WIDTH; }
    public static int getWindowHeight() { return WINDOW_HEIGHT; }
    public static int getMinWindowWidth() { return MIN_WINDOW_WIDTH; }
    public static int getMinWindowHeight() { return MIN_WINDOW_HEIGHT; }
    public static ScreenView getInitialScreen() { return INITIAL_SCREEN_VIEW; }
    public static int getScreenCacheSize() { return SCREEN_CACHE_SIZE; }
    public static boolean isEnableCaching() { return ENABLE_CACHING; }
    public static List<ScreenView> getPreloadScreens() { return PRELOAD_SCREEN_VIEWS; }

}
