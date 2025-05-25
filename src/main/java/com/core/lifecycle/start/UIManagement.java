package com.core.lifecycle.start;

import com.config.GUIConfig;
import com.core.screens.ScreenManager;
import com.core.screens.ScreenView;
import com.services.AlertService;
import com.services.LoginService;
import com.viewmodels.LoginViewModel;
import javafx.stage.Stage;

/**
 *
 * @authors Clement Luo
 * @date May 24, 2025
 */
public class UIManagement {

    /**
     * Initializes the UI and navigates to the login screen.
     */
    public static void initialize() throws Exception {
        LoginViewModel viewModel = new LoginViewModel(
                new LoginService(),
                ScreenManager.getInstance(),
                new AlertService()
        );
        ScreenManager.getInstance().navigateToWithViewModel(
                ScreenView.LOGIN,
                viewModel,
                com.gui_controllers.LoginController.class
        );
    }

    /**
     * Configures the primary stage with title, dimensions, and constraints.
     *
     * @param stage The primary stage of the application.
     */
    public static void configureStage(Stage stage) {
        stage.setTitle(GUIConfig.getAppTitle());
        stage.setWidth(GUIConfig.getWindowWidth());
        stage.setHeight(GUIConfig.getWindowHeight());
        stage.setMinWidth(GUIConfig.getMinWindowWidth());
        stage.setMinHeight(GUIConfig.getMinWindowHeight());
    }
}
