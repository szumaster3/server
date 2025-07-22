package core.game.consumable

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * Represents a fake consumable that only sends messages without applying effects.
 *
 * @param id The item id of the fake consumable.
 * @param messages Messages to send when consumed.
 */
class FakeConsumable(
    id: Int,
    vararg messages: String?,
) : Consumable(intArrayOf(id), null, *messages) {

    /**
     * Sends consumption messages without any effect.
     */
    override fun consume(item: Item, player: Player) {
        sendDefaultMessages(player, item)
    }

    /**
     * Sends all provided messages to the player.
     */
    override fun sendDefaultMessages(player: Player, item: Item) {
        for (message in messages) {
            sendMessage(player, message)
        }
    }

    override fun executeConsumptionActions(player: Player) { }
}
