package com.network.sessions;

import com.core.Services_old;
import com.entities.UserAccount;

/**
 * A user's login session.
 *
 * @author Clement Luo
 * @date March 3, 2025
 */
public class UserSession {
    private UserAccount account;
    private boolean active;

    public UserSession(UserAccount account){
        this.account = account;
        active = true;
        Services_old.db().startUserSession(account);
    }

    public UserAccount getAccount() {
        return account;
    }

    public void endSession() {
        active = false;
        Services_old.db().endUserSession(account);
    }
}
