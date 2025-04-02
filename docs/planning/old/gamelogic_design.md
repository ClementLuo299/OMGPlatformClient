# Core Game Logic System Design

## Game Logic Directory

***Preamble:***

Every game will have some universal functionalities that will be made as parental classes within the `gamelogic` directory whose functionality will be inherited by child classes for specific games or pieces inside the subdirectories `connect4`, `checkers`, `whist`, and `pieces` respectively.

***Design:***

*Universal Functionalities:*
- There will be a `GameType` enum which contains all the types of Games

| Enum     | Description                      |
|----------|----------------------------------|
| CONNECT4 | Declares the Connect 4 Game Type |
| CHECKERS | Declares the Checker Game Type   |
| WHIST    | Declares the Whist Game Type     |

- There will be a `PieceType` enum which contains all the types of Pieces

| Enum    | Description                          |
|---------|--------------------------------------|
| CHECKER | Declares the Checker Piece Type      |
| CARD    | Declares the Playing Card Piece Type |

- There will be a `GamePeice` class which handles the features all Game Pieces share

| Attributes              | Description                                              |
|-------------------------|----------------------------------------------------------|
| + pieceType (PieceType) | The specific Piece this is                               |
| + held (boolean)        | The Held State of the Piece. true = Held, false = Unheld |

- There will be a `Player` class which handles the logic and features all Players have

| Attributes                 | Description                                             |
|----------------------------|---------------------------------------------------------|
| - accountID (int)          | The account number of this Player                       |
| + name (String)            | The name within the account                             |
| + gameLevel (int)          | The Player's Experience Level in a specific Game Type   |
| + plays (int)              | The number of Turns/Rounds in a Game made by the Player |
| + score (int)              | The Score the Player has earned in a Game               |
| + hand (GamePiece Array)   | The Game Pieces in possession of the Player             |
| + spoils (GamePiece Array) | The Game Pieces the Player has captured                 |

- There will be a `Game` class (Perhaps Interface?) which handles the logic and features all Games have

| Attributes               | Description                                       |
|--------------------------|---------------------------------------------------|
| + gameType (GameType)    | The specific Game being played                    |
| + players (Player Array) | The list of all Players participating in the Game |
| - avgPlays (int)         | The Average Plays a Game takes to conclude        |
| - winner (Player)        | The Player declared winner of the Game            |
| - turnHolder (Player)    | The Player who is currently taking a turn         |

## Pieces Directory

***Preamble:***

Every Game utilizes pieces, so for the sake of making this program able to be adapted in the future for more games, Pieces will be made generic so that they can be used by any game.

***Design:***

- There will be a `ColourType` enum which contains all the available colours for a Checker

| Enum   | Description              |
|--------|--------------------------|
| RED    | Declares Red Checkers    |
| YELLOW | Declares Yellow Checkers |
| BLACK  | Declares Black Checkers  |
| WHITE  | Declares White Checkers  |

- There will be a `Checker` class which inherits from the `GamePiece` class and contains all the information about each Checker

| Unique Attributes     | Description                                                       |
|-----------------------|-------------------------------------------------------------------|
| + colour (ColourType) | The Colour of the Checker                                         |
| + stacked (boolean)   | The Stacked Status of the Checker. true = Stacked, false = Single |
| + yPosition (int)     | The Current Row of the Checker                                    |
| + xPosition (int)     | The Current Column of the Checker                                 |

- There will be a `CheckerBag` class which stores an Array of Checkers

| Attributes               | Description                                |
|--------------------------|--------------------------------------------|
| checkers (Checker Array) | The Array of all Checkers the Bag contains |

- There will be a `SuitType` enum which contains all the available suits for a Card

| Enum     | Description               |
|----------|---------------------------|
| CLUBS    | Declares the Club Suit    |
| SPADES   | Declares the Spade Suit   |
| HEARTS   | Declares the Heart Suit   |
| DIAMONDS | Declares the Diamond Suit |

- There will be a `Card` class which inherits from the `GamePiece` class and stores all the information about each Card

| Unique Attributes    | Description                                                        |
|----------------------|--------------------------------------------------------------------|
| + suit (SuitType)    | The Suit Type of the Card                                          |
| + rank (int)         | The Numerical Rank of the Card                                     |
| + faceDown (boolean) | The Visibility of the Card for the Holder. true = Down, false = Up |

- There will be a `CardPile` class which stores an Array of Cards

