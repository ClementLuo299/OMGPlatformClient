package networking.sessions;

import networking.Backend;
import networking.accounts.UserAccount;

/**
 * A user's login session.
 *
 * @author Clement Luo
 */
public class UserSession {
    private UserAccount account;
    private boolean active;

    public UserSession(UserAccount account){
        this.account = account;
        active = true;
        Backend.db().startUserSession(account);
    }

    public UserAccount getAccount() {
        return account;
    }

    public void endSession() {
        active = false;
        Backend.db().endUserSession(account);
    }
}
