package statistics;

import gamelogic.GameType;
import networking.accounts.UserAccount;

import java.util.*;

/**
 * Handles the sorting of Players into Matches based on their Statistics
 *
 * @authors Dylan Shiels, Irith Irith
 * @date March 18, 2025
 */
public class MatchmakingHandler {
    // ATTRIBUTES

    // The list of Players searching for Games
    private LinkedHashMap<UserAccount, GameType> searchingUsers;


    // CONSTRUCTOR

    /**
     * Instantiates the Matchmaking Handler for the OMG Platform
     */
    public MatchmakingHandler(LinkedHashMap<UserAccount, GameType> searchingPlayers) {
        this.searchingUsers = searchingPlayers;
    }

    // GETTERS

    /**
     * Gets the total number of Players that are queued for a Game
     *
     * @return The integer number of Queued Players
     */
    public int getQueueSizeAll() {
        return searchingUsers.size();
    }

    /**
     * Gets the total number of Users that are queued for a specific Game
     *
     * @param gameToCount The given Game Type to count Queued Users for
     * @return The integer number of Queued Users for the given Game
     */
    public int getQueueSizeSpecific(GameType gameToCount) {
        // The integer that keeps count of Queued Users
        int queueCount = 0;

        // Iterates through the list to check the Queued Game of each User to count them
        for (GameType queuedGame : searchingUsers.values()) {
            if (queuedGame == gameToCount) {
                queueCount++;
            }
        }

        // Returns the total number of Players Queued for the given Game Type
        return queueCount;
    }


    // SETTERS

    /**
     * Adds a User to the Matchmaking Queue with a specified Game
     *
     * @param joiningUser The given User that is joining the Queue
     * @param queuedGame The given Game Type that User is Queued for
     */
    public void joinQueue(UserAccount joiningUser, GameType queuedGame) {
        // Adds the User to the Queue
        this.searchingUsers.put(joiningUser, queuedGame);
        // Resorts the Queue based on Experience Level
        this.sortByExperience();
    }


    // METHODS

