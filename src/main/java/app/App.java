package app;

import gui.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the BoardGamePlatform application.
 * This class extends the JavaFX Application class and sets up the initial stage,
 * backend services, and UI navigation for the platform.
 */
public class App extends Application {

    /**
     * Initializes the primary stage and sets up the application environment.
     * This method is called when the JavaFX application starts.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Start up the backend system
            Services.getInstance();

            // Initialize the ScreenManager with the primary stage
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.initialize(primaryStage);

            // Navigate to the opening screen (initial screen)
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);

            // Start preloading common screens in background for faster navigation
            new Thread(() -> {
                screenManager.preloadCommonScreens();
            }).start();

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
