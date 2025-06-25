package com.core.lifecycle.start;

import com.core.ServiceManager;

/**
 * Initializes the core services (networking, logic, etc.).
 * Registers itself as a startup task for coordinated initialization.
 *
 * @authors Clement Luo
 * @date May 24, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ServiceManagement {

    /**
     * Registers service management as a startup task.
     * Should be called before startup begins.
     */
    public static void registerAsStartupTask() {
        StartupManager.registerStartupTask(new StartupTask() {
            @Override
            public void execute() throws Exception {
                ServiceManager.initializeCoreServices();
            }
            
            @Override
            public String getName() {
                return "ServiceManagement";
            }
        });
    }
}
