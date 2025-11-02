package core.game.ge

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.node.entity.player.Player
import core.tools.SystemLogger
import shared.consts.Components
import java.text.NumberFormat
import java.util.*

/**
 * Handles the grand exchange interfaces for the player.
 *
 * @author Emperor, Angle
 */
class ExchangeHistory(private val player: Player? = null) : PersistPlayer, LoginListener {

    /*
     * Stores the player's history of completed
     * Grand Exchange offers (maximum 5 records).
     */
    var history = arrayOfNulls<GrandExchangeOffer>(5)

    /*
     * Stores the records of the player's Grand Exchange
     * offers, including both active and completed offers.
     */
    val offerRecords = arrayOfNulls<OfferRecord>(6)

    /**
     * Called when the player logs in.
     */
    override fun login(player: Player) {
        val instance = ExchangeHistory(player)
        player.setAttribute("ge-records", instance)
    }

    /**
     * Parses the player Grand Exchange history from a JSON obj.
     */
    override fun parsePlayer(player: Player, data: JsonObject) {
        val historyRaw = data.getAsJsonArray("ge-history")
        if (historyRaw != null) {
            for (i in 0 until historyRaw.size()) {
                val offer = historyRaw.get(i).asJsonObject
                val o = GrandExchangeOffer()
                o.itemID = offer.get("itemId").asInt
                o.sell = offer.get("isSell").asBoolean
                o.totalCoinExchange = offer.get("totalCoinExchange").asInt
                o.completedAmount = offer.get("completedAmount").asInt
                getInstance(player).history[i] = o
            }
        }

        val needsIndex = ArrayDeque<GrandExchangeOffer>()
        val instance = getInstance(player)

        GEDatabase.run { conn ->
            val stmt = conn.createStatement()
            val offerRecords = stmt.executeQuery("SELECT * from player_offers where player_uid = ${player.details.uid} AND offer_state < 6")

            while (offerRecords.next()) {
                val offer = GrandExchangeOffer.fromQuery(offerRecords)
                if (offer.index == -1) {
                    needsIndex.push(offer)
                } else {
                    instance.offerRecords[offer.index] = OfferRecord(offer.uid, offer.index)
                }
            }
            stmt.close()
        }

        if (needsIndex.isNotEmpty()) {
            for ((index, offer) in instance.offerRecords.withIndex()) {
                if (offer == null && needsIndex.isNotEmpty()) {
                    val o = needsIndex.pop()
                    o.index = index
                    instance.offerRecords[o.index] = OfferRecord(o.uid, o.index)
                    o.update()
                }
            }

            while (needsIndex.isNotEmpty()) {
                val o = needsIndex.pop()
                SystemLogger.logGE("[WARN] PLAYER HAD EXTRA OFFER - RECOMMEND IMMEDIATE REFUND OF CONTENTS -> OFFER UID: ${o.uid}")
                SystemLogger.logGE("[WARN] AS PER ABOVE MESSAGE, REFUND CONTENTS OF OFFER AND MANUALLY SET offer_state = 6")
            }
        }

        instance.init()
    }


    /**
     * Saves the player's Grand Exchange history to a JSON object.
     */
    override fun savePlayer(player: Player, save: JsonObject) {
        val history = JsonArray()
        getInstance(player).history.forEach { it ->
            if (it != null) {
                val historyEntry = JsonObject()
                historyEntry.addProperty("isSell", it.sell)
                historyEntry.addProperty("itemId", it.itemID)
                historyEntry.addProperty("totalCoinExchange", it.totalCoinExchange)
                historyEntry.addProperty("completedAmount", it.completedAmount)
                history.add(historyEntry)
            }
        }
        save.add("ge-history", history)
    }

    /**
     * Opens the collection box interface for the player.
     */
    fun openCollectionBox() {
        if (!player!!.bankPinManager.isUnlocked) {
            player.bankPinManager.openType(3)
            return
        }
        player.interfaceManager.openComponent(Components.STOCKCOLLECT_109)
        player.packetDispatch.sendIfaceSettings(6, 18, Components.STOCKCOLLECT_109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 23, Components.STOCKCOLLECT_109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 28, Components.STOCKCOLLECT_109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 36, Components.STOCKCOLLECT_109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 44, Components.STOCKCOLLECT_109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 52, Components.STOCKCOLLECT_109, 0, 2)

        visualizeRecords()
    }

    /**
     * Visualizes the player's Grand Exchange offer records by sending them to the client interface.
     */
    fun visualizeRecords() {
        GEDatabase.run { conn ->
            val stmt = conn.createStatement()

            for (record in offerRecords) {
                if (record != null) {
                    val offer_raw = stmt.executeQuery("SELECT * FROM player_offers WHERE uid = ${record.uid}")
                    if (offer_raw.next()) {
                        val offer = GrandExchangeOffer.fromQuery(offer_raw)
                        if (offer.offerState == OfferState.REMOVED) continue
                        offer.index = record.slot
                        offer.visualize(player)
                    }
                }
            }
            stmt.close()
        }
    }

