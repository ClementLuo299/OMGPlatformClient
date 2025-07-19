# Connect4 Game Module

A Connect4 game module for the OMG Platform.

## Description

Connect4 is a classic strategy game where players take turns dropping colored tokens into a vertical grid. The goal is to connect four tokens of the same color in a row, either horizontally, vertically, or diagonally.

## Features

- **Classic Gameplay**: Standard 6x7 grid Connect4 rules
- **Multiple Modes**: Single player (vs AI), local multiplayer, online multiplayer
- **Configurable Options**: Adjustable board size, AI difficulty, time limits
- **Game State Management**: Save and load game progress
- **Modern UI**: Clean, responsive interface

## Game Rules

1. Players take turns dropping tokens into any of the seven columns
2. Tokens fall to the lowest available position in the chosen column
3. The first player to connect four tokens in a row wins
4. If the board fills up without a winner, the game is a draw

## Technical Details

- **Category**: Strategy
- **Difficulty**: Medium
- **Players**: 2 players
- **Duration**: ~10 minutes
- **Board Size**: 6 rows × 7 columns (configurable)

## Files

- `Connect4Module.java` - Main game module implementation
- `Connect4Controller.java` - Game logic and controller
- `README.md` - This documentation

## Dependencies

- OMG Platform Core (`com.games.BaseGameModule`)
- JavaFX for UI
- Logging utilities (`com.utils.error_handling.Logging`)

## Installation

1. Place this module in the `modules/connect4/` directory
2. The game will be automatically discovered and loaded by the platform
3. No additional configuration required

## Usage

The game will appear in the game library under the "Strategy" category. Players can select it to start a new game or continue a saved game.

## Development

To modify this game:

1. Edit the game logic in `Connect4Controller.java`
2. Update game options in `Connect4Module.java`
3. Recompile the module
4. The changes will be automatically detected

## License

This module is part of the OMG Platform and follows the same licensing terms. 