package com.core;

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
     * @param screen The screen to load, containing FXML and CSS paths
     * @param controllerType The class object representing the expected controller type
     * @return A ScreenLoadResult containing both the loaded screen content and its controller
     * @throws IllegalArgumentException if the screen or controllerType is null
     */
    <T> ScreenLoadResult<T> loadScreen(Screen screen, Class<T> controllerType);
}