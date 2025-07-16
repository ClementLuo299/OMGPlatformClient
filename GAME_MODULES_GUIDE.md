# Game Modules Development Guide

This guide explains how to create new game modules for the OMGPlatform application.

## Overview

The game system is designed to be modular, allowing each game to be developed independently while integrating seamlessly with the main application. Each game is a separate JavaFX module that implements the `GameModule` interface.

## Architecture

### Core Components

1. **GameModule Interface** (`com.games.GameModule`)
   - Defines the contract that all games must implement
   - Handles game metadata, launching, and state management

2. **GameLauncherService** (`com.services.GameLauncherService`)
   - Manages game registration and launching
   - Handles game lifecycle and state persistence

3. **GameRegistry** (`com.games.GameRegistry`)
   - Automatically discovers and registers game modules
   - Provides filtering and querying capabilities

4. **GameOptions** (`com.games.GameOptions`)
   - Flexible configuration system for game-specific options
   - Supports various data types (String, Integer, Boolean, Object)

5. **GameState** (`com.games.GameState`)
   - Handles saving and loading game states
   - Supports game-specific state data

## Creating a New Game Module

### Step 1: Create the Game Module Class

Create a new class that implements `GameModule`:

```java
package com.games.modules;

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
    
    // The BaseGameModule handles the launchGame method automatically
    // You only need to implement the abstract methods below
    
    @Override
    protected String getGameBasePath() {
        return "/games/mygame";
    }
    
    @Override
    protected Class<?> getGameControllerClass() {
        return MyGameController.class;
    }
    
    @Override
    protected void initializeGameController(Object controller, GameMode gameMode, int playerCount, GameOptions gameOptions) {
        if (controller instanceof MyGameController) {
            MyGameController myGameController = (MyGameController) controller;
            myGameController.initializeGame(gameMode, playerCount, gameOptions);
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
        // Add more state data as needed
        
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
      fx:controller="com.games.modules.MyGameModule$MyGameController"
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
        <!-- Add your game-specific UI elements here -->
        <Label fx:id="statusLabel" text="Game Status" styleClass="status-label"/>
    </VBox>

    <!-- Game Controls -->
    <HBox spacing="10" alignment="CENTER">
        <Button text="New Game" fx:id="newGameBtn" styleClass="control-button"/>
        <Button text="Reset" fx:id="resetBtn" styleClass="control-button"/>
        <Button text="Back to Library" fx:id="backBtn" styleClass="control-button"/>
    </HBox>

</VBox>
```

### Step 4: Create CSS File

Create CSS styles for your game:

```css
/* My Game Styles */

.game-container {
    -fx-background-color: #2c3e50;
    -fx-padding: 20px;
}

.game-title {
    -fx-font-size: 24px;
    -fx-font-weight: bold;
    -fx-text-fill: #ecf0f1;
    -fx-alignment: center;
}

.status-label {
    -fx-font-size: 18px;
    -fx-text-fill: #ecf0f1;
    -fx-alignment: center;
}

.control-button {
    -fx-background-color: #3498db;
    -fx-text-fill: white;
    -fx-font-size: 14px;
    -fx-padding: 8px 16px;
    -fx-cursor: hand;
}

.control-button:hover {
    -fx-background-color: #2980b9;
}

.control-button:pressed {
    -fx-background-color: #21618c;
}

/* Add your game-specific styles here */
```

### Step 5: Register the Game

Add your game to the `GameRegistry`:

```java
// In GameRegistry.java, add to registerGameModules() method:
registerGame(new MyGameModule());
```

### Step 6: Add Game Icon

Place your game icon in `src/main/resources/games/mygame/icons/mygame.png`

## File Structure

Your game should follow this organized structure:

```
src/main/java/com/games/modules/
├── MyGameModule.java

src/main/resources/games/mygame/
├── fxml/
│   └── mygame.fxml
├── css/
│   └── mygame.css
└── icons/
    └── mygame.png
```

This organization groups all assets for each game in their own folder, making it easier to manage and maintain individual games.

## Best Practices

1. **Naming Convention**: Use lowercase, descriptive names for game IDs (e.g., "tictactoe", "connect4")

2. **Error Handling**: Always wrap game launching in try-catch blocks and log errors appropriately

3. **Resource Management**: Clean up resources in `onGameClose()` method

4. **State Management**: Implement proper save/load functionality using `GameState`

5. **UI Responsiveness**: Don't block the UI thread with long-running operations

6. **Logging**: Use the `Logging` utility for consistent logging across the application

## Game Options

Use `GameOptions` to make your game configurable:

```java
// Setting options
gameOptions.setOption("boardSize", 8);
gameOptions.setOption("aiDifficulty", "hard");
gameOptions.setOption("timeLimit", 300);

// Reading options
int boardSize = gameOptions.getIntOption("boardSize", 8);
String aiDifficulty = gameOptions.getStringOption("aiDifficulty", "medium");
int timeLimit = gameOptions.getIntOption("timeLimit", 300);
```

## Game States

The `BaseGameModule` provides default implementations for save/load functionality. Override these methods if you need custom behavior:

```java
@Override
public GameState getGameState() {
    // Call the base implementation first
    GameState gameState = super.getGameState();
    
    // Add your game-specific state data
    gameState.setStateValue("currentPlayer", currentPlayer);
    gameState.setStateValue("boardState", boardState);
    gameState.setStateValue("score", score);
    
    return gameState;
}

@Override
public void loadGameState(GameState gameState) {
    // Call the base implementation first
    super.loadGameState(gameState);
    
    if (gameState != null) {
        // Load your game-specific state data
        currentPlayer = gameState.getStringStateValue("currentPlayer", "Player1");
        boardState = (String[][]) gameState.getStateValue("boardState");
        score = gameState.getIntStateValue("score", 0);
    }
}
```

## Testing Your Game

1. Compile the project: `mvn compile`
2. Run the application: `mvn javafx:run`
3. Navigate to the Game Library
4. Click on your game to launch it

## Example Games

See the existing implementations for reference:
- `TicTacToeModule` - Simple 3x3 grid game
- `Connect4Module` - More complex board game

These examples demonstrate different approaches to game implementation and can serve as templates for your own games. 