    /**
     * Sorts the Queue from Lowest Experience to Highest Experience
     */
    public void sortByExperience() {
        // Convert entries to a list for sorting
        List<Map.Entry<UserAccount, GameType>> entries = new ArrayList<>(searchingUsers.entrySet());

        // Sort by experience level (ascending)
        // entries.sort(Comparator.comparingInt(entry -> entry.getKey().getExperienceLevel()));

        // Clear and rebuild the LinkedHashMap in sorted order
        searchingUsers.clear();
        for (Map.Entry<UserAccount, GameType> entry : entries) {
            searchingUsers.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Finds Two Users from within the Matchmaking Queue to match together based on Queued Game, Experience Level, and Session Intensity Level
     */
    public void findMatch() {
        // The User with the Lowest Experience Level
        UserAccount searchingUser;
        // The User that is matched to the Lowest Experience Level Player
        UserAccount matchedUser;

        // The Game Type the Lowest Experience Level User is Queued for
        GameType queuedGame = GameType.ALL; // (Will leave this uninitialized once lowest exp lvl player code exists)
        // The integer Game specific Experience Level of the Lowest Experience Level User
        int gameExperienceLevel;
        // The float Session Intensity Level of the Lowest Experience Level User
        float sessionIntensityLevel;
        // The integer ideal Level of Opponent
        int idealOpponentLevel;


        // TODO: Get first entry of searchingUsers hashmap and set the variables above appropriately

        // Finds the Lowest Experience Level User to prioritize less experienced Players
        if (!searchingUsers.isEmpty()) {
            // Get the first entry of the sorted map (lowest experience player)
            Map.Entry<UserAccount, GameType> firstEntry = searchingUsers.entrySet().iterator().next();
            searchingUser = firstEntry.getKey();
            queuedGame = firstEntry.getValue();

            // Get game-specific experience level from the UserAccount
            //gameExperienceLevel = searchingUser.getExperienceLevel();

            // Get session intensity level from the UserAccount
            //sessionIntensityLevel = searchingUser.getSessionIntensityLevel();

            // Calculate ideal opponent level using experience and intensity
            //idealOpponentLevel = (int) (gameExperienceLevel * sessionIntensityLevel);
        } else {
            // No players in queue, exit matchmaking
            return;
        }
        /*
        It is assumed that UserAccount has methods:
        1. getExperienceLevel() returning game-specific XP as int
        2. getSessionIntensityLevel() returning intensity as float
         */



        // Searches through the Queue to check for close matches
        /*
        for (HashMap.Entry<UserAccount, GameType> entry : searchingUsers.entrySet()) {
            // Checks if the GameType is compatible
            if (queuedGame == entry.getValue()) {
                // The User to check
                UserAccount userToCheck = entry.getKey();

                // Skip the searching user itself
                if (userToCheck == searchingUser) {
                    continue;
                }

                // Get the Experience and Intensity Level of this Opponent
                int opponentExp = userToCheck.getExperienceLevel();
                float opponentIntensity = userToCheck.getSessionIntensityLevel();

                // Calculate match level using experience and intensity
                int opponentMatchLevel = (int) (opponentExp * opponentIntensity);

                // Calculate difference from ideal opponent level
                int levelDifference = idealOpponentLevel - opponentMatchLevel;

                // Check if this user is a close match (within ±3 levels)
                if (Math.abs(levelDifference) < 3) {
                    // Selects this User to be matched up against the searching Player
                    matchedUser = userToCheck;

                    // Remove both players from queue
                    searchingUsers.remove(searchingUser);
                    searchingUsers.remove(matchedUser);

                    // Matches the users in a game (implementation details would go here)

                    // Ends the search
                    return;
                }
            }
        }
         */

        // Searches through the Queue for broad matches
        /*
        for (HashMap.Entry<UserAccount, GameType> entry : searchingUsers.entrySet()) {
            // Checks if the GameType is compatible
            if (queuedGame == entry.getValue()) {
                // The User to check
                UserAccount userToCheck = entry.getKey();

                // Skip the searching user itself
                if (userToCheck == searchingUser) {
                    continue;
                }

                // Get the Experience Level of this Opponent
                int opponentLevel = userToCheck.getExperienceLevel();

                // Calculate difference from ideal opponent level
                int levelDifference = idealOpponentLevel - opponentLevel;

                // Check if this user is a broad match (within ±4 levels)
                if (Math.abs(levelDifference) < 4) {
                    // Selects this User to be matched up against the searching Player
                    matchedUser = userToCheck;

                    // Remove both players from queue
                    searchingUsers.remove(searchingUser);
                    searchingUsers.remove(matchedUser);

                    // Matches the users in a game (implementation details would go here)

                    // Ends the search
                    return;
                }
            }
        }
         */

        // Match with a random user if no good user is available
        List<UserAccount> candidates = new ArrayList<>();

        // Collect all users queued for the same game type, excluding the searching user
        for (HashMap.Entry<UserAccount, GameType> entry : searchingUsers.entrySet()) {
            // Check if the GameType matches and it's not the searching user
            if (entry.getValue() == queuedGame && entry.getKey() != searchingUser) {
                candidates.add(entry.getKey());
            }
        }

        // Proceed if potential candidates exist
        if (!candidates.isEmpty()) {
            // Generate random index from candidate list
            Random rand = new Random();
            int randomIndex = rand.nextInt(candidates.size());

            // Select random opponent from candidates
            matchedUser = candidates.get(randomIndex);

            // Remove both players from queue
            searchingUsers.remove(searchingUser);
            searchingUsers.remove(matchedUser);

            // Matches the users in a game (implementation details would go here)

            // Ends the search
            return;
        }

        // No valid candidates available - matchmaking failed

    }
}
