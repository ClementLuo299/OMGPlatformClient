package com;

import com.core.lifecycle.LifecycleManager;
import static com.utils.error_handling.ErrorHandler.handleCriticalError;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the application.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 * @edited June 24, 2025
 * @since 1.0
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) { LifecycleManager.start(primaryStage); }

    @Override
    public void stop() { LifecycleManager.stop(); }

    /**
     * Main entry point with basic error handling.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            handleCriticalError(e, "Failed to launch application");
        } catch (Error e) {
            handleCriticalError(e, "Fatal system error during application launch");
        }
    }
}
