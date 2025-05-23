package com.core;

import com.config.GUIConfig;
import com.config.ScreenManagementConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenView;
import com.services.AlertService;
import com.services.LoginService;
import com.viewmodels.LoginViewModel;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * Manages the lifecycle of the application, handling initialization, startup,
 * and cleanup processes. This class is responsible for configuring core components,
 * managing application screens, and ensuring proper shutdown procedures.
 * 
 * @authors Clement Luo
 * @date May 18, 2025
 */
public class LifecycleManager {

    /*--------------------------Lifecycle functions--------------------------------*/

    public static void start(Stage primaryStage) {
        try {
            initializeScreenManager(primaryStage); //Start screen manager
            initializeServices(); //Start services
            initializeUI(); //Initialize UI

            // Configure and display the primary stage
            configureStage(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            handleStartupError(e);
        }
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

    /*--------------------------Helper functions--------------------------------*/

    // Splitting logic into smaller methods
    private static void initializeScreenManager(Stage primaryStage) throws Exception {
        ScreenManagementConfig config = buildScreenConfig();
        ScreenManager.initializeInstance(primaryStage, config);
    }

    private static void initializeServices() { ServiceManager.initializeCoreServices(); }

    //Navigate to the first page
    private static void initializeUI() throws Exception {
        LoginViewModel viewModel = new LoginViewModel(
                new LoginService(),
                ScreenManager.getInstance(),
                new AlertService()
        );
        ScreenManager.getInstance().navigateToWithViewModel(
                ScreenView.LOGIN,
                viewModel,
                com.gui_controllers.LoginController.class
        );
    }

    /**
     * Builds and returns the `ScreenConfig` object for the application.
     *
     * @return ScreenConfig configured with preloaded screens and caching.
     */
    private static ScreenManagementConfig buildScreenConfig() {
        ScreenManagementConfig.Builder builder = new ScreenManagementConfig.Builder()
                .setCacheSize(GUIConfig.getScreenCacheSize())
                .setEnableCaching(GUIConfig.isEnableCaching());

        //Add preloaded screens from config
        for(ScreenView screenView : GUIConfig.getPreloadScreens()) {
            builder.addPreloadScreen(screenView);
        }

        return builder.build();
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
        // Log the error with appropriate severity
        Logger logger = Logger.getLogger(LifecycleManager.class.getName());
        logger.severe("Error during startup: " + e.getMessage());

        // Optional: Display user-friendly error message or UI dialog
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText("Failed to start the application.");
        alert.setContentText(e.getMessage());
        alert.showAndWait();

        // Exit the application after logging and informing the user
        Platform.exit();
        System.exit(1);
    }
}