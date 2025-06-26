package com.core.screens;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.config.GUIConfig;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Manages screen transitions, loading, and caching in the application.
 * This class provides a centralized system for controlling navigation between
 * different screens while optimizing performance using strategies like screen caching.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Scott Brown, Jason Bakajika
 * @date March 28, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenManager {
    
    // ==================== SINGLETON INSTANCE ====================
    
    private static ScreenManager instance; // Singleton instance
    
    // ==================== INSTANCE FIELDS ====================
    
    private final BorderPane mainContainer; // Outer container for every screen
    private final Stage mainStage; // Primary application window
    private final ScreenLoader loader; // Screen loader
    private Scene scene; // The main scene

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Private constructor to enforce the singleton pattern.
     */
    private ScreenManager(Stage stage) {
        try {
            if (stage == null) {
                throw new IllegalArgumentException("Stage cannot be null");
            }
            
            this.mainContainer = new BorderPane();
            this.mainStage = stage;
            this.loader = new ScreenLoader();

            // Set up the main scene with the specified dimensions
            this.scene = new Scene(this.mainContainer, 
                    GUIConfig.MAIN_SCENE_WIDTH,
                    GUIConfig.MAIN_SCENE_HEIGHT);
            
            // Set the scene on the stage so it's visible
            this.mainStage.setScene(this.scene);
                    
        } catch (Exception e) {
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
            if (instance != null) {
                ErrorHandler.handleNonCriticalError(
                    new IllegalStateException("ScreenManager already initialized"),
                    "ScreenManager initialization",
                    ErrorCategory.SYSTEM,
                    ErrorSeverity.MEDIUM
                );
                return;
            }
            instance = new ScreenManager(stage);
        } catch (Exception e) {
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
            // Validate input parameters
            validateScreen(screen);
            
            // Load the FXML and controller
            ScreenLoadResult<T> loadResult = loader.loadScreen(screen);
            if (loadResult == null) {
                String errorMsg = "Failed to load screen: " + screen.getFxmlPath();
                ErrorHandler.handleCriticalError(
                    new RuntimeException(errorMsg),
                    "Screen navigation failed"
                );
                throw new RuntimeException(errorMsg);
            }
            
            // Validate load result
            if (!loadResult.isValid()) {
                String errorMsg = "Invalid screen load result for: " + screen.getFxmlPath();
                ErrorHandler.handleCriticalError(
                    new IllegalStateException(errorMsg),
                    "Screen navigation failed"
                );
                throw new IllegalStateException(errorMsg);
            }
            
            T controller = loadResult.controller();

            // Attach style sheet if it exists
            if (screen.hasCss()) {
                loadCss(screen.getCssPath());
            }

            // Attach ViewModel if it exists
            if (screen.hasViewModel()) {
                initializeViewModel(controller, screen);
            }

            // Display the screen
            displayScreen(loadResult.root());
            return controller;
            
        } catch (Exception e) {
            String errorMsg = "Failed to navigate to screen: " + 
                            (screen != null ? screen.getFxmlPath() : "null");
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
     * Replaces the current stylesheet(s) with the one located at the specified path.
     * No-op if the Scene has not yet been set on the Stage.
     */
    private void loadCss(String cssPath) {
        try {
            if (cssPath == null || cssPath.trim().isEmpty()) {
                ErrorHandler.handleNonCriticalError(
                    new IllegalArgumentException("CSS path cannot be null or empty"),
                    "CSS loading failed",
                    ErrorCategory.RESOURCE,
                    ErrorSeverity.MEDIUM
                );
                return;
            }
            
            Scene scene = mainStage.getScene();
            if (scene != null) {
                // Remove previous styles and add new CSS
                scene.getStylesheets().clear();
                
                // Remove leading slash for classloader resource loading
                String resourcePath = cssPath.startsWith("/") ? cssPath.substring(1) : cssPath;
                var resource = getClass().getClassLoader().getResource(resourcePath);
                
                if (resource == null) {
                    ErrorHandler.handleNonCriticalError(
                        new RuntimeException("CSS not found: " + cssPath),
                        "Failed to load CSS stylesheet",
                        ErrorCategory.RESOURCE,
                        ErrorSeverity.MEDIUM
                    );
                    return;
                }
                
                scene.getStylesheets().add(resource.toExternalForm());
            }
        } catch (Exception e) {
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
            if (controller == null) {
                throw new IllegalArgumentException("Controller cannot be null");
            }
            
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

            // Call setViewModel on the controller instance
            setter.invoke(controller, vm);

        } catch (Exception e) {
            String errorMsg = "Failed to initialize ViewModel for screen: " + screen.getFxmlPath();
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
            if (root == null) {
                throw new IllegalArgumentException("Root node cannot be null");
            }
            
            if (mainStage == null) {
                throw new IllegalStateException("Stage is not initialized");
            }

            if (scene == null) {
                // Create new scene if none exists
                scene = new Scene(root);
                mainStage.setScene(scene);
            } else {
                // Swap root for existing scene
                scene.setRoot(root);
            }

        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Critical error occurred during screen display");
            throw e;
        }
    }
}