| Attribute              | Description                               |
|------------------------|-------------------------------------------|
| + cards (Card Array)   | The Array of all Cards this Pile Contains |

## Connect 4 Directory

***Preamble:***

This game will be the most casual of the games available to our users, but that does not mean it has no competitive rigor! One needs to get 4 pieces in a row while preventing their opponent from doing the same by blocking them. Each player will be assigned to the red or yellow colour and will compete to connect 4 pieces.

***Design:***

*Technical Aspects:*
- There will be a `ConnectGrid` class which handles the logic and features of the Game Board

| Attributes                 | Description                                                              |
|----------------------------|--------------------------------------------------------------------------|
| - width (int)              | The numerical Width of the Board (Represents columns in the slots Array) |
| - height (int)             | The numerical Height of the Board (Represents rows in the slots Array)   |
| + slots (2D Checker Array) | All of the Slots on the Board                                            |

- There will be a `Connect4Game` class which inherits from the `Game` class and handles the logic of the Game

| Unique Attributes           | Description                                          |
|-----------------------------|------------------------------------------------------|
| + grid (ConnectGrid)        | The Grid the Game is being played with               |
| + validColumns (int Array)  | The y positions of current valid Columns in the Grid |

*Goal Of Play:*
- The first Player to have a contiguous row of 4 Checkers horizontally, vertically, or diagonally is declared the Winner. The standard height of the board is 6 and the standard width is 7. The average number of turns to win is 22.

*Order of Play:*
- At the onset of the game, the Player using red checkers must be selected to determine turn order.
    - The Players will be given a text prompt to choose two Checkers to compete to use the red Checkers.
    - To select the Player with red Checkers, the Players must both draw two checkers at the same time from an opaque bag of randomly assorted Checkers displayed on the screen.
    - Each Player's selection is displayed and whoever picked two red Checkers gets to play with red Checkers (The red Checker Player is put into the turnHolder of the game).
    - The drawn Checkers are put back into the bag and 21 Checkers are then put into each Player's Hand and are displayed in a pile at the bottom of the screen.
    - Each column of the Grid is added to the validColumns Array.
    - There is a 10 second timer for selection. *(All Timers will be implemented once Real-Time Interactions exist)*

*Gameplay:*

The Game has how entered the turn taking stage and will alternate between each Player. This section of the Game loops until a Line is made or the Grid is full.
1. The move begins, and the Player whose turn it is must select a piece to move to progress the game.
    - The Player will be given a text prompt to select a Checker and make a move. All of their Checkers will glow white.
    - To select a Checker, the Player must click on one of their Checkers. The selected Checker will glow blue.
