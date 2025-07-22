package content.global.plugin.item

import content.data.tables.BirdNestDropTable
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Plugin

/**
 * Handles the "search" option for bird nest items.
 */
class BirdNestOptionPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any?>? {
        BirdNestDropTable.values().forEach { nest ->
            nest.nest.definition.handlers["option:search"] = this
        }
        return null
    }

    override fun handle(player: Player, node: Node, option: String, ): Boolean {
        val item = node as Item
        BirdNestDropTable.forNest(item)!!.search(player, item)
        return true
    }

    override fun isWalk(): Boolean = false
}
