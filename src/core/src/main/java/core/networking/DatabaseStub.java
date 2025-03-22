package core.networking;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simulates an external database.
 *
 * Schema (columns are in order):
 *
 * Table users:
 * Not Null Unique int uid,
 * Not Null Unique String username,
 * Not Null String password,
 * Unique String email,
 * String dob,
 * int privacyLevel,
 * Not Null String dateCreated,
 * String bio,
 * String fullName
 */
public class DatabaseStub {
    //File paths for tables
    HashMap<String, String> paths = new HashMap<String, String>();

    //Delimiter for text data
    char delim = '\u0007';

    //Constructor
    public DatabaseStub(){
        //Add file paths
        paths.put("users","./src/core/src/main/java/core/networking/users.txt");
    }

    /**
     * Table of users.
     */
    ArrayList<String[]> users = new ArrayList<>();

    /**
     * Insert data for a new user.
     */
    public void insertAccountData(String username, String password){
        String[] arr = new String[2];
        arr[0] = username;
        arr[1] = password;
        users.add(arr);
    }

    /**
     * Get account data.
     */
    public String[] getAccountData(String username){
        String[] data = null;
        for(String[] i : users){
            if(i[1].equals(username)){
                data = i;
                break;
            }
        }
        return data;
    }

    /**
     * Check if account exists.
     */
    public boolean checkAccountExists(String username){
        boolean result = false;
        for(String[] i : users){
            if (i[1].equals(username)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Save the DB data.
     */
    public void saveDBState(){
        //Save users table
        try{
            //Create file writer
            BufferedWriter buffer = new BufferedWriter(new FileWriter(paths.get("users")));

            //Write data by line
            for(String[] i : users){
                buffer.write(i[0] + delim + i[1]);
                buffer.newLine();
            }

            //Close file writer
            buffer.close();

        } catch (IOException e) {
            System.out.println("Users table could not be saved.");
            e.printStackTrace();
        }
    }

    /**
     * Load DB data from the files.
     */
    public void populateDB(){
        //Read users table
        try{
            //Create file reader
            BufferedReader inFile = new BufferedReader(new FileReader(paths.get("users")));

            //Read first line
            String line = inFile.readLine();

            //Add first line to database
            if(line != null){
                String[] cols = line.split(String.valueOf(delim));
                users.add(cols);
            }

            while(line != null){
                //
                line = inFile.readLine();
                if(line != null){
                    String[] cols = line.split(String.valueOf(delim));
                    users.add(cols);
                }
            }

            inFile.close();
        } catch (IOException e) {
            System.out.println("Users table could not be read.");
            e.printStackTrace();
        }
    }
}
