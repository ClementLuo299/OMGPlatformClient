package gamelogic;

import java.util.Random;

public class PlayerDatabaseStub {
    //The map stores usernames and their corresponding values
    private static final int DEFAULT_RETRIES = 3;

    private static final Random random = new Random(0);
    private static final double PROBABILITY_OF_USERNAME_TAKEN = 0.5;
    private static final double PROBABILITY_OF_AUTHENTICATION_FAILURE = 0.1;

    public boolean usernameIsTaken(String username){
        if(random.nextDouble() <= PROBABILITY_OF_USERNAME_TAKEN){
            //throw new usernameTakenException();
            return false;
        }
        return true;
    }
    
    public boolean authenticate(String username, String password){
        if(random.nextDouble() <= PROBABILITY_OF_AUTHENTICATION_FAILURE) {
            //throw new authenticationFailedException();
            return false;
        }
        return true;
    }
    public boolean authenticateWithRetry(String username, String password, int retries) {
        
    }
}