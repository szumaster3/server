package core.game.consumable

import content.data.consumables.Consumables
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * Consumable cake with multiple bites.
 */
class Cake(
    ids: IntArray?,
    effect: ConsumableEffect?,
    vararg messages: String?,
) : Food(ids, effect, *messages) {

    /**
     * Consumes the cake and applies effects.
     */
    override fun consume(item: Item, player: Player) {
        executeConsumptionActions(player)
        val nextId = getNextItemId(item.id)
        if (nextId != -1) player.inventory.replace(Item(nextId), item.slot)
        else player.inventory.remove(item)

        val initialHp = player.getSkills().lifepoints
        Consumables.getConsumableById(item.id)?.consumable?.effect?.activate(player)
        sendMessages(player, initialHp, item, messages)
    }

    /**
     * Sends messages after consumption.
     */
    override fun sendMessages(
        player: Player,
        initialLifePoints: Int,
        item: Item,
        messages: Array<String>
    ) {
        if (messages.isEmpty()) sendDefaultMessages(player, item)
        else sendCustomMessages(player, messages, item.id)
        sendHealingMessage(player, initialLifePoints)
    }

    /**
     * Sends a custom message based on item ID.
     */
    private fun sendCustomMessages(player: Player, messages: Array<String>, itemId: Int) {
        var i = 0
        while (ids[i] != itemId) i++
        sendMessage(player, messages[i])
    }
}
