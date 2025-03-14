package content.global.handlers.scenery

import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler.climb
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Scenery

@Initializable
class ModeratorOptionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.STAIRCASE_26806).handlers["option:climb-up"] = this
        SceneryDefinition.forId(Scenery.TABLE_26807).handlers["option:j-mod options"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (option) {
            "climb-up" -> climb(player, Animation(Animations.USE_LADDER_828), Location.create(3222, 3218, 0))
            "j-mod options" -> {
                if (player.details.rights == Rights.REGULAR_PLAYER) {
                    return true
                }
                sendMessage(player, "Disabled...")
            }
        }
        return true
    }
}
