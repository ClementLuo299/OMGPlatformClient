package com.config;

import com.core.Screen;
import com.gui_controllers.LoginController;
import com.viewmodels.LoginViewModel;

/**
 * A centralized class that holds the paths to FXML and CSS files,
 * along with references to the controllers and viewmodels for all screens in the application.
 *
 * This class allows easy maintenance and updating of GUI-related resources in one place.
 *
 * @author Clement Luo
 * @date May 19, 2025
 */
public final class GUILocations {

    // Map of screen names to screen configurations
    public static final Screen LOGIN = new Screen(
        "fxml/Login.fxml", 
        "css/login.css", 
        LoginController.class,
        LoginViewModel.class
    );
}