package content.region.misthalin.handlers

import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs
import org.rs.consts.Scenery

@Initializable
class MonasteryPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.LADDER_2641).handlers["option:climb-up"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (option) {
            "climb-up" -> {
                when (node.id) {
                    Scenery.LADDER_2641 -> {
                        val option = "climb-up"
                        val abbot = node.location.equals(Location(3057, 3483, 0))
                        if (!player.getSavedData().globalData.isJoinedMonastery()) {
                            player.dialogueInterpreter.open(if (abbot) NPCs.ABBOT_LANGLEY_801 else NPCs.MONK_7727, true)
                        } else {
                            ClimbActionHandler.climbLadder(player, node.asScenery(), option)
                        }
                    }
                }
            }
        }
        return true
    }
}
