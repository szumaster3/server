package core.game.ge

import core.ServerConstants
import core.api.getAttribute
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.repository.Repository
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.context.GrandExchangeContext
import core.net.packet.out.ContainerPacket
import core.net.packet.out.GrandExchangePacket
import java.sql.ResultSet
import kotlin.math.min

// import core.services.discord.Discord

class GrandExchangeOffer {
    var itemID = 0
    var completedAmount = 0
    var offeredValue = 0
    var index = 0
    var sell = false
    var offerState = OfferState.PENDING
    var uid: Long = 0
    var timeStamp: Long = 0
    var withdraw = arrayOfNulls<Item>(2)
    var totalCoinExchange = 0
    var player: Player? = null
    var playerUID = 0
    var tradeRestriction = false
    var isBot = false
    var amount: Int = 0
        get() = if (isBot) min(field, ServerConstants.BOTSTOCK_LIMIT) else field

    val totalValue: Int
        get() = offeredValue * amount

    val amountLeft: Int
        get() = if (isBot) min(ServerConstants.BOTSTOCK_LIMIT, amount - completedAmount) else amount - completedAmount

    val isActive: Boolean
        get() =
            offerState != OfferState.ABORTED &&
                offerState != OfferState.PENDING &&
                offerState != OfferState.COMPLETED &&
                offerState != OfferState.REMOVED

    fun addWithdrawItem(
        id: Int,
        amount: Int,
    ) {
        if (amount == 0) return
        for (item in withdraw) {
            if (item != null && item.id == id) {
                item.amount += amount
                return
            }
        }

        for ((index, item) in withdraw.withIndex()) {
            if (item == null) {
                withdraw[index] = Item(id, amount)
                return
            }
        }

        if (player != null) {
            visualize(player)
        }
    }

