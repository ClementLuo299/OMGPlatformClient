package com.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A class that encapsulates the setup and display of individual GUI screens.
 * Responsible for loading FXML views, injecting associated controllers and viewmodels,
 * and managing their lifecycle.
 *
 * Features:
 * - Dynamically loads FXML files at runtime.
 * - Injects controller and ViewModel instances for modular design.
 * - Simplifies display logic using JavaFX stages and scenes.
 *
 * Use this class to manage GUI components at a screen level, ensuring a clean separation
 * of concerns for controllers, views, and viewmodels.
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class GUI {

    private final Object controller;
    private final Object viewModel;
    private final String fxmlPath;
    private Parent rootView;

    // Constructor for injecting controller, viewmodel, and FXML path
    public GUI(Object controller, Object viewModel, String fxmlPath) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.fxmlPath = fxmlPath;
    }

    /**
     * Initializes and loads the FXML, injecting the controller and viewmodel.
     *
     * @throws IOException if there is an error loading the FXML.
     */
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        fxmlLoader.setController(controller); // Set the injected controller

        // Load the view
        rootView = fxmlLoader.load();

        // Optionally pass ViewModel to the controller after FXML is loaded
        if (controller instanceof ViewModelInjectable<?>) {
            ((ViewModelInjectable) controller).setViewModel(viewModel);
        }
    }

    /**
     * Displays the GUI in a JavaFX stage.
     *
     * @param stage the JavaFX stage to display the scene.
     */
    public void show(Stage stage) {
        stage.setScene(new Scene(rootView));
        stage.show();
    }

    public Parent getRootView() {
        return rootView;
    }
}