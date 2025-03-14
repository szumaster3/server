package core.game.system.mysql;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Sql table.
 */
public final class SQLTable {

    private final SQLColumn[] columns;

    /**
     * Instantiates a new Sql table.
     *
     * @param columns the columns
     */
    public SQLTable(SQLColumn... columns) {
        this.columns = columns;
    }

    /**
     * Gets column.
     *
     * @param name the name
     * @return the column
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
     * Gets changed.
     *
     * @return the changed
     */
    public List<SQLColumn> getChanged() {
        List<SQLColumn> updated = new ArrayList<>(20);
        for (int i = 0; i < columns.length; i++) {
            SQLColumn column = columns[i];
            if (column.isChanged()) {
                updated.add(column);
            }
        }
        return updated;
    }

    /**
     * Get columns sql column [ ].
     *
     * @return the sql column [ ]
     */
    public SQLColumn[] getColumns() {
        return columns;
    }

}