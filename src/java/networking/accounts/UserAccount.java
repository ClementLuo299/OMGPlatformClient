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
    private boolean isGuest; // Whether this is a guest account

    /**
     * Constructor that takes only required fields.
     */
    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.isGuest = false;
    }

    /**
     * Constructor with guest flag.
     */
    public UserAccount(String username, String password, boolean isGuest) {
        this.username = username;
        this.password = password;
        this.isGuest = isGuest;
    }

    /**
     * Constructor for guest accounts.
     */
    public UserAccount(String username, boolean isGuest) {
        this.username = username;
        this.password = ""; // No password for guest accounts
        this.isGuest = isGuest;
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
    public boolean isGuest() {
        return isGuest;
    }

    /**
     * Setters.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
    
    public void setGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }
}
