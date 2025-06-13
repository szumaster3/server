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
class BirdNestOptionHandler : OptionHandler() {
    /**
     * Registers this handler as the option handler for the "search" option on all bird nest items.
     *
     * @param arg Optional argument, ignored in this implementation.
     * @return Always returns null as this plugin does not need to be retained.
     */
    override fun newInstance(arg: Any?): Plugin<Any?>? {
        BirdNestDropTable.values().forEach { nest ->
            nest.nest.definition.handlers["option:search"] = this
        }
        return null
    }

    /**
     * Handles the "search" option on a bird nest item.
     */
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val item = node as Item
        BirdNestDropTable.forNest(item)!!.search(player, item)
        return true
    }

    /**
     * Indicates that this option handler does not require the player to walk to the item before handling.
     *
     * @return false indicating no walking is required.
     */
    override fun isWalk(): Boolean = false
}
