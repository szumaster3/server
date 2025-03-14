package core.game.system.mysql;

/**
 * The type Sql column.
 */
public final class SQLColumn {

    private final String name;

    private final Class<?> type;

    private final boolean neverUpdate;

    private Object value;

    private boolean changed;

    private boolean parse;

    /**
     * Instantiates a new Sql column.
     *
     * @param name the name
     * @param type the type
     */
    public SQLColumn(String name, Class<?> type) {
        this(name, type, false, true);
    }

    /**
     * Instantiates a new Sql column.
     *
     * @param name  the name
     * @param type  the type
     * @param parse the parse
     */
    public SQLColumn(String name, Class<?> type, boolean parse) {
        this(name, type, false, parse);
    }

    /**
     * Instantiates a new Sql column.
     *
     * @param name        the name
     * @param type        the type
     * @param neverUpdate the never update
     * @param parse       the parse
     */
    public SQLColumn(String name, Class<?> type, boolean neverUpdate, boolean parse) {
        this.name = name;
        this.type = type;
        this.neverUpdate = neverUpdate;
        this.parse = parse;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Update value.
     *
     * @param value the value
     */
    public void updateValue(Object value) {
        this.changed = value != this.value;
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
        this.value = value;
        this.changed = false;
    }

    /**
     * Is changed boolean.
     *
     * @return the boolean
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets changed.
     *
     * @param changed the changed
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Is never update boolean.
     *
     * @return the boolean
     */
    public boolean isNeverUpdate() {
        return neverUpdate;
    }

    /**
     * Is parse boolean.
     *
     * @return the boolean
     */
    public boolean isParse() {
        return parse;
    }

    /**
     * Sets parse.
     *
     * @param parse the parse
     */
    public void setParse(boolean parse) {
        this.parse = parse;
    }
}