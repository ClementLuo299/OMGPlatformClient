package networking.IO;

import networking.DatabaseStub;

/**
 * Handles communication between the database and the program.
 * Compatible with the built-in database stub.
 */
public class DatabaseIOHandler {
    //Database
    private DatabaseStub db;

    //Universal DatabaseIOHandler
    private static DatabaseIOHandler instance;

    //Constructor
    private DatabaseIOHandler(){
        //Create and populate database
        DatabaseStub db = new DatabaseStub();
        db.populateDB();
        this.db = db;
    }

    /**
     * Retrieve the database input/output handler
     */
    public static DatabaseIOHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseIOHandler();
        }
        return instance;
    }

    /**
     * Save database state
     */
    public void saveDBData() {
        db.saveDBState();
    }

    /**
     * Register an account.
     *
     * @param username
     *            The username of the account.
     * @param password
     *            The password of the account.
     */
    public void RegisterAccount(String username, String password){
        db.insertAccountData(username,password);
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
        String[] accountData = db.getAccountData(username);
        if (accountData != null && accountData[1].equals(password)) {
            return true;
        }
        return false;
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
    private boolean CheckAccountExists(String username) {
        return db.checkAccountExists(username);
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
     * Check if an account exists (for registration).
     *
     * @param username
     *            The username of the account.
     * @return boolean
     *             true if the account exists,
     *             false otherwise
     */
    public boolean isAccountExists(String username) {
        return db.checkAccountExists(username);
    }
}