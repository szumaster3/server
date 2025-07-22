package content.global.plugin.item

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items

class OysterOptionPlugin : InteractionListener {

    private val oysterIds =
        arrayOf(
            Item(Items.EMPTY_OYSTER_409),
            Item(Items.OYSTER_PEARL_411),
            Item(Items.OYSTER_PEARLS_413),
        )

    override fun defineListeners() {
        on(Items.OYSTER_407, IntType.ITEM, "open") { player, node ->
            player.inventory.replace(
                if (RandomFunction.random(5) < 3) {
                    oysterIds[RandomFunction.random(oysterIds.size - 1)]
                } else {
                    oysterIds[
                        RandomFunction.random(
                            oysterIds.size,
                        ),
                    ]
                },
                (node as Item).slot,
            )

            return@on true
        }
    }
}