package content.global.handlers.item

import content.data.tables.BirdNestDropTable
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player

class BirdNestScriptListener : InteractionListener {
    val nestIds = BirdNestDropTable.values().map { it.nest.id }.toIntArray()

    override fun defineListeners() {
        on(nestIds, IntType.ITEM, "search", handler = ::handleNest)
    }

    private fun handleNest(
        player: Player,
        node: Node,
    ): Boolean {
        val nest = BirdNestDropTable.forNest(node.asItem())
        nest!!.search(player, node.asItem())
        return true
    }
}
