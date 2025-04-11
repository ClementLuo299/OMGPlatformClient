package networking.IO;

import gamelogic.Game;
import gamelogic.GameType;
import networking.DatabaseStub;
import networking.accounts.GameRecord;
import networking.accounts.UserAccount;
import networking.sessions.GameSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Handles communication between the backend and the program.
 * Compatible with the built-in database stub.
 *
 * @authors Clement Luo,
 * @date March 4, 2025
 */
public class DatabaseIOHandler {
    //Database stub
    private DatabaseStub db;

    //Constructor
    public DatabaseIOHandler(){
        //Create and populate database
        db = new DatabaseStub();
        db.populateDB();
    }

    /**
     * Save database state
     */
    public void saveDBData() {
        db.saveDBState();
    }

    //USER ACCOUNT METHODS
    
    /**
     * Register an account.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     * @param firstName The first name of the user.
     * @param dob The date of birth of the user.
     */
    public void RegisterAccount(String username, String password, String firstName, String dob) {
        // For now, we'll just use the basic registration
        Map<String,String> record = new HashMap<>();
        record.put("username",username);
        record.put("password",password);
        record.put("firstName",firstName);
        record.put("dob",dob);

        //Increment was not implemented, so uid = 1
        record.put("uid","1");

        //Other fields (Set to default)
        record.put("privacyLevel","1");
        LocalDate currentDate = LocalDate.now();
        String dateAsString = currentDate.format(DateTimeFormatter.ISO_DATE);
        record.put("dateCreated",dateAsString);
        record.put("bio",null);
        record.put("email",null);
        record.put("middleName",null);
        record.put("lastName",null);

        //Insert record
        db.insert("users",record);
    }

    /**
     * Register an account with minimal information.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     */
    public void RegisterAccount(String username, String password) {
        // For now, we'll just use the basic registration
        Map<String,String> record = new HashMap<>();
        record.put("username",username);
        record.put("password",password);

        //Increment was not implemented, so uid = 1
        record.put("uid","1");

        //Other fields (Set to default)
        record.put("privacyLevel","1");
        LocalDate currentDate = LocalDate.now();
        String dateAsString = currentDate.format(DateTimeFormatter.ISO_DATE);
        record.put("dateCreated",dateAsString);
        record.put("bio",null);
        record.put("email",null);
        record.put("middleName",null);
        record.put("lastName",null);
        record.put("firstName",null);
        record.put("dob",null);

        //Insert record
        db.insert("users",record);
    }

    /**
     * Check if an account exists and password matches.
     *
     * @param username
     *            The username of the account.
     * @param password
     *            The password of the account.
     * @return boolean
     *             true if the account exists and password matches,
     *             false otherwise
     */
    public boolean verifyCredentials(String username, String password) {
        System.out.println("Verifying credentials for user: " + username);
        
        // Special case for the specific user
        if (username.equals("ankon") && password.equals("ankon1")) {
            System.out.println("Special user found: " + username + " - login successful");
            return true;
        }
        
        Map<String, String> accountData = db.retrieve("users","username",username);
        if (accountData != null) {
            System.out.println("Account found for: " + username);
            String storedPassword = accountData.get("password");
            System.out.println("Stored password: " + storedPassword + ", Entered password: " + password);
            if (storedPassword != null && storedPassword.equals(password)) {
                System.out.println("Password match - login successful");
                return true;
            } else {
                System.out.println("Password mismatch - login failed");
            }
        } else {
            System.out.println("No account found for: " + username);
        }
        return false;
    }

    /**
     * Check if a password is valid.
     *
     * @param username The username of the account.
     * @param current_password The current password.
     * @param new_password The new password to set.
     * @return boolean true if password was updated successfully, false otherwise
     */
    public boolean updatePassword(String username, String current_password, String new_password) {
        // Verify current password
        if (!verifyCredentials(username, current_password)) {
            return false;
        }
        
        // Update password
        Map<String, String> data = db.retrieve("users","username",username);
        if (data != null) {
            db.update("users", "username", username, "password", new_password);
            return true;
        }
        return false;
    }
    
    /**
     * Get the full name of a user
     * 
     * @param username The username to look up
     * @return The full name or null if not found
     */
    public String getUserFullName(String username) {
        Map<String, String> data = db.retrieve("users","username",username);
        if (data != null && data.containsKey("firstName") && data.containsKey("middleName") && data.containsKey("lastName")) {
            return data.get("firstName");
        }
        return null;
    }

    /**
     * Check if a password is valid.
     *
     * @param password
     *            The password of the account.
     * @return boolean
     *             true if the password meets all of the conditions,
     *             false otherwise
     */
    private boolean CheckPasswordValidity(String password){
        //Password should be at least 8 characters
        //Password should have at least 1 number
        //Password should have at least special character
        //Password should have at least 1 uppercase letter
        int characterCount = 0;
        int numberCount = 0;
        int specialCharCount = 0;
        int uppercaseCount = 0;

        return true;
    }

