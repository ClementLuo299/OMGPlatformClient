package com.core.lifecycle.start;

/**
 * Represents a task to be executed during application startup.
 * Implementations should handle initialization of specific resources or services.
 * 
 * @authors Clement Luo
 * @date June 24, 2025
 * @edited June 24, 2025
 * @since 1.0
 */
@FunctionalInterface
public interface StartupTask {

    /**
     * Executes the startup task.
     * This method should perform the necessary initialization operations.
     * 
     * @throws Exception if the startup task fails
     */
    void execute() throws Exception;

    /**
     * Gets the name of this startup task.
     * Used for logging and identification purposes.
     * 
     * @return the name of the startup task
     */
    default String getName() { return this.getClass().getSimpleName(); }
} 