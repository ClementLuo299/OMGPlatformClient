package core.networking.IO;

/**
 * Handles communication between the database and the program.
 * Compatible with the built-in database stub.
 *
 * @author Clement Luo
 */
public class DatabaseIOHandler {
    public DatabaseIOHandler(){}

    /**
     * Register an account.
     *
     * @param username
     *            The username of the account.
     * @param password
     *            The password of the account.
     */
    public void RegisterAccount(String username, String password){
        //
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
        return true;
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
        //Password shoudl have at least 1 uppercase letter
        int characterCount = 0;
        int numberCount = 0;
        int specialCharCount = 0;
        int uppercaseCount = 0;

        return true;
    }
}