# OMGPlatform - Complete Documentation

## Table of Contents

1. [Overview](#overview)
2. [Getting Started](#getting-started)
3. [Architecture](#architecture)
4. [Dynamic Module System](#dynamic-module-system)
5. [Creating Game Modules](#creating-game-modules)
6. [Module Structure](#module-structure)
7. [Development Guide](#development-guide)
8. [Testing](#testing)
9. [Available Games](#available-games)
10. [Troubleshooting](#troubleshooting)

---

## Overview

OMGPlatform is a multiplayer board game platform built with JavaFX that features a **completely dynamic module system**. The main application is completely separate from games and can discover and load them at runtime from the `modules/` directory.

### 🎯 Key Features

- **Dynamic Game Discovery**: Games are automatically discovered and displayed without manual GUI modifications
- **Modular Architecture**: Each game is completely independent and self-contained
- **Multiple Game Sources**: Support for local games and remote game servers
- **Advanced Filtering**: Filter games by category, difficulty, game mode, and search terms
- **Real-time Updates**: WebSocket messaging for real-time communication
- **User Authentication**: Complete user management system
- **Statistics Tracking**: User statistics and matchmaking

### 🏗️ Architecture Benefits

- **Complete Separation**: Main app has zero dependencies on specific games
- **Independent Development**: Each game can be developed separately
- **Plug-and-Play**: Games can be added/removed without touching the main app
- **Scalability**: Easy to add hundreds of games without performance issues
- **Extensibility**: Simple to add new game sources (local, remote, plugin-based)

---

## Getting Started

### Prerequisites

- Java 23 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd OMGPlatform
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```
   Or run the `com.MainApplication` class in the `src/java` directory

### Project Structure

```
OMGPlatform/
├── src/main/
│   ├── java/com/
│   │   ├── games/                    # Core game system
│   │   │   ├── BaseGameModule.java   # Base class for game modules
│   │   │   ├── GameModule.java       # Interface for game modules
│   │   │   ├── LocalGameSource.java  # Local game discovery
│   │   │   ├── RemoteGameSource.java # Remote game discovery
│   │   │   ├── ModuleLoader.java     # Dynamic module loading
│   │   │   ├── GameState.java        # Game state management
│   │   │   └── GameOptions.java      # Game configuration options
│   │   ├── gui_controllers/          # JavaFX UI controllers
│   │   ├── network/                  # Networking and authentication
│   │   ├── services/                 # Core services
│   │   │   ├── GameDiscoveryService.java # Dynamic game discovery
│   │   │   ├── GameLauncherService.java  # Game launching and management
│   │   ├── utils/                    # Utility classes
│   │   └── viewmodels/               # View models
│   └── resources/
│       ├── games/                    # Empty (games moved to modules)
│       ├── css/                      # Main app styles
│       ├── fxml/                     # Main app UI
│       ├── icons/                    # Main app icons
│       └── ...
├── modules/                          # Game modules directory
│   ├── tictactoe/                    # TicTacToe game
│   ├── snake/                        # Snake game
│   ├── chess/                        # Chess game
│   ├── solitaire/                    # Solitaire game
│   ├── tetris/                       # Tetris game
│   └── example/                      # Example/template module
├── tests/                            # Test files
└── target/                           # Build output
```

---

## Architecture

### Core Components

1. **GameDiscoveryService** - Main service that orchestrates game discovery
2. **GameSource Interface** - Abstract interface for different game sources
3. **LocalGameSource** - Discovers games from the local modules directory
4. **RemoteGameSource** - Discovers games from remote servers (future)
5. **ModuleLoader** - Dynamic module discovery and loading utility
6. **DynamicGameLibraryController** - UI controller that automatically displays discovered games

### System Flow

```
1. Application Startup
   ↓
2. GameDiscoveryService.initialize()
   ↓
3. Register Game Sources (Local, Remote)
   ↓
4. ModuleLoader.scanModules() - Scan modules directory
   ↓
5. discoverAllGames() - Async discovery from all sources
   ↓
6. Games automatically appear in UI
   ↓
7. Users can filter, search, and launch games
```

---

## Dynamic Module System

### Overview

The OMGPlatform features a completely dynamic module system where the main application is completely separate from games and can discover and load them at runtime from the `modules/` directory.

### Key Components

#### 1. ModuleLoader (`src/main/java/com/games/ModuleLoader.java`)

The core utility class that handles dynamic module discovery and loading.

**Features:**
- Scans the `modules/` directory for available games
- Attempts to load modules from compiled classes or source files
- Supports multiple naming conventions for module classes
- Provides detailed logging for debugging
- Handles class loading and instantiation

**Methods:**
- `loadAllModules()`: Discovers and loads all available modules
- `loadModule(File moduleDir)`: Loads a single module from a directory
- `findGameModuleClass()`: Finds the main game module class
- `tryLoadClass()`: Attempts to load classes from different sources

#### 2. LocalGameSource (`src/main/java/com/games/LocalGameSource.java`)

Updated to use the ModuleLoader for dynamic discovery.

**Changes:**
- No longer imports specific game modules
- Uses `ModuleLoader.loadAllModules()` for discovery
- Completely decoupled from individual games
- Provides runtime discovery capabilities

#### 3. ModuleDiscoveryTest (`src/main/java/com/test/ModuleDiscoveryTest.java`)

Test class to verify the module discovery system.

**Purpose:**
- Tests if modules are being discovered correctly
- Shows detailed information about found modules
- Helps debug module loading issues
- Can be run independently to test the system

### How It Works

#### 1. Discovery Process
1. **Scan Directory**: ModuleLoader scans the `modules/` directory
2. **Find Modules**: Identifies all subdirectories as potential modules
3. **Load Classes**: Attempts to load the main game module class
4. **Instantiate**: Creates instances of found GameModule classes
5. **Register**: Adds discovered games to the game registry

#### 2. Class Loading Strategy
The ModuleLoader tries multiple approaches to load module classes:

1. **Compiled Classes**: Look for `target/classes/` in the module
2. **Source Files**: Look for `src/main/java/` in the module
3. **Classpath**: Try to load from the current application classpath
4. **Multiple Names**: Try different naming conventions for the main class

#### 3. Naming Conventions
The system supports multiple naming patterns for module classes:
- `{moduleName}Module` (e.g., `snakeModule`)
- `{ModuleName}Module` (e.g., `SnakeModule`)
- `{moduleName}GameModule` (e.g., `snakeGameModule`)
- `{ModuleName}GameModule` (e.g., `SnakeGameModule`)

### Benefits

#### 1. Complete Separation
- Main app has zero dependencies on specific games
- Games can be added/removed without touching the main app
- No compilation dependencies between app and games

#### 2. Dynamic Discovery
- Games are discovered automatically at runtime
- No need to update the main app when adding new games
- Plug-and-play game system

#### 3. Independent Development
- Each game can be developed separately
- Different teams can work on different games
- No risk of breaking other games when modifying one

#### 4. Scalability
- Easy to add new games without modifying existing code
- Games can be distributed separately
- Support for third-party game modules

---

## Module Structure

### Standard Module Structure

Each module follows this standard structure:

```
modules/{gamename}/
├── README.md                        # Module documentation
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── games/
        │           └── modules/
        │               └── {gamename}/
        │                   └── {GameName}Module.java
        └── resources/
            └── games/
                └── {gamename}/
                    ├── fxml/
                    │   └── {gamename}.fxml
                    ├── css/
                    │   └── {gamename}.css
                    └── icons/
```

### Available Modules

Currently, the following modules are available:

- **tictactoe**: Classic 3x3 grid game (fully implemented)
- **snake**: Classic arcade game (placeholder)
- **chess**: Strategy board game (placeholder)
- **solitaire**: Card game (placeholder)
- **tetris**: Puzzle game (placeholder)
- **example**: Template for new games (placeholder)

Each module includes proper documentation and is ready for implementation.

---

## Creating Game Modules

### Step 1: Create the Game Module Class

Create a new class that extends `BaseGameModule`:

```java
package com.games.modules.mygame;

import com.games.BaseGameModule;
import com.games.GameModule;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;

public class MyGameModule extends BaseGameModule {
    
    private static final String GAME_ID = "mygame";
    private static final String GAME_NAME = "My Game";
    private static final String GAME_DESCRIPTION = "Description of my game";
    
    @Override
    public String getGameId() {
        return GAME_ID;
    }
    
    @Override
    public String getGameName() {
        return GAME_NAME;
    }
    
    @Override
    public String getGameDescription() {
        return GAME_DESCRIPTION;
    }
    
    @Override
    public int getMinPlayers() {
        return 2;
    }
    
    @Override
    public int getMaxPlayers() {
        return 4;
    }
    
    @Override
    public int getEstimatedDuration() {
        return 15; // minutes
    }
    
    @Override
    public GameDifficulty getDifficulty() {
        return GameDifficulty.MEDIUM;
    }
    
    @Override
    public String getGameCategory() {
        return "Strategy";
    }
    
    @Override
    public boolean supportsOnlineMultiplayer() {
        return true;
    }
    
    @Override
    public boolean supportsLocalMultiplayer() {
        return true;
    }
    
    @Override
    public boolean supportsSinglePlayer() {
        return false;
    }
    
    @Override
    protected String getGameBasePath() {
        return "/games/mygame";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return MyGameController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, 
                                          int playerCount, GameOptions gameOptions) {
        if (controller instanceof MyGameController) {
            MyGameController myController = (MyGameController) controller;
            myController.initializeGame(gameMode, playerCount, gameOptions);
        }
    }
    
    @Override
    public void onGameClose() {
        Logging.info("🔄 " + GAME_NAME + " closing - cleaning up resources");
        // Clean up any game-specific resources
    }
    
    @Override
    public GameState getGameState() {
        // Create game state for saving
        GameOptions options = new GameOptions();
        options.setOption("customOption", "value");
        
        GameState gameState = new GameState(GAME_ID, GAME_NAME, GameMode.LOCAL_MULTIPLAYER, 2, options);
        
        // Add game-specific state data
        gameState.setStateValue("currentPlayer", "Player1");
        gameState.setStateValue("movesCount", 0);
        
        return gameState;
    }
    
    @Override
    public void loadGameState(GameState gameState) {
        Logging.info("📂 Loading " + GAME_NAME + " game state");
        
        if (gameState != null) {
            // Load game-specific state data
            String currentPlayer = gameState.getStringStateValue("currentPlayer", "Player1");
            int movesCount = gameState.getIntStateValue("movesCount", 0);
            
            Logging.info("📊 Loaded game state - Current player: " + currentPlayer + ", Moves: " + movesCount);
        }
    }
}
```

### Step 2: Create the Game Controller

Create a controller class for your game:

```java
public static class MyGameController {
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Button newGameBtn;
    
    @FXML
    private Button resetBtn;
    
    @FXML
    private Button backBtn;
    
    private GameMode gameMode;
    private int playerCount;
    private GameOptions gameOptions;
    
    @FXML
    public void initialize() {
        // Set up event handlers
        newGameBtn.setOnAction(event -> startNewGame());
        resetBtn.setOnAction(event -> resetGame());
        backBtn.setOnAction(event -> returnToLibrary());
    }
    
    public void initializeGame(GameMode gameMode, int playerCount, GameOptions gameOptions) {
        this.gameMode = gameMode;
        this.playerCount = playerCount;
        this.gameOptions = gameOptions;
        
        Logging.info("🎯 Initializing " + GAME_NAME + " with " + playerCount + " players");
        
        // Initialize your game logic here
        startNewGame();
    }
    
    private void startNewGame() {
        // Implement new game logic
        Logging.info("🆕 Starting new game");
        statusLabel.setText("New game started!");
    }
    
    private void resetGame() {
        // Implement reset logic
        Logging.info("🔄 Resetting game");
        statusLabel.setText("Game reset!");
    }
    
    private void returnToLibrary() {
        // Return to the game library
        Logging.info("🏠 Returning to game library");
        // This would typically navigate back to the main application
    }
}
```

### Step 3: Create FXML File

Create the FXML file for your game UI:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.games.modules.mygame.MyGameModule$MyGameController"
      styleClass="game-container"
      spacing="20"
      alignment="CENTER">

    <padding>
        <Insets top="20" right="20" bottom="20" left="20"/>
    </padding>

    <!-- Game Title -->
    <Label text="My Game" styleClass="game-title"/>

    <!-- Game Content -->
    <VBox spacing="10" alignment="CENTER">
        <Label fx:id="statusLabel" text="Welcome to My Game!" styleClass="status-label"/>
        
        <!-- Game Board/Area -->
        <VBox styleClass="game-board" alignment="CENTER">
            <!-- Add your game-specific UI elements here -->
        </VBox>
    </VBox>

    <!-- Control Buttons -->
    <HBox spacing="10" alignment="CENTER">
        <Button fx:id="newGameBtn" text="New Game" styleClass="game-button"/>
        <Button fx:id="resetBtn" text="Reset" styleClass="game-button"/>
        <Button fx:id="backBtn" text="Back to Library" styleClass="game-button"/>
    </HBox>

</VBox>
```

### Step 4: Create CSS File

Create a CSS file for styling your game:

```css
/* Game Container */
.game-container {
    -fx-background-color: #f5f5f5;
    -fx-font-family: 'Segoe UI', Arial, sans-serif;
}

/* Game Title */
.game-title {
    -fx-font-size: 24px;
    -fx-font-weight: bold;
    -fx-text-fill: #333333;
}

/* Status Label */
.status-label {
    -fx-font-size: 16px;
    -fx-text-fill: #666666;
}

/* Game Board */
.game-board {
    -fx-background-color: white;
    -fx-border-color: #cccccc;
    -fx-border-width: 2px;
    -fx-border-radius: 5px;
    -fx-padding: 20px;
    -fx-min-width: 400px;
    -fx-min-height: 300px;
}

/* Game Buttons */
.game-button {
    -fx-background-color: #4CAF50;
    -fx-text-fill: white;
    -fx-font-size: 14px;
    -fx-padding: 10px 20px;
    -fx-border-radius: 5px;
    -fx-cursor: hand;
}

.game-button:hover {
    -fx-background-color: #45a049;
}
```

### Step 5: Create Module Directory Structure

Create the module directory structure:

```bash
mkdir -p modules/mygame/src/main/java/com/games/modules/mygame
mkdir -p modules/mygame/src/main/resources/games/mygame/fxml
mkdir -p modules/mygame/src/main/resources/games/mygame/css
mkdir -p modules/mygame/src/main/resources/games/mygame/icons
```

### Step 6: Add Module to Discovery

The module will be automatically discovered by the ModuleLoader when placed in the `modules/` directory. No additional configuration is needed!

---

## Development Guide

### Game Categories

The system supports these game categories:

- **Classic** - Traditional board games (TicTacToe, Checkers)
- **Strategy** - Strategic thinking games (Chess)
- **Puzzle** - Logic and puzzle games (Sudoku, Minesweeper)
- **Card** - Card-based games (Solitaire, Poker)
- **Arcade** - Fast-paced action games (Snake, Tetris)

### Game Metadata

Each game provides rich metadata:

```java
// Required metadata
getGameId()           // Unique identifier
getGameName()         // Display name
getGameDescription()  // Brief description
getMinPlayers()       // Minimum players
getMaxPlayers()       // Maximum players
getEstimatedDuration() // Duration in minutes
getDifficulty()       // Game difficulty
getGameCategory()     // Game category

// Optional metadata
getGameIconPath()     // Path to game icon
getGameFxmlPath()     // Path to FXML file
getGameCssPath()      // Path to CSS file

// Game mode support
supportsSinglePlayer()
supportsLocalMultiplayer()
supportsOnlineMultiplayer()
```

### Game State Management

The system provides robust game state management:

```java
// Saving game state
GameState gameState = getGameState();
// The system automatically saves this state

// Loading game state
loadGameState(gameState);
// Called when resuming a game
```

### Error Handling

Use the logging system for debugging:

```java
import com.utils.error_handling.Logging;

Logging.info("🎯 Game initialized successfully");
Logging.warning("⚠️ Player disconnected");
Logging.error("❌ Game error occurred", exception);
```

---

## Testing

### Running the Module Discovery Test

```bash
mvn exec:java -Dexec.mainClass="com.test.ModuleDiscoveryTest"
```

### Expected Output

- Should scan all modules in the `modules/` directory
- Will show which modules were found and loaded
- Provides detailed logging for debugging

### Current Test Results

The test currently shows that modules are being discovered but not loaded because they're not compiled yet, which is expected behavior.

### Testing Your Game Module

1. **Compile your module**:
   ```bash
   cd modules/mygame
   mvn compile
   ```

2. **Run the discovery test**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.test.ModuleDiscoveryTest"
   ```

3. **Check the output** for your game module

---

## Current Status

### ✅ Completed

- Dynamic module discovery system implemented
- ModuleLoader utility class created
- LocalGameSource updated to use dynamic loading
- Test class for verifying module discovery
- Complete separation of main app from games
- All games moved to separate modules
- Comprehensive documentation
- Dynamic game discovery system
- Advanced filtering and search capabilities
- Multiple game sources support

### ⏳ Next Steps

- **Module Compilation**: Compile individual modules to generate class files
- **Classpath Integration**: Ensure module classes are available to the main app
- **Resource Loading**: Handle loading of FXML, CSS, and other resources from modules
- **Testing**: Verify that all games work correctly when loaded dynamically
- **Remote Game Sources**: Implement remote game discovery from servers

---

## Troubleshooting

### Common Issues

#### 1. Module Not Found
**Problem**: Module is not being discovered
**Solution**: 
- Ensure the module is in the `modules/` directory
- Check that the module follows the standard structure
- Verify the class name follows the naming conventions

#### 2. Class Loading Errors
**Problem**: Module classes cannot be loaded
**Solution**:
- Compile the module first: `mvn compile`
- Check that the module implements `GameModule`
- Verify package declarations match the directory structure

#### 3. Resource Loading Issues
**Problem**: FXML or CSS files not found
**Solution**:
- Ensure resources are in the correct directory structure
- Check that paths in `getGameBasePath()` are correct
- Verify file names match the expected patterns

#### 4. Game Not Appearing in UI
**Problem**: Game is discovered but not shown in the interface
**Solution**:
- Check that the game implements all required methods
- Verify game metadata is properly set
- Check the game discovery logs for errors

### Debugging Tips

1. **Use the ModuleDiscoveryTest** to verify module discovery
2. **Check the logs** for detailed error messages
3. **Verify the module structure** matches the standard template
4. **Test with the example module** as a reference

---

## Conclusion

The OMGPlatform provides a robust, scalable architecture for game development. The dynamic module system enables independent development, easy maintenance, and a true plug-and-play game system. With the comprehensive documentation and tools provided, developers can easily create and integrate new games into the platform.

The system is designed to grow with your needs, supporting both local and remote game sources, advanced filtering and search capabilities, and a complete user management system. Whether you're developing a simple puzzle game or a complex multiplayer strategy game, the OMGPlatform provides the tools and architecture you need to succeed. 