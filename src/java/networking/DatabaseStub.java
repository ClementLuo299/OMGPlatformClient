package networking;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Database schema (columns are in order):
 *
 * Table users:
 * Increment Not Null Unique int uid,
 * Not Null Unique String username,
 * Not Null String password,
 * Unique String email,
 * String dob,
 * int privacyLevel,
 * Not Null String dateCreated,
 * String bio,
 * String firstName
 * String middleName
 * String lastName
 */


/**
 * Simulates an external database.
 *
 * @authors Clement Luo, Fatin Abrar Ankon,
 * @date March 4, 2025
 */
public class DatabaseStub {
    // ATTRIBUTES

    //File paths for tables
    HashMap<String, String> paths = new HashMap<String, String>();

    //Delimiter for text data
    char delim = '\u0007';

    //UID counter
    int uidCounter = 1;

    // TABLES

    /**
     * Table of users.
     */
    ArrayList<String[]> users = new ArrayList<>();

    // CONSTRUCTOR

    public DatabaseStub(){
        //Add file paths - using relative path from resources
        String dataDir = "src/resources/data";
        paths.put("users", dataDir + "/users.txt");
    }

    // USERS TABLE METHODS

    /**
     * Insert data for a new user.
     */
    public void insertAccountData(
            String username,
            String password,
            String email,
            String dob,
            int privacyLevel,
            String dateCreated,
            String bio,
            String firstName,
            String middleName,
            String lastName)
    {
        //Get UID
        String uid = Integer.toString(uidCounter);

        //Enforce database restrictions

        //Check not null fields
        if(username == null || password == null || dateCreated == null){
            return;
        }

        //Check unique fields
        for(String[] i : users){
            if(i[1].equals(username) || i[3].equals(email)){
                return;
            }
        }

        //Find smallest available UID
        for(String[] i : users){
            if(i[0].equals(uid)){
                uidCounter++;
                uid = Integer.toString(uidCounter);
            }
        }

        //Insert data
        String[] arr = new String[11];
        arr[0] = uid;
        arr[1] = username;
        arr[2] = password;
        arr[3] = email;
        arr[4] = dob;
        arr[5] = Integer.toString(privacyLevel);
        arr[6] = dateCreated;
        arr[7] = bio;
        arr[8] = firstName;
        arr[9] = middleName;
        arr[10] = lastName;
        users.add(arr);
    }

    /**
     * Reinsert account given uid, used for updating entries.
     */
    public void insertAccountData(
            String uid,
            String username,
            String password,
            String email,
            String dob,
            int privacyLevel,
            String dateCreated,
            String bio,
            String firstName,
            String middleName,
            String lastName)
    {
        //Enforce database restrictions

        //Check not null fields
        if(username == null || password == null || dateCreated == null || uid == null){
            return;
        }

        //Check unique fields
        for(String[] i : users){
            if(i[0].equals(uid) || [1].equals(username) || i[3].equals(email)){
                return;
            }
        }


        //Insert data
        String[] arr = new String[11];
        arr[0] = uid;
        arr[1] = username;
        arr[2] = password;
        arr[3] = email;
        arr[4] = dob;
        arr[5] = Integer.toString(privacyLevel);
        arr[6] = dateCreated;
        arr[7] = bio;
        arr[8] = firstName;
        arr[9] = middleName;
        arr[10] = lastName;
        users.add(arr);
    }

    /**
     * Get account data.
     */
    public String[] getAccountData(String uid){
        String[] data = null;
        for(String[] i : users){
            if(i[0].equals(uid)){
                data = i;
                break;
            }
        }
        return data;
    }

    /**
     * Delete an account.
     */
    public void deleteAccount(String uid) {
        //
    }

    /**
     * Reset the UID counter.
     */
    private void resetUIDCounter() {
        uidCounter = 1;
    }

    /**
     * Sort the users table by UID.
     */
    private void sortUsersTable() {
        users.sort((o1, o2) -> {
            //Convert uid to integer
            int num1 = Integer.parseInt(o1[0]);
            int num2 = Integer.parseInt(o2[0]);
            //Compare uid
            return Integer.compare(num1, num2);
        });
    }

    // METHODS

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
            //Retrieve file
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

            //Read the rest of the file
            while(line != null){
                line = inFile.readLine();
                if(line != null){
                    String[] cols = line.split(String.valueOf(delim));
                    users.add(cols);
                }
            }

            //Sort the users table
            sortUsersTable();

            //Close file
            inFile.close();

        } catch (IOException e) {
            System.out.println("Users table could not be read.");
            e.printStackTrace();
        }
    }
}
