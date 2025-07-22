package core.game.consumable

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item
import java.util.*

/**
 * Food that can be eaten in halves.
 */
class HalfableFood(
    ids: IntArray?,
    effect: ConsumableEffect?,
    vararg messages: String?,
) : Food(ids, effect, *messages) {

    /**
     * Sends messages specific to which half of the food is eaten.
     */
    override fun sendDefaultMessages(player: Player, item: Item) {
        val formattedName = getFormattedName(item)

        when (item.id) {
            ids[0] -> sendMessage(player, "You eat half of the $formattedName.")
            ids[1] -> sendMessage(player, "You eat the remaining $formattedName.")
            else -> super.sendDefaultMessages(player, item)
        }
    }

    /**
     * Formats the item name by removing half-related prefixes and trimming.
     */
    override fun getFormattedName(item: Item): String =
        item.name
            .replace(Regex("1/2|Half an|Half a"), "")
            .trim()
            .lowercase(Locale.getDefault())
}
