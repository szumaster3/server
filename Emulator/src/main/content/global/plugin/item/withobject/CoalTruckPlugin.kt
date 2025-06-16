package content.global.plugin.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class CoalTruckPlugin : InteractionListener {

    companion object {
        private const val ATTRIBUTE_COAL_TRUCK_INVENTORY = "/save:coal-truck-inventory"
    }

    private fun calculateAvailableSpace(player: Player, coalInTruck: Int): Int =
        when {
            inEquipment(player, Items.SEERS_HEADBAND_3_14641) -> 196 - coalInTruck
            inEquipment(player, Items.SEERS_HEADBAND_2_14640) -> 168 - coalInTruck
            inEquipment(player, Items.SEERS_HEADBAND_1_14631) -> 140 - coalInTruck
            else -> 120 - coalInTruck
        }

    private fun calculateMaxCoalCapacity(player: Player): Int = when {
        inEquipment(player, Items.SEERS_HEADBAND_3_14641) -> 196
        inEquipment(player, Items.SEERS_HEADBAND_2_14640) -> 168
        inEquipment(player, Items.SEERS_HEADBAND_1_14631) -> 140
        else -> 120
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.COAL_453, Scenery.COAL_TRUCK_2114) { player, _, _ ->
            val coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            var coalInInventory = amountInInventory(player, Items.COAL_453)
            val maxCoalCapacity = calculateMaxCoalCapacity(player)

            if (coalInInventory + coalInTruck >= maxCoalCapacity) {
                coalInInventory = maxCoalCapacity - coalInTruck
                sendMessage(player, "You have filled up the coal truck.")
            }

            if (removeItem(player, Item(Items.COAL_453, coalInInventory))) {
                setAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, coalInTruck + coalInInventory)
            }
            return@onUseWith true
        }

        on(Scenery.COAL_TRUCK_2114, IntType.SCENERY, "remove-coal") { player, _ ->
            var coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            if (coalInTruck == 0) {
                sendDialogue(player, "The coal truck is empty.")
                return@on true
            }
            var toRemove = freeSlots(player)
            if (toRemove > coalInTruck) {
                toRemove = coalInTruck
            }
            if (addItem(player, Items.COAL_453, toRemove)) {
                coalInTruck -= toRemove
                setAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, coalInTruck)
            }
            return@on true
        }

        on(Scenery.COAL_TRUCK_2114, IntType.SCENERY, "investigate") { player, _ ->
            val coalInTruck = getAttribute(player, ATTRIBUTE_COAL_TRUCK_INVENTORY, 0)
            val availableSpace = calculateAvailableSpace(player, coalInTruck)
            sendDialogue(
                player,
                "There is currently $coalInTruck coal in the truck. The truck has space for $availableSpace more coal.",
            )
            return@on true
        }
    }
}
