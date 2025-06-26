package com.config;

import com.core.ServiceManager;
import com.core.screens.ScreenLoadable;
import com.core.screens.ScreenManager;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A centralized registry that holds the definitions for all screens in the application.
 * Each screen definition includes paths to FXML and CSS files, along with references 
 * to the controllers and view models.
 *
 * This registry allows easy maintenance and updating of GUI-related resources in one place.
 * Currently only contains the login screen - additional screens can be added as needed.
 *
 * @author Clement Luo
 * @date May 19, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
@UtilityClass
public class ScreenRegistry {

    /** Login screen - entry point for authenticated users */
    public static final ScreenLoadable LOGIN = ScreenLoadable.of(
            "/fxml/Login.fxml",
            com.gui_controllers.LoginController.class,
            "/css/login.css",
            () -> new com.viewmodels.LoginViewModel(
                    ServiceManager.getInstance(),
                    ScreenManager.getInstance())
    );

    /** Register screen - for new user registration */
    public static final ScreenLoadable REGISTER = ScreenLoadable.of(
            "/fxml/Register.fxml",
            com.gui_controllers.RegisterController.class,
            "/css/register.css",
            () -> new com.viewmodels.RegisterViewModel(
                    ServiceManager.getInstance(),
                    ScreenManager.getInstance())
    );

    // ==================== UTILITY METHODS ====================
    
    /**
     * Returns a list of all screen definitions registered in this class.
     * Uses reflection to find all public static final ScreenLoadable fields.
     *
     * @return List of all registered screen definitions
     */
    public static List<ScreenLoadable> getAllScreens() {
        // Get all declared fields from this class
        return Arrays.stream(ScreenRegistry.class.getDeclaredFields())
                // Filter for ScreenLoadable type fields
                .filter(field -> field.getType() == ScreenLoadable.class)
                // Filter for static fields
                .filter(field -> java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                // Filter for final fields
                .filter(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()))
                // Filter for public fields
                .filter(field -> java.lang.reflect.Modifier.isPublic(field.getModifiers()))
                // Extract the actual ScreenLoadable instances
                .map(field -> {
                    try {
                        return (ScreenLoadable) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access screen definition field: " + field.getName(), e);
                    }
                })
                // Collect into a list
                .collect(Collectors.toList());
    }
} 