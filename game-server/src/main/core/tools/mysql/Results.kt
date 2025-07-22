package core.tools.mysql

import java.sql.ResultSet
import java.sql.ResultSetMetaData

/**
 * Represents the results of an SQL query.
 */
class Results(
    private val set: ResultSet,
) {
    /**
     * Retrieves a column value as a String.
     */
    fun string(column: String): String? =
        try {
            set.getString(column)
        } catch (e: Exception) {
            null
        }

    /**
     * Retrieves a column value as an Int.
     */
    fun integer(column: String): Int =
        try {
            set.getInt(column)
        } catch (e: Exception) {
            -1
        }

    /**
     * Returns a list of column names in the result set.
     */
    fun columns(): List<String>? =
        try {
            val meta: ResultSetMetaData = set.metaData
            List(meta.columnCount) { i -> meta.getColumnName(i + 1) }
        } catch (e: Exception) {
            null
        }

    /**
     * Checks if the result set is empty.
     */
    fun empty(): Boolean =
        try {
            !set.next()
        } catch (e: Exception) {
            true
        }

    companion object {
        /**
         * Converts an array of strings to an array of integers.
         */
        fun integers(values: Array<String>): IntArray = values.map { it.toIntOrNull() ?: -1 }.toIntArray()

        /**
         * Converts an array of strings to an array of doubles.
         */
        fun doubles(values: Array<String>): DoubleArray = values.map { it.toDoubleOrNull() ?: -1.0 }.toDoubleArray()
    }

    /**
     * Returns the current result set.
     */
    fun set(): ResultSet = set
}
