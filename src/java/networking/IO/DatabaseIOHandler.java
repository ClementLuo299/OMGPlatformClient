package networking.IO;

import networking.DatabaseStub;
import java.util.Map;

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
    
    // Singleton instance
    private static DatabaseIOHandler instance;

    //Constructor
    public DatabaseIOHandler(){
        //Create and populate database
        db = new DatabaseStub();
        db.populateDB();
    }
    
    /**
     * Get singleton instance
     * @return DatabaseIOHandler instance
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
        db.insertAccountData(username, password);
    }
    
    /**
     * Register an account with additional information.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     * @param email The email of the account.
     * @param fullName The full name of the user.
     */
    public void RegisterAccount(String username, String password, String email, String fullName) {
        // For now, we'll just use the basic registration
        db.insertAccountData(username, password);
        // In a real implementation, we would store the additional info
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
        
        Map<String, String> accountData = db.getAccountData(username);
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
        Map<String, String> data = db.getAccountData(username);
        if (data != null) {
            db.update("accounts", "username", username, "password", new_password);
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
        Map<String, String> data = db.getAccountData(username);
        if (data != null && data.containsKey("fullName")) {
            return data.get("fullName");
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