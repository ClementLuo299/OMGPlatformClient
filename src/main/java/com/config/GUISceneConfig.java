package com.config;

import com.core.ScreenManager;

/**
 * Configuration class for the JavaFX GUI window and initial scene settings.
 * This class provides centralized configuration for window dimensions,
 * application title, and initial screen settings. All configurations
 * are immutable and accessed through static methods.
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class GUISceneConfig {
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

    /** FXML file path for the initial screen */
    private static final String INITIAL_SCREEN = ScreenManager.LOGIN_SCREEN;

    /** CSS file path for the initial screen */
    private static final String INITIAL_SCREEN_CSS = ScreenManager.LOGIN_CSS;

    /** GETTERS */
    public static String getAppTitle() { return APP_TITLE; }
    public static int getWindowWidth() { return WINDOW_WIDTH; }
    public static int getWindowHeight() { return WINDOW_HEIGHT; }
    public static int getMinWindowWidth() { return MIN_WINDOW_WIDTH; }
    public static int getMinWindowHeight() { return MIN_WINDOW_HEIGHT; }
    public static String getInitialScreen() { return INITIAL_SCREEN; }
    public static String getInitialScreenCSS() { return INITIAL_SCREEN_CSS; }

}
