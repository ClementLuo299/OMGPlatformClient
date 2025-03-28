package networking;

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
        //Add file paths - using relative path from resources
        String dataDir = "src/resources/data";
        paths.put("users", dataDir + "/users.txt");
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
     * Insert data for a new user with name and date of birth.
     */
    public void insertAccountData(String username, String password, String fullName, String dateOfBirth){
        String[] arr = new String[4];
        arr[0] = username;
        arr[1] = password;
        arr[2] = fullName;
        arr[3] = dateOfBirth;
        users.add(arr);
    }

    /**
     * Get account data.
     */
    public String[] getAccountData(String username){
        String[] data = null;
        for(String[] i : users){
            if(i[0].equals(username)){
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
            if (i[0].equals(username)) {
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
            File file = new File(paths.get("users"));
            file.getParentFile().mkdirs(); // Create directories if they don't exist
            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));

            //Write data by line
            for(String[] i : users){
                // Write all available fields
                StringBuilder line = new StringBuilder();
                line.append(i[0]).append(delim).append(i[1]); // Username and password
                
                // Add name and date of birth if they exist
                if (i.length > 2) {
                    line.append(delim).append(i[2]); // Full name
                    if (i.length > 3) {
                        line.append(delim).append(i[3]); // Date of birth
                    }
                }
                
                buffer.write(line.toString());
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
            File file = new File(paths.get("users"));
            if (!file.exists()) {
                System.out.println("Users table does not exist yet. Will be created on first save.");
                return;
            }

            //Create file reader
            BufferedReader inFile = new BufferedReader(new FileReader(file));

            //Read first line
            String line = inFile.readLine();

            //Add first line to database
            if(line != null){
                String[] cols = line.split(String.valueOf(delim));
                users.add(cols);
            }

            while(line != null){
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
