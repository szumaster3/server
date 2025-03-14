package content.global.skill.firemaking

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class FiremakingHandler : OptionHandler() {
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        submitIndividualPulse(
            player,
            FiremakingPulse(
                player,
                (node as Item),
                (node as GroundItem),
            ),
        )
        return true
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.setOptionHandler("light", this)
        return this
    }

    override fun handleSelectionCallback(
        skill: Int,
        player: Player,
    ) {
        super.handleSelectionCallback(skill, player)
    }
}
