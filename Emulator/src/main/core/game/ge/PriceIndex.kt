package core.game.ge

import core.api.item.itemDefinition
import java.sql.ResultSet

/**
 * Handles database operations for Grand Exchange item pricing.
 */
object PriceIndex {

    /**
     * Checks if an item is tradeable in the Grand Exchange.
     */
    @JvmStatic
    fun canTrade(id: Int): Boolean {
        var canTrade = false
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(EXISTS_QUERY)
            stmt.setInt(1, id)
            val res = stmt.executeQuery()
            if (res.next()) {
                canTrade = res.getInt(1) == 1
            }
        }
        return canTrade
    }

    /**
     * Bans an item from being traded.
     */
    @JvmStatic
    fun banItem(id: Int) {
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(REMOVE_QUERY)
            stmt.setInt(1, id)
            stmt.execute()
        }
    }

    /**
     * Allows an item to be traded if not already allowed.
     */
    @JvmStatic
    fun allowItem(id: Int) {
        if (canTrade(id)) return
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(INSERT_QUERY)
            stmt.setInt(1, id)
            val alch = itemDefinition(id).getAlchemyValue(true)
            stmt.setInt(2, alch)
            stmt.setInt(3, alch)
            stmt.setInt(4, 0)
            stmt.setInt(5, 0)
            stmt.execute()
        }
    }

    /**
     * Gets the current price of an item.
     */
    @JvmStatic
    fun getValue(id: Int): Int {
        var value = itemDefinition(id).getAlchemyValue(true)
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(GET_VALUE_QUERY)
            stmt.setInt(1, id)
            val res = stmt.executeQuery()
            if (res.next()) {
                value = res.getInt(1)
            }
        }
        return value
    }

    /**
     * Adds a trade record, updating volume and recalculating price averages.
     */
    @JvmStatic
    fun addTrade(id: Int, amount: Int, pricePerUnit: Int) {
        val oldInfo = getInfo(id) ?: return
        val newInfo = oldInfo.copy()
        val volumeResetThreshold = when {
            pricePerUnit >= 1_000_000 -> 500
            pricePerUnit >= 100_000 -> 1000
            pricePerUnit >= 25_000 -> 2500
            else -> 10_000
        }
        newInfo.totalValue += amount.toLong() * pricePerUnit
        newInfo.uniqueTrades += amount
        newInfo.lastUpdate = System.currentTimeMillis()
        newInfo.currentValue = (newInfo.totalValue / newInfo.uniqueTrades).toInt()
        if (newInfo.uniqueTrades > volumeResetThreshold) {
            val newAmt = (0.1 * volumeResetThreshold).toInt()
            newInfo.uniqueTrades = newAmt
            newInfo.totalValue = newAmt.toLong() * newInfo.currentValue
        }
        updateInfo(newInfo)
    }

    /**
     * Updates price info in the database.
     */
    @JvmStatic
    private fun updateInfo(newInfo: PriceInfo) {
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(UPDATE_QUERY)
            stmt.setInt(1, newInfo.currentValue)
            stmt.setLong(2, newInfo.totalValue)
            stmt.setInt(3, newInfo.uniqueTrades)
            stmt.setLong(4, newInfo.lastUpdate)
            stmt.setInt(5, newInfo.itemId)
            stmt.execute()
        }
    }

    /**
     * Gets price info from the database, or null if missing.
     */
    @JvmStatic
    private fun getInfo(id: Int): PriceInfo? {
        var priceInfo: PriceInfo? = null
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(SELECT_QUERY)
            stmt.setInt(1, id)
            val res = stmt.executeQuery()
            if (res.next()) {
                priceInfo = PriceInfo.fromQuery(res)
            }
        }
        return priceInfo
    }

    const val SELECT_QUERY = "SELECT * FROM price_index WHERE item_id = ?;"
    const val UPDATE_QUERY = "UPDATE price_index SET value = ?, total_value = ?, unique_trades = ?, last_update = ? WHERE item_id = ?;"
    const val EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM price_index WHERE item_id = ?);"
    const val REMOVE_QUERY = "DELETE FROM price_index WHERE item_id = ?;"
    const val INSERT_QUERY = "INSERT INTO price_index (item_id, value, total_value, unique_trades, last_update) VALUES (?,?,?,?,?);"
    const val GET_VALUE_QUERY = "SELECT value FROM price_index WHERE item_id = ?;"
}

/**
 * Data class representing price details for an item.
 *
 * @param itemId Item id.
 * @param currentValue Current price.
 * @param totalValue Sum of all trade values.
 * @param uniqueTrades Number of trades.
 * @param lastUpdate Timestamp of last update.
 */
data class PriceInfo(var itemId: Int, var currentValue: Int, var totalValue: Long, var uniqueTrades: Int, var lastUpdate: Long) {

    /**
     * Creates a copy of this PriceInfo.
     */
    fun copy(): PriceInfo = PriceInfo(itemId, currentValue, totalValue, uniqueTrades, lastUpdate)

    companion object {
        /**
         * Builds PriceInfo from a SQL result set.
         */
        fun fromQuery(result: ResultSet): PriceInfo = PriceInfo(
            result.getInt(1),
            result.getInt(2),
            result.getLong(3),
            result.getInt(4),
            result.getLong(5),
        )
    }
}
