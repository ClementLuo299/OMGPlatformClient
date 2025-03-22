package com.boardgameplatform.projecttest;

import core.networking.IO.DatabaseIOHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BoardGamePlatformApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boardgameplatform/projecttest/Login.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root, 1280, 730); // Adjust size as needed
        scene.getStylesheets().add(getClass().getResource("/com/boardgameplatform/projecttest/login.css").toExternalForm());

        primaryStage.setTitle("OMG Platform");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop(){
        DatabaseIOHandler.getInstance().saveDBData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
