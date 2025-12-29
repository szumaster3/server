package custom.guild.warehouse.slot

import core.game.node.item.Item
import custom.guild.GuildRank
import java.util.concurrent.ConcurrentHashMap

/**
 * Defines minimal rank required to withdraw from a WareHouse (slot).
 */
enum class WarehouseSlotPermission(
    val minRank: GuildRank,
    val changeCost: Long
) {
    ANY(GuildRank.MEMBER, 50000),
    VETERAN(GuildRank.VETERAN, 100000),
    CAPTAIN(GuildRank.CAPTAIN, 250000),
    GUILDMASTER(GuildRank.GUILDMASTER, 500000),
    WITHDRAW_ONLY(GuildRank.MEMBER, 0); /*
                                                   * special slot: any member can deposit,
                                                   * withdrawals depend on rank.
                                                   */

    fun canWithdraw(rank: GuildRank): Boolean {
        return when (this) {
            WITHDRAW_ONLY -> rank.ordinal >= GuildRank.VETERAN.ordinal
            else -> rank.ordinal >= minRank.ordinal
        }
    }

    private val items: MutableMap<Int, Item> = ConcurrentHashMap()

    fun canDeposit(rank: GuildRank): Boolean = true

    fun addItemToSlot(item: Item, playerRank: GuildRank) {
        if (!canDeposit(playerRank)) throw IllegalAccessException("Cannot deposit item.")
        items[item.id] = item
    }

    fun withdrawItem(item: Item, playerRank: GuildRank): Item? {
        if (!canWithdraw(playerRank)) return null
        val stored = items[item.id] ?: return null
        items.remove(item.id)
        return stored
    }

    fun getItemCount(): Int = items.values.sumOf { it.amount }
}