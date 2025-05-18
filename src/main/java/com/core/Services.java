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
public class Services {
    //Universal backend instance
    private static Services instance;

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
    private Services() {
        //Activate IO handlers
        guiHandler = new GUIEventHandler();
        dbHandler = new DatabaseIOHandler();
        dbHandlerHTTP = new HTTPHandler();
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