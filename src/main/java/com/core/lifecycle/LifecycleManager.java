package com.core.lifecycle;

import com.core.lifecycle.start.ServiceManagement;
import com.core.lifecycle.start.UIManagement;
import com.core.lifecycle.start.ScreenManagement;
import com.utils.ErrorHandler;

import javafx.stage.Stage;

/**
 * Manages the lifecycle of the application, handling initialization, startup,
 * and cleanup processes. This class is responsible for configuring core components,
 * managing application screens, and ensuring proper shutdown procedures.
 * 
 * @authors Clement Luo
 * @date May 18, 2025
 */
public class LifecycleManager {

    /**
     * Initializes the application and starts the primary stage.
     */
    public static void start(Stage primaryStage) {
        try {
            // Configure stage first
            UIManagement.configureStage(primaryStage);

            // Initialize core components
            ScreenManagement.initialize(primaryStage);
            ServiceManagement.initialize();

            // Initialize UI (this will set the root scene)
            UIManagement.initialize();

            // Show the stage last
            primaryStage.show();

        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Critical error occurred during startup");
        }

    }

    /**
     * Performs cleanup and saves the database before exiting the application.
     */
    public static void stop() {
        try {
            //Services.db().saveDBData();
        } catch (Exception e) {
            System.err.println("Error saving data on exit: " + e.getMessage());
            e.printStackTrace();
        }
    }
}