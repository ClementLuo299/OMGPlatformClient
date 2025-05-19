package com.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.config.ScreenConfig;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Manages screen transitions, loading, and caching in the application.
 * This class provides a centralized system for controlling navigation between
 * different screens while optimizing performance using strategies like screen caching.
 *
 * Features:
 * - Singleton pattern to ensure a single instance throughout the application.
 * - Supports dynamic navigation between screens with or without viewmodels.
 * - Preloads and caches screens based on configuration to reduce loading times.
 * - Handles CSS theming and error management during navigation.
 *
 * Usage:
 * 1. Initialize the `ScreenManager` instance during application startup.
 * 2. Use `navigateTo` methods to switch between screens, optionally injecting viewmodels or custom setups.
 *
 * Associated Classes:
 * - `Screen`: Defines all available screens with their FXML paths, CSS paths, and cacheability.
 * - `ScreenLoadingStrategy`: Interface for implementing different screen-loading strategies.
 * - `CachingScreenLoader`: Default implementation of `ScreenLoadingStrategy` for caching screens.
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Scott Brown, Jason Bakajika
 * @date March 28, 2025
 */
public class ScreenManager {
    private static ScreenManager instance;
    private final BorderPane mainContainer;
    private final Scene mainScene;
    private final Stage mainStage;
    private final ScreenLoadingStrategy loadingStrategy;
    private final ScreenConfig config;

    private ScreenManager(Stage stage, ScreenConfig config) {
        this.mainContainer = new BorderPane();
        this.mainScene = new Scene(mainContainer, 1400, 800);
        this.mainStage = stage;
        this.config = config;
        this.loadingStrategy = new CachingScreenLoader(config);
        initialize();
    }

    public static void initializeInstance(Stage stage, ScreenConfig config) {
        if (instance == null) {
            instance = new ScreenManager(stage, config);
        }
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ScreenManager must be initialized first");
        }
        return instance;
    }

    public <T> T navigateTo(Screen screen) {
        return navigateToWithSetup(screen, null);
    }

    public <T, V> T navigateToWithViewModel(Screen screen, V viewModel, Class<T> controllerClass) {
        return navigateToWithSetup(screen, controller -> {
            if (controller instanceof ViewModelInjectable<?>) {
                ((ViewModelInjectable<V>) controller).setViewModel(viewModel);
            }
        });
    }

    public <T> T navigateToWithSetup(Screen screen, Consumer<T> setup) {
        try {
            ScreenLoadResult<T> result = loadingStrategy.loadScreen(screen, null);
            if (setup != null && result.controller() != null) {
                setup.accept(result.controller());
            }
            
            applyCssTheme(screen.getCssPath());
            mainContainer.setCenter(result.root());
            return result.controller();
        } catch (Exception e) {
            handleNavigationError(e);
            return null;
        }
    }

    private void initialize() {
        mainStage.setScene(mainScene);
        if (config.isEnableCaching()) {
            preloadScreens();
        }
    }

    private void preloadScreens() {
        CompletableFuture.runAsync(() -> {
            for (Screen screen : config.getPreloadScreens()) {
                if (screen.isCacheable()) {
                    loadingStrategy.loadScreen(screen, null);
                }
            }
        });
    }

    private void applyCssTheme(String cssPath) {
        mainScene.getStylesheets().clear();
        if (cssPath != null) {
            String cssUrl = getClass().getClassLoader()
                    .getResource(ThemeManager.getInstance().isDarkTheme() ? "css/dark-theme.css" : cssPath)
                    .toExternalForm();
            mainScene.getStylesheets().add(cssUrl);
        }
    }

    private void handleNavigationError(Exception e) {
        System.err.println("Navigation error: " + e.getMessage());
        e.printStackTrace();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to navigate to screen: " + e.getMessage());
            alert.showAndWait();
        });
    }

    // Add compatibility methods for existing code
    public Object navigateTo(String fxmlPath, String cssPath) {
        Screen screen = Screen.valueOf(fxmlPath.replace("fxml/", "")
                .replace(".fxml", "")
                .toUpperCase());
        return navigateTo(screen);
    }

    public <T> T navigateToWithViewModel(String fxmlPath, String cssPath, Object viewModel, Class<T> controllerClass) {
        Screen screen = Screen.valueOf(fxmlPath.replace("fxml/", "")
                .replace(".fxml", "")
                .toUpperCase());
        return navigateToWithViewModel(screen, viewModel, controllerClass);
    }

}