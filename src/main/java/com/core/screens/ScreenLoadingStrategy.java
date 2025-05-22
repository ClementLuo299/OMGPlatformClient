package com.core.screens;

/**
 * Strategy interface for loading application screens.
 * Defines a contract for different screen loading implementations,
 * allowing for flexible loading mechanisms such as caching, lazy loading,
 * or direct loading.
 *
 * @author Clement Luo
 * @date May 18, 2025
 */
public interface ScreenLoadingStrategy {

    /**
     * Loads a screen and its controller according to the implemented strategy.
     *
     * @param <T> The expected type of the screen's controller
     * @param screenView The screen to load, containing FXML and CSS paths
     * @param controllerType The class object representing the expected controller type
     * @return A ScreenLoadResult containing both the loaded screen content and its controller
     * @throws IllegalArgumentException if the screen or controllerType is null
     */
    <T> ScreenLoadResult<T> loadScreen(ScreenView screenView, Class<T> controllerType);
    void clearCache();
}