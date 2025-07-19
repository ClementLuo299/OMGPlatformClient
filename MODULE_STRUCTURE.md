# Module Structure Documentation

## Overview

The OMGPlatform now uses a completely modular architecture where games are separated from the main application. This provides better organization, maintainability, and allows for independent development of games.

## Main Application Structure

The main application (`src/main/`) contains only the core platform infrastructure:

```
src/main/
├── java/com/games/
│   ├── BaseGameModule.java          # Base class for game modules
│   ├── GameModule.java              # Interface for game modules
│   ├── GameRegistry.java            # Game registration and management
│   ├── GameDiscoveryService.java    # Dynamic game discovery
│   ├── LocalGameSource.java         # Local game discovery (now empty)
│   ├── RemoteGameSource.java        # Remote game discovery
│   ├── GameState.java               # Game state management
│   └── GameOptions.java             # Game configuration options
└── resources/
    ├── games/                       # Empty (games moved to modules)
    ├── css/                         # Main app styles
    ├── fxml/                        # Main app UI
    ├── icons/                       # Main app icons
    └── ...
```

## Module Structure

Each game is now a separate module in the `modules/` directory:

```
modules/
├── tictactoe/                       # Existing TicTacToe game
├── snake/                           # Snake game module
├── chess/                           # Chess game module
├── solitaire/                       # Solitaire game module
├── tetris/                          # Tetris game module
└── example/                         # Example/template module
```

### Individual Module Structure

Each module follows this structure:

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

## Benefits of This Structure

1. **Separation of Concerns**: Main app focuses on platform features, games are independent
2. **Independent Development**: Each game can be developed separately
3. **Easier Maintenance**: Changes to one game don't affect others
4. **Better Organization**: Clear structure for adding new games
5. **Scalability**: Easy to add new games without cluttering the main app

## Current Status

- ✅ Main application is clean and focused on core functionality
- ✅ All games moved to separate modules
- ✅ Each module has proper documentation
- ✅ Application compiles successfully
- ⏳ Module integration with main app (build system updates needed)

## Next Steps for Integration

To fully integrate the modules with the main application, you would need to:

1. **Update Build System**: Configure Maven/Gradle to include modules in classpath
2. **Update LocalGameSource**: Add logic to discover modules dynamically
3. **Add Module Loading**: Implement dynamic loading of game modules
4. **Testing**: Ensure all games work correctly when loaded from modules

## Available Games

Currently, the following games are available as separate modules:

- **TicTacToe**: Classic 3x3 grid game (fully implemented)
- **Snake**: Classic arcade game (placeholder)
- **Chess**: Strategy board game (placeholder)
- **Solitaire**: Card game (placeholder)
- **Tetris**: Puzzle game (placeholder)
- **Example**: Template for new games (placeholder)

Each module includes proper documentation and is ready for implementation. 