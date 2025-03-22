package core.networking.accounts;

public class UserAccount {
    private String username;
    private String password;
    private String email;
    private String created_date;
    private String bio;
    private String fullName;
    private String dob; // Date of Birth as String

    public UserAccount(String username, String password, String fullName, String dob) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dob = dob;
    }

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
}
