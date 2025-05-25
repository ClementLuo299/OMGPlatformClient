package com.core.screens;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import com.config.GUIConfig;
import com.config.ScreenManagementConfig;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Manages screen transitions, loading, and caching in the application.
 * This class provides a centralized system for controlling navigation between
 * different screens while optimizing performance using strategies like screen caching.
 *
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Scott Brown, Jason Bakajika
 * @date March 28, 2025
 */
public class ScreenManager {
    private static ScreenManager instance;
    private final BorderPane mainContainer;
    private Scene scene;
    private final Stage mainStage;
    private final ScreenLoadingStrategy loader;
    private final ScreenManagementConfig config;

    private ScreenManager(Stage stage, ScreenManagementConfig config) {
        this.mainContainer = new BorderPane();
        this.scene = new Scene(mainContainer, GUIConfig.getMainSceneWidth(), GUIConfig.getMainSceneHeight());
        this.mainStage = stage;
        this.config = config;
        this.loader = new CachingScreenLoader(config);
    }

    public static void initializeInstance(Stage stage, ScreenManagementConfig config) {
        if (stage == null) {
            throw new IllegalArgumentException("Stage cannot be null");
        }

        System.out.println("Initializing ScreenManager with stage: " + stage);

        instance = new ScreenManager(stage, config);

        // Configure stage
        stage.setX(100);
        stage.setY(100);
        stage.setWidth(1400);
        stage.setHeight(800);


        System.out.println("ScreenManager initialized successfully");

    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ScreenManager must be initialized first");
        }
        return instance;
    }

    public <T> T navigateTo(ScreenTemplate screen) {
        return navigateToWithSetup(screen, null);
    }

    public <T> T navigateToWithSetup(ScreenTemplate screen, Consumer<T> setupAction) {
        ScreenLoadResult<T> loadResult = loader.loadScreen(screen);
        T controller = loadResult.controller();

        if (setupAction != null) {
            setupAction.accept(controller);
        }

        if (screen.hasCss()) {
            loadCss(screen.getCssPath());
        }

        if (screen.hasViewModel()) {
            initializeViewModel(controller, screen.getViewModelType());
        }

        displayScreen(loadResult.root());
        return controller;
    }

    private void loadCss(String cssPath) {
        Scene scene = mainStage.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
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

    private <T> void initializeViewModel(T controller, Class<?> viewModelType) {
        try {
            Object viewModel = viewModelType.getDeclaredConstructor().newInstance();
            Method initMethod = controller.getClass().getMethod("setViewModel", viewModelType);
            initMethod.invoke(controller, viewModel);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ViewModel for controller: " + controller.getClass(), e);
        }
    }

    private void displayScreen(Parent root) {
        try {
            if (mainStage == null) {
                throw new IllegalStateException("Stage is not initialized");
            }

            System.out.println("Displaying screen...");
            System.out.println("Root node: " + root);
            System.out.println("Root visible: " + root.isVisible());
            System.out.println("Root dimensions: " + root.getBoundsInLocal());

            if (scene == null) {
                System.out.println("Creating new Scene");
                scene = new Scene(root);
                mainStage.setScene(scene);
            } else {
                System.out.println("Updating existing Scene");
                scene.setRoot(root);
            }

            // Force a white background on the scene:
            scene.setFill(javafx.scene.paint.Color.WHITE);

            // Ensure root node fills scene
            if (root instanceof javafx.scene.layout.Region region) {
                region.minWidthProperty().bind(scene.widthProperty());
                region.minHeightProperty().bind(scene.heightProperty());
                region.prefWidthProperty().bind(scene.widthProperty());
                region.prefHeightProperty().bind(scene.heightProperty());
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
            }

            // Force layout pass
            root.applyCss();
            root.layout();

            System.out.println("Root class: " + root.getClass());
            System.out.println("Scene size: " + scene.getWidth() + "x" + scene.getHeight());

            System.out.println(scene.getStylesheets());

            System.out.println("Root node children: " + (scene.getRoot()).getChildrenUnmodifiable());

            for (Node n : scene.getRoot().getChildrenUnmodifiable()) {
                System.out.println(n + " bounds: " + n.getBoundsInParent());
            }



            System.out.println("Stage showing: " + mainStage.isShowing());
            System.out.println("Scene dimensions: " + scene.getWidth() + "x" + scene.getHeight());

            mainStage.setScene(scene);

        } catch (Exception e) {
            System.err.println("Error displaying screen:");
            e.printStackTrace();
            throw new RuntimeException("Failed to display screen", e);
        }
    }


}