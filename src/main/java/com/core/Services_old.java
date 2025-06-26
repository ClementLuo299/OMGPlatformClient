package com.core;

import com.network.IO.DatabaseIOHandler;
import com.network.IO.HTTPHandler;
import com.network.IO.GUIEventHandler;
import com.network.IO.GameIOHandler;

/**
 * Collection of services
 *
 * @authors Clement Luo, Fatin Abrar Ankon, Dylan Shiels, Susmita Chakrabarty, Dominic Afuba, Scott Brown
 * @date March 3, 2025
 */
public class Services_old {
    //Universal backend instance
    private static Services_old instance;

    // ATTRIBUTES

    //IO handlers
    public static GUIEventHandler guiHandler;
    public static DatabaseIOHandler dbHandler;
    public static HTTPHandler dbHandlerHTTP;
    public static GameIOHandler gameHandler;


    // CONSTRUCTOR

    /**
     * Instantiates the backend.
     */
    private Services_old() {
        //Activate IO handlers
        guiHandler = new GUIEventHandler();
        dbHandler = new DatabaseIOHandler();
        dbHandlerHTTP = new HTTPHandler();
        gameHandler = new GameIOHandler();
    }

    /**
     * Retrieve the backend
     */
    public static Services_old getInstance() {
        if (instance == null) {
            instance = new Services_old();
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

    public static HTTPHandler dbHTTP(){
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