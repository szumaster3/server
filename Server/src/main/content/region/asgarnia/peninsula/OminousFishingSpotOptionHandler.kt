package content.region.asgarnia.peninsula

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.*
import shared.consts.Scenery as Objects

val OMINOUS_FISHING_SPOTS = intArrayOf(
    Objects.OMINOUS_FISHING_SPOT_10087,
    Objects.OMINOUS_FISHING_SPOT_10088,
    Objects.OMINOUS_FISHING_SPOT_10089
)

/**
 * Represents the fishing explosive interaction.
 */
@Initializable
class OminousFishingSpotOptionHandler : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        OMINOUS_FISHING_SPOTS.forEach { id ->
            val def = SceneryDefinition.forId(id)
            def.handlers["option:lure"] = this
            def.handlers["option:bait"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        sendMessage(player, "Something seems to have scared all the fishes away...")
        return true
    }
}