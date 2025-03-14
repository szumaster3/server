package content.region.kandarin.quest.regicide.handlers

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class RegicideListener : InteractionListener {
    private val FURNACES =
        intArrayOf(
            Scenery.FURNACE_4304,
            Scenery.FURNACE_6189,
            Scenery.FURNACE_11010,
            Scenery.FURNACE_11666,
            Scenery.FURNACE_12100,
            Scenery.FURNACE_12809,
            Scenery.FURNACE_18497,
            Scenery.FURNACE_26814,
            Scenery.FURNACE_30021,
            Scenery.FURNACE_30510,
            Scenery.FURNACE_36956,
            Scenery.FURNACE_37651,
        )

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.LIMESTONE_3211, *FURNACES) { player, used, _ ->

            val hasGloves = player.equipment.get(EquipmentContainer.SLOT_HANDS) != null
            if (!hasGloves) return@onUseWith true

            if (removeItem(player, used.asItem())) {
                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                submitIndividualPulse(
                    player,
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> sendMessage(player, "You put the lime stone in the furnace.")
                                3 -> {
                                    sendMessage(player, "You retrieve a block of quicklime.")
                                    addItem(player, Items.QUICKLIME_3213)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.QUICKLIME_3213, Items.PESTLE_AND_MORTAR_233) { player, used, _ ->
            if (!inInventory(player, Items.EMPTY_POT_1931)) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                replaceSlot(player, used.asItem().slot, Item(Items.POT_OF_QUICKLIME_3214))
                removeItem(player, Items.EMPTY_POT_1931)
            }
            return@onUseWith true
        }
    }
}
