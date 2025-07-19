# OMGPlatform - Board Game Platform

A multiplayer board game platform built with JavaFX featuring a dynamic module system for game discovery and loading.

## 📚 Documentation

For comprehensive documentation including architecture, module development, and system overview, see:
**[OMGPLATFORM_DOCUMENTATION.md](OMGPLATFORM_DOCUMENTATION.md)**

## Prerequisites

- Java 23 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd BoardGamePlatform
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```
   Or:
   Run the `com.MainApplication` class in the `src/java` directory

## Project Structure

```
OMGPlatform/
├── src/main/
│   ├── java/com/
│   │   ├── games/                    # Core game system & dynamic module loading
│   │   ├── gui_controllers/          # JavaFX UI controllers
│   │   ├── network/                  # Networking and authentication
│   │   ├── services/                 # Core services
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

**Key Features:**
- **Dynamic Module System**: Games are discovered and loaded at runtime
- **Complete Separation**: Main app is independent of specific games
- **Modular Architecture**: Each game is self-contained
- **Plug-and-Play**: Add/remove games without touching the main app

## Features

- **Dynamic Game Discovery**: Games are automatically discovered and displayed
- **Modular Architecture**: Each game is completely independent and self-contained
- **User Authentication**: Complete user management system
- **Multiplayer Support**: Local and online multiplayer games
- **Real-time Updates**: WebSocket messaging for real-time communication
- **Advanced Filtering**: Filter games by category, difficulty, and game mode
- **User Statistics**: Comprehensive statistics tracking and matchmaking
- **Modern UI**: JavaFX-based user interface
- **Plug-and-Play**: Add/remove games without modifying the main application

## Development

The project uses:
- JavaFX for the UI
- Maven for build management
- JUnit for testing

