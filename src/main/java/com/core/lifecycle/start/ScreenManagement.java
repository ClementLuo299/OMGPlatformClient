package com.core.lifecycle.start;

import com.core.screens.ScreenManager;
import com.utils.error_handling.ErrorHandler;
import com.utils.error_handling.Logging;
import com.utils.error_handling.enums.ErrorCategory;
import com.utils.error_handling.enums.ErrorSeverity;

import javafx.stage.Stage;

/**
 * Manages the initialization of screen management components.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenManagement {

    /**
     * Initializes the ScreenManager.
     * This method is called directly by LifecycleManager.
     *
     * @param primaryStage the primary stage
     */
    public static void initialize(Stage primaryStage) {
        Logging.info("Initializing ScreenManager...");
        
        try {
            ScreenManager.initializeInstance(primaryStage);
            Logging.info("ScreenManager initialized successfully");
            
        } catch (Exception e) {
            ErrorHandler.handleCriticalError(e, "Failed to initialize ScreenManager", 
                                           ErrorCategory.SYSTEM, ErrorSeverity.HIGH);
        }
    }
}
