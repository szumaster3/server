package core.tools.mysql

/**
 * Represents a database connection configuration.
 */
class Database(
    private val host: String,
    private val name: String,
    private val username: String,
    private val password: String,
) {
    /**
     * Returns the database host.
     */
    fun host(): String = host

    /**
     * Returns the database name.
     */
    fun name(): String = name

    /**
     * Returns the database username.
     */
    fun username(): String = username

    /**
     * Returns the database password.
     */
    fun password(): String = password
}
