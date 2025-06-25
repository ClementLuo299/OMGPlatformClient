package com.config;

import com.core.ServiceManager;
import com.core.screens.ScreenLoadable;
import com.core.screens.ScreenManager;
import com.gui_controllers.LoginController;
import com.viewmodels.LoginViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A centralized class that holds the paths to FXML and CSS files,
 * along with references to the controllers and viewmodels for all screens in the application.
 *
 * This class allows easy maintenance and updating of GUI-related resources in one place.
 *
 * @author Clement Luo
 * @date May 19, 2025
 * @edited June 1, 2025
 * @since 1.0
 */
public final class Screens {

    // Map of screen names to screen configurations
    public static final ScreenLoadable LOGIN = new ScreenLoadable.Builder(
            "/fxml/Login.fxml",
                    LoginController.class)
            .withCssPath("/css/login.css")
            .withViewModelSupplier(() ->
                    new LoginViewModel(
                            ServiceManager.getLoginService(),
                            ScreenManager.getInstance(), // Now safe - ScreenManager is initialized first
                            ServiceManager.getAlertService()))
            .cacheable(true)
            .build();

    /**
     * Returns a list of all screen templates defined in this class.
     * Uses reflection to find all public static final ScreenTemplate fields.
     *
     * @return List of all screen templates
     */
    public static List<ScreenLoadable> getAllScreens() {
        return Arrays.stream(Screens.class.getDeclaredFields())
                .filter(field -> field.getType() == ScreenLoadable.class)
                .filter(field -> java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .filter(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()))
                .filter(field -> java.lang.reflect.Modifier.isPublic(field.getModifiers()))
                .map(field -> {
                    try {
                        return (ScreenLoadable) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access screen template field: " + field.getName(), e);
                    }
                })
                .collect(Collectors.toList());
    }
}