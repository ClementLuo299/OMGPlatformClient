package core.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile) {
        try {
            // Prevent loading any game-related screens
            if (fxmlFile.equals("ConnectFour.fxml") || fxmlFile.equals("Checkers.fxml")) {
                System.out.println("Game logic is disabled for now.");
                return;  // Do not switch to game logic screens
            }

            Parent root = FXMLLoader.load(GUIManager.class.getResource("/fxml/" + fxmlFile));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(GUIManager.class.getResource("/css/login.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
