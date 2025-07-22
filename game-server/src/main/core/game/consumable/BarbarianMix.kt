package core.game.consumable

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * Potion representing the Barbarian Mix.
 */
class BarbarianMix(
    ids: IntArray?,
    effect: ConsumableEffect?,
    vararg messages: String?,
) : Potion(ids, effect, *messages) {

    /**
     * Sends the default drinking message to the player.
     */
    override fun sendDefaultMessages(player: Player, item: Item) {
        sendMessage(player, "You drink the lumpy potion.")
    }
}
