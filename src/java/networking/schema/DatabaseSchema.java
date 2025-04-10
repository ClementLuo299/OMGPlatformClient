package networking.schema;

import java.util.*;

/**
 * Database schema (columns are in order):
 *
 * Table users:
 * Increment Primary key int uid,
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
 *
 * Table gameRecords:
 * int id
 * String game
 * String player1
 * String player2
 * int winner
 * int score
 */

/**
 * Outlines the database schema.
 *
 * @authors Clement Luo,
 * @date March 27, 2025
 */
public class DatabaseSchema {
    // ATTRIBUTES

    private final Map<String, TableSchema> tableSchemas; //Collection of table schemas
    private final Map<String, List<String>> columns; //Collection of column names
    private final Map<String, Map<String, Set<ConstraintType>>> constraints = new HashMap<String, Map<String, Set<ConstraintType>>>(); //
    private final Map<String, String[]> foreignKeys = new HashMap<>(); //List of foreign key relations
    private static DatabaseSchema instance; //Database schema instance

    // CONSTRUCTOR

    /**
     * Instantiates the database schema.
     */
    private DatabaseSchema() {
        this.tableSchemas = new HashMap<>();
        this.columns = new HashMap<>();

        /**
         * Lists of columns for each table
         */

        //Users table
        List<String> users = List.of(
                "uid",
                "username",
                "password",
                "email",
                "dob",
                "privacyLevel",
                "dateCreated",
                "bio",
                "firstName",
                "middleName",
                "lastName"
        );
        columns.put("users", users);

        //Game records table
        List<String> gameRecords = List.of(
                "id",
                "game",
                "player1",
                "player2",
                "winner",
                "score"
        );
        columns.put("gameRecords", gameRecords);

        //Create schema
        createSchema();
    }

    // GETTERS

    /**
     * Get the schema of a table.
     *
     * @param tableName the table of the desired schema
     * @return The desired table schema
     */
    public TableSchema getTableSchema(String tableName) { return tableSchemas.get(tableName); }

    /**
     * Get the names of the columns.
     *
     * @return A set containing the names of all the tables
     */
    public Set<String> getTableNames() { return tableSchemas.keySet(); }

    /**
     * Get the schema instance.
     *
     * @return The database schema
     */
    public static DatabaseSchema getInstance() {
        if (instance == null) {
            instance = new DatabaseSchema();
        }
        return instance;
    }

    // METHODS

    /**
     * Add a table schema.
     *
     * @param tableSchema The table schema to add
     */
    public void addTableSchema(TableSchema tableSchema) {
        tableSchemas.put(tableSchema.getName(), tableSchema);
    }

    /**
     * Create and add all the table schemas.
     */
    private void createSchema() {
        //Loop through each table
        for(Map.Entry<String, List<String>> entry : columns.entrySet()) {
            //Create table
            TableSchema table = new TableSchema(entry.getKey(), entry.getValue());

            //Add constraints
            /*
            Map<String, Set<ConstraintType>> constrnts = constraints.get(entry.getKey());
            for(Map.Entry<String, Set<ConstraintType>> entry2 : constrnts.entrySet()){
                for(ConstraintType c : entry2.getValue()){
                    table.addConstraint(entry2.getKey(),c.getName());
                }
            }
             */

            //Add table schema
            tableSchemas.put(entry.getKey(), table);
        }
    }
}
