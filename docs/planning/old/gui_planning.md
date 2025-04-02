# Graphical User Interface -Planning Document
## Overview
The Board Game Platform is an online multiplayer gaming platform that allows users to play classic board games online with multiple players.
## Responsibilities
The GUI team is responsible for building an intuitive and user-friendly interface that allows players to interact with the game seamlessly. 
Our key responsibilities include:
 - Building a user-friendly interface where players can interact with the game boards, move pieces, and view game stats.
 - Creating an online multiplayer experience, allowing players to select and join games, challenge others, and track their gameplay.
 - Developing a responsive UI that works across different screen sizes and devices.
 - Integrating in-game chat functionality so players can communicate during gameplay.
 - Collaborating with the Game Logic and Networking Teams to ensure seamless interaction between the game interface, backend logic, and multiplayer networking.
 - Ensuring accessibility and usability by following UI/UX best practices.
 - Testing and debugging UI components to ensure a smooth user experience.

## Games to Implement
The GUI will support the following games as part of the platform:
- Connect 4
- Checkers
- Whist
## Development Plan
### Phase 1: Design and Planning (Iteration 1 - Deadline: March 7)
 - Outline the structure and flow of the GUI components.
 - Develop user flows for game selection, matchmaking, and gameplay.
 - Collaborate with the Game Logic and Networking teams to define integration points.
 - Prepare planning documents including use case diagrams, structure diagrams, and a task timeline.
 - Set up a GitLab repository and submit initial planning deliverables.
### Phase 2: Initial Implementation (Iteration 2 - Deadline: March 21)
 - Begin implementing UI components .
 - Design UI components (buttons, game boards, chat windows).
 - Create wireframes and mockups for each game’s interface.
 - Implement basic game board displays for Connect 4, Checkers, and Whist.
 - Develop basic user interaction (placing pieces, selecting options).
 - Integrate UI elements with placeholders for game logic interactions.
 - Implement in-game chat functionality.
 - Start testing the UI design and make improvements based on usability feedback.
 - Submit the first working version of the GUI for review and feedback!
### Phase 3: Integration and Testing (Iteration 3 - Deadline: April 11)
 - Fully integrate the UI with the Game Logic and Networking teams' implementations.
 - Implement animations and visual effects for a polished game experience.
 - Ensure smooth transitions and real-time updates for multiplayer gameplay.
 - Conduct usability testing and gather feedback.
 - Debug and fix UI issues based on feedback.
 - Finalize UI documentation and provide instructions for usage.
 - Record and submit a video demo showcasing the platform's functionality.

## GUI Architecture & Screen Design

This GUI follows a structured layout for seamless navigation.

## Sign-Up & Profile Setup Screen

### Flow
1. Sign-Up Screen : Username, Password, Date of Birth
2. Redirect to Log-In Screen 

### Integration Notes
- Validation messages for incorrect inputs
- Profile settings must be stored and retrieved

## Log-In Screen
### Flow
1. User enters Username & Password
2. On Success, redirects to Dashboard
3. On Failure, displays an error message
### Integration Notes:
- Error handling for incorrect credentials
- Support password reset option
- Ensure updates of stats


## Dashboard Screen

### Features
- Profile Summary : Avatar, Username, Level, Game Stats
- General Stats : Account Level, Most Played Game, Total Games Played, Total Games Won
- Game Specific Stats: Game Level, Total Games Played, Total Games Won
- Search Bar: Look up other players using account ID
- Navigation Menu: Access Leaderboard, Game Library, Matchmaking, Settings
### Integration Notes
- GUI must fetch player stats upon login
- Provide updates for ranking changes

## Game Library & Matchmaking Screen
### Features
- Game Library: Browse available board games.
- Matchmaking Options: 
	- Quick Match : Auto-assigned game
	- Find A Game: Join an open match
	- View : Ongoing games
- Match Status Updates: "Searching for an Opponent"
### Integration Notes
- GUI must sync with matchmaking API
- Display match availability dynamically
## Leaderboard
### Features
- General Leaderboard:
	- Highest Account Level
	- Most Win Across All Games
	- Most Games Played across All Games
- Game Specific Leaderboard:
	- Highest Level
	- Most Wins
	- Most Games Played
	- Best Player
### Integration Notes
- Fetch leaderboard data
- Ensure ranking updates in real-time
- Implement player search 
## Game Screen
### Features
- Game Board & Real-Time Updates
- Turn Indicator : Active Player
- Player Stats 
- Timer : if applicable
### Integration Notes
- Display moves after each  turn
- Fetch updated game state after each turn



### **General Enhancements**
- **Dark Mode & Theme Customization:** Allow users to switch between light and dark mode for better accessibility.
- **Sound & Notification Settings:** Enable sound effects for game moves and notifications for challenges or messages.
- **Error Handling & Recovery:** Implement clear error messages and retry options for connectivity issues.

### **GUI Development Plan Enhancements**
- **UI Component Reusability:** Design modular UI components that can be reused across different game interfaces.
- **Mobile-Friendly Design:** Ensure the UI adapts well to different screen sizes, including mobile devices.
- **Accessibility Compliance:** Follow WCAG guidelines to make the interface accessible to users with disabilities.

### **Game Features Enhancements**
- **Spectator Mode:** Allow users to watch ongoing games.
- **Game Replay & History:** Provide a way to review past matches.
- **Pause & Resume Feature:** Allow users to temporarily pause a game if applicable.

### **Leaderboard Enhancements**
- **Friends & Community Rankings:** Allow users to compare rankings with friends in addition to the global leaderboard.
- **Achievements & Badges:** Introduce rewards for milestones like winning 10 games in a row.

### **In-Game Chat Enhancements**
- **Emote & Quick Chat Options:** Provide pre-set messages for quick communication.
- **Report & Mute Feature:** Allow players to report toxic behavior and mute users in chat.


## Settings
### Features
- Account Information 
- Log Out
- Build Version & Terms and Conditions
### Integration Notes
- Dynamic user settings update
- Store changes