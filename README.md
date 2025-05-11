# Board Game Platform

A multiplayer board game platform built with JavaFX.

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
   Run the `com.app.App` class in the `src/java` directory

## Project Structure

```
BoardGamePlatform/
├── src/
│   ├── java/
│   │   ├── gamelogic/         # Game logic codebase
│   │   ├── com.gui/               # JavaFX UI codebase
│   │   ├── com.networking/        # Networking and Authentication codebase
│   │   └── statistics/        # Matchmaking and Statistics codebase
│   └── resources/
│       ├── fxml/              # FXML files
│       ├── css/               # Stylesheets
│       └── data/              # Game data, user data
├── tests/
│   ├── gamelogic/             # Game logic tests
│   ├── com.gui/                   # JavaFX UI tests
│   ├── com.networking/            # Networking and Authentication tests
│   └── statistics/            # Matchmaking and Statistics tests              
├── docs/
│   ├── critique-bravo-echo/   # Critiques to other teams
│   ├── critique-received/     # Critiques from other teams
│   ├── diagrams/              # Diagram Documents
│   └── planning/              # Planning Documents
└── target/                    # Build output
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

