package com.core.screens;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

import com.config.GUIConfig;
import com.core.screens.ScreenCacheConfig;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.ErrorCategory;
import com.utils.error_handling.ErrorSeverity;
import com.utils.error_handling.Logging;

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
    private static ScreenManager instance; // Singleton instance
    private final BorderPane mainContainer; // Outer container for every screen
    private Scene scene; // The main scene
    private final Stage mainStage; // Primary application window
    private final ScreenLoader loader; // Screen loader
    private final ScreenCacheConfig config; // Screen cache configuration

    /**
     * Private to enforce the singleton pattern.
     */
    private ScreenManager(Stage stage, ScreenCacheConfig config) {
        this.mainContainer = new BorderPane();
        this.mainStage = stage;
        this.loader = new ScreenLoader(config);
        this.config = config;

        // Set up the main scene with the specified dimensions
        this.scene = new Scene(this.mainContainer, 
                GUIConfig.MAIN_SCENE_WIDTH,
                GUIConfig.MAIN_SCENE_HEIGHT);
    }

    /**
     * Initialize the instance of the ScreenManager.
     */
    public static void initializeInstance(Stage stage, ScreenCacheConfig config) { instance = new ScreenManager(stage, config); }

    /**
     * Retrieve the instance of the ScreenManager.
     */
    public static ScreenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ScreenManager must be initialized first");
        }
        return instance;
    }

    /**
     * Load the screen when no additional controller setup is required.
     *
     * @return Controller instance for the requested screen.
     */
    public <T> T navigateTo(ScreenLoadable screen) {
        return navigateToWithSetup(screen, null);
    }

    /**
     * Loads a screen, optionally configures its controller, applies CSS and
     * ViewModel declarations, and finally displays it.
     *
     * @param screen      Screen descriptor (contains path to FXML, etc.).
     * @param setupAction Optional lambda that receives the freshly-created
     *                    controller. Caller can inject dependencies or state
     *                    before the UI becomes visible. May be {@code null}.
     * @param <T>         Controller type inferred from FXML.
     *
     * @return The controller instance for further interaction.
     */
    public <T> T navigateToWithSetup(ScreenLoadable screen, Consumer<T> setupAction) {
        //Load the FXML and controller
        ScreenLoadResult<T> loadResult = loader.loadScreen(screen);
        T controller = loadResult.controller();

        //Perform extra initialization for the controller
        if (setupAction != null) {
            setupAction.accept(controller);
        }

        //Attach style sheet if it exists
        if (screen.hasCss()) {
            loadCss(screen.getCssPath());
        }

        //Attach ViewModel if it exists
        if (screen.hasViewModel() || screen.getViewModelSupplier() != null) {
            initializeViewModel(controller, screen);
        }

        //Display the screen
        displayScreen(loadResult.root());
        return controller;
    }

    /**
     * Replaces the current stylesheet(s) with the one located at
     * {@code cssPath}. No-op if the Scene has not yet been set on the Stage.
     */
    private void loadCss(String cssPath) {
        Scene scene = mainStage.getScene();
        if (scene != null) {
            scene.getStylesheets().clear(); // Remove previous styles
            var resource = getClass().getClassLoader().getResource(cssPath);
            System.out.println("Trying to load CSS: " + cssPath + " => " + resource);
            if (resource == null) {
                System.err.println("CSS not found: " + cssPath);
                return;
            }
            scene.getStylesheets().add(resource.toExternalForm());
            System.out.println("Loaded CSS: " + resource);
        }

    }

    /**
     * Uses reflection to call a <code>setViewModel(&lt;VM&gt;)</code> method on
     * the given controller. A fresh ViewModel instance is created via the
     * default constructor.
     */
    private void initializeViewModel(Object controller, ScreenLoadable screen) {
        try {
            // 1. obtain a ViewModel instance
            Object vm;
            if (screen.getViewModelSupplier() != null) {
                vm = screen.getViewModelSupplier().get();
            } else {
                Class<?> vmType = screen.getViewModelType();
                if (vmType == null) {
                    // nothing to inject
                    return;
                }
                vm = vmType.getDeclaredConstructor().newInstance();
            }

            // 2. look for a method  setViewModel(<VM super-type>)
            //    (use declared parameter to avoid concrete-class mismatch)
            Method setter = Arrays.stream(controller.getClass().getMethods())
                    .filter(m -> m.getName().equals("setViewModel")
                            && m.getParameterCount() == 1
                            && m.getParameterTypes()[0]
                            .isAssignableFrom(vm.getClass()))
                    .findFirst()
                    .orElseThrow(() ->
                            new NoSuchMethodException(
                                    "setViewModel(...) not found in "
                                            + controller.getClass().getSimpleName()));

            // 3. call it on *the controller instance*
            setter.invoke(controller, vm);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise ViewModel", e);
        }
    }


    /**
     * Shows the supplied root node in the main window. If no Scene exists yet,
     * one is created; otherwise we simply swap the root to enable smooth and
     * resource-friendly screen transitions.
     */
    private void displayScreen(Parent root) {
        try {
            if (mainStage == null) {
                throw new IllegalStateException("Stage is not initialized");
            }


            if (scene == null) {
                scene = new Scene(root);
                mainStage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            mainStage.setScene(scene);

        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Critical error occurred during screen display");
        }
    }
}