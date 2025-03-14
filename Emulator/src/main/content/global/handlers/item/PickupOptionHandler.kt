package content.global.handlers.item

import core.api.removeAttribute
import core.cache.def.impl.ItemDefinition
import core.game.global.action.PickupHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class PickupOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.setOptionHandler("take", this)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        if (player.attributes.containsKey("pickup")) return false

        val handleResult = PickupHandler.take(player, (node as GroundItem))
        removeAttribute(player, "pickup")
        return handleResult
    }

    override fun getDestination(
        node: Node,
        item: Node,
    ): Location? {
        return null
    }
}
