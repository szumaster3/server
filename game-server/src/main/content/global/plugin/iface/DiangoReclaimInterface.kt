package content.global.plugin.iface

import content.global.skill.construction.decoration.costumeroom.Storable
import core.api.openInterface
import core.api.sendMessage
import core.api.setAttribute
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components

/**
 * Represents interface plugin for the Diango Reclaimable.
 * @author Ceikry
 */
@Initializable
class DiangoReclaimInterface : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(COMPONENT_ID, this)
        ITEMS.addAll(getEligibleItemsList(Storable.Type.TOY))
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        var reclaimable = player.getAttribute<Array<Item?>>("diango-reclaimables", null)
        if (reclaimable == null) reclaimable = getEligibleItems(player)

        val reclaimItem = reclaimable?.getOrNull(slot)
        if (reclaimItem == null) {
            sendMessage(player, "Something went wrong there. Please try again.")
            return true
        }

        when (opcode) {
            155 -> {
                player.inventory.add(reclaimItem)
                refresh(player)
            }

            9 -> sendMessage(player, reclaimItem.definition.examine)
        }
        return false
    }

    companion object {
        private const val COMPONENT_ID = Components.DIANGO_RECLAIMABLE_468
        private val ITEMS: MutableList<Item> = mutableListOf()

        /**
         * Returns a list of eligible items that can be reclaimed.
         *
         * @return List of eligible reclaimable items.
         */
        private fun getEligibleItemsList(type: Storable.Type): List<Item> {
            return Storable.values()
                .filter { it.type == Storable.Type.TOY && it.takeIds.isNotEmpty() }
                .map { Item(it.takeIds.first()) }
        }

        /**
         * Opens the reclaim interface for the specified player.
         * Sets up the reclaimable items and displays the interface.
         *
         * @param player The player opening the interface.
         */
        @JvmStatic
        fun open(player: Player) {
            val curOpen = player.interfaceManager.opened
            curOpen?.close(player)
            val reclaimable = getEligibleItems(player)
            setAttribute(player, "diango-reclaimables", reclaimable)
            if (reclaimable!!.isNotEmpty()) {
                InterfaceContainer.generateItems(
                    player,
                    reclaimable as Array<Item>,
                    arrayOf("Take"),
                    COMPONENT_ID,
                    2,
                    8,
                    8,
                )
            }
            openInterface(player, COMPONENT_ID)
        }

        /**
         * Refreshes the reclaim interface.
         *
         * @param player The player.
         */
        private fun refresh(player: Player) {
            player.interfaceManager.close()
            open(player)
        }

        /**
         * Returns an array of items that the player is eligible to reclaim.
         *
         * @param player The player whose eligibility is checked.
         * @return Array of reclaimable items.
         */
        fun getEligibleItems(player: Player): Array<Item?>? = ITEMS.filter { item ->
            !player.equipment.containsItem(item) && !player.inventory.containsItem(item) && !player.bank.containsItem(item)
        }.toTypedArray()
    }
}
