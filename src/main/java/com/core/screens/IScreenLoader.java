package com.core.screens;

/**
 * Interface for loading application screens.
 * Defines a contract for different screen loading implementations,
 * allowing for flexible loading mechanisms such as caching, lazy loading,
 * or direct loading.
 *
 * @author Clement Luo
 * @date May 18, 2025
 * @edited May 31, 2025
 * @since 1.0
 */
public interface IScreenLoader {

    /**
     * Loads a screen and its controller according to the implemented strategy.
     *
     * @param <T> The expected type of the screen's controller
     * @param screen The screen to load, containing FXML, controller, and possibly CSS and other resources.
     * @return A ScreenLoadResult containing both the loaded screen content and its controller
     * @throws IllegalArgumentException if the screen or controllerType is null
     */
    <T> ScreenLoadResult<T> loadScreen(ScreenLoadable screen);
}