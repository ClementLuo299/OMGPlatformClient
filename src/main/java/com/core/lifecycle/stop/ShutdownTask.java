package com.core.lifecycle.stop;

/**
 * Represents a task to be executed during application shutdown.
 * Implementations should handle cleanup of specific resources or services.
 * 
 * @authors Clement Luo
 * @date June 24, 2025
 * @edited June 24, 2025
 * @since 1.0
 */
@FunctionalInterface
public interface ShutdownTask {

    /**
     * Executes the shutdown task.
     * This method should perform the necessary cleanup operations.
     * 
     * @throws Exception if the shutdown task fails
     */
    void execute() throws Exception;

    /**
     * Gets the name of this shutdown task.
     * Used for logging and identification purposes.
     * 
     * @return the name of the shutdown task
     */
    default String getName() { return this.getClass().getSimpleName(); }
} 