package com;

import com.network.IO.DatabaseIOHandler;
import com.network.IO.DatabaseIOHandlerHTTP;
import com.network.IO.GUIEventHandler;
import com.network.IO.GameIOHandler;

/**
 * Collection of services
 *
 * @authors Clement Luo,
 * @date March 27, 2025
 */
public class Services {
    //Universal backend instance
    private static Services instance;

    // ATTRIBUTES

    //IO handlers
    public static GUIEventHandler guiHandler;
    public static DatabaseIOHandler dbHandler;
    public static DatabaseIOHandlerHTTP dbHandlerHTTP;
    public static GameIOHandler gameHandler;


    // CONSTRUCTOR

    /**
     * Instantiates the backend.
     */
    private Services() {
        //Activate IO handlers
        guiHandler = new GUIEventHandler();
        dbHandler = new DatabaseIOHandler();
        dbHandlerHTTP = new DatabaseIOHandlerHTTP();
        gameHandler = new GameIOHandler();
    }

    /**
     * Retrieve the backend
     */
    public static Services getInstance() {
        if (instance == null) {
            instance = new Services();
        }
        return instance;
    }

    // GETTERS
    public static GUIEventHandler gui(){
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