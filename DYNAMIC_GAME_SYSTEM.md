# Dynamic Game Discovery System

## Overview

The OMGPlatform now features a **Dynamic Game Discovery System** that automatically discovers and displays games without requiring manual GUI modifications. This system makes it incredibly easy to add new games to the platform.

## 🎯 Key Benefits

- **No Manual GUI Changes**: Adding new games doesn't require modifying FXML files or controllers
- **Automatic Discovery**: Games are automatically discovered and displayed
- **Multiple Sources**: Support for both local games and remote game servers
- **Advanced Filtering**: Filter games by category, difficulty, game mode, and search terms
- **Scalable**: Easy to add hundreds of games without performance issues
- **Extensible**: Simple to add new game sources (local, remote, plugin-based)

## 🏗️ Architecture

### Core Components

1. **GameDiscoveryService** - Main service that orchestrates game discovery
2. **GameSource Interface** - Abstract interface for different game sources
3. **LocalGameSource** - Discovers games from the local classpath
4. **RemoteGameSource** - Discovers games from remote servers (future)
5. **DynamicGameLibraryController** - UI controller that automatically displays discovered games

### System Flow

```
1. Application Startup
   ↓
2. GameDiscoveryService.initialize()
   ↓
3. Register Game Sources (Local, Remote)
   ↓
4. discoverAllGames() - Async discovery from all sources
   ↓
5. Games automatically appear in UI
   ↓
6. Users can filter, search, and launch games
```

## 🚀 Adding New Games

### Method 1: Local Games (Recommended)

1. **Create your game module** by extending `BaseGameModule`:

```java
package com.games.modules.mygame;

import com.games.BaseGameModule;
import com.games.GameModule;

public class MyGameModule extends BaseGameModule {
    
    private static final String GAME_ID = "my-game";
    private static final String GAME_NAME = "My Awesome Game";
    private static final String GAME_DESCRIPTION = "A fantastic new game";
    
    @Override
    public String getGameId() { return GAME_ID; }
    
    @Override
    public String getGameName() { return GAME_NAME; }
    
    @Override
    public String getGameDescription() { return GAME_DESCRIPTION; }
    
    @Override
    public int getMinPlayers() { return 2; }
    
    @Override
    public int getMaxPlayers() { return 4; }
    
    @Override
    public int getEstimatedDuration() { return 20; }
    
    @Override
    public GameDifficulty getDifficulty() { return GameDifficulty.MEDIUM; }
    
    @Override
    public String getGameCategory() { return "Strategy"; }
    
    @Override
    public boolean supportsOnlineMultiplayer() { return true; }
    
    @Override
    public boolean supportsLocalMultiplayer() { return true; }
    
    @Override
    public boolean supportsSinglePlayer() { return false; }
    
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
}
```

2. **Add your game to LocalGameSource**:

```java
// In LocalGameSource.java
@Override
public List<GameModule> discoverGames() {
    List<GameModule> games = new ArrayList<>();
    
    // Existing games
    games.add(new TicTacToeModule());
    
    // Your new game
    games.add(new MyGameModule());
    
    return games;
}
```

3. **Create your game resources**:
   ```
   src/main/resources/games/mygame/
   ├── fxml/
   │   └── mygame.fxml
   ├── css/
   │   └── mygame.css
   └── icons/
       └── mygame.png
   ```

**That's it!** Your game will automatically appear in the game library.

### Method 2: Remote Games (Future)

The system is designed to support remote game discovery from servers:

```java
// RemoteGameSource will automatically fetch games from:
// https://api.omgplatform.com/games
```

## 🎮 Game Categories

The system supports these game categories:

- **Classic** - Traditional board games (TicTacToe, Checkers)
- **Strategy** - Strategic thinking games (Chess)
- **Puzzle** - Logic and puzzle games (Sudoku, Minesweeper)
- **Card** - Card-based games (Solitaire, Poker)
- **Arcade** - Fast-paced action games (Snake, Tetris)

