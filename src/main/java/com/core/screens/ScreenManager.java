package com.core.screens;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.config.GUIConfig;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages screen transitions, loading, and caching in the application.
 * This class provides a centralized system for controlling navigation between
 * different screens while optimizing performance using strategies like screen caching.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Scott Brown, Jason Bakajika
 * @date March 28, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
public class ScreenManager {
    
    // ==================== SINGLETON INSTANCE ====================
    
    private static ScreenManager instance; // Singleton instance
    
    // ==================== INSTANCE FIELDS ====================
    
    private final Stage mainStage; // Primary application window
    private final ScreenLoader loader; // Screen loader
    private Scene scene; // The main scene

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Private constructor to enforce the singleton pattern.
     */
    private ScreenManager(Stage stage) {
        try {
            Logging.info("Initializing ScreenManager...");
            
            // Validate input parameter
            if (stage == null) {
                throw new IllegalArgumentException("Stage cannot be null");
            }
            
            // Initialize core components
            this.mainStage = stage;
            this.loader = new ScreenLoader();

            Logging.info("ScreenManager initialized successfully with scene dimensions: " + 
                        GUIConfig.MAIN_SCENE_WIDTH + "x" + GUIConfig.MAIN_SCENE_HEIGHT);
                    
        } catch (Exception e) {
            Logging.error("Failed to initialize ScreenManager: " + e.getMessage(), e);
            ErrorHandler.handleCriticalError(e, "Failed to initialize ScreenManager");
            throw e;
        }
    }

    // ==================== PUBLIC STATIC METHODS ====================
    
