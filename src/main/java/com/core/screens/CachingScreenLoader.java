package com.core.screens;

import com.core.screens.ScreenManagerConfig;
import com.utils.error_handling.ErrorHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A screen loader that caches loaded screens in memory for faster subsequent access.
 * This loader preloads a set of screens specified by the supplied {@link ScreenManagerConfig}.
 *
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class CachingScreenLoader {
    /** Stores already-parsed screens; key = template, value = immutable result. */
    private final Map<ScreenLoadable, ScreenLoadResult<?>> screenCache = new HashMap<>();

    /**
     * The configuration for this screen loader.
     * Contains settings for caching and preloading.
     */
    private final ScreenManagerConfig config;

    /**
     * Constructs a new CachingScreenLoader with the specified configuration.
     *
     * @param config The configuration for this screen loader
     */
    public CachingScreenLoader(ScreenManagerConfig config) {
        this.config = config;
        for(ScreenLoadable screen : config.getPreloadScreens()) {
            screenCache.put(screen, loadScreenFresh(screen));
        }
    }

    /**
     * Loads a screen, returning a cached copy when available.
     *
     * @param screen the template that describes where to find the FXML, CSS and
     *               optional ViewModel information.
     * @param <T>    the concrete controller type declared inside the requested
     *               FXML document.
     *
     * @return an immutable {@link ScreenLoadResult} bundling the root node and
     *         its controller.
     *
     * @throws IllegalArgumentException if {@code screen} is {@code null} or the
     *                                  resource path is invalid.
     */
    @SuppressWarnings("unchecked")
    public <T> ScreenLoadResult<T> loadScreen(ScreenLoadable screen) {
        //Check if the screen is cached
        if(screen.isCacheable()) {
            //Get a cached screen and return
            ScreenLoadResult<?> cachedScreen = screenCache.get(screen);
            if(cachedScreen != null) {
                return (ScreenLoadResult<T>) cachedScreen;
            }
        }

        //Load fresh if not cached
        try {
            //Get FXML path
            URL location = getClass().getResource(screen.getFxmlPath());

            //Parse FXML and get controller
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            T controller = loader.getController();

            //Return screen load result
            return new ScreenLoadResult<>(root, controller);
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Critical error occurred during screen loading");
        }
        return null;
    }

    /**
     * Loads a screen completely fresh, bypassing the cache.
     * @param screen the template describing FXML, CSS and related metadata;
     *               must not be {@code null}.
     * @param <T>    the concrete controller type declared inside the FXML file.
     *
     * @return a brand-new {@code ScreenLoadResult} containing the freshly
     *         constructed root node and controller, or {@code null} if a fatal
     *         error occurs (the error will have been forwarded to
     *         {@link com.utils.error_handling.ErrorHandler}).
     *
     * @throws IllegalArgumentException if the FXML resource cannot be resolved.
     */
    public <T> ScreenLoadResult<T> loadScreenFresh(ScreenLoadable screen) {
        try {
            //Get FXML path
            URL location = getClass().getResource(screen.getFxmlPath());

            //Parse FXML and get controller
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            T controller = loader.getController();

            //Return screen load result
            return new ScreenLoadResult<>(root, controller);
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Critical error occurred during screen loading");
        }
        return null;
    }

    /**
     * Clears the screen cache.
     */
    public void clearCache() { screenCache.clear(); }
}