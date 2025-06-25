package com.core.lifecycle;

/**
 * Represents the different states of the application lifecycle.
 * Used for tracking and managing application state transitions.
 * 
 * @authors Clement Luo
 * @date June 24, 2025
 * @edited June 24, 2025
 * @since 1.0
 */
public enum LifecycleState {
    /**
     * Application is being initialized (before JavaFX starts)
     */
    INITIALIZING("Initializing"),
    
    /**
     * Application is starting up (JavaFX start method)
     */
    STARTING("Starting"),
    
    /**
     * Application is fully started and running
     */
    RUNNING("Running"),
    
    /**
     * Application is shutting down
     */
    SHUTTING_DOWN("Shutting Down"),
    
    /**
     * Application has been stopped
     */
    STOPPED("Stopped"),
    
    /**
     * Application encountered an error
     */
    ERROR("Error");

    private final String displayName;

    LifecycleState(String displayName) { this.displayName = displayName; }

    /**
     * Gets the user-friendly display name for this lifecycle state.
     *
     * @return the display name
     */
    public String getDisplayName() { return displayName; }
} 