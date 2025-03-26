package networking.accounts;

/**
 * Represents a user account.
 */
public class UserAccount {
    private String username;
    private String password;
    private String email;
    private String created_date; //Date that account was created
    private String bio; //Biography
    private String fullName;
    private String dob; // Date of Birth as String

    /**
     * Constructor that takes only required fields.
     */
    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getters.
     */
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFullName() {
        return fullName;
    }
    public String getDob() {
        return dob;
    }

    /**
     * Setters.
     */
}
