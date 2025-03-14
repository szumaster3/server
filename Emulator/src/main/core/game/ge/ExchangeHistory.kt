package core.game.ge

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.node.entity.player.Player
import core.tools.SystemLogger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs.consts.Components
import java.text.NumberFormat
import java.util.*

class ExchangeHistory(
    private val player: Player? = null,
) : PersistPlayer,
    LoginListener {
    var history = arrayOfNulls<GrandExchangeOffer>(5)
    val offerRecords = arrayOfNulls<OfferRecord>(6)

    override fun login(player: Player) {
        val instance = ExchangeHistory(player)
        player.setAttribute("ge-records", instance)
    }

    override fun parsePlayer(
        player: Player,
        data: JSONObject,
    ) {
        val historyRaw = data["ge-history"]
        if (historyRaw != null) {
            val history = historyRaw as JSONArray
            for (i in history.indices) {
                val offer = history[i] as JSONObject
                val o = GrandExchangeOffer()
                o.itemID = offer["itemId"].toString().toInt()
                o.sell = offer["isSell"] as Boolean
                o.totalCoinExchange = (offer["totalCoinExchange"].toString().toInt())
                o.completedAmount = (offer["completedAmount"].toString().toInt())
                getInstance(player).history[i] = o
            }
        }

        val needsIndex = ArrayDeque<GrandExchangeOffer>()
        val instance = getInstance(player)

        GEDatabase.run { conn ->
            val stmt = conn.createStatement()
            val offer_records =
                stmt.executeQuery(
                    "SELECT * from player_offers where player_uid = ${player.details.uid} AND offer_state < 6",
                )

            while (offer_records.next()) {
                val offer = GrandExchangeOffer.fromQuery(offer_records)
                if (offer.index == -1) {
                    needsIndex.push(offer)
                } else {
                    instance.offerRecords[offer.index] = OfferRecord(offer.uid, offer.index)
                }
            }
            stmt.close()
        }

        if (needsIndex.isNotEmpty()) {
            for ((index, offer) in offerRecords.withIndex()) {
                if (offer == null) {
                    val o = needsIndex.pop()
                    o.index = index
                    instance.offerRecords[o.index] = OfferRecord(o.uid, o.index)
                    o.update()
                }
            }

            while (needsIndex.isNotEmpty()) {
                val o = needsIndex.pop()
                SystemLogger.logGE(
                    "[WARN] PLAYER HAD EXTRA OFFER - RECOMMEND IMMEDIATE REFUND OF CONTENTS -> OFFER UID: ${o.uid}",
                )
                SystemLogger.logGE(
                    "[WARN] AS PER ABOVE MESSAGE, REFUND CONTENTS OF OFFER AND MANUALLY SET offer_state = 6",
                )
            }
        }

        instance.init()
    }

    override fun savePlayer(
        player: Player,
        save: JSONObject,
    ) {
        val history = JSONArray()
        getInstance(player).history.map {
            if (it != null) {
                val historyEntry = JSONObject()
                historyEntry["isSell"] = it.sell
                historyEntry["itemId"] = it.itemID.toString()
                historyEntry["totalCoinExchange"] = it.totalCoinExchange.toString()
                historyEntry["completedAmount"] = it.completedAmount.toString()
                history.add(historyEntry)
            }
        }
        save["ge-history"] = history
    }

    fun openCollectionBox() {
        if (!player!!.bankPinManager.isUnlocked) {
            player.bankPinManager.openType(3)
            return
        }
        player.interfaceManager.openComponent(Components.STOCKCOLLECT_109)
        player.packetDispatch.sendIfaceSettings(6, 18, 109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 23, 109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 28, 109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 36, 109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 44, 109, 0, 2)
        player.packetDispatch.sendIfaceSettings(6, 52, 109, 0, 2)

        visualizeRecords()
    }

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

    fun getOffer(index: Int): GrandExchangeOffer? {
        if (index == -1) return getOffer(null)
        return getOffer(offerRecords[index])
    }

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

    fun openHistoryLog(p: Player) {
        p.interfaceManager.open(Component(643))
        for (i in history.indices) {
            val o: GrandExchangeOffer? = history[i]
            if (o == null) {
                for (j in 0..3) {
                    p.packetDispatch.sendString("-", 643, 25 + i + j * 5)
                }
                continue
            }
            sendString(player!!, if (o.sell) "You sold" else "You bought", 643, 25 + i)
            sendString(
                player,
                NumberFormat.getNumberInstance(Locale.US).format(o.completedAmount.toLong()),
                643,
                30 + i,
            )
            sendString(player, ItemDefinition.forId(o.itemID).name, 643, 35 + i)
            sendString(
                player,
                NumberFormat.getNumberInstance(Locale.US).format(o.totalCoinExchange.toLong()) + " gp",
                643,
                40 + i,
            )
        }
    }

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

    fun hasActiveOffer(): Boolean {
        for (i in offerRecords) {
            if (i != null) {
                return true
            }
        }
        return false
    }

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

    data class OfferRecord(
        val uid: Long,
        val slot: Int,
    )

    companion object {
        @JvmStatic
        fun getInstance(player: Player? = null): ExchangeHistory {
            return player?.getAttribute("ge-records", ExchangeHistory()) ?: ExchangeHistory()
        }
    }
}
