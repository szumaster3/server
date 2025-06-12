package content.region.feldiphills.gutanoth.plugin

import content.global.skill.summoning.SummoningPouch
import content.global.skill.summoning.SummoningScroll
import core.api.getItemName
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
 * @author Ceikry
 */
object BogrogPouchUtils {
    private const val OP_VALUE = 0
    private const val OP_SWAP_1 = 1
    private const val OP_SWAP_5 = 2
    private const val OP_SWAP_10 = 3
    private const val OP_SWAP_X = 4
    private const val OP_SWAP_ALL = 5
    private const val EXAMINE = 6

    private const val SPIRIT_SHARD = Items.SPIRIT_SHARDS_12183

    fun handle(player: Player, optionIndex: Int, slot: Int): Boolean {
        val item = player.inventory.get(slot) ?: return false
        return when (optionIndex) {
            OP_VALUE -> sendValue(item.id, player)
            OP_SWAP_1 -> swap(player, 1, item.id)
            OP_SWAP_5 -> swap(player, 5, item.id)
            OP_SWAP_10 -> swap(player, 10, item.id)
            OP_SWAP_X -> true.also {
                sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                    swap(player, value as Int, item.id)
                }
            }

            OP_SWAP_ALL -> swap(player, item.amount, item.id)
            EXAMINE -> {
                item.definition?.let {
                    player.packetDispatch.sendMessage(it.examine)
                }
                false
            }

            else -> false
        }
    }

    /**
     * Swaps the given amount of summoning pouches for spirit shards.
     */
    private fun swap(player: Player, amount: Int, itemID: Int): Boolean {
        var amt = amount
        val value = getValue(itemID)
        if (value == 0.0) {
            sendMessage(player, "This item cannot be swapped here.")
            return false
        }
        val inInventory = player.inventory.getAmount(itemID)
        if (amt > inInventory) {
            amt = inInventory
        }
        if (amt == 0) {
            sendMessage(player, "You don't have any pouches to swap.")
            return false
        }

        if(amt > 30_000) {
            sendMessage(player, "You can't swap more than 30,000 at a time.")
            return false
        }

        player.inventory.remove(Item(itemID, amt))
        val shardsReceived = floor(value * amt).toInt()

        player.inventory.add(Item(SPIRIT_SHARD, shardsReceived))
        sendMessage(
            player,
            "You swapped $amt pouch${if (amt != 1) "es" else ""} and received $shardsReceived shard${if (shardsReceived != 1) "s" else ""}."
        )
        return true
    }

    /**
     * Show exchange values.
     */
    private fun sendValue(itemID: Int, player: Player, ): Boolean {
        val value = getValue(itemID)
        if (value == 0.0) {
            sendMessage(player, "This item cannot be swapped here.")
            return false
        }

        val count = floor(value).toInt()
        val amount = if (count == 1) "shard" else "shards"
        sendMessage(player, "You'll receive $count $amount for ${getItemName(itemID)}.")
        return true
    }

    /**
     * Calculates the shard value of a pouch item.
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