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
public class App {
    // ATTRIBUTES

    //IO handlers
    public static GUIIOHandler guiHandler;
    public static DatabaseIOHandler dbHandler;
    public static DatabaseIOHandlerHTTP dbHandlerHTTP;
    public static GameIOHandler gameHandler;

    //Universal backend instance
    private static App instance;

    // CONSTRUCTOR

    /**
     * Instantiates the backend.
     */
    private App() {
        //Activate IO handlers
        guiHandler = new GUIIOHandler();
        dbHandler = new DatabaseIOHandler();
        dbHandlerHTTP = new DatabaseIOHandlerHTTP();
        gameHandler = new GameIOHandler();
    }

    /**
     * Retrieve the backend
     */
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    // GETTERS
    public static GUIIOHandler gui(){
        return guiHandler;
    }

    public static DatabaseIOHandler db(){
        return dbHandler;
    }

    public static DatabaseIOHandlerHTTP dbHTTP(){
        return dbHandlerHTTP;
    }

    public static GameIOHandler game(){
        return gameHandler;
    }

    // SETTERS

    // METHODS

    public void saveDBState() {
        dbHandler.saveDBData();
    }
}