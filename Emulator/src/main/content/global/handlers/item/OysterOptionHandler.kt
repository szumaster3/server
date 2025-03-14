package content.global.handlers.item

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items

@Initializable
class OysterOptionHandler : OptionHandler() {
    private val OYSTERS =
        arrayOf(
            Item(Items.EMPTY_OYSTER_409),
            Item(Items.OYSTER_PEARL_411),
            Item(Items.OYSTER_PEARLS_413),
        )

    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.OYSTER_407).handlers["option:open"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        player.inventory.replace(
            if (RandomFunction.random(5) < 3) {
                OYSTERS[RandomFunction.random(OYSTERS.size - 1)]
            } else {
                OYSTERS[
                    RandomFunction.random(
                        OYSTERS.size,
                    ),
                ]
            },
            (node as Item).slot,
        )
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}
