package com;

import com.core.lifecycle.LifecycleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Main entry point of the application.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date March 17, 2025
 */
public class MainApplication extends Application {

    @Override public void start(Stage primaryStage) throws IOException {
        try {
            // Debug resource loading
            URL loginFxml = getClass().getResource("/fxml/Login.fxml");
            if (loginFxml == null) {
                System.err.println("Could not find Login.fxml");
                System.err.println("Working Directory: " + System.getProperty("user.dir"));
                throw new IOException("Login.fxml not found");
            }
            System.out.println("Found Login.fxml at: " + loginFxml);

            LifecycleManager.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override public void stop() { LifecycleManager.stop(); }

    public static void main(String[] args) { launch(args); }
}
