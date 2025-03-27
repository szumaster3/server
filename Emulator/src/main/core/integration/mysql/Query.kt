package core.integration.mysql

/**
 * Represents an SQL query builder.
 */
class Query {
    private var start: String = ""
    private val content = StringBuilder()
    private var end: String = ""

    /**
     * Sets the starting part of the query.
     */
    fun start(start: String): String {
        this.start = start
        return this.start
    }

    /**
     * Sets the ending part of the query.
     */
    fun end(end: String): String {
        this.end = end
        return this.end
    }

    /**
     * Adds a key-value pair to the query.
     */
    fun add(key: String, value: String): StringBuilder {
        content.append("$key='$value',")
        return content
    }

    /**
     * Adds a key-int pair to the query.
     */
    fun add(key: String, value: Int): StringBuilder {
        content.append("$key='$value',")
        return content
    }

    /**
     * Adds a key-boolean pair to the query.
     */
    fun add(key: String, value: Boolean): StringBuilder {
        content.append("$key='${if (value) 1 else 0}',")
        return content
    }

    /**
     * Builds and returns the full query.
     */
    fun total(): String {
        return buildString {
            append(start)
            append(content.dropLast(1))
            append(end)
        }
    }
}
