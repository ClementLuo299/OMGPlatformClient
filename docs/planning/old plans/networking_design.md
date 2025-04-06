**Account Registration**

1. User inputs username/password
2. Check for proper input 
    2a. Check for password complexity (length, special characters, uppercase/lowercase mix)
3. Insert into database using (DatabaseIOHandler)
    3a. Check if the username already exists in database
    3b. If not, then send request to database to add account
    3c. Verify that the operation has been successful
4. Login (But skip username/password input)

**Account Login**
1. User input username/password
2. Send login request to server
3. Check for valid character input (if some special characters are invalid)
4. Server verifies input with database
5. Server produces output:
    5a.Server responds with retry message if failure in execution
    5b.Screen displays success message if credentials are valid
    5c.Screen displays failure message if credentials are invalid
6. If successful initialise user session and retrieve data from database


** Forgot password **
1. User clicks on forgot password on the login screen
2. User inputs username/email
    2a. Check if username/email format is valid
    2b. Verifies input with database
3. Provide user with a temporary one time password in provided email
    3a. Add temporary password into database, set to expire in 15 minutes
4. User logs in with temporary password
    4a. If user logs in with temporary password or 15 mins has passed
        4ai. Delete temporary password from database
5. User gets prompted to make a new password
    5a. System checks password validity
    5b. New password is added to database




**Account Logout**
1. User initiates logout
2. Server asks for confirmation from user
3. Send logout request to server
4. User confirms termination of session
5. Login screen return is initiated.

**Game Start**
1. User initiates request to start a game
2. Server initiates matchmaking process
3. Server matches all available players
4. Game session is created and sent to all players
5. Game begins

**Game End**
1. Game ends through winning/losing condition or initiated through user
2. Server validates request from user/ Server validates winning/losing condition
3. Game result is displayed
4. Server sends players to main menu

**Gameplay Communication**
1. Receive string input from gameplay
2. Validate move
3. Add to game session log
4. Update game state
5. Notify database
6. Check if all players' game state is syncronized

When the current player makes a move:
1. Recieve string input from gameplay
2. Do necessary processing
3. Add to the game session log
4. Notify database
5. Check if all players gamestate is synchronized

When other players make moves:
1. Recieve notification from database
2. Update game session log
3. Do necessary processing
4. Send input to gameplay
5. Check if all players gamestate is synchronized

In-game chat:
1. User inputs message to chat field
2. Server receives and validates message
3. Server relays message to other players
4. Messages are added to the game log chat history
5. Server will block or blur inappropraite content

Sending friend request:
1. User selects another user and sends friend request
2. Server receives prompt and validates request
3. Receiving user receives a notification and can choose whether to accept or decline
4. If accepted, both players' friend's list will be updated
5. If declined, the request is removed from pending list