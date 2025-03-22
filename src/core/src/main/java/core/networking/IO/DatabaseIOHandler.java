package core.networking.IO;

import core.networking.DatabaseStub;

/**
 * Handles communication between the database and the program.
 * Compatible with the built-in database stub.
 */
public class DatabaseIOHandler {
    //Database
    private DatabaseStub db;

    private static DatabaseIOHandler instance;

    //Constructor
    private DatabaseIOHandler(){
        DatabaseStub db = new DatabaseStub();
        db.populateDB();
        this.db = db;
    }

    public static DatabaseIOHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseIOHandler();
        }
        return instance;
    }

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
     * Check if an account exists.
     *
     * @param username
     *            The username of the account.
     * @return boolean
     *             true if the account exists,
     *             false otherwise
     */
    public boolean CheckAccountExists(String username){
        //
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
}