## 🔍 Filtering and Search

The dynamic game library provides powerful filtering:

- **Category Filter**: Filter by game category
- **Difficulty Filter**: Easy, Medium, Hard, Variable
- **Game Mode Filter**: Single Player, Local Multiplayer, Online Multiplayer
- **Search**: Search by game name, description, or category
- **Refresh**: Manually refresh to discover new games

## 📊 Game Metadata

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

## 🔧 Configuration

### Enabling Remote Games

To enable remote game discovery:

```java
RemoteGameSource remoteSource = new RemoteGameSource();
remoteSource.setEnabled(true);
remoteSource.setServerUrl("https://your-game-server.com/api/games");

GameDiscoveryService.getInstance().addGameSource(remoteSource);
```

### Custom Game Sources

You can create custom game sources by implementing `GameSource`:

```java
public class CustomGameSource implements GameDiscoveryService.GameSource {
    
    @Override
    public String getName() {
        return "Custom Games";
    }
    
    @Override
    public List<GameModule> discoverGames() {
        // Your custom discovery logic
        return discoveredGames;
    }
    
    @Override
    public boolean isAvailable() {
        return true; // Check if source is available
    }
}
```

## 📈 Performance

The system is designed for performance:

- **Async Discovery**: Game discovery happens asynchronously
- **Lazy Loading**: Game resources are loaded only when needed
- **Caching**: Discovered games are cached for quick access
- **Filtering**: Efficient filtering using JavaFX FilteredList

## 🧪 Testing

### Adding a Test Game

1. Create `ExampleGameModule` (already included)
2. The game will appear in the library automatically
3. Test filtering, searching, and launching

### Testing Game Discovery

```java
// Test discovery service
GameDiscoveryService service = GameDiscoveryService.getInstance();
service.initialize();

CompletableFuture<List<GameModule>> future = service.discoverAllGames();
future.thenAccept(games -> {
    System.out.println("Discovered " + games.size() + " games");
    games.forEach(game -> System.out.println("- " + game.getGameName()));
});
```

## 🚀 Migration from Old System

If you have existing games using the old manual system:

1. **Keep existing games working** - The old system still works
2. **Gradually migrate** - Move games to the new dynamic system one by one
3. **Update GameRegistry** - Remove manual registration from GameRegistry
4. **Test thoroughly** - Ensure all games still work after migration

## 📝 Best Practices

### Game Development

1. **Use meaningful IDs**: `getGameId()` should be unique and descriptive
2. **Provide good descriptions**: Help users understand your game
3. **Set appropriate metadata**: Accurate player counts, duration, difficulty
4. **Include proper icons**: 60x60 PNG icons work best
5. **Test all game modes**: Ensure single/multiplayer modes work correctly

### Resource Organization

```
src/main/resources/games/yourgame/
├── fxml/
│   └── yourgame.fxml      # Main game UI
├── css/
│   └── yourgame.css       # Game-specific styles
└── icons/
    └── yourgame.png       # Game icon (60x60 recommended)
```

### Error Handling

```java
@Override
protected void initializeGameController(Object controller, GameMode gameMode, 
                                      int playerCount, GameOptions gameOptions) {
    try {
        if (controller instanceof MyGameController) {
            MyGameController myController = (MyGameController) controller;
            myController.initializeGame(gameMode, playerCount, gameOptions);
        }
    } catch (Exception e) {
        Logging.error("Failed to initialize game controller: " + e.getMessage(), e);
    }
}
```

## 🎉 Conclusion

The Dynamic Game Discovery System makes adding new games to OMGPlatform incredibly simple. No more manual GUI modifications, no more complex registration processes. Just create your game module, add it to the discovery source, and it automatically appears in the game library!

This system scales from a few games to hundreds of games without any performance issues, and provides users with powerful filtering and search capabilities to find the games they want to play. 