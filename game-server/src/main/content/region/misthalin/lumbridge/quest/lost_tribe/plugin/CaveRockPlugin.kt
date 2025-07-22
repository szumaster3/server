package content.region.misthalin.lumbridge.quest.lost_tribe.plugin

import core.api.openInterface
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Scenery

@Initializable
class CaveRockPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (i in Scenery.SYMBOL_6921..Scenery.SYMBOL_6924) {
            SceneryDefinition.forId(i).handlers["option:look-at"] = this
        }
        return this
    }

    override fun handle(
        player: Player?,
        node: Node?,
        option: String?,
    ): Boolean {
        player ?: return false
        when (node?.id) {
            Scenery.SYMBOL_6921 -> showRock(player, Scenery.SYMBOL_6923)
            Scenery.SYMBOL_6922 -> showRock(player, Scenery.SYMBOL_6924)
            Scenery.SYMBOL_6923 -> showRock(player, Scenery.SYMBOL_6922)
            Scenery.SYMBOL_6924 -> showRock(player, Scenery.BIG_BIG_BOULDER_6927)
        }
        return true
    }

    fun showRock(
        player: Player,
        model: Int,
    ) {
        openInterface(player, Components.CAVE_GOBLIN_MARKERS_62)
        player.packetDispatch.sendModelOnInterface(model, 62, 1, 1)
    }

    override fun getDestination(
        n: Node?,
        node: Node?,
    ): Location {
        n ?: return super.getDestination(n, node)
        node ?: return super.getDestination(n, node)

        var diffX = 0
        var diffY = 0

        if (node.direction == Direction.SOUTH) {
            diffX = -1
        }
        if (node.direction == Direction.NORTH) {
            diffX = 1
        }
        if (node.direction == Direction.WEST) {
            diffY = 1
        }
        if (node.direction == Direction.EAST) {
            diffY = -1
        }

        return node.location.transform(diffX, diffY, 0)
    }
}
