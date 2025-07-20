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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Discovers games from source code in the modules directory.
 * This source can load uncompiled game modules directly from Java source files.
 * 
 * @authors Clement Luo
 * @date July 18, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
public class SourceCodeGameSource implements GameSource {
    
    @Override
    public String getName() {
        return ModuleConfig.SOURCE_CODE_SOURCE_NAME;
    }
    
    @Override
    public boolean isAvailable() {
        File modulesDir = new File(ModuleConfig.MODULES_DIR);
        return modulesDir.exists() && modulesDir.isDirectory();
    }
    
    @Override
    public List<GameModule> discoverGames() {
        Logging.info("🔍 Discovering games from source code in modules directory...");
        
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
                GameModule game = loadGameFromSource(moduleDir);
                if (game != null) {
                    games.add(game);
                }
            }
            
            Logging.info("✅ Discovered " + games.size() + " games from source code");
            
        } catch (Exception e) {
            Logging.error("❌ Error discovering games from source code: " + e.getMessage(), e);
        }
        
        return games;
    }
    
    /**
     * Loads a game module from source code in a module directory.
     * 
     * @param moduleDir The module directory
     * @return The loaded GameModule, or null if loading failed
     */
    private GameModule loadGameFromSource(File moduleDir) {
        try {
            String moduleName = moduleDir.getName();
            Logging.info("🔍 Loading source code module: " + moduleName);
            
            // Try to load the actual game module class first
            GameModule actualModule = loadActualGameModule(moduleName);
            if (actualModule != null) {
                Logging.info("✅ Successfully loaded actual module: " + actualModule.getGameName() + " from " + moduleName);
                return actualModule;
            }
            
            // Fallback to parsing source files if actual module loading fails
            List<File> sourceFiles = findJavaSourceFiles(moduleDir);
            if (sourceFiles.isEmpty()) {
                Logging.warning("⚠️ No Java source files found in: " + moduleName);
                return null;
            }
            
            // Try to find and parse game module class
            GameModule game = parseGameModuleFromSource(moduleDir, moduleName, sourceFiles);
            if (game != null) {
                Logging.info("✅ Successfully loaded from source: " + game.getGameName() + " from " + moduleName);
            }
            
            return game;
            
        } catch (Exception e) {
            Logging.error("❌ Error loading source module " + moduleDir.getName() + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Try to load the actual game module class using reflection.
     */
    private GameModule loadActualGameModule(String moduleName) {
        try {
            // Use the standardized Main class name
            String className = "com.games.modules." + moduleName + "." + ModuleConfig.MODULE_MAIN_CLASS_NAME;
            
            try {
                Class<?> moduleClass = Class.forName(className);
                if (GameModule.class.isAssignableFrom(moduleClass)) {
                    Object instance = moduleClass.getDeclaredConstructor().newInstance();
                    Logging.info("✅ Loaded actual game module class: " + className);
                    return (GameModule) instance;
                }
            } catch (ClassNotFoundException e) {
                Logging.warning("⚠️ Could not find Main class in: " + moduleName);
                return null;
            } catch (Exception e) {
                Logging.warning("⚠️ Error instantiating " + className + ": " + e.getMessage());
                return null;
            }
            
            Logging.warning("⚠️ Could not find game module class in: " + moduleName);
            return null;
            
        } catch (Exception e) {
            Logging.error("❌ Error loading actual game module: " + e.getMessage(), e);
            return null;
        }
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
     * Parses a game module from source files.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @param sourceFiles List of source files
     * @return The parsed GameModule, or null if parsing failed
     */
    private GameModule parseGameModuleFromSource(File moduleDir, String moduleName, List<File> sourceFiles) {
        try {
            // Look for files that might be game modules
            for (File sourceFile : sourceFiles) {
                String fileName = sourceFile.getName();
                
                // Check if this looks like a game module class
                if (isGameModuleClass(fileName, moduleName)) {
                    GameModule game = parseGameModuleFromFile(sourceFile, moduleName);
                    if (game != null) {
                        return game;
                    }
                }
            }
            
            // If no specific game module found, try to create a basic one from any Java file
            if (!sourceFiles.isEmpty()) {
                return createBasicGameModule(moduleDir, moduleName, sourceFiles.get(0));
            }
            
        } catch (Exception e) {
            Logging.error("❌ Error parsing game module from source: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Checks if a file name looks like a game module class.
     * 
     * @param fileName The file name
     * @param moduleName The module name
     * @return true if it looks like a game module class
     */
    private boolean isGameModuleClass(String fileName, String moduleName) {
        // Use the standardized Main class name
        return fileName.equals(ModuleConfig.MODULE_MAIN_CLASS_NAME + ".java");
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
            
            // Create a basic game module from the parsed information
            return createGameModuleFromInfo(gameId, gameName, description, minPlayers, maxPlayers, duration, moduleName);
            
        } catch (IOException e) {
            Logging.error("❌ Error reading source file " + sourceFile.getName() + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Creates a basic game module when detailed parsing fails.
     * 
     * @param moduleDir The module directory
     * @param moduleName The module name
     * @param sourceFile A source file to use as reference
     * @return A basic GameModule
     */
    private GameModule createBasicGameModule(File moduleDir, String moduleName, File sourceFile) {
        String gameName = capitalize(moduleName);
        String gameId = moduleName.toLowerCase();
        String description = "Game module loaded from source code";
        
        return createGameModuleFromInfo(gameId, gameName, description, 1, 4, 15, moduleName);
    }
    
    /**
     * Creates a GameModule from extracted information.
     * 
     * @param gameId The game ID
     * @param gameName The game name
     * @param description The game description
     * @param minPlayers Minimum players
     * @param maxPlayers Maximum players
     * @param duration Estimated duration
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
            public String getGameCategory() { return "Source Code"; }
            
            @Override
            public boolean supportsOnlineMultiplayer() { return true; }
            
            @Override
            public boolean supportsLocalMultiplayer() { return true; }
            
            @Override
            public boolean supportsSinglePlayer() { return true; }
            
            @Override
            public Scene launchGame(Stage primaryStage, GameMode gameMode, int playerCount, GameOptions gameOptions) {
                Logging.info("🎮 Launching source code game: " + gameName);
                // TODO: Implement actual game launch for source code games
                return null;
            }
            
            @Override
            public String getGameIconPath() { 
                return "/icons/games/source_code_icon.png"; 
            }
            
            @Override
            public String getGameFxmlPath() { 
                return "/games/" + moduleName + "/fxml/" + gameId + ".fxml"; 
            }
            
            @Override
            public String getGameCssPath() { 
                return "/games/" + moduleName + "/css/" + gameId + ".css"; 
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
    
    // Helper methods for extracting information from source code
    private String extractGameName(String content, String moduleName) {
        // Look for patterns like "getGameName() { return \"Game Name\"; }"
        String[] patterns = {
            "getGameName\\(\\)\\s*\\{\\s*return\\s*\"([^\"]+)\"",
            "GAME_NAME\\s*=\\s*\"([^\"]+)\"",
            "gameName\\s*=\\s*\"([^\"]+)\""
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group(1);
            }
        }
        
        return capitalize(moduleName);
    }
    
    private String extractGameId(String content, String moduleName) {
        String[] patterns = {
            "getGameId\\(\\)\\s*\\{\\s*return\\s*\"([^\"]+)\"",
            "GAME_ID\\s*=\\s*\"([^\"]+)\"",
            "gameId\\s*=\\s*\"([^\"]+)\""
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group(1);
            }
        }
        
        return moduleName.toLowerCase();
    }
    
    private String extractDescription(String content) {
        String[] patterns = {
            "getGameDescription\\(\\)\\s*\\{\\s*return\\s*\"([^\"]+)\"",
            "GAME_DESCRIPTION\\s*=\\s*\"([^\"]+)\"",
            "description\\s*=\\s*\"([^\"]+)\""
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group(1);
            }
        }
        
        return "Game loaded from source code";
    }
    
    private int extractMinPlayers(String content) {
        String[] patterns = {
            "getMinPlayers\\(\\)\\s*\\{\\s*return\\s*(\\d+)",
            "MIN_PLAYERS\\s*=\\s*(\\d+)",
            "minPlayers\\s*=\\s*(\\d+)"
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        }
        
        return 1;
    }
    
    private int extractMaxPlayers(String content) {
        String[] patterns = {
            "getMaxPlayers\\(\\)\\s*\\{\\s*return\\s*(\\d+)",
            "MAX_PLAYERS\\s*=\\s*(\\d+)",
            "maxPlayers\\s*=\\s*(\\d+)"
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        }
        
        return 4;
    }
    
    private int extractDuration(String content) {
        String[] patterns = {
            "getEstimatedDuration\\(\\)\\s*\\{\\s*return\\s*(\\d+)",
            "ESTIMATED_DURATION\\s*=\\s*(\\d+)",
            "duration\\s*=\\s*(\\d+)"
        };
        
        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        }
        
        return 15;
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
} 