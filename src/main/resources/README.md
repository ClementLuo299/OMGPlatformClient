# Resources Directory

This directory contains all the static resources for the OMG Platform application.

## 📁 Directory Structure

```
src/main/resources/
├── css/           # Stylesheets for UI components
│   ├── screens/   # Screen-specific stylesheets
│   ├── alert-style.css
│   └── dark-theme.css
├── fxml/          # JavaFX FXML layout files
├── fonts/         # Custom fonts used in the application
├── games/         # Game-specific resources (currently empty)
├── icons/         # Application icons and images
├── images/        # General application images
└── README.md      # This file
```

## 🎨 CSS Files

### Core Stylesheets
- **`alert-style.css`** - Styling for alert dialogs and notifications
- **`dark-theme.css`** - Dark theme color scheme and styling

### Screen-Specific Stylesheets (`screens/` directory)
- **`screens/dashboard.css`** - Dashboard screen styling
- **`screens/leaderboard.css`** - Leaderboard screen styling
- **`screens/library.css`** - Game library screen styling (enhanced with filtering)
- **`screens/login.css`** - Login screen styling
- **`screens/register.css`** - Registration screen styling
- **`screens/setting.css`** - Settings screen styling

## 🖼️ FXML Files

### Screen Layouts
- **`Dashboard.fxml`** - Main dashboard screen layout
- **`GameLibrary.fxml`** - Game library with advanced filtering
- **`Leaderboard.fxml`** - Player rankings and statistics
- **`Login.fxml`** - User authentication screen
- **`Register.fxml`** - New user registration screen
- **`Setting.fxml`** - User preferences and profile settings

## 🎯 Icons Directory

### Navigation Icons
- **`navigation/dashboard.png`** - Dashboard navigation icon
- **`navigation/games.png`** - Games navigation icon
- **`navigation/settings.png`** - Settings navigation icon
- **`navigation/unknown.png`** - Default/unknown navigation icon

### General Icons
- **`badge.png`** - Achievement badges
- **`close.png`** - Close button icon
- **`collapse.png`** - Collapse/expand icon
- **`draw.png`** - Draw/tie game icon
- **`game-over.png`** - Game over indicator
- **`hints.png`** - Help/hints icon
- **`leaderboard.png`** - Leaderboard icon
- **`notification.png`** - Notification bell icon
- **`pause.png`** - Pause game icon
- **`play.png`** - Play game icon
- **`refresh-arrow.png`** - Refresh/reload icon
- **`restart-game.png`** - Restart game icon
- **`reward.png`** - Reward/achievement icon
- **`rules.png`** - Game rules icon
- **`sand-timer.png`** - Timer icon
- **`search.png`** - Search functionality icon
- **`sign-out.png`** - Sign out/logout icon
- **`trophy.png`** - Trophy/winner icon
- **`userprofile.png`** - User profile icon

## 🖼️ Images Directory

### Game Mode Images
- **`back-arrow.png`** - Back navigation arrow
- **`couch-co-op.png`** - Local multiplayer mode
- **`multiplayer.png`** - Multiplayer mode
- **`online-mode.png`** - Online multiplayer mode
- **`singleplayer.png`** - Single player mode

## 🔤 Fonts Directory

- **`Freshman.ttf`** - Custom font for special text elements

## 🎮 Games Directory

Currently empty. This directory is reserved for game-specific resources such as:
- Game-specific FXML layouts
- Game-specific CSS styles
- Game assets and sprites
- Game configuration files

## 📝 Notes

- All FXML files are properly registered in `ScreenRegistry.java`
- CSS files are linked to their corresponding FXML files
- Icons follow a consistent naming convention
- Images are optimized for web/desktop use
- The structure supports easy addition of new games and features

## 🧹 Cleanup History

The following files were removed during cleanup:
- `dynamic-library.css` - Functionality merged into `library.css`
- `GameLobby.fxml` - Unused screen layout
- `game_lobby.css` - Unused stylesheet
- Empty data files - Replaced with proper data management
- Empty `data/` directory - No longer needed

## 🔄 CSS Refactoring

The CSS structure was refactored to improve organization:
- **Screen-specific CSS files** moved to `css/screens/` directory
- **Core CSS files** (`alert-style.css`, `dark-theme.css`) remain in root `css/` directory
- **All FXML files** updated to reference new CSS paths
- **ScreenRegistry** updated to use new CSS paths
- **Better separation** between core styles and screen-specific styles 