package com.games;

import com.utils.error_handling.Logging;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for dynamically loading game modules from the modules directory.
 * This class handles the discovery and loading of game modules at runtime.
 * 
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class ModuleLoader {
    
    private static final String MODULES_DIR = "modules";
    private static final String CLASSES_DIR = "target/classes";
    private static final String SRC_DIR = "src/main/java";
    
    /**
     * Discovers and loads all available game modules from the modules directory.
     * 
     * @return List of loaded GameModule instances
     */
    public static List<GameModule> loadAllModules() {
        List<GameModule> modules = new ArrayList<>();
        
        try {
            File modulesDir = new File(MODULES_DIR);
            if (!modulesDir.exists() || !modulesDir.isDirectory()) {
                Logging.warning("⚠️ Modules directory not found: " + modulesDir.getAbsolutePath());
                return modules;
            }
            
            File[] moduleDirs = modulesDir.listFiles(File::isDirectory);
            if (moduleDirs == null) {
                Logging.warning("⚠️ Could not list modules directory");
                return modules;
            }
            
            for (File moduleDir : moduleDirs) {
                GameModule module = loadModule(moduleDir);
                if (module != null) {
                    modules.add(module);
                }
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error loading modules: " + e.getMessage(), e);
        }
        
        return modules;
    }
    
    /**
     * Loads a single game module from a module directory.
     * 
     * @param moduleDir The module directory
     * @return The loaded GameModule, or null if loading failed
     */
    public static GameModule loadModule(File moduleDir) {
        try {
            String moduleName = moduleDir.getName();
            Logging.info("🔍 Loading module: " + moduleName);
            
            // Try to find the game module class
            Class<?> gameClass = findGameModuleClass(moduleDir, moduleName);
            if (gameClass == null) {
                Logging.warning("⚠️ Could not find game module class in: " + moduleName);
                return null;
            }
            
            // Instantiate the game module
            GameModule game = instantiateGameModule(gameClass);
            if (game != null) {
                Logging.info("✅ Successfully loaded: " + game.getGameName() + " from " + moduleName);
            }
            
            return game;
            
        } catch (Exception e) {
            Logging.error("❌ Error loading module " + moduleDir.getName() + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Finds the game module class in a module directory.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @return The found Class, or null if not found
     */
    private static Class<?> findGameModuleClass(File moduleDir, String moduleName) {
        // Try different possible class names
        String[] possibleClassNames = {
            moduleName + "Module",
            capitalize(moduleName) + "Module",
            moduleName + "GameModule",
            capitalize(moduleName) + "GameModule"
        };
        
        for (String className : possibleClassNames) {
            Class<?> gameClass = tryLoadClass(moduleDir, moduleName, className);
            if (gameClass != null) {
                return gameClass;
            }
        }
        
        return null;
    }
    
    /**
     * Attempts to load a class from a module directory.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @param className The class name
     * @return The loaded Class, or null if not found
     */
    private static Class<?> tryLoadClass(File moduleDir, String moduleName, String className) {
        try {
            // Try to load from compiled classes first
            Class<?> gameClass = tryLoadFromCompiledClasses(moduleDir, moduleName, className);
            if (gameClass != null) {
                return gameClass;
            }
            
            // Try to load from source (for development)
            gameClass = tryLoadFromSource(moduleDir, moduleName, className);
            if (gameClass != null) {
                return gameClass;
            }
            
            // Try to load from current classpath
            String fullClassName = "com.games.modules." + moduleName.toLowerCase() + "." + className;
            return Class.forName(fullClassName);
            
        } catch (ClassNotFoundException e) {
            // Class not found, this is expected
            return null;
        } catch (Exception e) {
            Logging.error("❌ Error loading class " + className + " from " + moduleName + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Attempts to load a class from compiled classes in the module.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @param className The class name
     * @return The loaded Class, or null if not found
     */
    private static Class<?> tryLoadFromCompiledClasses(File moduleDir, String moduleName, String className) {
        try {
            File classesDir = new File(moduleDir, CLASSES_DIR);
            if (!classesDir.exists()) {
                return null;
            }
            
            URLClassLoader classLoader = new URLClassLoader(new URL[]{classesDir.toURI().toURL()});
            String fullClassName = "com.games.modules." + moduleName.toLowerCase() + "." + className;
            
            return classLoader.loadClass(fullClassName);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Attempts to load a class from source files in the module.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @param className The class name
     * @return The loaded Class, or null if not found
     */
    private static Class<?> tryLoadFromSource(File moduleDir, String moduleName, String className) {
        try {
            File srcDir = new File(moduleDir, SRC_DIR);
            if (!srcDir.exists()) {
                return null;
            }
            
            URLClassLoader classLoader = new URLClassLoader(new URL[]{srcDir.toURI().toURL()});
            String fullClassName = "com.games.modules." + moduleName.toLowerCase() + "." + className;
            
            return classLoader.loadClass(fullClassName);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Instantiates a GameModule from a Class.
     * 
     * @param gameClass The game module class
     * @return The instantiated GameModule, or null if instantiation failed
     */
    private static GameModule instantiateGameModule(Class<?> gameClass) {
        try {
            // Check if it implements GameModule
            if (!GameModule.class.isAssignableFrom(gameClass)) {
                Logging.warning("⚠️ Class " + gameClass.getName() + " does not implement GameModule");
                return null;
            }
            
            // Try to instantiate the class
            Constructor<?> constructor = gameClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            GameModule game = (GameModule) constructor.newInstance();
            
            return game;
            
        } catch (Exception e) {
            Logging.error("❌ Error instantiating game module " + gameClass.getName() + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Capitalizes the first letter of a string.
     * 
     * @param str The input string
     * @return The capitalized string
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
} 