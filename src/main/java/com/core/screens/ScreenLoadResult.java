package com.core.screens;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;

import javafx.scene.Parent;

/**
 * Container class that holds the results of loading a screen.
 * Pairs the loaded screen content (Parent node) with its controller.
 * Provides validation and error handling for screen loading results.
 *
 * @param <T>        The type of the screen's controller
 * @param root       The root node of the loaded screen
 * @param controller The controller instance for the loaded screen
 * @author Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public record ScreenLoadResult<T>(Parent root, T controller) {

    /**
     * Creates a new ScreenLoadResult with the specified root node and controller.
     * Validates that both parameters are not null and logs any validation errors.
     *
     * @param root       The root node of the loaded screen
     * @param controller The controller instance for the screen
     * @throws IllegalArgumentException if either parameter is null
     */
    public ScreenLoadResult {
        validateParameters(root, controller);
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

    // ==================== PUBLIC VALIDATION METHODS ====================
    
    /**
     * Validates that this ScreenLoadResult contains valid data.
     * Checks that both root and controller are not null and accessible.
     *
     * @return true if the result is valid, false otherwise
     */
    public boolean isValid() {
        try {
            validateParameters(root, controller);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Validates that this ScreenLoadResult contains valid data.
     * Throws an exception if validation fails.
     *
     * @throws IllegalStateException if the result is invalid
     */
    public void validate() {
        validateParameters(root, controller);
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Validates that the root and controller parameters are not null.
     * Provides specific error messages for each validation failure.
     *
     * @param root The root node to validate
     * @param controller The controller to validate
     * @throws IllegalArgumentException if either parameter is null
     */
    private static void validateParameters(Parent root, Object controller) {
        if (root == null) {
            String errorMsg = "Root node cannot be null in ScreenLoadResult";
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "ScreenLoadResult validation failed",
                ErrorCategory.RESOURCE,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
        }
        
        if (controller == null) {
            String errorMsg = "Controller cannot be null in ScreenLoadResult";
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "ScreenLoadResult validation failed",
                ErrorCategory.RESOURCE,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
        }
    }
}