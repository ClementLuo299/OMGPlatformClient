package networking;

import networking.schema.DatabaseSchema;
import networking.schema.TableSchema;

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

            for (Map<String, String> row : tableData) {
                if (row.get(column).equals(value)) {
                    return row;
                }
            }
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
    public void populateDB(){
        for(String table : schema.getTableNames()) {
            try{
                //Retrieve file
                File file = new File(paths.get(table));
                if (!file.exists()) {
                    System.out.println(table + " table does not exist yet. Will be created on first save.");
                    return;
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
                        String[] pairArr = pair.split(String.valueOf(delim1));
                        row.put(pairArr[0],pairArr[1]);
                    }
                    tableData.add(row);
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
    }
}
