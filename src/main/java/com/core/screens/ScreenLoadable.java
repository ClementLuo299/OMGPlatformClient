package com.core.screens;

import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;

import lombok.Getter;
import lombok.NonNull;

import java.net.URL;
import java.util.function.Supplier;

/**
 * Represents a screen definition that maps FXML, CSS, controllers, and view models.
 * This class provides a builder pattern for creating screen configurations with
 * optional CSS styling and view model injection.
 *
 * CSS and view models are optional - screens can be defined with just FXML and controller.
 * View models are provided via a supplier to ensure proper dependency injection.
 *
 * @author Clement Luo
 * @date May 19, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
@Getter
public class ScreenLoadable {

    // ==================== INSTANCE FIELDS ====================
    
    @NonNull
    private final String fxmlPath;
    
    @NonNull
    private final Class<?> controllerType;
    
    private final String cssPath;
    private final Supplier<?> viewModelSupplier;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Private constructor for factory method.
     */
    private ScreenLoadable(String fxmlPath, Class<?> controllerType, String cssPath, Supplier<?> viewModelSupplier) {
        this.fxmlPath = fxmlPath;
        this.controllerType = controllerType;
        this.cssPath = cssPath;
        this.viewModelSupplier = viewModelSupplier;
    }

    // ==================== FACTORY METHOD ====================
    /**
     * Factory method to create and validate a ScreenLoadable instance.
     */
    public static ScreenLoadable of(String fxmlPath, Class<?> controllerType, String cssPath, Supplier<?> viewModelSupplier) {
        if (fxmlPath == null || fxmlPath.trim().isEmpty()) {
            throw new IllegalStateException("FXML path cannot be null or empty");
        }
        if (controllerType == null) {
            throw new IllegalStateException("Controller type cannot be null");
        }
        if (!fxmlPath.startsWith("/")) {
            throw new IllegalStateException("FXML path must start with '/': " + fxmlPath);
        }
        if (cssPath != null && !cssPath.trim().isEmpty() && !cssPath.startsWith("/")) {
            throw new IllegalStateException("CSS path must start with '/': " + cssPath);
        }
        ScreenLoadable screen = new ScreenLoadable(fxmlPath, controllerType, cssPath, viewModelSupplier);
        // Only validate resources, not ViewModel (to avoid circular dependency)
        screen.validateResources();
        return screen;
    }

    // ==================== PUBLIC UTILITY METHODS ====================
    
    /**
     * Checks if this screen has a view model defined.
     */
    public boolean hasViewModel() { 
        return viewModelSupplier != null; 
    }
    
    /**
     * Checks if this screen has CSS styling defined.
     */
    public boolean hasCss() { 
        return cssPath != null && !cssPath.trim().isEmpty(); 
    }
    
    /**
     * Gets a human-readable name for this screen based on the controller class.
     */
    public String getScreenName() {
        return controllerType != null ? controllerType.getSimpleName() : "Unknown";
    }
    
    /**
     * Validates that all required resources exist and are accessible.
     * Throws IllegalStateException if validation fails.
     */
    public void validate() {
        try {
            // Validate FXML resource exists
            if (!isResourceAccessible(fxmlPath)) {
                String errorMsg = "FXML resource not found: " + fxmlPath;
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen validation failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Validate CSS resource exists if specified
            if (hasCss() && !isResourceAccessible(cssPath)) {
                String errorMsg = "CSS resource not found: " + cssPath;
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen validation failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.MEDIUM
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Validate controller class is accessible
            validateControllerClass();
            
            // Validate view model supplier if provided
            if (hasViewModel()) {
                validateViewModelSupplier();
            }
            
        } catch (Exception e) {
            if (!(e instanceof IllegalStateException)) {
                ErrorHandler.handleNonCriticalError(e, 
                    "Unexpected error during screen validation: " + getScreenName(),
                    ErrorCategory.CONFIGURATION,
                    ErrorSeverity.HIGH);
            }
            throw e;
        }
    }
    
    /**
     * Validates only resources (FXML and CSS) without ViewModel validation.
     * This avoids circular dependency issues during initialization.
     */
    public void validateResources() {
        try {
            // Validate FXML resource exists
            if (!isResourceAccessible(fxmlPath)) {
                String errorMsg = "FXML resource not found: " + fxmlPath;
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen validation failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Validate CSS resource exists if specified
            if (hasCss() && !isResourceAccessible(cssPath)) {
                String errorMsg = "CSS resource not found: " + cssPath;
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen validation failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.MEDIUM
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Validate controller class is accessible
            validateControllerClass();
            
        } catch (Exception e) {
            if (!(e instanceof IllegalStateException)) {
                ErrorHandler.handleNonCriticalError(e, 
                    "Unexpected error during screen validation: " + getScreenName(),
                    ErrorCategory.CONFIGURATION,
                    ErrorSeverity.HIGH);
            }
            throw e;
        }
    }
    
    /**
     * Validates that the controller class can be instantiated.
     */
    public void validateControllerClass() {
        try {
            // Check if class has a default constructor
            controllerType.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            String errorMsg = "Controller class " + controllerType.getSimpleName() + 
                            " must have a default constructor";
            ErrorHandler.handleNonCriticalError(
                new IllegalStateException(errorMsg, e),
                "Controller validation failed",
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.HIGH
            );
            throw new IllegalStateException(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "Failed to validate controller class: " + controllerType.getSimpleName();
            ErrorHandler.handleNonCriticalError(e, 
                errorMsg,
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.HIGH);
            throw new IllegalStateException(errorMsg, e);
        }
    }
    
    /**
     * Validates that the view model supplier can create a valid instance.
     */
    public void validateViewModelSupplier() {
        try {
            if (viewModelSupplier == null) {
                throw new IllegalStateException("ViewModel supplier is null");
            }
            
            // Test the supplier by creating an instance
            Object viewModel = viewModelSupplier.get();
            if (viewModel == null) {
                throw new IllegalStateException("ViewModel supplier returned null");
            }
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate view model supplier for screen: " + getScreenName();
            ErrorHandler.handleNonCriticalError(e, 
                errorMsg,
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.MEDIUM);
            throw new IllegalStateException(errorMsg, e);
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Checks if a resource is accessible using multiple class loaders.
     */
    private boolean isResourceAccessible(String resourcePath) {
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            return false;
        }
        
        // Try current class loader
        URL resource = getClass().getResource(resourcePath);
        if (resource != null) {
            return true;
        }
        
        // Try thread context class loader
        resource = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        if (resource != null) {
            return true;
        }
        
        // Try system class loader
        resource = ClassLoader.getSystemResource(resourcePath);
        return resource != null;
    }

    // ==================== EXPLICIT GETTERS ====================
    public String getFxmlPath() { return fxmlPath; }
    public Class<?> getControllerType() { return controllerType; }
    public String getCssPath() { return cssPath; }
    public Supplier<?> getViewModelSupplier() { return viewModelSupplier; }
}