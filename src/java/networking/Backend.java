package networking;

import networking.IO.DatabaseIOHandler;
import networking.IO.DatabaseIOHandlerHTTP;
import networking.IO.GUIIOHandler;
import networking.IO.GameIOHandler;

/**
 * The backend of the program.
 *
 * @authors Clement Luo,
 * @date March 27, 2025
 */
public class Backend {
    // ATTRIBUTES

    //IO handlers
    GUIIOHandler guiHandler;
    DatabaseIOHandler dbHandler;
    DatabaseIOHandlerHTTP dbHandlerHTTP;
    GameIOHandler gameHandler;

    //Universal backend instance
    private static Backend instance;

    // CONSTRUCTOR

    /**
     * Instantiates the backend.
     */
    private Backend() {
        //Activate IO handlers
        guiHandler = new GUIIOHandler();
        dbHandler = new DatabaseIOHandler();
        dbHandlerHTTP = new DatabaseIOHandlerHTTP();
        gameHandler = new GameIOHandler();
    }

    /**
     * Retrieve the backend
     */
    public static Backend getInstance() {
        if (instance == null) {
            instance = new Backend();
        }
        return instance;
    }

    // GETTERS

    // SETTERS

    // METHODS

    public void saveDBState() {
        dbHandler.saveDBData();
    }
}