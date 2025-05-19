package com.core;

import com.config.ScreenConfig;
import com.core.Screen;
import com.core.ScreenLoadResult;
import com.core.ScreenLoadingStrategy;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class CachingScreenLoader implements ScreenLoadingStrategy {
    private final Map<Screen, Parent> screenCache = new HashMap<>();
    private final ScreenConfig config;

    public CachingScreenLoader(ScreenConfig config) {
        this.config = config;
    }

    @Override
    public <T> ScreenLoadResult<T> loadScreen(Screen screen, Class<T> controllerType) {
        try {
            if (!screen.isCacheable() || !config.isEnableCaching()) {
                return loadFreshScreen(screen, controllerType);
            }

            Parent root = screenCache.get(screen);
            if (root == null) {
                ScreenLoadResult<T> result = loadFreshScreen(screen, controllerType);
                screenCache.put(screen, result.root());
                return result;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(screen.getFxmlPath()));
            loader.setRoot(root);
            T controller = loader.getController();
            return new ScreenLoadResult<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load screen: " + screen, e);
        }
    }

    private <T> ScreenLoadResult<T> loadFreshScreen(Screen screen, Class<T> controllerType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(screen.getFxmlPath()));
        Parent root = loader.load();
        T controller = loader.getController();
        return new ScreenLoadResult<>(root, controller);
    }

    @Override
    public void clearCache() {
        screenCache.clear();
    }
}