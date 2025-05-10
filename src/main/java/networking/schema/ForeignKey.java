package networking.schema;

/**
 * Represents a foreign key relationship.
 *
 * @authors Clement Luo,
 * @date March 27, 2025
 */
public class ForeignKey {
    // ATTRIBUTES

    //Table and column that is referenced
    private String referencedTable;
    private String referencedColumn;

    // CONSTRUCTOR

    /**
     * Instantiates a foreign key relationship.
     *
     * @param referencedTable The table to reference.
     * @param referencedColumn The column to reference.
     */
    public ForeignKey(String referencedTable, String referencedColumn) {
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    // GETTERS

    /**
     * Gets the name of the referenced table.
     *
     * @return The name of the referenced table.
     */
    public String getReferencedTable() { return referencedTable; }

    /**
     * Gets the name of the referenced column.
     *
     * @return The name of the referenced column.
     */
    public String getReferencedColumn() { return referencedColumn; }
}
