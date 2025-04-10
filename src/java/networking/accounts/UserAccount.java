package networking.accounts;

import java.time.LocalDate;

/**
 * Represents a user account.
 *
 * @authors Clement Luo,
 * @date March 4, 2025
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
        this.created_date = LocalDate.now().toString();
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
    public String getEmail() {
        return email;
    }

    /**
     * Setters.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
}
