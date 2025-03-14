package core.game.consumable

import core.game.node.entity.player.Player
import core.game.node.item.Item

class BarbarianMix(
    ids: IntArray?,
    effect: ConsumableEffect?,
    vararg messages: String?,
) : Potion(ids, effect, *messages) {
    override fun sendDefaultMessages(
        player: Player,
        item: Item,
    ) {
        player.packetDispatch.sendMessage("You drink the lumpy potion.")
    }
}
