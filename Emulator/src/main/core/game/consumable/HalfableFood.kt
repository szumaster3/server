package core.game.consumable

import core.game.node.entity.player.Player
import core.game.node.item.Item
import java.util.*

class HalfableFood(
    ids: IntArray?,
    effect: ConsumableEffect?,
    vararg messages: String?,
) : Food(ids, effect, *messages) {
    override fun sendDefaultMessages(
        player: Player,
        item: Item,
    ) {
        val formattedName = getFormattedName(item)

        when (item.id) {
            ids[0] -> player.packetDispatch.sendMessage("You eat half of the $formattedName.")
            ids[1] -> player.packetDispatch.sendMessage("You eat the remaining $formattedName.")
            else -> super.sendDefaultMessages(player, item)
        }
    }

    override fun getFormattedName(item: Item): String {
        return item.name
            .replace(Regex("1/2|Half an|Half a"), "")
            .trim()
            .lowercase(Locale.getDefault())
    }
}
