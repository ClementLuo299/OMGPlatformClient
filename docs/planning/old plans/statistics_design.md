# Matchmaking and Player Statistics System Design

## Player Matchmaking:

***Motivation:***

Skill based matchmaking systems exist to encourage healthy competition by providing players with opponents who are at their exact same skill level. However, this comes with several downsides that should be addressed before making a traditional system.
1. Games are fundamentally designed for fun, and matchmaking creates a stale player experience by providing what is essentially the same game repeatedly, *forever*.
2. Matchmaking pushes players to always be playing at their best, which causes stress, dampens fun, and discourages players from playing when wanting to relax.
3. When a player gets too skilled at the game they struggle to find matches quickly because the game cannot find other players to match them with.

***Design:***

1. Players will have an experience level which represents their success over time. This level gradually increases as they play and earn points.
   - Each level will require more points than the last to reach the next level. This means that the first several levels will all be relatively close in skill, and that more skilled players will all be quite close in level.
   - This level does not depreciate over time, so it more accurately represents how long a player has played. Playing with high skill will increase their level faster, but playing non-seriously will not be punishing.
2. Play sessions will have an intensity value which starts at 0.5 and can range from 0.0 to 2.0. This number slides up and down throughout a play session based on victory or defeat and the level of the opponent.
   - Winning against a higher level opponent will increase the intensity level by 0.2.
   - Winning against a lower level opponent will not affect the intensity level.
   - Losing against a higher level opponent will decrease the intensity level by 0.1.
   - Losing against a lower level opponent will decrease the intensity score by 0.3.
3. Players will be matched against other players at random with a preference for opponents whose level is close to the player's level multiplied by their session intensity value.
   - Lower level players' intensity score will be weighed as more important than higher level players', because they are more likely to get discouraged.

Through these aspects, each game will be different to the last while still being strongly competitive on average throughout a play session. Because the experience level isn't directly related to the intensity level, players will be matched up against others of varying experience levels, which will provide challenging opponents of both higher and lower levels, as well as easy opponents who are leveled likewise.

## Leveling:

***Motivation:***

Matchmaking systems require that some skill level measurement is recorded, so it is important to create a robust levelling system that is both rewarding to skilled players, but doesn't punish players who are trying to have fun.
1. Player levels should be exponentially more difficult to reach so that lower level players feel a sense of progression, while higher level players feel a sense of reward for reaching the next level.
2. Points should be how a player levels up, and points should be limited to whole numbers to make designing a fair compensation system require more thoughtful design.
3. Players should be rewarded for playing extremely well, but also be compensated for participating in a full match against an opponent no matter the outcome.

***Design:***

1. Level 1 requires 10 points to earn, and each subsequent level will require more points than the preceding level.
   - Each level will take 1.2x more points than the previous level, rounded up to the next integer.
2. Players will earn points for fully participating in games, with each game resulting in some number of additional bonus points based on player performance.
   - Players will earn 1 point for completing a game.
   - Players will earn 1 point for winning a game.
   - Players will earn 2 points for winning a game quickly, which is determined by the winning player making less than 40% of the average expected moves of a given game.
   - Players will earn 1 point for playing a long game, which is determined by a player making more than 125% of the average expected moves of a given game.
3. Each game has a different number of average turns, so each game will require a different number of turns to count as a quick or long game.
   - ~~The average player moves in Chess is 20, so quick games are 8 turns and long games are 25 turns.~~ (We have decided against developing Chess)
   - The average player moves in Connect 4 is 22, so quick games are 9 turns and long games are 28 turns.
   - The average player moves in Checkers is 25, so quick games are 10 turns and long games are 32 turns.
   - The average player rounds in Whist is 8, so quick games are 3 rounds and long games are 10 rounds. 

Through these aspects, players will be effectively rewarded for playing with high skill, but will also still gain experience for participating in a full game. This discourages players from quitting and will keep all players motivated to play regardless of skill level.

## Leaderboard:

***Motivation:***

Leaderboards exist to spur players on to achieve better scores and compete in an external way other than just within each game like usual. However, a problem can arise where too much competitiveness can create toxicity.
1. Leaderboards should be designed to encourage competition while avoiding giving players ammunition to berate other players with.
2. There should be several categories so that different skillsets are rewarded and allow more than just the most skilled players to dominate.
3. No viewable stats should have negative connotations, such as losses, win/loss ratio.

***Design:***

1. A Leaderboard for highest level will encourage players to play more and doesn't necessarily represent raw skill level.
2. A Leaderboard for most wins will be the most competitive but is fairly easy to climb by playing more games.
3. A Leaderboard for most games played is a less competitive and is pretty much purely dedication based.
4. A Leaderboard for best current player will be highly competitive and highly dynamic which encouraged sprints of high intensity play.
   - This is calculated by multiplying the player's level with the player's intensity, and only shows active players.

Through these aspects, competition will be encouraged through Leaderboard without creating too much competitiveness based toxicity in the community. Keeping the leaderboards as something enjoyable, accessible, but still competitive will encourage players to try to get on it by playing more and trying harder when they want to.
