package core.integration.mysql;

/**
 * The type Database.
 */
public class Database {
    private String host;
    private String name;
    private String username;
    private String password;

    /**
     * Instantiates a new Database.
     *
     * @param host     the host
     * @param name     the name
     * @param username the username
     * @param password the password
     */
    public Database(String host, String name, String username, String password) {
        this.host = host;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Host string.
     *
     * @return the string
     */
    public String host() {
        return host;
    }

    /**
     * Name string.
     *
     * @return the string
     */
    public String name() {
        return name;
    }

    /**
     * Username string.
     *
     * @return the string
     */
    public String username() {
        return username;
    }

    /**
     * Password string.
     *
     * @return the string
     */
    public String password() {
        return password;
    }
}