    fun visualize(player: Player?) {
        player ?: return
        PacketRepository.send(
            GrandExchangePacket::class.java,
            GrandExchangeContext(
                player,
                index.toByte(),
                offerState.ordinal.toByte(),
                itemID.toShort(),
                sell,
                offeredValue,
                amount,
                completedAmount,
                totalCoinExchange,
            ),
        )
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, -1, -1757, 523 + index, withdraw, false),
        )
    }

    fun update() {
        GEDatabase.run { conn ->
            if (isBot) {
                val stmt = conn.prepareStatement("UPDATE bot_offers SET amount = ? WHERE item_id = ?")
                stmt.setInt(1, amountLeft)
                stmt.setInt(2, itemID)
                stmt.executeUpdate()
                stmt.close()
            } else {
                val stmt =
                    conn.prepareStatement(
                        "UPDATE player_offers SET amount_complete = ?, offer_state = ?, total_coin_xc = ?, withdraw_items = ?, slot_index = ? WHERE uid = ?",
                    )
                stmt.setInt(1, completedAmount)
                stmt.setInt(2, offerState.ordinal)
                stmt.setInt(3, totalCoinExchange)
                stmt.setString(4, encodeWithdraw())
                stmt.setInt(5, index)
                stmt.setLong(6, uid)
                stmt.executeUpdate()
                stmt.close()
            }
        }
    }

    fun writeNew() {
        GEDatabase.run { conn ->
            if (isBot) {
                val stmt = conn.createStatement()
                val result = stmt.executeQuery("SELECT * from bot_offers where item_id = $itemID")
                val isExists = result.next()

                if (isExists) {
                    val oldAmount = result.getInt("amount")
                    stmt.executeUpdate("UPDATE bot_offers set amount = ${oldAmount + amount} where item_id = $itemID")
                } else {
                    stmt.executeUpdate("INSERT INTO bot_offers(item_id,amount) values($itemID,$amount)")
                }
                stmt.close()
            } else {
                val stmt = conn.createStatement()
                stmt.executeUpdate(
                    "INSERT INTO player_offers(player_uid, item_id, amount_total, offered_value, time_stamp, offer_state, is_sale, slot_index) " +
                        "values($playerUID,$itemID,$amount,$offeredValue,${System.currentTimeMillis()},${offerState.ordinal},${if (sell) 1 else 0}, $index)",
                )
                val nowuid = stmt.executeQuery("SELECT last_insert_rowid()")
                uid = nowuid.getLong(1)
                ExchangeHistory.getInstance(player).offerRecords[index] =
                    ExchangeHistory.OfferRecord(uid, index)
                visualize(player)
                stmt.close()

                val username =
                    if (getAttribute(player ?: return@run, "ge-exclude", false)) {
                        "?????"
                    } else {
                        player?.username
                            ?: "?????"
                    }

                // Discord.postNewOffer(sell, itemID, offeredValue, amount, username)
            }
        }
    }

    private fun encodeWithdraw(): String {
        val sb = StringBuilder()
        for ((index, item) in withdraw.withIndex()) {
            sb.append(index)
            sb.append(",")
            if (item == null) {
                sb.append("null")
            } else {
                sb.append(item.id)
            }
            sb.append(",")
            if (item == null) {
                sb.append("null")
            } else {
                sb.append(item.amount)
            }

            if (index + 1 < withdraw.size) {
                sb.append(":")
            }
        }

        return sb.toString()
    }

    override fun toString(): String {
        return "[name=" + ItemDefinition.forId(itemID).name + ", itemId=" + itemID + ", amount=" + amount +
            ", completedAmount=" +
            completedAmount +
            ", offeredValue=" +
            offeredValue +
            ", index=" +
            index +
            ", sell=" +
            sell +
            ", state=" +
            offerState +
            ", withdraw=" +
            withdraw.contentToString() +
            ", totalCoinExchange=" +
            totalCoinExchange +
            ", playerUID=" +
            playerUID +
            "]"
    }

    companion object {
        fun fromQuery(result: ResultSet): GrandExchangeOffer {
            val o = GrandExchangeOffer()
            o.itemID = result.getInt("item_id")
            o.amount = result.getInt("amount_total")
            o.completedAmount = result.getInt("amount_complete")
            o.offeredValue = result.getInt("offered_value")
            o.sell = result.getInt("is_sale") == 1
            o.offerState = OfferState.values()[result.getInt("offer_state")]
            o.uid = result.getLong("uid")
            o.timeStamp = result.getLong("time_stamp")

            val itemString = result.getString("withdraw_items")
            if (itemString != null && itemString.isNotEmpty()) {
                val items = itemString.split(":")
                for (item in items) {
                    if (item.isEmpty()) continue
                    val tokens = item.split(",")
                    val index = tokens[0].toInt()
                    if (tokens[1] == "null") continue
                    o.withdraw[index] = Item(tokens[1].toInt(), tokens[2].toInt())
                }
            }

            o.totalCoinExchange = result.getInt("total_coin_xc")
            o.playerUID = result.getInt("player_uid")
            o.index = result.getInt("slot_index")
            o.player = Repository.players.firstOrNull { it.details.accountInfo.uid == o.playerUID }

            return o
        }

        fun fromBotQuery(result: ResultSet): GrandExchangeOffer {
            val o = GrandExchangeOffer()
            o.sell = true
            o.amount = result.getInt("amount")
            o.offerState = OfferState.REGISTERED
            o.itemID = result.getInt("item_id")
            o.offeredValue = GrandExchange.getRecommendedPrice(o.itemID, true)
            o.isBot = true
            o.timeStamp = System.currentTimeMillis()
            return o
        }

        fun createBotOffer(
            itemId: Int,
            amount: Int,
            sale: Boolean = true,
        ): GrandExchangeOffer {
            val o = GrandExchangeOffer()
            o.sell = sale
            o.itemID = itemId
            o.amount = amount
            o.offeredValue = GrandExchange.getRecommendedPrice(itemId, true)
            o.offerState = OfferState.REGISTERED
            o.isBot = true
            return o
        }
    }

    fun cacheValue(): Int {
        var value = 0
        if (sell) {
            value += ItemDefinition.forId(itemID).value * amountLeft
        } else {
            value += offeredValue * amountLeft
        }
        for (item in withdraw) {
            if (item != null) {
                value += item.definition.value * item.amount
            }
        }
        return value
    }
}
