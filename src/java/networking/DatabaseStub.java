package networking;

import networking.schema.DatabaseSchema;
import networking.schema.TableSchema;
import networking.accounts.UserAccount;

import java.io.*;
import java.util.*;

/**
 * Simulates an external database.
 *
 * @authors Clement Luo, Fatin Abrar Ankon,
 * @date March 4, 2025
 */
public class DatabaseStub {
    // ATTRIBUTES

    //Database schema
    private DatabaseSchema schema;

    //Data
    private Map<String, List<Map<String,String>>> data;

    //File paths for tables
    HashMap<String, String> paths = new HashMap<String, String>();

    //Delimiter for text data
    char delim1 = '\u0007';
    char delim2 = '\u0006';

    // CONSTRUCTOR

    public DatabaseStub() {
        schema = DatabaseSchema.getInstance();
        //Add file paths - using relative path from resources
        String dataDir = "src/resources/data";
        //Get filenames from schema
        for(String i : schema.getTableNames()){
            paths.put(i, dataDir + "/" + i + ".txt");
        }
        
        // Initialize the data map
        data = new HashMap<>();
        for(String tableName : schema.getTableNames()) {
            data.put(tableName, new ArrayList<>());
        }
    }

    // METHODS

    //Insert record
    public void insert(String table, Map<String,String> record){
        TableSchema tableSchema = schema.getTableSchema(table);
        //Check if table exists
        if(tableSchema != null){
            //Add record to data
            if(tableSchema.checkRecord(record)){
                data.get(table).add(record);
                saveDBState();
            }
        }
    }

    //Retrieve record based on column=value
    public Map<String,String> retrieve(String table, String column, String value){
        TableSchema tableSchema = schema.getTableSchema(table);

        if(tableSchema != null){
            //retrieve
            List<Map<String,String>> tableData = data.get(table);

            if(tableData != null){
                for (Map<String, String> row : tableData) {
                    if (row.get(column).equals(value)) {
                        return row;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get list of all entries
     * @return List of records
     */
    public List<Map<String,String>> retrieveAll(String table) {
        List<Map<String,String>> tableData = data.get(table);

        if (tableData != null) {
            return tableData;
        }

        return null;
    }

    //Update record
    public void update(String table, String searchColumn, String value, String modColumn, String newValue){
        Map<String,String> rec = retrieve(table,searchColumn,value);
        rec.put(modColumn,newValue);
        delete(table,searchColumn,value);
        insert(table,rec);
        saveDBState();
    }

    //Delete record
    public void delete(String table, String column, String value){
        TableSchema tableSchema = schema.getTableSchema(table);

        if(tableSchema != null) {
            //retrieve table data
            List<Map<String, String>> tableData = data.get(table);

            //Delete
            tableData.removeIf(row -> row.get(column).equals(value));
            data.put(table, tableData);
            saveDBState();
        }
    }

    // METHODS

    /**
     * Save the DB data.
     */
    public void saveDBState(){
        for(String table : schema.getTableNames()){
            try{
                //Create file writer
                File file = new File(paths.get(table));
                file.getParentFile().mkdirs(); // Create directories if they don't exist
                BufferedWriter buffer = new BufferedWriter(new FileWriter(file));

                //Get table data
                List<Map<String,String>> tableData = data.get(table);

                for(Map<String,String> row : tableData){
                    StringBuilder out = new StringBuilder();
                    for(String col : row.keySet()){
                        out.append(col).append(delim1).append(row.get(col)).append(delim2);
                    }
                    buffer.write(out.toString());
                    buffer.newLine();
                }

                //Close file writer
                buffer.close();
            } catch (IOException e) {
                System.out.println(table + " table could not be saved");
                e.printStackTrace();
            }
        }
    }

    /**
     * Load DB data from the files.
     */
    public void populateDB() {
        // First check for users.txt file which has a different format
        try {
            File userFile = new File("src/resources/data/users.txt");
            if (userFile.exists()) {
                System.out.println("DatabaseStub:\tLoading users from users.txt");
                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                
                // Create accounts table if not exists
                if (!data.containsKey("accounts")) {
                    data.put("accounts", new ArrayList<>());
                }
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    // Extract username and password (assuming pattern: username#password)
                    // For "ankon#ankon1", username="ankon", password="ankon1"
                    String username = "";
                    String password = "";

                    // Default fallback for other formats
                    // Assuming the first half is username, second half is password
                    int middle = 0;
                    for(middle=0; middle<line.length(); middle++){
                        if(line.charAt(middle)=='#'){
                            break;
                        }
                    }

                    username = line.substring(0, middle);
                    password = line.substring(middle);
                    
                    Map<String, String> record = new HashMap<>();
                    record.put("username", username);
                    record.put("password", password);
                    
                    // Add to accounts table
                    data.get("accounts").add(record);
                    System.out.println("Loaded user: " + username + " with password: " + password);
                }
                
                reader.close();
            } else {
                System.out.println("DatabaseStub:\tusers.txt file not found, will look for table-specific files");
            }
        } catch (IOException e) {
            System.out.println("DatabaseStub:\tError reading users.txt: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Continue with normal table loading for other tables
        for(String table : schema.getTableNames()) {
            try{
                //Retrieve file
                File file = new File(paths.get(table));
                if (!file.exists()) {
                    System.out.println(table + " table does not exist yet. Will be created on first save.");
                    continue; // Skip this table and move to next
                }

                //Create file reader
                BufferedReader inFile = new BufferedReader(new FileReader(file));

                List<Map<String,String>> tableData = new ArrayList<>();

                //Read first line
                String line = inFile.readLine();

                //Read the rest of the file
                while(line != null){
                    Map<String,String> row = new HashMap<>();
                    String[] kvPairs = line.split(String.valueOf(delim2));

                    for(String pair : kvPairs){
                        if (pair.isEmpty()) continue;
                        String[] pairArr = pair.split(String.valueOf(delim1));
                        if (pairArr.length >= 2) {
                            if(pairArr[1].equals("null")){
                                row.put(pairArr[0],null);
                            }
                            else {
                                row.put(pairArr[0], pairArr[1]);
                            }
                        }
                    }

                    if (!row.isEmpty()) {
                        tableData.add(row);
                    }
                    line = inFile.readLine();
                }

                //Add to data
                data.put(table,tableData);

                //Close file
                inFile.close();

            } catch (IOException e){
                System.out.println(table + " table could not be read.");
                e.printStackTrace();
            }
        }
        
        // Debug: Print loaded accounts
        if (data.containsKey("accounts")) {
            System.out.println("DatabaseStub:\tLoaded " + data.get("accounts").size() + " accounts:");
            for (Map<String, String> account : data.get("accounts")) {
                System.out.println("Username: " + account.get("username") + ", Password: " + account.get("password"));
            }
        } else {
            System.out.println("No accounts table found in data");
        }
    }
}
