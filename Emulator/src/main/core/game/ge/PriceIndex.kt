package core.game.ge

import core.api.item.itemDefinition
import java.sql.ResultSet

object PriceIndex {
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

    @JvmStatic
    fun banItem(id: Int) {
        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(REMOVE_QUERY)
            stmt.setInt(1, id)
            stmt.execute()
        }
    }

    @JvmStatic
    fun allowItem(id: Int) {
        if (canTrade(id)) return

        GEDatabase.run { conn ->
            val stmt = conn.prepareStatement(INSERT_QUERY)
            stmt.setInt(1, id)
            stmt.setInt(2, itemDefinition(id).getAlchemyValue(true))
            stmt.setInt(3, itemDefinition(id).getAlchemyValue(true))
            stmt.setInt(4, 0)
            stmt.setInt(5, 0)
            stmt.execute()
        }
    }

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

    @JvmStatic
    fun addTrade(
        id: Int,
        amount: Int,
        pricePerUnit: Int,
    ) {
        val oldInfo = getInfo(id) ?: return
        val newInfo = oldInfo.copy()

        val volumeResetThreshold =
            if (pricePerUnit >= 1000000) {
                500
            } else if (pricePerUnit >= 100000) {
                1000
            } else if (pricePerUnit >= 25000) {
                2500
            } else {
                10000
            }

        // Update total value, unique trades, and current value
        newInfo.totalValue += (amount * pricePerUnit)
        newInfo.uniqueTrades += amount
        newInfo.lastUpdate = System.currentTimeMillis()
        newInfo.currentValue = (newInfo.totalValue / newInfo.uniqueTrades).toInt()

        if (newInfo.uniqueTrades > volumeResetThreshold) {
            val newAmt = (0.1 * volumeResetThreshold).toInt()
            newInfo.uniqueTrades = newAmt
            newInfo.totalValue = newAmt.toLong() * newInfo.currentValue
        }

        // Update the price info in the database
        updateInfo(newInfo)
    }

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
    const val INSERT_QUERY =
        "INSERT INTO price_index (item_id, value, total_value, unique_trades, last_update) VALUES (?,?,?,?,?);"
    const val GET_VALUE_QUERY = "SELECT value FROM price_index WHERE item_id = ?;"
}

data class PriceInfo(
    var itemId: Int,
    var currentValue: Int,
    var totalValue: Long,
    var uniqueTrades: Int,
    var lastUpdate: Long,
) {
    fun copy(): PriceInfo {
        return PriceInfo(itemId, currentValue, totalValue, uniqueTrades, lastUpdate)
    }

    companion object {
        fun fromQuery(result: ResultSet): PriceInfo {
            return PriceInfo(
                result.getInt(1), // item_id
                result.getInt(2), // value
                result.getLong(3), // total_value
                result.getInt(4), // unique_trades
                result.getLong(5), // last_update
            )
        }
    }
}
