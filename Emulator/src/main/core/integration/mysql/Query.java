package core.integration.mysql;

/**
 * The type Query.
 */
public class Query {
    private String start;
    private StringBuilder content;
    private String end;

    /**
     * Instantiates a new Query.
     */
    public Query() {
        this.start = "";
        this.content = new StringBuilder();
        this.end = "";
    }

    /**
     * Start string.
     *
     * @param start the start
     * @return the string
     */
    public String start(String start) {
        this.start = start;
        return this.start;
    }

    /**
     * End string.
     *
     * @param end the end
     * @return the string
     */
    public String end(String end) {
        this.end = end;
        return this.end;
    }

    /**
     * Add string builder.
     *
     * @param key   the key
     * @param value the value
     * @return the string builder
     */
    public StringBuilder add(String key, String value) {
        this.content.append(key + "='" + value + "'").append(",");
        return this.content;
    }

    /**
     * Add string builder.
     *
     * @param key   the key
     * @param value the value
     * @return the string builder
     */
    public StringBuilder add(String key, int value) {
        this.content.append(key + "='" + value + "'").append(",");
        return this.content;
    }

    /**
     * Add string builder.
     *
     * @param key   the key
     * @param value the value
     * @return the string builder
     */
    public StringBuilder add(String key, boolean value) {
        this.content.append(key + "='" + (value ? 1 : 0) + "'").append(",");
        return this.content;
    }

    /**
     * Total string.
     *
     * @return the string
     */
    public String total() {
        StringBuilder total = new StringBuilder();
        total.append(this.start);
        total.append(this.content.substring(0, this.content.length() - 1));
        total.append(this.end);
        return total.toString();
    }
}
