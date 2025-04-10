package statistics;

import gamelogic.GameType;
import networking.accounts.UserAccount;

import java.util.*;

/**
 * Handles the sorting of Players into Matches based on their Statistics
 *
 * @authors Dylan Shiels, Irith
 * @date April 08, 2025
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


        // Finds the Lowest Experience Level User to prioritize less experienced Players

        // TODO: Get first entry of searchingUsers hashmap and set the variables above appropriately
        // TODO: Set idealOpponentLevel = (gameExperienceLevel * sessionIntensityLevel)



        // Searches through the Queue to check for close matches
        for (HashMap.Entry<UserAccount, GameType> entry : searchingUsers.entrySet()) {
            // Checks if the GameType is compatible
            if (queuedGame == entry.getValue()) {
                // The User to check
                UserAccount userToCheck = entry.getKey();

                // TODO: Get the Experience and Intensity Level of this Opponent
                // TODO: Calculate them together to get the OpponentMatchLevel to check against
                // TODO: Calculate difference between idealOpponentLevel and opponentMatchLevel to get the levelDifference

                // Checks if this user is a close match, if so the search is ended and the Users are matched together
                //if (levelDifference < 3 || levelDifference > -3) {

                // Selects this User to be matched up against the searching Player
                matchedUser = userToCheck;

                // Matches the user in a game

                // Ends the search
                return;
                //}
            }
        }

        // Searches through the Queue for broad matches
        for (HashMap.Entry<UserAccount, GameType> entry : searchingUsers.entrySet()) {
            // Checks if the GameType is compatible
            if (queuedGame == entry.getValue()) {
                // The User to check
                UserAccount userToCheck = entry.getKey();

                // TODO: Get the Experience Level of this Opponent
                // TODO: Calculate difference between idealOpponentLevel and opponentLevel to get the levelDifference

                // Checks if this user is a broad match, if so the search is ended and the Users are matched together
                //if (levelDifference < 4 || levelDifference > -4) {

                // Selects this User to be matched up against the searching Player
                matchedUser = userToCheck;

                // Matches the user in a game

                // Ends the search
                return;
                //}
            }
        }

        // Match with a random user if no good user is available
        // TODO: Add this part

    }
}
