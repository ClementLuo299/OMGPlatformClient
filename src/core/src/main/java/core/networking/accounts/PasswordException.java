package core.networking.accounts;

/**
 * Invalid password exception, thrown when password does not meet complexity requirement.
 *
 * @author Clement Luo
 */
public class PasswordException extends Exception {
    public PasswordException(String message){
        super(message);
    }
}