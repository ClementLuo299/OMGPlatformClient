package com;

import com.core.LifecycleManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the application.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 */
public class App extends Application {

    @Override public void start(Stage primaryStage) { LifecycleManager.start(primaryStage); }

    @Override public void stop() { LifecycleManager.stop(); }

    public static void main(String[] args) { launch(args); }
}