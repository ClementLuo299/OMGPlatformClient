package com.core.screens;

import com.utils.ErrorHandler;
import javafx.scene.Parent;

/**
 * Container class that holds the results of loading a screen.
 * Pairs the loaded screen content (Parent node) with its controller.
 *
 * @param <T>        The type of the screen's controller
 * @param root       The root node of the loaded screen
 * @param controller The controller instance for the loaded screen
 * @author Clement Luo
 * @date May 18, 2025
 * @edited June 1, 2025
 * @since 1.0
 */
public record ScreenLoadResult<T>(Parent root, T controller) {

    /**
     * Creates a new ScreenLoadResult with the specified root node and controller.
     *
     * @param root       The root node of the loaded screen
     * @param controller The controller instance for the screen
     * @throws IllegalArgumentException if either parameter is null
     */
    public ScreenLoadResult {
        if (root == null || controller == null) {
            ErrorHandler.handleCriticalError(new IllegalArgumentException("ScreenLoadResult cannot be null"), "ScreenLoadResult cannot be null");
        }
    }

    /**
     * Gets the root node of the loaded screen.
     *
     * @return The JavaFX Parent node containing the screen content
     */
    @Override
    public Parent root() {
        return root;
    }

    /**
     * Gets the controller instance for the loaded screen.
     *
     * @return The controller instance of type T
     */
    @Override
    public T controller() { return controller; }
}