package com.core.screens;

import com.config.GUIConfig;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A screen loader that caches loaded screens in memory for faster subsequent access.
 * This loader preloads screens specified in GUIConfig and provides both cached and
 * fresh loading capabilities.
 *
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenLoader {

    // ==================== INSTANCE FIELDS ====================
    
    /** Stores already-parsed screens; key = screen template, value = immutable result. */
    private final Map<ScreenLoadable, ScreenLoadResult<?>> screenCache = new HashMap<>();

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructs a new ScreenLoader and preloads screens from GUIConfig.
     */
    public ScreenLoader() {
        preloadScreens();
    }

    // ==================== PUBLIC LOADING METHODS ====================
    
    /**
     * Loads a screen, returning a cached copy when available.
     *
     * @param screen the template that describes where to find the FXML, CSS and
     *               optional ViewModel information
     * @param <T> the concrete controller type declared inside the requested FXML document
     * @return an immutable {@link ScreenLoadResult} bundling the root node and its controller
     * @throws IllegalArgumentException if {@code screen} is {@code null} or the resource path is invalid
     */
    @SuppressWarnings("unchecked")
    public <T> ScreenLoadResult<T> loadScreen(ScreenLoadable screen) {
        try {
            validateScreen(screen);
            
            // Check if caching is enabled and screen is cached
            if (GUIConfig.ENABLE_SCREEN_CACHING) {
                ScreenLoadResult<?> cachedScreen = screenCache.get(screen);
                if (cachedScreen != null) {
                    return (ScreenLoadResult<T>) cachedScreen;
                }
            }

            // Load fresh if not cached or caching disabled
            ScreenLoadResult<T> result = loadScreenFresh(screen);
            
            // Cache the result for future use if caching is enabled
            if (GUIConfig.ENABLE_SCREEN_CACHING && result != null) {
                screenCache.put(screen, result);
            }
            
            return result;
            
        } catch (Exception e) {
            String errorMsg = "Failed to load screen: " + (screen != null ? screen.getFxmlPath() : "null");
            ErrorHandler.handleCriticalError(e, errorMsg);
            return null;
        }
    }

    /**
     * Loads a screen completely fresh, bypassing the cache.
     *
     * @param screen the template describing FXML, CSS and related metadata; must not be {@code null}
     * @param <T> the concrete controller type declared inside the FXML file
     * @return a brand-new {@code ScreenLoadResult} containing the freshly constructed root node and controller,
     *         or {@code null} if a fatal error occurs (the error will have been forwarded to ErrorHandler)
     * @throws IllegalArgumentException if the FXML resource cannot be resolved
     */
    public <T> ScreenLoadResult<T> loadScreenFresh(ScreenLoadable screen) {
        try {
            validateScreen(screen);
            
            // Get FXML resource location
            URL location = getFxmlResource(screen.getFxmlPath());
            
            // Parse FXML and get controller
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            T controller = loader.getController();
            
            // Validate controller was loaded
            if (controller == null) {
                String errorMsg = "Controller not found in FXML: " + screen.getFxmlPath();
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen loading failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Validate root was loaded
            if (root == null) {
                String errorMsg = "Root node not found in FXML: " + screen.getFxmlPath();
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen loading failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Return screen load result
            return new ScreenLoadResult<>(root, controller);
            
        } catch (Exception e) {
            String errorMsg = "Critical error occurred during screen loading: " + 
                            (screen != null ? screen.getFxmlPath() : "null");
            ErrorHandler.handleCriticalError(e, errorMsg);
            return null;
        }
    }

    /**
     * Clears the screen cache, freeing up memory.
     */
    public void clearCache() { 
        try {
            int cacheSize = screenCache.size();
            screenCache.clear();
            
            if (cacheSize > 0) {
                ErrorHandler.handleNonCriticalError(
                    new RuntimeException("Cache cleared"),
                    "Screen cache cleared (" + cacheSize + " items)",
                    ErrorCategory.SYSTEM,
                    ErrorSeverity.LOW
                );
            }
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, 
                "Failed to clear screen cache",
                ErrorCategory.SYSTEM,
                ErrorSeverity.MEDIUM);
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Preloads screens specified in GUIConfig for faster subsequent access.
     */
    private void preloadScreens() {
        try {
            if (GUIConfig.PRELOAD_SCREENS == null || GUIConfig.PRELOAD_SCREENS.isEmpty()) {
                return; // No screens to preload
            }
            
            for (ScreenLoadable screen : GUIConfig.PRELOAD_SCREENS) {
                try {
                    // Only validate resources during preloading to avoid circular dependencies
                    screen.validateResources();
                    
                    // Load FXML and controller without ViewModel initialization
                    ScreenLoadResult<?> result = loadScreenWithoutViewModel(screen);
                    if (result != null) {
                        screenCache.put(screen, result);
                    }
                    
                } catch (Exception e) {
                    ErrorHandler.handleNonCriticalError(e, 
                        "Failed to preload screen: " + screen.getFxmlPath(),
                        ErrorCategory.RESOURCE,
                        ErrorSeverity.MEDIUM);
                    // Continue with other screens even if one fails
                }
            }
            
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, 
                "Failed to initialize screen preloading",
                ErrorCategory.SYSTEM,
                ErrorSeverity.MEDIUM);
        }
    }
    
    /**
     * Loads a screen without ViewModel initialization for preloading purposes.
     */
    private <T> ScreenLoadResult<T> loadScreenWithoutViewModel(ScreenLoadable screen) {
        try {
            validateScreen(screen);
            
            // Get FXML resource location
            URL location = getFxmlResource(screen.getFxmlPath());
            
            // Parse FXML and get controller
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            T controller = loader.getController();
            
            // Validate controller was loaded
            if (controller == null) {
                String errorMsg = "Controller not found in FXML: " + screen.getFxmlPath();
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen loading failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Validate root was loaded
            if (root == null) {
                String errorMsg = "Root node not found in FXML: " + screen.getFxmlPath();
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen loading failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH
                );
                throw new IllegalStateException(errorMsg);
            }
            
            // Return screen load result (without ViewModel initialization)
            return new ScreenLoadResult<>(root, controller);
            
        } catch (Exception e) {
            String errorMsg = "Critical error occurred during screen loading: " + 
                            (screen != null ? screen.getFxmlPath() : "null");
            ErrorHandler.handleCriticalError(e, errorMsg);
            return null;
        }
    }
    
    /**
     * Validates that the screen parameter is not null and has a valid FXML path.
     */
    private void validateScreen(ScreenLoadable screen) {
        if (screen == null) {
            String errorMsg = "Screen cannot be null";
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "Screen validation failed",
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
        }
        
        if (screen.getFxmlPath() == null || screen.getFxmlPath().trim().isEmpty()) {
            String errorMsg = "Screen FXML path cannot be null or empty";
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "Screen validation failed",
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
        }
        
        if (screen.getControllerType() == null) {
            String errorMsg = "Screen controller type cannot be null";
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "Screen validation failed",
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
        }
    }
    
    /**
     * Gets the FXML resource URL, trying multiple class loaders if needed.
     */
    private URL getFxmlResource(String fxmlPath) {
        try {
            // Try current class loader first
            URL resource = getClass().getResource(fxmlPath);
            if (resource != null) {
                return resource;
            }
            
            // Try thread context class loader
            resource = Thread.currentThread().getContextClassLoader().getResource(fxmlPath);
            if (resource != null) {
                return resource;
            }
            
            // Try system class loader
            resource = ClassLoader.getSystemResource(fxmlPath);
            if (resource != null) {
                return resource;
            }
            
            // If all attempts fail, throw exception with detailed error
            String errorMsg = "FXML resource not found: " + fxmlPath + 
                            " (tried current, thread context, and system class loaders)";
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "Resource loading failed",
                ErrorCategory.RESOURCE,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
            
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                String errorMsg = "Unexpected error loading FXML resource: " + fxmlPath;
                ErrorHandler.handleNonCriticalError(e, 
                    errorMsg,
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.HIGH);
            }
            throw e;
        }
    }
}