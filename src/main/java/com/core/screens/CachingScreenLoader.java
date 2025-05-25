package com.core.screens;

import com.config.ScreenManagementConfig;
import com.config.Screens;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for loading and caching GUI screens to optimize performance.
 * Ensures that frequently used screens are preloaded and reused, reducing the overhead
 * of re-initializing FXML views and controllers.
 *
 * Features:
 * - Loads screens dynamically via FXML.
 * - Caches screens for quick access.
 * - Handles screen invalidation for managing memory efficiently.
 *
 * Use in conjunction with the ScreenManager to streamline navigation.
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class CachingScreenLoader implements ScreenLoadingStrategy {
    private final Map<ScreenTemplate, Parent> screenCache = new HashMap<>();
    private final ScreenManagementConfig config;

    public CachingScreenLoader(ScreenManagementConfig config) { this.config = config; }

    @Override
    public <T> ScreenLoadResult<T> loadScreen(ScreenTemplate screen) {
        try {
            // Debug output
            System.out.println("Loading screen: " + screen.getFxmlPath());
            URL location = getClass().getResource(screen.getFxmlPath());
            System.out.println("Resource URL: " + location);

            if (location == null) {
                throw new IOException("Could not find resource: " + screen.getFxmlPath());
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            T controller = loader.getController();

            System.out.println("Successfully loaded FXML");
            System.out.println("Root node type: " + root.getClass());
            System.out.println("Root size: " + root.getBoundsInLocal());
            System.out.println("Controller: " + (controller != null ? controller.getClass() : "null"));

            return new ScreenLoadResult<>(root, controller);
        } catch (Exception e) {
            System.err.println("Error loading screen: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load screen: " + screen.getFxmlPath(), e);
        }

    }

    private <T> ScreenLoadResult<T> loadFreshScreen(ScreenTemplate screen) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(screen.getFxmlPath()));
        Parent root = loader.load();
        T controller = loader.getController();
        return new ScreenLoadResult<>(root, controller);
    }

    public void clearCache() { screenCache.clear(); }
}