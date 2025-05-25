package com.config;

import com.core.screens.ScreenTemplate;
import com.gui_controllers.LoginController;
import com.viewmodels.LoginViewModel;

import java.net.URL;
import java.util.ArrayList;
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
 */
public final class Screens {

    // Map of screen names to screen configurations
    public static final ScreenTemplate LOGIN = new ScreenTemplate.Builder(
            "/fxml/Login.fxml",
                    LoginController.class)
            .withCssPath("/css/login.css")
            .withViewModelType(LoginViewModel.class)
            .cacheable(true)
            .build();

    /**
     * Returns a list of all screen templates defined in this class.
     * Uses reflection to find all public static final ScreenTemplate fields.
     *
     * @return List of all screen templates
     */
    public static List<ScreenTemplate> getAllScreens() {
        return Arrays.stream(Screens.class.getDeclaredFields())
                .filter(field -> field.getType() == ScreenTemplate.class)
                .filter(field -> java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .filter(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()))
                .filter(field -> java.lang.reflect.Modifier.isPublic(field.getModifiers()))
                .map(field -> {
                    try {
                        return (ScreenTemplate) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access screen template field: " + field.getName(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Validates that all screen resources are accessible.
     * Call this during application startup to catch resource issues early.
     */
    public static void validateResources() {
        for (ScreenTemplate screen : getAllScreens()) {
            try {
                // Check FXML
                URL fxmlUrl = Screens.class.getResource(screen.getFxmlPath());
                if (fxmlUrl == null) {
                    throw new IllegalStateException("FXML not found: " + screen.getFxmlPath());
                }

                // Check CSS if present
                if (screen.hasCss()) {
                    URL cssUrl = Screens.class.getResource(screen.getCssPath());
                    if (cssUrl == null) {
                        throw new IllegalStateException("CSS not found: " + screen.getCssPath());
                    }
                }

                // Verify controller can be instantiated
                screen.getControllerType().getDeclaredConstructor().newInstance();

                System.out.println("✓ Validated screen resources for: " + screen.getFxmlPath());
            } catch (Exception e) {
                System.err.println("Failed to validate screen: " + screen.getFxmlPath());
                e.printStackTrace();
            }
        }
    }

}