2. The move continues, and the Player now must select which column they want to put their selected Checker in.
    - The same text prompt remains at the top of the screen, but now all of the tops of valid columns will glow white (Column validity is checked by looking at the first (top) slot of the column for emptiness).
    - To select the column to put their Checker in, the Player must click anywhere on the column.
    - The selected Checker will be moved to the farthest right (down) empty position in the column (The Checker's xPosition and yPosition attributes are updated).
    - There is a 15 second timer for the entire move selection process.
3. The move concludes, and the Game checks for win conditions.
    - The Player's plays attribute increments by 1.
    - If the validColumns Array is empty, the Game ends in a draw; if the Game detects a horizontal, vertical, or diagonal line attached to the played Checker, the Game ends and the Player Wins; Otherwise, the Game will transfer the turnHolder attribute to the Opponent and loop through the turn taking steps again.

*Conclusion:*

When the Game reaches its final win conditions, it will use the parental class to declare the Winner, calculate the experience points to award Players, and communicate these results and other relevant information to the Player's Account.

## Checkers Directory

***Preamble:***

This game will be one of our more competitive options, as it has a lot of space for skill expression. One must continuously try to move their pieces to the other side of the board while maintaining defense against the opponent. Each player will be assigned a black or white colour to compete to be the last colour standing.

***Design:***

*Technical Aspects:*

- There will be a `CheckerBoard` class which handles the logic and features of the Game Board

| Attributes                   | Description                      |
|------------------------------|----------------------------------|
| - size (int)                 | The numerical Width of the Board |
| + squares (2D Checker Array) | All of the Squares on the Board  |

- There will be a `CheckersGame` class which inherits from the `Game` class and handles the logic of the Game

| Unique Attributes             | Description                                                |
|-------------------------------|------------------------------------------------------------|
| + board (CheckerBoard)        | The Board the Game is being played on                      |
| + validSquares (2D int Array) | The x and y position of current valid Squares on the Board |

*Goal Of Play:*
- The first Player to clear the board of all opposing pieces by capturing them, or forcing the opponent to have no valid moves, is declared the Winner. Captured pieces are displayed on the left of each Player. The standard size of the board is 8. The average number of turns to win is 25.

*Order of Play:*
- At the onset of the game, the Player using black checkers must be selected to determine turn order.
    - The Players will be given a text prompt to choose two Checkers to compete to use the black Checkers.
    - To select the Player with black Checkers, the Players must both draw two checkers at the same time from an opaque bag of randomly assorted Checkers displayed on the screen.
    - Each Player's selection is displayed and whoever picked two black Checkers gets to play with black Checkers (The black Checker Player is put into the turnHolder of the game).
    - The drawn Checkers are put back into the bag and Checkers are then put into each Player's Hand.
    - The Checkers are then sequentially displayed in the correct positions on the Board (Each Checker's xPosition and yPosition attributes will be updated accordingly. Black Checkers will occupy the top three rows of the board. White Checkers will occupy the bottom three rows).
    - There is a 10 second timer for selection. *(All Timers will be implemented once Real-Time Interactions exist)*

*Gameplay:*

The Game has now entered the turn taking stage and will alternate between each Player. This section of the Game loops until only one colour of Checker remains on the Board or the Player cannot make a move.
1. The move begins, and the Player whose turn it is must select a piece to move to progress the game.
    - The Player will be given a text prompt to select a Checker and make a move. All of their Checkers will glow white.
    - To select a Checker, the Player must click on one of their Checkers. The selected Checker glow blue and the white glow of their other Checkers to significantly dim.
    - The Player can select whichever Checker they wish to use and change their selection as much as they want before making a move.
2. The move continues, and the Player must now select where they want to move their selected Checker.
    - The same text prompt remains at the top of the screen, but now valid squares will glow white.
    - To select a square to move to, the Player must click on one of the glowing squares that is shown to them.
    - Valid squares are initially calculated based on the colour of the current Player's Checkers, so long as the validSquares Array is currently empty:
        - For Black Checkers with a false stack attribute: The current yPosition - 1, and the current xPosition + and - 1.
        - For White Checkers with a false stack attribute: The current yPosition + 1, and the current xPosition + and - 1.
        - For any Checker with a true stack attribute: The current yPosition + and - 1, and the current xPosition + and - 1.
        - Each square is added to the validSquares Array.
        - Each of these square checks will be subject to an out-of-bounds check which ensures no squares that aren't in the Board array are looked at.
    - Valid squares are then modified by checking the conditions within the square:
        - If there is a same coloured Checker in the square, that square is disqualified.
        - If there is an opposite coloured Checker in the square, that square is disqualified and the next square diagonally is checked for emptiness, validating that square if so.
        - All initially valid squares are checked for conditions, and if there is a jump available, all valid squares within 1 square of the selected Checker are disqualified.
        - Jump detection is subject to an out-of-bounds check as well.
    - There is a 20 second timer for the entire move selection process.
3. The move concludes, and the Game handles any special events that took place during the move.
    - The Player's plays attribute increments by 1.
    - The validSquares Array is cleared.
    - The Game will check for opposite colour Checkers to determine if the enemy will be forced to jump over the Player's Checker (If a valid jump is found, then the validSquares Array is populated accordingly).
    - If the Player jumped over an Opponent's Checker, that Checker is removed from the Board and the Game will check for more available jumps (The jumped Checker is moved from the Opponent's hand to the Player's spoils, which are displayed to the left of the Player).
    - If the Player's Checker reached the opposite edge of the Board, it will become a King (That Checker's stack attribute becomes true).
    - If there are no more opposite colour Checkers on the board, the Game ends and the Player wins; If the validSquares Array is empty, the Game ends and the Opponent wins; Otherwise, the Game will transfer the turnHolder attribute to the Opponent and loop through the turn taking steps again.

*Conclusion:*

When the Game reaches its final win conditions, it will use the parental class to declare the Winner, calculate the experience points to award Players, and communicate these results and other relevant information to the Player's Account.

## Whist Directory

***Preamble:***

This game will be our most paced game, as it is played over multiple rounds and so will take some time. This slower pace and round based format allows for exciting comebacks and requires strategic endurance on the part of both players. Players must compete to win tricks in two stages, trying to ensure that their hand is as good as possible while keeping in mind the possible hand of the other player.

***Design:***

*Technical Aspects:*

- There will be a `StageType` enum which contains all the possible stages for the Game

| Enum  | Description                             |
|-------|-----------------------------------------|
| DEAL  | Declares the Dealing Stage of the Game  |
| DRAFT | Declares the Drafting Stage of the Game |
| DUEL  | Declares the Dueling Stage of the Game  |

- There will be a `WhistGame` class which inherits from the `Game` class and handles the logic of the Game

| Unique Attributes   | Description                                                                                |
|---------------------|--------------------------------------------------------------------------------------------|
| + round (int)       | The Numerical Round of the Game                                                            |
| + stage (StageType) | The Current Stage Type of the Round                                                        |
| + trump (SuitType)  | The Trump Suit for the Round                                                               |
| - deck (Pile)       | The initial Deck of 52 Cards. These are in standard order, are not held, and are Face-Down |
| - draw (Pile)       | The Pile for Players to draw from                                                          |
| - discard (Pile)    | The Pile for Discarded Cards                                                               |

- The cards are valued by rank from highest to lowest as 14 (Ace), 13 (King), 12 (Queen), 11 (Jack), 10, 9, 8, 7, 6, 5, 4, 3, 2.

*Goal of Play:*
- Over multiple rounds, the first Player to reach 6 points is declared the Winner. The current round is prominently displayed in the top right corner. The points to win is prominently displayed in the top left corner with the names and score of each Player beneath. The average number of rounds to win is 8.

*Order of Play:*
- At the onset of each round, the Dealer must be selected to shuffle up the deck and deal out the cards
    - The Game's round attribute will increment by 1, as will each Players' plays attribute.
    - The players are given a text prompt to choose a card to compete for the Dealer position.
    - To select the dealer, the players must both draw a card at the same time from a face-down deck of 52 cards splayed out on the screen.
    - Each player's selection is displayed and whoever selects the lowest ranked card is deemed the Dealer (The Dealer is put into the turnHolder of the Game).
    - The drawn cards are then placed back to the bottom of the deck.
    - There is a 10 second timer for selection. *(All Timers will be implemented once Real-Time Interactions exist)*

*Gameplay:*

The Game has entered the Dealing Stage, which is displayed under the current round in the top right corner.
1. The Deck is given to the Dealer who must perform 3 shuffles on the deck.
    - The Dealer is given a text prompt to shuffle the Deck, including how many shuffles they have to perform.
    - To perform each shuffle, the Dealer must click on one of the few types of shuffles that will be shown at the bottom of the screen, glowing white.
        - Those types of shuffles being: Scramble, Riffle, Overhand, and Cut.
    - There will be a 7 second timer through the whole shuffling process.
2. The shuffled Deck is retained by the Dealer as they still have to deal out the cards.
    - The Dealer is given a text prompt to deal the cards and the topmost card of the Deck will glow white.
    - To deal the cards, the Dealer must click the topmost card repeatedly, which will alternate between giving the Opponent and the Dealer a single card. Dealing the cards will be complete once each Player has 13 cards.
    - When a card is clicked, it is removed from the Deck pile and put into the appropriate Player's hand (The given card's held attribute becomes true).
    - The remaining cards in the Deck pile are transferred into the Draw pile, which is in the middle of the table and off to the right.
    - There will be a 7 second timer through the whole dealing process.
