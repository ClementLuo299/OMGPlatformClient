package com.network.schema;

/**
 * Collection of constraints.
 *
 * @authors Clement Luo,
 * @date March 27, 2025
 */
public enum ConstraintType {
    //Constraint types
    NOT_NULL("NOT_NULL"),
    UNIQUE("UNIQUE"),
    PRIMARY_KEY("PRIMARY_KEY"), //Combination of unique and not null
    FOREIGN_KEY("FOREIGN_KEY"), //Entry refers to some value in another table
    INCREMENT("INCREMENT"); //Auto increment (only for integers)

    // ATTRIBUTES

    //Name of constraint
    private final String name;

    // CONSTRUCTOR

    /**
     * Instantiates a constraint type.
     *
     * @param name The name of the constraint.
     */
    ConstraintType(String name) { this.name = name; }

    // GETTERS

    /**
     * Gets the name of the constraint.
     *
     * @return The name of the constraint.
     */
    public String getName() { return name; }
}
