package networking.accounts;

import networking.DatabaseStub;

public class UserAccountRegistration {
    private final DatabaseStub database;

    public UserAccountRegistration(DatabaseStub database) {
        this.database = database;
    }

    /*
    public boolean registerUser(String fullName, String username, String password, String confirmPassword, String dob) {
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty()) {
            System.out.println("Please fill all fields.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }

        if (database.insertAccount(username, password, fullName, dob)) {
            System.out.println("Registration successful.");
            return true;
        } else {
            System.out.println("Username already exists.");
            return false;
        }
    }
     */
}
