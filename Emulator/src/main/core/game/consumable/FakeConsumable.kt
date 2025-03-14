package core.game.consumable

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.item.Item

class FakeConsumable(
    id: Int,
    messages: Array<String?>,
) : Consumable(intArrayOf(id), null, *messages) {
    override fun consume(
        item: Item,
        player: Player,
    ) {
        sendDefaultMessages(player, item)
    }

    override fun sendDefaultMessages(
        player: Player,
        item: Item,
    ) {
        for (message in messages) {
            sendMessage(player, message)
        }
    }

    override fun executeConsumptionActions(player: Player) {
    }
}
