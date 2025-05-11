package com.network.IO;

import com.models.accounts.GameRecord;
import com.network.sessions.GameSession;

/**
 * Handles communication between the backend and gameplay.
 *
 * @authors Clement Luo,
 * @date March 4, 2025
 */
public class GameIOHandler {

    private GameSession session;

    /**
     * basic constructor
     */
    public GameIOHandler() {
        this.session = new GameSession();
    }

    public void endGame() {
        GameRecord record = session.generateGameRecord();
    }

}
