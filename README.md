# Board Game Platform

A multiplayer board game platform built with JavaFX.

## Prerequisites

- Java 17 or higher
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

## Project Structure

```
BoardGamePlatform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── core/           # Core game logic
│   │   │   └── gui/            # JavaFX UI
│   │   └── resources/
│   │       ├── fxml/           # FXML files
│   │       ├── css/            # Stylesheets
│   │       └── data/           # Game data, user data
│   └── test/
│       └── java/               # Test files
├── docs/                       # Documentation
└── target/                     # Build output
```

## Features

- User authentication
- Multiplayer game support
- Real-time game updates
- User statistics tracking
- Modern UI with JavaFX

## Development

The project uses:
- JavaFX for the UI
- Maven for build management
- JUnit for testing

## License

[Your License Here]
