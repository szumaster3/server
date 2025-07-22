package core.tools

/**
 * Used for debugging duration of executing code.
 * @author Emperor
 */
class TimeStamp {
    private var start: Long = System.currentTimeMillis()
    private var interval: Long = start

    init {
        start = System.currentTimeMillis()
        interval = start
    }

    /**
     * Set current interval.
     * @return The duration of this interval.
     */
    fun interval(): Long = interval(true, "")

    /**
     * Set current interval.
     * @param debug If we should print out the duration.
     * @return The duration of this interval.
     */
    fun interval(debug: Boolean): Long = interval(debug, "")

    /**
     * Set current interval.
     * @param debug If we should print out the duration.
     * @param info Extra information.
     * @return The duration of this interval.
     */
    fun interval(
        debug: Boolean,
        info: String,
    ): Long {
        val current = System.currentTimeMillis()
        val difference = current - interval
        if (debug || difference > 100) {
            // Perform debug operations if debug is true or difference is greater than 100 milliseconds
        }
        interval = current
        return difference
    }

    /**
     * Gets the amount of milliseconds passed since the creation of this object.
     * @return The duration.
     */
    fun duration(
        debug: Boolean,
        info: String,
    ): Long {
        val current = System.currentTimeMillis()
        val difference = current - start
        if (debug) {
        }
        return difference
    }
}
