package com.config;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Configuration class for module directories and paths.
 * This class provides centralized configuration for module discovery,
 * loading paths, and directory structures. All configurations
 * are immutable and accessed through static constants.
 *
 * @authors Clement Luo
 * @date July 19, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
@UtilityClass
public class ModuleConfig {
    
    // Module directory settings
    public static final String MODULES_DIR = "modules";
    public static final String CLASSES_DIR = "target/classes";
    public static final String SRC_DIR = "src/main/java";
    
    // Module package structure
    public static final String MODULE_PACKAGE_PREFIX = "com.games.modules.";
    
    // Module class naming patterns
    public static final String MODULE_MAIN_CLASS_NAME = "Main";
    
    // Remote game source settings
    public static final String REMOTE_SOURCE_NAME = "Remote Games";
    public static final String DEFAULT_REMOTE_SERVER_URL = "https://api.omgplatform.com/games";
    
    // Source code game source settings
    public static final String SOURCE_CODE_SOURCE_NAME = "Source Code Games";
} 