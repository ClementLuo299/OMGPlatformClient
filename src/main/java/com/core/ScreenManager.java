package com.core;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.config.ScreenManagementConfig;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private final ScreenManagementConfig config;

    private ScreenManager(Stage stage, ScreenManagementConfig config) {
        this.mainContainer = new BorderPane();
        this.mainScene = new Scene(mainContainer, 1400, 800);
        this.mainStage = stage;
        this.config = config;
        this.loadingStrategy = new CachingScreenLoader(config);
        initialize();
    }

    public static void initializeInstance(Stage stage, ScreenManagementConfig config) {
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

    public <T> T navigateTo(ScreenView screenView) {
        return navigateToWithSetup(screenView, null);
    }

    public <T, V> T navigateToWithViewModel(ScreenView screenView, V viewModel, Class<T> controllerClass) {
        return navigateToWithSetup(screenView, controller -> {
            if (controller instanceof ViewModelInjectable<?>) {
                ((ViewModelInjectable<V>) controller).setViewModel(viewModel);
            }
        });
    }

    public <T> T navigateToWithSetup(ScreenView screenView, Consumer<T> setup) {
        try {
            ScreenLoadResult<T> result = loadingStrategy.loadScreen(screenView, null);
            if (setup != null && result.controller() != null) {
                setup.accept(result.controller());
            }
            
            applyCssTheme(screenView.getCssPath());
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
            for (ScreenView screenView : config.getPreloadScreens()) {
                if (screenView.isCacheable()) {
                    loadingStrategy.loadScreen(screenView, null);
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
        ScreenView screenView = ScreenView.valueOf(fxmlPath.replace("fxml/", "")
                .replace(".fxml", "")
                .toUpperCase());
        return navigateTo(screenView);
    }

    public <T> T navigateToWithViewModel(String fxmlPath, String cssPath, Object viewModel, Class<T> controllerClass) {
        ScreenView screenView = ScreenView.valueOf(fxmlPath.replace("fxml/", "")
                .replace(".fxml", "")
                .toUpperCase());
        return navigateToWithViewModel(screenView, viewModel, controllerClass);
    }

}