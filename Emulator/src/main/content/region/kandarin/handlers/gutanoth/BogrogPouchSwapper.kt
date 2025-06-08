package content.region.kandarin.handlers.gutanoth

import content.global.skill.summoning.SummoningPouch
import content.global.skill.summoning.SummoningScroll
import core.api.sendInputDialogue
import core.api.sendMessage
import core.game.dialogue.InputType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Utility object to handle swapping Summoning Pouches for Spirit Shards via Bogrog.
 */
object BogrogPouchSwapper {
    private const val OP_VALUE = 0
    private const val OP_SWAP_1 = 1
    private const val OP_SWAP_5 = 2
    private const val OP_SWAP_10 = 3
    private const val OP_SWAP_X = 4
    private const val SPIRIT_SHARD = Items.SPIRIT_SHARDS_12183

    /**
     * Processes a pouch swap or value check request based on the option selected.
     *
     * @param player the player.
     * @param optionIndex the option.
     * @param slot the inventory slot containing the pouch item.
     * @return `true` if the action was successful, `false` otherwise.
     */
    @JvmStatic
    fun handle(
        player: Player,
        optionIndex: Int,
        slot: Int,
    ): Boolean {
        val item = player.inventory.get(slot) ?: return false
        return when (optionIndex) {
            OP_VALUE -> sendValue(item.id, player)
            OP_SWAP_1 -> swap(player, 1, item.id)
            OP_SWAP_5 -> swap(player, 5, item.id)
            OP_SWAP_10 -> swap(player, 10, item.id)
            OP_SWAP_X ->
                true.also {
                    sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                        swap(player, value as Int, item.id)
                    }
                }

            else -> false
        }
    }

    /**
     * Swaps the given amount of summoning pouches for spirit shards.
     *
     * @param player the player.
     * @param amount the pouches amount to exchange.
     * @param itemID the pouch id to exchange.
     * @return `true` if the swap succeeded, `false` otherwise.
     */
    @JvmStatic
    private fun swap(
        player: Player,
        amount: Int,
        itemID: Int,
    ): Boolean {
        var amt = amount
        val value = getValue(itemID)
        if (value == 0.0) {
            return false
        }
        val inInventory = player.inventory.getAmount(itemID)
        if (amount > inInventory) {
            amt = inInventory
        }
        player.inventory.remove(Item(itemID, amt))
        player.inventory.add(Item(SPIRIT_SHARD, floor(value * amt).toInt()))
        return true
    }

    /**
     * Sends a message to the player showing the shard value of a pouch.
     *
     * @param itemID the id of the pouch item.
     * @param player the player to send the message to.
     * @return `true` if the value was successfully retrieved and message sent, `false` otherwise.
     */
    private fun sendValue(
        itemID: Int,
        player: Player,
    ): Boolean {
        val value = getValue(itemID)
        if (value == 0.0) {
            return false
        }

        sendMessage(player, "Bogrog will give you ${floor(value).toInt()} shards for that.")
        return true
    }

    /**
     * Calculates the shard value of a pouch item.
     *
     * @param itemID the item id.
     * @return the shard value as a Double; returns 0.0 if no valid pouch found
     */
    private fun getValue(itemID: Int): Double {
        var item = SummoningPouch.get(itemID)
        var isScroll = false
        if (item == null) item = SummoningPouch.get(Item(itemID).noteChange)
        if (item == null) {
            item = SummoningPouch.get(SummoningScroll.forItemId(itemID)?.pouch ?: -1).also { isScroll = true }
        }
        item ?: return 0.0
        var shardQuantity = item.items[item.items.size - 1].amount * 0.7
        return if (isScroll) shardQuantity / 20.0 else ceil(shardQuantity)
    }
}
