package networking.accounts;
import networking.DatabaseStub;

public class UserAccountForgotPassword {

    private DatabaseStub database;

    public ForgottenPassword(DatabaseStub database) {
        this.database = database;
    }

    /*
    Generates random string of length 10
    
    */
    private String generateTempPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*";
        StringBuilder tempPassword = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            tempPassword.append(characters.charAt(random.nextInt(characters.length())));
        }
        return tempPassword.toString();
    }

    public boolean processForgottenPassword(String identifier) {
        for (UserAccount user : database.getUsers()) {
            if (identifier.equals(user.getUsername()) || identifier.equals(user.getEmail())) {
                String tempPassword = generateTempPassword();
                displayTempPassword(tempPassword);
                user.setPassword(tempPassword);
                system.out.println("Your new password is: \n");
                system.out.println(tempPassword);
                database.saveDBState();
                return true;
            }
        }
        return false; // No matching user
    }

    public String displayTempPassword(String tempPass){
        return tempPass;
    }


}
