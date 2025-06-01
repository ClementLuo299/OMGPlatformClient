package com.core.lifecycle.start;

import com.core.ServiceManager;

/**
 * Initializes the core services (networking, logic, etc.).
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited May 31, 2025
 * @since 1.0
 */
public class ServiceManagement {

    /**
     * Initializes the core services.
     */
    public static void initialize() { ServiceManager.initializeCoreServices(); }
}