    /**
     * Initialize the singleton instance of ScreenManager.
     */
    public static void initializeInstance(Stage stage) { 
        try {
            Logging.info("Initializing ScreenManager instance...");
            
            // Prevent duplicate initialization
            if (instance != null) {
                Logging.warning("ScreenManager already initialized, ignoring duplicate initialization");
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException("ScreenManager already initialized"),
                    "ScreenManager initialization",
                    ErrorCategory.SYSTEM,
                    ErrorSeverity.MEDIUM
                );
                return;
            }
            
            // Create singleton instance
            instance = new ScreenManager(stage);
            Logging.info("ScreenManager instance initialized successfully");
        } catch (Exception e) {
            Logging.error("Failed to initialize ScreenManager instance: " + e.getMessage(), e);
            ErrorHandler.handleCriticalError(e, "Failed to initialize ScreenManager instance");
            throw e;
        }
    }

    /**
     * Retrieve the singleton instance of ScreenManager.
     */
    public static ScreenManager getInstance() {
        if (instance == null) {
            String errorMsg = "ScreenManager must be initialized first";
            Logging.error(errorMsg);
            ErrorHandler.handleNonCriticalError(
                new IllegalStateException(errorMsg),
                "ScreenManager access",
                ErrorCategory.SYSTEM,
                ErrorSeverity.HIGH
            );
            throw new IllegalStateException(errorMsg);
        }
        return instance;
    }

    // ==================== PUBLIC NAVIGATION METHODS ====================
    
    /**
     * Loads a screen, applies CSS and ViewModel declarations, and displays it.
     *
     * @param screen Screen descriptor (contains path to FXML, etc.)
     * @param <T> Controller type inferred from FXML
     * @return Controller instance for the requested screen
     * @throws IllegalArgumentException if screen is null
     */
    public <T> T navigateTo(ScreenLoadable screen) {
        try {
            Logging.info("Navigating to screen: " + (screen != null ? screen.getFxmlPath() : "null"));
            
            // Validate input parameters
            validateScreen(screen);
            
            // Load the FXML and controller
            Logging.info("Loading screen: " + screen.getFxmlPath());
            ScreenLoadResult<T> loadResult = loader.loadScreen(screen);
            if (loadResult == null) {
                String errorMsg = "Failed to load screen: " + screen.getFxmlPath();
                Logging.error(errorMsg);
                ErrorHandler.handleCriticalError(
                    new RuntimeException(errorMsg),
                    "Screen navigation failed"
                );
                throw new RuntimeException(errorMsg);
            }
            
            // Validate load result
            if (!loadResult.isValid()) {
                String errorMsg = "Invalid screen load result for: " + screen.getFxmlPath();
                Logging.error(errorMsg);
                ErrorHandler.handleCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen navigation failed"
                );
                throw new IllegalStateException(errorMsg);
            }
            
            T controller = loadResult.controller();
            Logging.info("Screen loaded successfully: " + screen.getFxmlPath());

            // Apply styling if CSS is specified
            if (screen.hasCss()) {
                Logging.info("Loading CSS for screen: " + screen.getCssPath());
                loadCss(screen.getCssPath());
            }

            // Initialize ViewModel if specified
            if (screen.hasViewModel()) {
                Logging.info("Initializing ViewModel for screen: " + screen.getFxmlPath());
                initializeViewModel(controller, screen);
            }

            // Display the screen to user
            Logging.info("Displaying screen: " + screen.getFxmlPath());
            displayScreen(loadResult.root());
            
            Logging.info("Successfully navigated to screen: " + screen.getFxmlPath());
            return controller;
            
        } catch (Exception e) {
            String errorMsg = "Failed to navigate to screen: " + 
                            (screen != null ? screen.getFxmlPath() : "null");
            Logging.error(errorMsg + ": " + e.getMessage(), e);
            ErrorHandler.handleCriticalError(e, errorMsg);
            throw e;
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Validates that the screen parameter is not null.
     */
    private void validateScreen(ScreenLoadable screen) {
        if (screen == null) {
            String errorMsg = "Screen cannot be null";
            Logging.error(errorMsg);
            ErrorHandler.handleNonCriticalError(
                new IllegalArgumentException(errorMsg),
                "Screen validation failed",
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.HIGH
            );
            throw new IllegalArgumentException(errorMsg);
        }
        Logging.info("Screen validation passed: " + screen.getFxmlPath());
    }
    
    /**
     * Replaces the current stylesheet(s) with the one located at the specified path.
     * No-op if the Scene has not yet been set on the Stage.
     */
    private void loadCss(String cssPath) {
        try {
            Logging.info("Loading CSS: " + cssPath);
            
            // Validate CSS path
            if (cssPath == null || cssPath.trim().isEmpty()) {
                Logging.warning("CSS path is null or empty, skipping CSS loading");
                ErrorHandler.handleNonCriticalError(
                    new IllegalArgumentException("CSS path cannot be null or empty"),
                    "CSS loading failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.MEDIUM
                );
                return;
            }
            
            // Get current scene
            Scene scene = mainStage.getScene();
            if (scene != null) {
                // Clear existing stylesheets
                scene.getStylesheets().clear();
                
                // Prepare resource path for classloader
                String resourcePath = cssPath.startsWith("/") ? cssPath.substring(1) : cssPath;
                var resource = getClass().getClassLoader().getResource(resourcePath);
                
                // Validate resource exists
                if (resource == null) {
                    Logging.error("CSS not found: " + cssPath);
                    ErrorHandler.handleNonCriticalError(
                        new RuntimeException("CSS not found: " + cssPath),
                        "Failed to load CSS stylesheet",
                        ErrorCategory.RESOURCE,
                        ErrorSeverity.MEDIUM
                    );
                    return;
                }
                
                // Apply CSS to scene
                scene.getStylesheets().add(resource.toExternalForm());
                Logging.info("CSS loaded successfully: " + cssPath);
            } else {
                Logging.warning("Scene is null, cannot load CSS: " + cssPath);
            }
        } catch (Exception e) {
            Logging.error("Failed to load CSS: " + cssPath + " - " + e.getMessage(), e);
            ErrorHandler.handleNonCriticalError(e, 
                "Failed to load CSS: " + cssPath,
                ErrorCategory.RESOURCE,
                ErrorSeverity.MEDIUM);
        }
    }

    /**
     * Uses reflection to call a setViewModel method on the given controller.
     * A fresh ViewModel instance is created via the supplier.
     */
    private void initializeViewModel(Object controller, ScreenLoadable screen) {
        try {
            Logging.info("Initializing ViewModel for controller: " + controller.getClass().getSimpleName());
        
            // Validate ViewModel supplier exists
            if (screen.getViewModelSupplier() == null) {
                throw new IllegalStateException("ViewModel supplier is null");
            }
            
            // Create ViewModel instance using supplier
            Object vm = screen.getViewModelSupplier().get();
            if (vm == null) {
                throw new IllegalStateException("ViewModel supplier returned null");
            }

            // Find setViewModel method using reflection
            Method setter = Arrays.stream(controller.getClass().getMethods())
                    .filter(m -> m.getName().equals("setViewModel")
                            && m.getParameterCount() == 1
                            && m.getParameterTypes()[0].isAssignableFrom(vm.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodException(
                            "setViewModel(...) not found in " + controller.getClass().getSimpleName()));

            // Inject ViewModel into controller
            setter.invoke(controller, vm);
            Logging.info("ViewModel initialized successfully for: " + controller.getClass().getSimpleName());

        } catch (Exception e) {
            String errorMsg = "Failed to initialize ViewModel for screen: " + screen.getFxmlPath();
            Logging.error(errorMsg + " - " + e.getMessage(), e);
            ErrorHandler.handleNonCriticalError(e, 
                errorMsg,
                ErrorCategory.CONFIGURATION,
                ErrorSeverity.MEDIUM);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Shows the supplied root node in the main window. If no Scene exists yet,
     * one is created; otherwise we simply swap the root to enable smooth and
     * resource-friendly screen transitions.
     */
    private void displayScreen(Parent root) {
        try {
            Logging.info("Displaying screen with root: " + root.getClass().getSimpleName());
            
            // Validate stage is initialized
            if (mainStage == null) {
                throw new IllegalStateException("Stage is not initialized");
            }

            // Create new scene or reuse existing one
            if (scene == null) {
                Logging.info("Creating new scene for screen display");
                scene = new Scene(root, GUIConfig.MAIN_SCENE_WIDTH, GUIConfig.MAIN_SCENE_HEIGHT);
                mainStage.setScene(scene);
            } else {
                Logging.info("Reusing existing scene, updating root");
                scene.setRoot(root);
            }
            
            Logging.info("Screen displayed successfully");

        } catch (Exception e) {
            Logging.error("Critical error occurred during screen display: " + e.getMessage(), e);
            ErrorHandler.handleCriticalError(e, "Critical error occurred during screen display");
            throw e;
        }
    }
}