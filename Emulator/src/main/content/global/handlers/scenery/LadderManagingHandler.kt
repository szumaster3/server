package content.global.handlers.scenery

import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class LadderManagingHandler : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("climb-up", this)
        SceneryDefinition.setOptionHandler("climb-down", this)
        SceneryDefinition.setOptionHandler("climb", this)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        ClimbActionHandler.climbLadder(player, node as Scenery, option)
        return true
    }

    override fun getDestination(
        n: Node,
        `object`: Node,
    ): Location? {
        return ClimbActionHandler.getDestination(`object` as Scenery)
    }
}
