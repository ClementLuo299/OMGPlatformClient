package com.core;

import com.config.GUIConfig;
import com.config.ScreenConfig;
import com.services.AlertService;
import com.services.LoginService;
import com.viewmodels.LoginViewModel;
import javafx.stage.Stage;

/**
 * Handles application initialization, stage setup, and error handling.
 */
public class LifecycleManager {

    /**
     * Handles the entire application initialization process, including screen setup and navigation.
     *
     * @param primaryStage The primary stage of the application.
     * @throws Exception If initialization fails.
     */
    public static void onAppStart(Stage primaryStage) throws Exception {
        // Build screen configuration
        ScreenConfig config = buildScreenConfig();

        // Initialize screen manager
        ScreenManager.initializeInstance(primaryStage, config);

        // Initialize services and viewmodels
        LoginService loginService = new LoginService();
        AlertService alertService = new AlertService();
        LoginViewModel viewModel = new LoginViewModel(
            loginService,
            ScreenManager.getInstance(),
            alertService
        );

        // Navigate to the login screen
        ScreenManager
            .getInstance()
            .navigateToWithViewModel(Screen.LOGIN, viewModel, com.gui_controllers.LoginController.class);
    }

    public static void start(Stage primaryStage) {
        try {
            // Initialize application (Screen configuration, services, navigation)
            LifecycleManager.onAppStart(primaryStage);

            // Configure and display the primary stage
            LifecycleManager.configureStage(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            LifecycleManager.handleStartupError(e);
        }
    }

    /**
     * Builds and returns the `ScreenConfig` object for the application.
     *
     * @return ScreenConfig configured with preloaded screens and caching.
     */
    private static ScreenConfig buildScreenConfig() {
        return new ScreenConfig.Builder()
                .addPreloadScreen(Screen.DASHBOARD)
                .addPreloadScreen(Screen.GAME_LIBRARY)
                .setCacheSize(10)
                .setEnableCaching(true)
                .build();
    }

    /**
     * Configures the primary stage with title, dimensions, and constraints.
     *
     * @param stage The primary stage of the application.
     */
    public static void configureStage(Stage stage) {
        stage.setTitle(GUIConfig.getAppTitle());
        stage.setWidth(GUIConfig.getWindowWidth());
        stage.setHeight(GUIConfig.getWindowHeight());
        stage.setMinWidth(GUIConfig.getMinWindowWidth());
        stage.setMinHeight(GUIConfig.getMinWindowHeight());
    }

    /**
     * Handles startup errors by logging and rethrowing the exception.
     *
     * @param e The exception that occurred during startup.
     * @throws RuntimeException Rethrows the startup error.
     */
    public static void handleStartupError(Exception e) {
        System.err.println("Error during startup: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Application failed to start.", e);
    }

    /**
     * Performs cleanup and saves the database before exiting the application.
     */
    public static void stop() {
        try {
            Services.db().saveDBData();
        } catch (Exception e) {
            System.err.println("Error saving data on exit: " + e.getMessage());
            e.printStackTrace();
        }
    }
}