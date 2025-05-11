package com;

import javafx.scene.Scene;
import java.util.HashSet;
import java.util.Set;

/**
 * ThemeManager is a singleton class responsible for managing the application theme
 * (dark or light) and applying it to all registered scenes.
 * It ensures that the theme can be globally changed and that all scenes reflect this change.
 */
public class ThemeManager {
    private static ThemeManager instance;  // Singleton instance of ThemeManager
    private boolean isDarkTheme = false;  // Indicates if dark theme is currently active
    private Set<Scene> registeredScenes = new HashSet<>();  // Set of scenes to be updated when the theme changes

    // Private constructor to prevent instantiation
    private ThemeManager() {}

    /**
     * Returns the singleton instance of ThemeManager.
     * If the instance doesn't exist, it creates a new one.
     *
     * @return The singleton instance of ThemeManager.
     */
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();  // Create new instance if it doesn't exist
        }
        return instance;
    }

    /**
     * Returns whether the dark theme is currently active.
     *
     * @return true if the dark theme is active, false otherwise.
     */
    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    /**
     * Sets the theme to dark or light based on the provided value.
     * This method also updates all registered scenes to reflect the new theme.
     *
     * @param isDarkTheme boolean indicating whether to enable dark theme (true) or light theme (false)
     */
    public void setDarkTheme(boolean isDarkTheme) {
        this.isDarkTheme = isDarkTheme;

        // Apply the theme to all registered scenes after setting the theme
        applyThemeToAllScenes();
    }

    /**
     * Registers a scene to be updated when the theme changes.
     * The scene will have the current theme applied whenever the theme is changed.
     *
     * @param scene The scene to register.
     */
    public void registerScene(Scene scene) {
        if (scene != null && !registeredScenes.contains(scene)) {
            registeredScenes.add(scene);  // Add the scene to the registered list
            applyTheme(scene);  // Immediately apply the current theme to the scene
        }
    }

    /**
     * Applies the current theme to a specific scene.
     * If the dark theme is enabled, the corresponding stylesheet will be applied.
     * If the theme is not dark, the default stylesheet (assumed to be set in the controller) is applied.
     *
     * @param scene The scene to apply the theme to.
     */
    public void applyTheme(Scene scene) {
        if (scene != null) {
            scene.getStylesheets().clear();  // Clear any existing stylesheets
            if (isDarkTheme) {
                // If dark theme is active, apply the dark theme stylesheet
                scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            }
        }
    }

    /**
     * Applies the current theme to all registered scenes.
     * This is typically called after the theme has been changed to ensure all scenes reflect the update.
     */
    private void applyThemeToAllScenes() {
        for (Scene scene : registeredScenes) {
            applyTheme(scene);  // Apply the theme to each registered scene
        }
    }
}