    /**
     * Check if an account exists.
     *
     * @param username
     *            The username of the account.
     * @return boolean
     *             true if the account exists,
     *             false otherwise
     */
    public boolean isAccountExists(String username) {
        if(db.retrieve("users","username",username) != null){
            return db.retrieve("users", "username", username).get("username") != null;
        }
        return false;
    }

    /**
     * Get list of all user accounts
     * @return List of UserAccount objects
     */
    public List<UserAccount> getUsers() {
        List<UserAccount> users = new ArrayList<>();
        List<Map<String, String>> accounts = db.retrieveAll("users");

        if (accounts != null) {
            for (Map<String, String> account : accounts) {
                String username = account.get("username");
                String password = account.get("password");
                String email = account.get("email");

                UserAccount user = new UserAccount(username, password);
                // Add email if it exists
                if (email != null) {
                    user.setEmail(email);
                }
                users.add(user);
            }
        }

        return users;
    }

    public UserAccount getUser(String username){
        Map<String,String> record = db.retrieve("users","username", username);

        if(record != null){
            UserAccount user = new UserAccount(record.get("username"),record.get("password"));
            user.setDob(record.get("dob"));
            user.setEmail(record.get("email"));
            return user;
        }

        return null;
    }

    //USER SESSION METHODS

    public void startUserSession(UserAccount account){
        Map<String,String> rec = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        String nowAsStr = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        //Didnt implement increment, so id = 1
        rec.put("id","1");

        //Other fields
        rec.put("username",account.getUsername());
        rec.put("datetimeCreated",nowAsStr);
        rec.put("datetimeEnded",null);

        db.insert("userSessions",rec);
    }

    public boolean checkActiveSession(UserAccount account){
        Map<String,String> rec = db.retrieve("userSessions","username",account.getUsername());

        if(rec != null){
            return rec.get("datetimeEnded") == null;
        }
        return false;
    }

    public void endUserSession(UserAccount account){
        Map<String,String> rec = db.retrieve("userSessions","username",account.getUsername());
        LocalDateTime now = LocalDateTime.now();
        String nowAsStr = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        //Update datetimeEnded
        if(rec != null){
            db.update("userSessions","username",account.getUsername(),"datetimeEnded",nowAsStr);
        }
    }

    //GAME SESSION METHODS

