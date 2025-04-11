package gui;

import javafx.scene.Scene;

public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkTheme = false;

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
    }

    public void applyTheme(Scene scene) {
        if (scene != null) {
            scene.getStylesheets().clear();
            if (isDarkTheme) {
                scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            } else {
                // Load the default stylesheet for the current screen
                // This will be handled in the controllers
            }
        }
    }
}
