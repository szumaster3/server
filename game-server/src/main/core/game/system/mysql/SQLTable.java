package core.game.system.mysql;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a SQL table definition.
 */
public final class SQLTable {

    private final SQLColumn[] columns;

    /**
     * Creates a new SQLTable.
     *
     * @param columns The table columns.
     */
    public SQLTable(SQLColumn... columns) {
        this.columns = columns;
    }

    /**
     * Returns the column with the given name.
     *
     * @param name The column name.
     * @return The matching column, or null if not found.
     */
    public SQLColumn getColumn(String name) {
        for (SQLColumn column : columns) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Returns all columns marked as changed.
     *
     * @return List of changed columns.
     */
    public List<SQLColumn> getChanged() {
        List<SQLColumn> updated = new ArrayList<>(20);
        for (SQLColumn column : columns) {
            if (column.isChanged()) {
                updated.add(column);
            }
        }
        return updated;
    }

    /**
     * Returns all columns in the table.
     *
     * @return Array of table columns.
     */
    public SQLColumn[] getColumns() {
        return columns;
    }
}
