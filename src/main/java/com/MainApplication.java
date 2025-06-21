package com;

import com.core.lifecycle.LifecycleManager;
import com.utils.ErrorHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point of the application.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 * @edited June 20, 2025
 * @since 1.0
 */
public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) { LifecycleManager.start(primaryStage); }
    

    @Override
    public void stop() { LifecycleManager.stop(); }

    public static void main(String[] args) { launch(args);}
}
