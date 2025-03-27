package gui;

import networking.Backend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BoardGamePlatformApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Login.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root, 1280, 730);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/login.css").toExternalForm());

            primaryStage.setTitle("OMG Platform");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
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
            Backend.getInstance().saveDBState();
        } catch (Exception e) {
            System.err.println("Error saving database data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
