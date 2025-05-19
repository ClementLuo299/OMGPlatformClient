package com;

import com.config.GUISceneConfig;
import com.config.ScreenConfig;
import com.core.Screen;
import com.gui_controllers.LoginController;
import com.services.AlertService;
import com.services.LoginService;
import com.viewmodels.LoginViewModel;
import com.core.ScreenManager;
import com.core.Services;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the BoardGamePlatform application.
 * This class extends the JavaFX Application class and sets up the initial stage,
 * backend services, and UI navigation for the platform.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 */
public class App extends Application {

    /**
     * Initializes the primary stage and sets up the application environment.
     * This method is called when the JavaFX application starts.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Services.getInstance();

            ScreenConfig config = new ScreenConfig.Builder()
                    .addPreloadScreen(Screen.DASHBOARD)
                    .addPreloadScreen(Screen.GAME_LIBRARY)
                    .setCacheSize(10)
                    .setEnableCaching(true)
                    .build();

            ScreenManager.initializeInstance(primaryStage, config);

            LoginService loginService = new LoginService();
            AlertService alertService = new AlertService();
            LoginViewModel viewModel = new LoginViewModel(loginService, ScreenManager.getInstance(), alertService);

            ScreenManager.getInstance().navigateToWithViewModel(
                    Screen.LOGIN,
                    viewModel,
                    LoginController.class
            );

            configureStage(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            handleStartupError(e);
        }

    }

    /**
     *
     */
    private void configureStage(Stage primaryStage) {
        primaryStage.setTitle(GUISceneConfig.getAppTitle());
        primaryStage.setWidth(GUISceneConfig.getWindowWidth());
        primaryStage.setHeight(GUISceneConfig.getWindowHeight());
        primaryStage.setMinWidth(GUISceneConfig.getMinWindowWidth());
        primaryStage.setMinHeight(GUISceneConfig.getMinWindowHeight());
    }

    /**
     *
     */
    private void handleStartupError(Exception e) throws Exception {
        System.err.println("Error starting Application: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }

    /**
     * Saves the application data to the database before exiting.
     * This method is called when the JavaFX application stops.
     */
    @Override
    public void stop() {
        try {
            Services.db().saveDBData();
        } catch (Exception e) {
            System.err.println("Error saving database data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method to launch the BoardGamePlatform application.
     * This method calls the launch() method of the Application class, which
     * internally calls the start() method.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
