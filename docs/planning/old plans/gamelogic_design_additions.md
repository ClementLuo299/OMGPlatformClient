# Handling Invalid Moves

To enforce fair gameplay, the system must prevent illegal moves.

## Connect Four:
- If a player **drops a checker in a full column**, the move is rejected.
- If a player **clicks outside the board**, no move is registered.
- If the network **delays a move**, only the **first received valid move**
- If **all columns are full** and no player has won, the game ends in a **draw**.
- A message is displayed: **"Game Over! It's a Draw!"**

## Checkers:
- If a player **tries to move a piece backward (without being a King)**, the move is blocked.
- If a player **has a jump available but does not take it**, the move is invalid.
- If a player **tries to move into an occupied square**, they must choose another move.