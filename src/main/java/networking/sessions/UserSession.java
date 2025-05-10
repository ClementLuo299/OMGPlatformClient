package networking.sessions;

import networking.App;
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
        App.db().startUserSession(account);
    }

    public UserAccount getAccount() {
        return account;
    }

    public void endSession() {
        active = false;
        App.db().endUserSession(account);
    }
}