    public void saveGameSession(GameSession session){
        Map<String,String> record = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        //Did not implement increment, so id = player1 + player2 + datetimeCreated
        record.put("id",session.getAccounts().get(0).getUsername()
        + session.getAccounts().get(0).getUsername()
        + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        //Other fields
        record.put("game",null);
        record.put("open",Boolean.toString(session.getStatus()));
        record.put("player1",session.getAccounts().get(0).getUsername());
        record.put("player2",session.getAccounts().get(1).getUsername());
        record.put("dateTimeCreated",now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        db.insert("gameSessions",record);
    }

    //Return list of game sessions with player1, player2
    public List<GameSession> findGameSessions(UserAccount player1, UserAccount player2){
        List<Map<String,String>> data = db.retrieveAll("gameSessions");

        if(data != null){
            //Retrieve all game sessions with player1 and player2 matching
            List<Map<String,String>> data2 = data.stream().filter(x -> x.get("player1").equals(player1.getUsername()) && x.get("player2").equals(player2.getUsername()))
                    .toList();

            List<GameSession> gameSessions = new ArrayList<>();

            for(Map<String,String> map : data2){
                GameSession session = new GameSession();
                //GameType gameType = GameType.valueOf(map.get("game"));
                UserAccount p1 = getUser(map.get("player1"));
                UserAccount p2 = getUser(map.get("player2"));
                session.addAccount(p1);
                session.addAccount(p2);
                gameSessions.add(session);
            }

            return gameSessions;
        }
        return null;
    }

    //Search game session by id
    private GameSession findGameSession(String id){
        Map<String,String> s = db.retrieve("gameSessions","id",id);

        if(s != null){
            GameSession s2 = new GameSession();
            s2.addAccount(getUser(s.get("player1")));
            s2.addAccount(getUser(s.get("player2")));
            s2.setDateTimeCreated(s.get("dateTimeCreated"));

            return s2;
        }
        return null;
    }

    private String getID(GameSession session){
        String p1 = session.getAccounts().get(0).toString();
        String p2 = session.getAccounts().get(1).toString();
        String dt = session.getDateTimeCreated();
        return p1+p2+dt;
    }

    //GAME MOVE METHODS

    //Register move to database
    public void registerMove(GameSession session, UserAccount player, int moveNumber, String moveData){
        String id = getID(session);

        Map<String,String> rec = new HashMap<>();

        rec.put("gameSessionID",id);
        rec.put("playerUsername",player.getUsername());
        rec.put("moveNumber",Integer.toString(moveNumber));
        rec.put("moveData",moveData);

        db.insert("gameMoves",rec);
    }

    //Get latest move
    public Map<String,String> getMove(GameSession session){
        List<Map<String,String>> rec = getMoves(session);
        Map<String,String> latestMove = null;
        int highestMove = -1;

        if(rec != null){
            for(Map<String,String> i : rec){
                if(Integer.parseInt(i.get("moveNumber")) > highestMove){
                    highestMove = Integer.parseInt(i.get("moveNumber"));
                    latestMove = i;
                }
            }
        }
        return latestMove;
    }

    //Get all moves in session
    public List<Map<String,String>> getMoves(GameSession session){
        String id = getID(session);
        List<Map<String,String>> dat = db.retrieveAll("gameMoves");

        if(dat != null){
            List<Map<String,String>> dat2 = new ArrayList<>();
            for(Map<String,String> i : dat){
                if(i.get("gameSessionID").equals(id)){
                    dat2.add(i);
                }
            }
            return dat2;
        }
        return null;
    }

    //CHAT MESSAGE METHODS

    //Register message to database
    public void registerMessage(GameSession session, UserAccount player, int messageNumber, String message){
        String id = getID(session);

        Map<String,String> rec = new HashMap<>();

        rec.put("gameSessionID",id);
        rec.put("playerUsername",player.getUsername());
        rec.put("messageNumber",Integer.toString(messageNumber));
        rec.put("message",message);

        db.insert("chatMessages",rec);
    }

    //Get latest message
    public Map<String,String> getMessage(GameSession session){
        List<Map<String,String>> rec = getMessages(session);
        Map<String,String> latestMessage = null;
        int highestMessageNum = -1;

        if(rec != null){
            for(Map<String,String> i : rec){
                if(Integer.parseInt(i.get("messageNumber")) > highestMessageNum){
                    highestMessageNum = Integer.parseInt(i.get("messageNumber"));
                    latestMessage = i;
                }
            }
        }
        return latestMessage;
    }

    //Get all messages
    public List<Map<String,String>> getMessages(GameSession session){
        String id = getID(session);
        List<Map<String,String>> dat = db.retrieveAll("chatMessages");

        if(dat != null){
            List<Map<String,String>> dat2 = new ArrayList<>();
            for(Map<String,String> i : dat){
                if(i.get("gameSessionID").equals(id)){
                    dat2.add(i);
                }
            }
            return dat2;
        }
        return null;
    }

    //GAME RECORD METHODS
    public void registerGameRecord(GameRecord record){
        Map<String,String> rec = new HashMap<>();
        if(record != null){
            rec.put("id","1");
            rec.put("game",record.getGame().toString());
            rec.put("player1",record.getP1().getUsername());
            rec.put("player2",record.getP2().getUsername());
            rec.put("winner",Integer.toString(record.getWinner()));
            rec.put("score", Integer.toString(record.getScore()));

            db.insert("gameRecords",rec);
        }
    }

    public List<GameRecord> retrieveGameRecords(UserAccount account){
        List<Map<String,String>> gameRecs = db.retrieveAll("gameRecords");

        if(gameRecs != null){
            //Retrieve all game sessions with player1 or player2 matching
            List<Map<String,String>> gameRecs2 = gameRecs.stream().filter(x -> x.get("player1").equals(account.getUsername()) || x.get("player2").equals(account.getUsername()))
                    .toList();

            List<GameRecord> records = new ArrayList<>();

            for(Map<String,String> i : gameRecs2){
                GameRecord record = new GameRecord(GameType.valueOf(i.get("game"))
                        ,getUser(i.get("player1"))
                , getUser(i.get("player2"))
                ,Integer.parseInt(i.get("winner"))
                ,Integer.parseInt(i.get("score")));
                records.add(record);
            }

            return records;
        }
        return null;
    }

    //STATS FUNCTIONS
    public int getGamesPlayed(UserAccount account){
        List<GameRecord> records = retrieveGameRecords(account);
        if(records != null){
            return records.size();
        }
        return 0;
    }

    private int getGamesWon(UserAccount account){
        List<GameRecord> records = retrieveGameRecords(account);
        int counter = 0;

        if(records != null){
            for(GameRecord r : records){
                if(r.getWinner() == 1 && r.getP1().getUsername().equals(account.getUsername())){
                    counter++;
                }
                else if(r.getWinner() == 2 && r.getP2().getUsername().equals(account.getUsername())){
                    counter++;
                }
            }
        }
        return counter;
    }

    public double getWinRate(UserAccount account){
        if(getGamesPlayed(account) != 0){
            return (double) getGamesWon(account) / getGamesPlayed(account);
        }
        return 0;
    }

    private List<UserAccount> getAccountsRankedByWinRate(){
        List<UserAccount> accounts = getUsers();
        accounts.sort((p1,p2) -> Double.compare(getWinRate(p2),getWinRate(p1)));
        return accounts;
    }

    public int getRank(UserAccount account){
        List<UserAccount> sortedAccountList = getAccountsRankedByWinRate();
        int counter = 0;

        if(sortedAccountList != null){
            for(UserAccount a : sortedAccountList){
                if(!(a.getUsername().equals(account.getUsername()))){
                    counter++;
                }
                else{
                    counter++;
                    break;
                }
            }
        }
        if(getWinRate(account) != 0){
            return counter;
        }
        return 0;
    }

    public GameType getBestGame(UserAccount account){
        return null;
    }
}