package com;

import com.core.lifecycle.LifecycleManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point of the application.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 * @edited May 31, 2025
 * @since 1.0 SNAPSHOT
 */
public class MainApplication extends Application {

    @Override public void start(Stage primaryStage) throws IOException { LifecycleManager.start(primaryStage); }

    @Override public void stop() { LifecycleManager.stop(); }

    public static void main(String[] args) { launch(args); }
}
