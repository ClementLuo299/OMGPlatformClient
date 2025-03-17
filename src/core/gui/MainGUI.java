package core.gui;  // Adjust based on your package structure

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Login Screen
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Login.fxml"));


            // Set scene with login.css
            Scene scene = new Scene(root, 1280, 720);  // Adjust if needed
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());

            // Configure window
            primaryStage.setTitle("Login - Multiplayer Board Game Platform");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);  // Prevent resizing
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