    /**
     * Retrieves the Grand Exchange offer at a specific index.
     *
     * @param index The index of the offer to retrieve.
     * @return The Grand Exchange offer at the specified index, or null if no offer exists at that index.
     */
    fun getOffer(index: Int): GrandExchangeOffer? {
        if (index == -1) return getOffer(null)
        return getOffer(offerRecords[index])
    }

    /**
     * Retrieves the Grand Exchange offer for a specific record.
     *
     * @param record The offer record containing the offer UID and slot.
     * @return The Grand Exchange offer corresponding to the provided record, or null if not found.
     */
    fun getOffer(record: OfferRecord?): GrandExchangeOffer? {
        record ?: return null
        var offerToReturn: GrandExchangeOffer? = null
        GEDatabase.run { conn ->
            val stmt = conn.createStatement()
            val offer_raw = stmt.executeQuery("SELECT * FROM player_offers WHERE uid = ${record.uid}")
            if (offer_raw.next()) {
                val offer = GrandExchangeOffer.fromQuery(offer_raw)
                offer.index = record.slot
                stmt.close()
                offerToReturn = offer
            }
            stmt.close()
        }
        return offerToReturn
    }

    /**
     * Opens the history log.
     *
     * @param p The player to open it for.
     */
    fun openHistoryLog(p: Player) {
        p.interfaceManager.open(Component(643))
        for (i in history.indices) {
            val o: GrandExchangeOffer? = history[i]
            if (o == null) {
                for (j in 0..3) {
                    p.packetDispatch.sendString("-", Components.EXCHANGE_HISTORY_643, 25 + i + j * 5)
                }
                continue
            }
            sendString(player!!, if (o.sell) "You sold" else "You bought", Components.EXCHANGE_HISTORY_643, 25 + i)
            sendString(
                player,
                NumberFormat.getNumberInstance(Locale.US).format(o.completedAmount.toLong()),
                Components.EXCHANGE_HISTORY_643,
                30 + i
            )
            sendString(player, ItemDefinition.forId(o.itemID).name, Components.EXCHANGE_HISTORY_643, 35 + i)
            sendString(
                player,
                NumberFormat.getNumberInstance(Locale.US).format(o.totalCoinExchange.toLong()) + " gp",
                Components.EXCHANGE_HISTORY_643,
                40 + i
            )
        }
    }

    /**
     * Initializes the player's Grand Exchange offers, visualizing any updated offers.
     * If an offer has been updated, a message is sent to the player.
     */
    fun init() {
        var updated = false
        for (record in offerRecords) {
            if (record != null) {
                val offer = getOffer(record) ?: continue
                if (!updated && (offer.withdraw[0] != null || offer.withdraw[1] != null)) {
                    updated = true
                }
                offer.visualize(player)
            }
        }
        if (updated) {
            sendMessage(player!!, "One or more of your Grand Exchange offers have been updated.")
            playJingle(player, 284)
        }
    }

    /**
     * Checks if the player has any active Grand Exchange offers.
     *
     * @return True if the player has at least one active offer, otherwise false.
     */
    fun hasActiveOffer(): Boolean {
        for (i in offerRecords) {
            if (i != null) {
                return true
            }
        }
        return false
    }

    /**
     * Formats the grand exchange.
     *
     * @return the formatted offer for the SQL database.
     */
    fun format(): String {
        var log = ""
        for (record in offerRecords) {
            if (record != null) {
                val offer = getOffer(record) ?: continue
                log += offer.itemID.toString() + "," + offer.amount + "," + offer.sell + "|"
            }
        }
        if (log.isNotEmpty() && log[log.length - 1] == '|') {
            log = log.substring(0, log.length - 1)
        }
        return log
    }

    /**
     * Data class representing an individual offer record in the Grand Exchange.
     *
     * @param uid The unique identifier of the offer.
     * @param slot The slot of the offer in the player's record.
     */
    data class OfferRecord(
        val uid: Long,
        val slot: Int,
    )

    companion object {
        /**
         * Retrieves the instance of `ExchangeHistory` for a player.
         *
         * @param player The player whose instance of `ExchangeHistory` is being retrieved.
         * @return The `ExchangeHistory` instance associated with the player.
         */
        @JvmStatic
        fun getInstance(player: Player? = null): ExchangeHistory =
            player?.getAttribute("ge-records", ExchangeHistory()) ?: ExchangeHistory()
    }
}
