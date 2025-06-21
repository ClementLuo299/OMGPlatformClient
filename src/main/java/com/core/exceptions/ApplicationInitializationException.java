package com.core.exceptions;

/**
 * Thrown when the application fails to initialize properly.
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Zaman Dogar
 * @date June 20, 2025
 * @edited June 20, 2025
 * @since 1.0
 */
public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(String message) {
        super(message);
    }

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
