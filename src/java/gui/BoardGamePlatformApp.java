package gui;

import networking.IO.DatabaseIOHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class BoardGamePlatformApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Initialize the ScreenManager with the primary stage
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.initialize(primaryStage);
            
            // Navigate to the opening screen (initial screen)
            screenManager.navigateTo(ScreenManager.OPENING_SCREEN, null);
            
            // Start preloading common screens in background for faster navigation
            new Thread(() -> {
                screenManager.preloadCommonScreens();
            }).start();
            
            primaryStage.setTitle("OMG Platform");
            primaryStage.setWidth(1400);
            primaryStage.setHeight(800);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void stop() {
        try {
            DatabaseIOHandler.getInstance().saveDBData();
        } catch (Exception e) {
            System.err.println("Error saving database data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
