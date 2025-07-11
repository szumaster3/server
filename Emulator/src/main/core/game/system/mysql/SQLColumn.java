package core.game.system.mysql;

/**
 * Represents a SQL column and its associated metadata.
 */
public final class SQLColumn {

    private final String name;
    private final Class<?> type;
    private final boolean neverUpdate;
    private Object value;
    private boolean changed;
    private boolean parse;

    /**
     * Creates a SQL column.
     *
     * @param name Column name.
     * @param type Column data type.
     */
    public SQLColumn(String name, Class<?> type) {
        this(name, type, false, true);
    }

    /**
     * Creates a SQL column.
     *
     * @param name  Column name.
     * @param type  Column data type.
     * @param parse Whether to parse the value.
     */
    public SQLColumn(String name, Class<?> type, boolean parse) {
        this(name, type, false, parse);
    }

    /**
     * Creates a SQL column.
     *
     * @param name        Column name.
     * @param type        Column data type.
     * @param neverUpdate Whether the column should be excluded from updates.
     * @param parse       Whether to parse the value.
     */
    public SQLColumn(String name, Class<?> type, boolean neverUpdate, boolean parse) {
        this.name = name;
        this.type = type;
        this.neverUpdate = neverUpdate;
        this.parse = parse;
    }

    /** @return Column name. */
    public String getName() {
        return name;
    }

    /**
     * Updates the value and sets changed if different.
     *
     * @param value New value.
     */
    public void updateValue(Object value) {
        this.changed = value != this.value;
        this.value = value;
    }

    /** @return Current value. */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value without marking it as changed.
     *
     * @param value Value to set.
     */
    public void setValue(Object value) {
        this.value = value;
        this.changed = false;
    }

    /** @return Whether the value has changed. */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the changed flag.
     *
     * @param changed Whether the value is changed.
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /** @return Column data type. */
    public Class<?> getType() {
        return type;
    }

    /** @return True if excluded from updates. */
    public boolean isNeverUpdate() {
        return neverUpdate;
    }

    /** @return Whether parsing is enabled. */
    public boolean isParse() {
        return parse;
    }

    /**
     * Sets the parse flag.
     *
     * @param parse Whether to parse.
     */
    public void setParse(boolean parse) {
        this.parse = parse;
    }
}