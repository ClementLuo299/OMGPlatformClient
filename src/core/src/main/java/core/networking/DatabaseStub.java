package core.networking;

import core.networking.accounts.UserAccount;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulates an external database.
 */
public class DatabaseStub {
    private static final Map<String, UserAccount> userDatabase = new HashMap<>();

    // Insert new user account
    public boolean insertAccount(String username, String password, String fullName, String dob) {
        if (userDatabase.containsKey(username)) {
            return false; // User already exists
        }
        userDatabase.put(username, new UserAccount(username, password, fullName, dob));
        return true;
    }

    // Check if an account exists
    public boolean findAccount(String username) {
        return userDatabase.containsKey(username);
    }

    // Validate user login
    public boolean validateAccountPassword(String username, String password) {
        UserAccount user = userDatabase.get(username);
        return user != null && user.getPassword().equals(password);
    }
}
