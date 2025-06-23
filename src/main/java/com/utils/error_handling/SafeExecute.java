package com.utils.error_handling;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Safe execution utility for the application.
 * Provides methods for executing code with error handling.
 * 
 * @authors Clement Luo
 * @date June 22, 2025
 * @edited June 22, 2025
 * @since 1.0
 * @version 1.0
 */
public final class SafeExecute {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SafeExecute() { throw new UnsupportedOperationException("Utility class cannot be instantiated"); }

    //region ==================== BASIC SAFE EXECUTION ====================

    /**
     * Executes a task safely, handling any exceptions that occur.
     * If an exception occurs, it's handled as a non-critical error and null is returned.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     * @return the result of the task, or null if an exception occurred
     */
    public static <T> T execute(Callable<T> task, String errorMessage) {
        try {
            return task.call();
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, errorMessage);
            return null;
        }
    }

    /**
     * Executes a task safely, handling any exceptions that occur.
     * If an exception occurs, it's handled as a non-critical error.
     *
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     */
    public static void execute(Runnable task, String errorMessage) {
        execute(() -> {
            task.run();
            return null;
        }, errorMessage);
    }

    //endregion

    //region ==================== SAFE EXECUTION WITH DEFAULTS ====================

    /**
     * Executes a task safely with a default value if the task fails.
     * If an exception occurs, it's handled as a non-critical error and the default value is returned.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     * @param defaultValue the default value to return if the task fails
     * @return the result of the task, or the default value if an exception occurred
     */
    public static <T> T executeWithDefault(Callable<T> task, String errorMessage, T defaultValue) {
        try {
            return task.call();
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, errorMessage);
            return defaultValue;
        }
    }

    /**
     * Executes a task safely with a default supplier if the task fails.
     * If an exception occurs, it's handled as a non-critical error and the default supplier is called.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @param errorMessage the error message to display if the task fails
     * @param defaultSupplier the supplier to call if the task fails
     * @return the result of the task, or the result of the default supplier if an exception occurred
     */
    public static <T> T executeWithDefault(Callable<T> task, String errorMessage, Supplier<T> defaultSupplier) {
        try {
            return task.call();
        } catch (Exception e) {
            ErrorHandler.handleNonCriticalError(e, errorMessage);
            return defaultSupplier.get();
        }
    }

    //endregion

    //region ==================== UNCHECKED EXECUTION ====================

    /**
     * Executes a task and rethrows any checked exceptions as runtime exceptions.
     * This is useful for converting checked exceptions to unchecked ones.
     *
     * @param <T> the return type of the task
     * @param task the task to execute
     * @return the result of the task
     * @throws RuntimeException if the task throws any exception
     */
    public static <T> T unchecked(ThrowingSupplier<T> task) {
        try {
            return task.get();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes a task and rethrows any checked exceptions as runtime exceptions.
     * This is useful for converting checked exceptions to unchecked ones.
     *
     * @param task the task to execute
     * @throws RuntimeException if the task throws any exception
     */
    public static void unchecked(ThrowingRunnable task) {
        try {
            task.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //endregion

    //region ==================== FUNCTIONAL INTERFACES ====================

    /**
     * A functional interface for suppliers that may throw checked exceptions.
     * This allows for cleaner exception handling in lambda expressions.
     *
     * @param <T> the type of the result
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        /**
         * Gets a result, potentially throwing an exception.
         *
         * @return the result
         * @throws Exception if an error occurs
         */
        T get() throws Exception;
    }

    /**
     * A functional interface for runnables that may throw checked exceptions.
     * This allows for cleaner exception handling in lambda expressions.
     */
    @FunctionalInterface
    public interface ThrowingRunnable {
        /**
         * Runs the task, potentially throwing an exception.
         *
         * @throws Exception if an error occurs
         */
        void run() throws Exception;
    }

    //endregion
}
