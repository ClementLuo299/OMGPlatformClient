# Module Standardization: Main Class Approach

## Overview

All game modules now use a standardized `Main` class as their entry point. This simplifies module discovery, improves performance, and provides a consistent structure across all modules.

## Benefits

### 1. **Ultra-Simple Discovery**
- No need to guess class names based on module names
- Direct lookup: `com.games.modules.{modulename}.Main`
- Eliminates complex naming pattern logic

### 2. **Consistent Structure**
- Every module follows the exact same pattern
- No confusion about naming conventions
- Clear and predictable structure

### 3. **Better Performance**
- Single class name lookup instead of multiple attempts
- Faster module loading
- No need to try different naming patterns

### 4. **Easier Development**
- Developers always know to create a `Main` class
- No need to remember specific naming rules
- Reduces cognitive load

### 5. **Future-Proof**
- Works regardless of module name complexity
- No issues with special characters or unusual names
- Scales perfectly as you add more modules

## Implementation

### Configuration Changes

**ModuleConfig.java:**
```java
// Before: Multiple naming patterns
public static final List<String> MODULE_CLASS_SUFFIXES = List.of(
    "Module",
    "GameModule"
);

// After: Single standardized name
public static final String MODULE_MAIN_CLASS_NAME = "Main";
```

### Module Structure

Every module now follows this structure:

```
modules/{gamename}/
├── src/main/java/com/games/modules/{gamename}/
│   └── Main.java                    # ← Standardized main class
├── src/main/resources/games/{gamename}/
│   ├── fxml/
│   ├── css/
│   └── icons/
└── README.md
```

### Class Name Pattern

**Before (Multiple patterns):**
- `TicTacToeModule`
- `SnakeGameModule`
- `ChessModule`
- `ExampleGameModule`

**After (Standardized):**
- `Main` (for all modules)

### Package Structure

All modules use the same package structure:
```java
package com.games.modules.{modulename};

public class Main implements GameModule {
    // Implementation
}
```

## Migration Guide

### For Existing Modules

1. **Rename the main class file** from `{GameName}Module.java` to `Main.java`
2. **Update the class declaration** from `public class {GameName}Module` to `public class Main`
3. **Update the package** to `com.games.modules.{modulename}`
4. **Test the module** to ensure it loads correctly

### Example Migration

**Before:**
```java
// File: TicTacToeModule.java
package com.games.modules.tictactoe;

public class TicTacToeModule implements GameModule {
    // Implementation
}
```

**After:**
```java
// File: Main.java
package com.games.modules.tictactoe;

public class Main implements GameModule {
    // Implementation
}
```

## Code Simplification

### ModuleLoader Changes

**Before (Complex):**
```java
private static Class<?> findGameModuleClass(File moduleDir, String moduleName) {
    // Try different possible class names using config-based suffixes
    List<String> configSuffixes = ModuleConfig.MODULE_CLASS_SUFFIXES;
    
    for (String suffix : configSuffixes) {
        // Try both lowercase and capitalized module names
        String[] possibleClassNames = {
            moduleName + suffix,
            capitalize(moduleName) + suffix
        };
        
        for (String className : possibleClassNames) {
            Class<?> gameClass = tryLoadClass(moduleDir, moduleName, className);
            if (gameClass != null) {
                return gameClass;
            }
        }
    }
    
    return null;
}
```

**After (Simple):**
```java
private static Class<?> findGameModuleClass(File moduleDir, String moduleName) {
    // Use the standardized Main class name
    String className = ModuleConfig.MODULE_MAIN_CLASS_NAME;
    return tryLoadClass(moduleDir, moduleName, className);
}
```

### SourceCodeGameSource Changes

**Before (Complex):**
```java
String[] possibleClassNames = {
    "com.games.modules." + moduleName + "." + capitalize(moduleName) + "Module",
    "com.games.modules." + moduleName + "." + capitalize(moduleName) + "GameModule",
    "com.games.modules." + moduleName + "." + capitalize(moduleName) + "Game",
    // Special case for tictactoe -> TicTacToe
    "com.games.modules." + moduleName + ".TicTacToeModule"
};
```

**After (Simple):**
```java
String className = "com.games.modules." + moduleName + "." + ModuleConfig.MODULE_MAIN_CLASS_NAME;
```

## Performance Improvements

### Before
- Multiple class name attempts per module
- Complex string manipulation and capitalization
- Potential for failed lookups due to naming mismatches

### After
- Single class name lookup per module
- Direct package + class name construction
- Guaranteed consistency across all modules

## Development Guidelines

### Creating New Modules

1. **Create the module directory**: `modules/{gamename}/`
2. **Create the package structure**: `src/main/java/com/games/modules/{gamename}/`
3. **Create the main class**: `Main.java` (always named `Main`)
4. **Implement GameModule interface**: All required methods
5. **Add resources**: FXML, CSS, and icons as needed

### Example Template

```java
package com.games.modules.mygame;

import com.games.GameModule;
import com.games.enums.GameDifficulty;
import com.games.enums.GameMode;
import com.games.GameOptions;
import com.games.GameState;
import com.utils.error_handling.Logging;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main implements GameModule {
    
    private static final String GAME_ID = "mygame";
    private static final String GAME_NAME = "My Game";
    private static final String GAME_DESCRIPTION = "Description of my game";
    
    // Implement all GameModule methods...
    
    @Override
    public Scene launchGame(Stage stage, GameMode mode, int playerCount, GameOptions options) {
        // Game launch implementation
        return scene;
    }
}
```

## Testing

### Module Discovery Test

Run the `ModuleDiscoveryTest` to verify that all modules are discovered correctly:

```bash
mvn test -Dtest=ModuleDiscoveryTest
```

### Expected Output

```
🧪 Starting Module Discovery Test
📊 Module Discovery Results:
Found 13 modules
✅ Tic Tac Toe (tictactoe)
✅ Chess (chess)
✅ Snake (snake)
...
🏁 Module Discovery Test completed
```

## Conclusion

The standardized `Main` class approach provides:

- **Simplicity**: One clear naming convention
- **Performance**: Faster module discovery
- **Reliability**: No more naming-related failures
- **Maintainability**: Easier to understand and modify
- **Scalability**: Works with any module name

This standardization makes the module system much more robust and developer-friendly. 