3. The Dealer must now reveal the Trump Suit for the round to enter the first stage of play.
    - The Dealer is given a text prompt to reveal the Trump Suit, and the topmost card on the Draw pile will glow white.
    - To reveal the Trump Suit, the Dealer must click the topmost card on the Draw pile (The topmost card's faceDown attribute becomes false).
    - Both players will be given a text prompt showing the Trump Suit of the round.
    - The Dealer has concluded their turn (The opponent is put into the turnHolder of the Game).
    - The Cards in the Players' hands are sorted by Suit and Rank and revealed to them (The faceDown attribute of all cards in each Players' hands becomes false).
    - There will be a 5 second timer for the revealing process.

The Game has entered the Drafting Stage, with the Trump Suit and Stage being displayed under the current round in the top right corner. This section of the Game loops until the Draw pile has been depleted.
1. The Trick begins, and the Player whose turn it is must select a card to lead the trick.
    - If the revealed card on the Draw deck is of the Trump Suit or has a Rank greater than 10, it will glow gold to indicate desirability.
    - The Player is given a text prompt to select a card to lead the trick.
    - To select a card, the Player must click on any one of the cards in their hand, which will be placed in the middle of the table face-up (The played card's held attribute becomes false).
    - The turn is now given over to the other Player.
    - There will be a 15 second timer for the trick leading process.
2. The Trick continues, and the Player whose turn it is must select a card to compete in the trick.
    - If the Player has cards that match the suit of the card in play, then they are given a text prompt to follow suit to complete the trick. All of their cards which do not match the played suit will be grayed out and unclickable.
    - If the Player does not have cards that match the suit of the card in play, then they are given a text prompt to select any card to complete the trick.
    - To select a card, the Player must click on any one of the available cards in their hand, which will be placed next to the leading card face-up (The played card's held attribute becomes false).
    - There will be a 15 second timer for the trick completing process.
3. The trick is complete, and the winner of this trick must be determined to see who gets the revealed card on the Draw pile.
    - The winner of the trick is determined by whichever played card has the highest rank (Trump Suited cards have their rank valued as rank + 14).
       - If the following player couldn't follow suit, then they lose unless they played a trump suited card.
    - Each player's hand is checked to see if they possess the more valuable card.
    - The turn is now given to the Player who won the trick.
    - The played cards are both moved from the Players' hands into the Discard pile in the middle of the table to the left (Both card's faceDown attribute becomes true and their held attribute becomes false).
4. The Players must now draw one card each from the Draw pile, with the person whose turn it is going first.
    - Each Player, with the winner of the trick going first, will be given a text prompt to draw the topmost card. The topmost card in the Draw pile will be glowing white.
    - To take their card, the Player must click the Draw pile, which will move the topmost card into their hand (The drawn card's held attribute becomes true and its faceDown attribute becomes false).
    - The last Player to take a card will be given a text prompt to reveal the next prize card. The topmost card in the Draw pile will be glowing white.
    - To reveal the next prize card, the Player must click the Draw pile (The topmost card's faceDown attribute becomes false).
    - There will be a 5 second timer for each player for the drawing process.
    - If the Draw pile is not empty, the game will go through another loop of the Drafting Stage. If the Draw pile is empty, the game will move on to the next stage.

The Game has entered the Dueling Stage, with the Trump Suit and Stage being displayed under the current round in the rop right corner. This section of the Game loops until everyone's hand is empty.
1. The Trick begins, and the Player whose turn it is must select a card to lead the trick.
    - The Player is given a text prompt to select a card to lead the trick.
    - To select a card, the Player must click on any one of the cards in their hand, which will be placed in the middle of the table face-up (The played card's held attribute becomes false).
    - The turn is now given over to the other Player.
    - There will be a 15 second timer for the trick leading process.
2. The Trick continues, and the Player whose turn it is must select a card to compete in the trick.
    - If the Player has cards that match the suit of the card in play, then they are given a text prompt to follow suit to complete the trick. All of their cards which do not match the played suit will be grayed out and unclickable.
    - If the Player does not have cards that match the suit of the card in play, then they are given a text prompt to select any card to complete the trick.
    - To select a card, the Player must click on any one of the available cards in their hand, which will be placed next to the leading card face-up (The played card's held attribute becomes false).
    - There will be a 15 second timer for the trick completing process.
3. The trick is complete, and the winner of this trick must be determined to see who gets the revealed card on the Draw pile.
    - The winner of the trick is determined by whichever played card has the highest rank (Trump Suited cards have their rank valued as rank + 14).
       - If the following player couldn't follow suit, then they lose unless they played a trump suited card.
    - Each Player's hand is checked to see if they possess the more valuable card.
    - The turn is now given to the Player who won the trick.
    - The played cards are both moved from the Players' hands into the Winning Player's spoils and placed as a pair to the right of them (Both card's faceDown attribute becomes true).
    - If the Player's hands are not empty, the game will go through another loop of the Dueling Stage. If their hands are empty, the game will move on to the next stage.

The Round is now over, so the Game will calculate how many points each player earned that round to determine if the next round is to start.
1. The Game will calculate the score of each Player and compare it to the score needed to win the game.
    - The score of each Player is calculated by taking the total number of pairs in their spoils and subtracting that number by 6.
    - The score of each Player is given to them and displayed in the top left corner.
    - If no score meets the required score to win, the game will go through another round of play. If one Player's score meets the required score to win, the game will conclude.

*Conclusion:*

When the Game reaches its final state, it will use the parental class to declare the Winner, calculate the experience points to award Players, and communicate these results and other relevant information to the Player's Account.