package content.region.kandarin.handlers.guilds.fishing

import core.api.getDynLevel
import core.api.sendNPCDialogue
import core.api.withinDistance
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

@Initializable
class FishingGuildHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(2025).handlers["option:open"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (option) {
            "open" ->
                when (node.id) {
                    2025 -> {
                        if (getDynLevel(player, Skills.FISHING) < 68 &&
                            withinDistance(player, Location(2611, 3394, 0))
                        ) {
                            sendNPCDialogue(
                                player,
                                NPCs.MASTER_FISHER_308,
                                "Hello, I'm afraid only the top fishers are allowed to use our premier fishing facilities.",
                            )
                            return true
                        }
                        DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
                    }
                }
        }
        return true
    }
}
