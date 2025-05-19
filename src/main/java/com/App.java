package com;

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
    public void start(Stage primaryStage) {
        try {
            // Start up the backend system
            Services.getInstance();

            // Initialize the ScreenManager with the primary stage
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.initialize(primaryStage);

            //Could move capability to services
            LoginService loginService = new LoginService();
            AlertService alertService = new AlertService();
            LoginViewModel viewModel = new LoginViewModel(loginService, screenManager, alertService);

            // Navigate to the opening screen (initial screen)
            screenManager.navigateToWithViewModel(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS, viewModel, LoginController.class);

            // Start preloading common screens in background for faster navigation
            new Thread(screenManager::preloadCommonScreens).start();

            // Set the main stage properties (e.g., title, dimensions)
            primaryStage.setTitle("OMG Platform"); //Set the title of the stage
            primaryStage.setWidth(1500); // Set the window width to 1500px
            primaryStage.setHeight(800); // Set the window height to 800px
            primaryStage.setMinWidth(800); // Set the minimum width of the window
            primaryStage.setMinHeight(600); // Set the minimum height of the window
            primaryStage.show(); // Display the main stage
        } catch (Exception e) {
            // Log any errors that occur during application startup
            System.err.println("Error starting Application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
