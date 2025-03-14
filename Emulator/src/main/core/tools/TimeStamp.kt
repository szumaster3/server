package core.tools

class TimeStamp {
    private var start: Long = System.currentTimeMillis()
    private var interval: Long = start

    init {
        start = System.currentTimeMillis()
        interval = start
    }

    fun interval(): Long {
        return interval(true, "")
    }

    fun interval(debug: Boolean): Long {
        return interval(debug, "")
    }

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
