package com.core.screens;

import com.config.ScreenManagementConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
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
    private final Map<ScreenView, Parent> screenCache = new HashMap<>();
    private final ScreenManagementConfig config;

    public CachingScreenLoader(ScreenManagementConfig config) {
        this.config = config;
    }

    @Override
    public <T> ScreenLoadResult<T> loadScreen(ScreenView screenView, Class<T> controllerType) {
        try {
            if (!screenView.isCacheable() || !config.isEnableCaching()) {
                return loadFreshScreen(screenView, controllerType);
            }

            Parent root = screenCache.get(screenView);
            if (root == null) {
                ScreenLoadResult<T> result = loadFreshScreen(screenView, controllerType);
                screenCache.put(screenView, result.root());
                return result;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(screenView.getFxmlPath()));
            loader.setRoot(root);
            T controller = loader.getController();
            return new ScreenLoadResult<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load screen: " + screenView, e);
        }
    }

    private <T> ScreenLoadResult<T> loadFreshScreen(ScreenView screenView, Class<T> controllerType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(screenView.getFxmlPath()));
        Parent root = loader.load();
        T controller = loader.getController();
        return new ScreenLoadResult<>(root, controller);
    }

    @Override
    public void clearCache() {
        screenCache.clear();
    }
}