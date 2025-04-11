package gui;

import javafx.scene.Scene;
import java.util.HashSet;
import java.util.Set;

public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkTheme = false;
    private Set<Scene> registeredScenes = new HashSet<>();

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean isDarkTheme) {
        this.isDarkTheme = isDarkTheme;

        // Apply the theme to all registered scenes
        applyThemeToAllScenes();
    }

    /**
     * Register a scene for theme updates
     * @param scene The scene to register
     */
    public void registerScene(Scene scene) {
        if (scene != null && !registeredScenes.contains(scene)) {
            registeredScenes.add(scene);
            applyTheme(scene);
        }
    }

    /**
     * Apply the current theme to a specific scene
     * @param scene The scene to apply the theme to
     */
    public void applyTheme(Scene scene) {
        if (scene != null) {
            scene.getStylesheets().clear();
            if (isDarkTheme) {
                scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            }
            // Note: Default CSS should be applied by the controller
        }
    }

    /**
     * Apply the current theme to all registered scenes
     */
    private void applyThemeToAllScenes() {
        for (Scene scene : registeredScenes) {
            applyTheme(scene);
        }
    }
}