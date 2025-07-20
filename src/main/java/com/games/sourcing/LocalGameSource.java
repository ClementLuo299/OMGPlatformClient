package com.games.sourcing;

import com.config.ModuleConfig;
import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.games.enums.GameDifficulty;
import com.games.enums.GameMode;
import com.utils.error_handling.Logging;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Discovers games from the modules directory dynamically.
 * This source can load both compiled game modules and uncompiled source code.
 * 
 * @authors Clement Luo
 * @date July 19, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public class LocalGameSource implements GameSource {
    
    private static final String SOURCE_NAME = "Local Games";
    
    @Override
    public String getName() {
        return SOURCE_NAME;
    }
    
    @Override
    public boolean isAvailable() {
        File modulesDir = new File(ModuleConfig.MODULES_DIR);
        return modulesDir.exists() && modulesDir.isDirectory();
    }
    
    @Override
    public List<GameModule> discoverGames() {
        Logging.info("🔍 Discovering games from modules directory...");
        
        List<GameModule> games = new ArrayList<>();
        
        try {
            File modulesDir = new File(ModuleConfig.MODULES_DIR);
            if (!modulesDir.exists() || !modulesDir.isDirectory()) {
                Logging.warning("⚠️ Modules directory not found: " + modulesDir.getAbsolutePath());
                return games;
            }
            
            File[] moduleDirs = modulesDir.listFiles(File::isDirectory);
            if (moduleDirs == null) {
                Logging.warning("⚠️ Could not list modules directory");
                return games;
            }
            
            for (File moduleDir : moduleDirs) {
                GameModule game = loadGameModule(moduleDir);
                if (game != null) {
                    games.add(game);
                }
            }
            
            Logging.info("✅ Discovered " + games.size() + " games from modules directory");
            
        } catch (Exception e) {
            Logging.error("❌ Error discovering games from modules: " + e.getMessage(), e);
        }
        
        return games;
    }
    
    /**
     * Loads a game module from a module directory.
     * Tries compiled modules first, then falls back to source code parsing.
     * 
     * @param moduleDir The module directory
     * @return The loaded GameModule, or null if loading failed
     */
    private GameModule loadGameModule(File moduleDir) {
        try {
            String moduleName = moduleDir.getName();
            Logging.info("🔍 Loading module: " + moduleName);
            
            // Try to load compiled module first
            GameModule compiledModule = loadCompiledModule(moduleDir, moduleName);
            if (compiledModule != null) {
                Logging.info("✅ Successfully loaded compiled module: " + compiledModule.getGameName() + " from " + moduleName);
                return compiledModule;
            }
            
            // Fallback to source code parsing
            GameModule sourceModule = loadFromSourceCode(moduleDir, moduleName);
            if (sourceModule != null) {
                Logging.info("✅ Successfully loaded from source: " + sourceModule.getGameName() + " from " + moduleName);
            }
            
            return sourceModule;
            
        } catch (Exception e) {
            Logging.error("❌ Error loading module " + moduleDir.getName() + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Attempts to load a compiled game module.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @return The loaded GameModule, or null if loading failed
     */
    private GameModule loadCompiledModule(File moduleDir, String moduleName) {
        try {
            // Try to load from compiled classes
            Class<?> gameClass = tryLoadFromCompiledClasses(moduleDir, moduleName);
            if (gameClass != null) {
                return instantiateGameModule(gameClass);
            }
            
            // Try to load from current classpath
            gameClass = tryLoadFromClasspath(moduleName);
            if (gameClass != null) {
                return instantiateGameModule(gameClass);
            }
            
            return null;
            
        } catch (Exception e) {
            Logging.warning("⚠️ Error loading compiled module " + moduleName + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Attempts to load a class from compiled classes in the module.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @return The loaded Class, or null if not found
     */
    private Class<?> tryLoadFromCompiledClasses(File moduleDir, String moduleName) {
        try {
            File classesDir = new File(moduleDir, ModuleConfig.CLASSES_DIR);
            if (!classesDir.exists()) {
                return null;
            }
            
            URLClassLoader classLoader = new URLClassLoader(new URL[]{classesDir.toURI().toURL()});
            String fullClassName = ModuleConfig.MODULE_PACKAGE_PREFIX + moduleName.toLowerCase() + "." + ModuleConfig.MODULE_MAIN_CLASS_NAME;
            
            return classLoader.loadClass(fullClassName);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Attempts to load a class from the current classpath.
     * 
     * @param moduleName The module name
     * @return The loaded Class, or null if not found
     */
    private Class<?> tryLoadFromClasspath(String moduleName) {
        try {
            String fullClassName = ModuleConfig.MODULE_PACKAGE_PREFIX + moduleName.toLowerCase() + "." + ModuleConfig.MODULE_MAIN_CLASS_NAME;
            return Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * Instantiates a GameModule from a Class.
     * 
     * @param gameClass The game module class
     * @return The instantiated GameModule, or null if instantiation failed
     */
    private GameModule instantiateGameModule(Class<?> gameClass) {
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
     * Loads a game module from source code by parsing Java files.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @return The loaded GameModule, or null if loading failed
     */
    private GameModule loadFromSourceCode(File moduleDir, String moduleName) {
        try {
            List<File> sourceFiles = findJavaSourceFiles(moduleDir);
            if (sourceFiles.isEmpty()) {
                Logging.warning("⚠️ No Java source files found in: " + moduleName);
                return null;
            }
            
            // Look for the main class file
            for (File sourceFile : sourceFiles) {
                if (sourceFile.getName().equals(ModuleConfig.MODULE_MAIN_CLASS_NAME + ".java")) {
                    return parseGameModuleFromFile(sourceFile, moduleName);
                }
            }
            
            // If no main class found, create a basic module from any Java file
            if (!sourceFiles.isEmpty()) {
                return createBasicGameModule(moduleDir, moduleName, sourceFiles.get(0));
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error loading from source code: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Finds all Java source files in a module directory.
     * 
     * @param moduleDir The module directory
     * @return List of Java source files
     */
    private List<File> findJavaSourceFiles(File moduleDir) {
        List<File> sourceFiles = new ArrayList<>();
        
        try {
            Path srcPath = Paths.get(moduleDir.getAbsolutePath(), ModuleConfig.SRC_DIR);
            if (!Files.exists(srcPath)) {
                return sourceFiles;
            }
            
            try (Stream<Path> paths = Files.walk(srcPath)) {
                sourceFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            }
            
        } catch (IOException e) {
            Logging.error("❌ Error finding source files in " + moduleDir.getName() + ": " + e.getMessage(), e);
        }
        
        return sourceFiles;
    }
    
    /**
     * Parses a game module from a single source file.
     * 
     * @param sourceFile The source file
     * @param moduleName The module name
     * @return The parsed GameModule, or null if parsing failed
     */
    private GameModule parseGameModuleFromFile(File sourceFile, String moduleName) {
        try {
            String content = Files.readString(sourceFile.toPath());
            
            // Extract basic information from source code
            String gameName = extractGameName(content, moduleName);
            String gameId = extractGameId(content, moduleName);
            String description = extractDescription(content);
            int minPlayers = extractMinPlayers(content);
            int maxPlayers = extractMaxPlayers(content);
            int duration = extractDuration(content);
            
            // Create a game module from the parsed information
            return createGameModuleFromInfo(gameId, gameName, description, minPlayers, maxPlayers, duration, moduleName);
            
        } catch (IOException e) {
            Logging.error("❌ Error reading source file: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Creates a basic game module when no specific game module class is found.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @param sourceFile A source file to use as reference
     * @return The created GameModule
     */
    private GameModule createBasicGameModule(File moduleDir, String moduleName, File sourceFile) {
        String gameName = capitalize(moduleName);
        String gameId = moduleName.toLowerCase();
        String description = "Game module: " + gameName;
        
        return createGameModuleFromInfo(gameId, gameName, description, 1, 4, 30, moduleName);
    }
    
    /**
     * Creates a GameModule from basic information.
     * 
     * @param gameId The game ID
     * @param gameName The game name
     * @param description The game description
     * @param minPlayers Minimum players
     * @param maxPlayers Maximum players
     * @param duration Estimated duration in minutes
     * @param moduleName The module name
     * @return The created GameModule
     */
    private GameModule createGameModuleFromInfo(String gameId, String gameName, String description, 
                                               int minPlayers, int maxPlayers, int duration, String moduleName) {
        return new GameModule() {
            @Override
            public String getGameId() { return gameId; }
            
            @Override
            public String getGameName() { return gameName; }
            
            @Override
            public String getGameDescription() { return description; }
            
            @Override
            public int getMinPlayers() { return minPlayers; }
            
            @Override
            public int getMaxPlayers() { return maxPlayers; }
            
            @Override
            public int getEstimatedDuration() { return duration; }
            
            @Override
            public GameDifficulty getDifficulty() { return GameDifficulty.MEDIUM; }
            
            @Override
            public String getGameCategory() { return "Local Module"; }
            
            @Override
            public boolean supportsOnlineMultiplayer() { return true; }
            
            @Override
            public boolean supportsLocalMultiplayer() { return true; }
            
            @Override
            public boolean supportsSinglePlayer() { return true; }
            
            @Override
            public Scene launchGame(Stage primaryStage, GameMode gameMode, int playerCount, GameOptions gameOptions) {
                // Create a simple placeholder scene
                javafx.scene.control.Label label = new javafx.scene.control.Label("Game: " + gameName + " (Source Code)");
                return new Scene(label, 400, 300);
            }
            
            @Override
            public String getGameIconPath() { 
                return ModuleConfig.MODULES_DIR + "/" + moduleName + "/" + ModuleConfig.ICONS_DIR + "/icon.png";
            }
            
            @Override
            public String getGameFxmlPath() { 
                return ModuleConfig.MODULES_DIR + "/" + moduleName + "/" + ModuleConfig.FXML_DIR + "/" + moduleName + ".fxml";
            }
            
            @Override
            public String getGameCssPath() { 
                return ModuleConfig.MODULES_DIR + "/" + moduleName + "/" + ModuleConfig.CSS_DIR + "/" + moduleName + ".css";
            }
            
            @Override
            public void onGameClose() { }
            
            @Override
            public GameState getGameState() { 
                return new GameState(gameId, gameName, GameMode.SINGLE_PLAYER, 1, new GameOptions());
            }
            
            @Override
            public void loadGameState(GameState gameState) { }
        };
    }
    
    /**
     * Extracts game name from source code content.
     * 
     * @param content The source code content
     * @param moduleName The module name
     * @return The extracted game name
     */
    private String extractGameName(String content, String moduleName) {
        // Look for class name patterns
        String[] patterns = {
            "class\\s+(\\w+)\\s+implements\\s+GameModule",
            "class\\s+(\\w+)\\s+extends\\s+\\w+",
            "public\\s+class\\s+(\\w+)"
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                String className = m.group(1);
                if (!className.equals(ModuleConfig.MODULE_MAIN_CLASS_NAME)) {
                    return className.replaceAll("Module$", "").replaceAll("Game$", "");
                }
            }
        }
        
        return capitalize(moduleName);
    }
    
    /**
     * Extracts game ID from source code content.
     * 
     * @param content The source code content
     * @param moduleName The module name
     * @return The extracted game ID
     */
    private String extractGameId(String content, String moduleName) {
        // Look for getGameId() method
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("getGameId\\(\\)\\s*\\{\\s*return\\s*[\"']([^\"']+)[\"']");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return moduleName.toLowerCase();
    }
    
    /**
     * Extracts description from source code content.
     * 
     * @param content The source code content
     * @return The extracted description
     */
    private String extractDescription(String content) {
        // Look for getGameDescription() method
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("getGameDescription\\(\\)\\s*\\{\\s*return\\s*[\"']([^\"']+)[\"']");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return "A local game module";
    }
    
    /**
     * Extracts minimum players from source code content.
     * 
     * @param content The source code content
     * @return The extracted minimum players
     */
    private int extractMinPlayers(String content) {
        // Look for getMinPlayers() method
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("getMinPlayers\\(\\)\\s*\\{\\s*return\\s*(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        
        return 1;
    }
    
    /**
     * Extracts maximum players from source code content.
     * 
     * @param content The source code content
     * @return The extracted maximum players
     */
    private int extractMaxPlayers(String content) {
        // Look for getMaxPlayers() method
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("getMaxPlayers\\(\\)\\s*\\{\\s*return\\s*(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        
        return 4;
    }
    
    /**
     * Extracts duration from source code content.
     * 
     * @param content The source code content
     * @return The extracted duration
     */
    private int extractDuration(String content) {
        // Look for getEstimatedDuration() method
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("getEstimatedDuration\\(\\)\\s*\\{\\s*return\\s*(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        
        return 30;
    }
    
    /**
     * Capitalizes the first letter of a string.
     * 
     * @param str The string to capitalize
     * @return The capitalized string
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
} 