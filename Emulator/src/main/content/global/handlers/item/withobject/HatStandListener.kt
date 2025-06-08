package content.global.handlers.item.withobject

import core.api.EquipmentSlot
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItemManager
import org.rs.consts.Items
import org.rs.consts.Scenery

class HatStandListener : InteractionListener {

    private val hats = ItemDefinition.definitions.values.filter { it.getConfiguration("equipment_slot",0) == EquipmentSlot.HEAD.ordinal }.map { it.id }.toIntArray()
    private val hatStand = Scenery.HAT_STAND_374

    override fun defineListeners() {

        /*
         * Handles basic interaction with hat stand.
         */

        onUseWith(IntType.SCENERY, hats, hatStand) { player, used, _ ->
            val def = ItemDefinition.forId(used.id)

            if (def.getConfiguration("equipment_slot", -1) != EquipmentSlot.HEAD.ordinal) {
                sendMessage(player, "You can't put that on a hatstand.")
                return@onUseWith true
            }

            if (used.id == Items.RAM_SKULL_HELM_7917) {
                sendMessage(player, "That is too valuable to use here. What if it fell off?")
                return@onUseWith true
            }

            if (def.options?.any { it.equals("Destroy", ignoreCase = true) } == true) {
                sendMessage(player, "It'll probably fall off if you do that.")
                return@onUseWith true
            }

            if(player.inventory.remove(used.asItem())) {
                GroundItemManager.create(used.asItem(), player.location, player)
                sendMessage(player, "Oops - it fell off the hatstand!")
            }

            return@onUseWith true
        }
    }
}
