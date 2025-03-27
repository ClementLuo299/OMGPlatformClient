package networking.accounts;

/**
 * Invalid password exception, thrown when password does not meet complexity requirements.
 */
public class PasswordException extends Exception {
    public PasswordException(String message){
        super(message);
    }
}