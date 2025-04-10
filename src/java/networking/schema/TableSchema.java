package networking.schema;

import java.util.*;

/**
 * Outlines a schema for a table.
 *
 * @authors Clement Luo,
 * @date March 27, 2025
 */
public class TableSchema {
    // ATTRIBUTES

    private String name; //Name of table
    private List<String> columnNames; //Name of columns
    private Map<String, Set<ConstraintType>> constraints; //Set of constraints for each column
    private Map<String, ForeignKey> foreignKeys; //Foreign key constraints
    private Map<String, Integer> incrementCounters; //Counters for increments

    // CONSTRUCTOR

    /**
     * Instantiates a foreign key relationship.
     *
     * @param name The name of the table.
     * @param columnNames A list of the column names.
     */
    public TableSchema(String name, List<String> columnNames) {
        this.name = name;
        this.columnNames = columnNames;
        this.constraints = new HashMap<>();
        this.foreignKeys = new HashMap<>();
        this.incrementCounters = new HashMap<>();
    }

    // GETTERS

    /**
     * Gets the name of the table.
     *
     * @return The name of the table.
     */
    public String getName() { return name; }

    /**
     * Gets the list of column names.
     *
     * @return A list of the column names.
     */
    public List<String> getColumnNames() { return columnNames; }

    /**
     * Gets column constraints.
     *
     * @return A set of column constraints.
     */
    public Set<ConstraintType> getConstraints(String columnName) {
        return constraints.getOrDefault(columnName, new HashSet<>());
    }

    /**
     * Gets foreign key relationship.
     *
     * @return A foreign key of which the column references.
     */
    public ForeignKey getForeignKey(String columnName) {
        return foreignKeys.get(columnName);
    }

    // METHODS

    /**
     * Adds a constraint to a column
     *
     * @param columnName The name of the column
     * @param constraintType The type of constraint to be applied
     */
    public void addConstraint(String columnName, String constraintType) {
        //Create set of constraints if it does not exist
        constraints.computeIfAbsent(columnName, _ -> new HashSet<>());

        //Add the constraint
        constraints.get(columnName).add(ConstraintType.valueOf(constraintType));

        //Add counter if applicable
        if(ConstraintType.valueOf(constraintType) == ConstraintType.INCREMENT){
            incrementCounters.put(columnName,1);
        }
    }

    /**
     * Check if a column has a constraint
     *
     * @param columnName The name of the column
     * @param constraint The constraint to check for
     */
    public boolean hasConstraint(String columnName, String constraint) {
        //Check if the column has the constraint, send an empty set to check if it does not exist
        return constraints.getOrDefault(columnName, new HashSet<>()).contains(constraint);
    }

    /**
     * Add a foreign key relationship
     *
     * @param columnName The name of the column
     * @param referencedTable The table of the referenced column
     * @param referencedColumn The column to be referenced
     */
    public void addForeignKey(String columnName, String referencedTable, String referencedColumn) {
        foreignKeys.put(columnName, new ForeignKey(referencedTable, referencedColumn));
    }

    //Check if a record has all of the columns
    public boolean checkRecord(Map<String,String> record){
        for(String col : record.keySet()){
            if(!columnNames.contains(col)){
                return false;
            }
        }
        return true;
    }
}
