package content.global.plugin.iface.ge

import core.api.*
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.access.InterfaceContainer.generateItems
import core.game.ge.ItemSet
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Components

/**
 * Handles the Exchange Item Sets interface logic.
 */
class ExchangeItemSets : InterfaceListener {
    override fun defineInterfaceListeners() {
        onClose(Components.EXCHANGE_ITEMSETS_645) { player, _ ->
            val listener = getAttribute<InventoryListener?>(player, "ge-listener", null)
            player.inventory.listeners.remove(listener)
            closeSingleTab(player)
            player.removeAttribute("container-key")
            player.removeAttribute("ge-listener")
            return@onClose true
        }
    }

    companion object {
        /**
         * Opens the exchange set interface.
         */
        @JvmStatic
        fun openFor(player: Player) {
            openInterface(player, Components.EXCHANGE_ITEMSETS_645)
            openSingleTab(player, Components.EXCHANGE_SETS_SIDE_644)
            val listener: InventoryListener
            setAttribute(player, "ge-listener", InventoryListener(player).also { listener = it })
            player.inventory.listeners.add(listener)
        }
    }

    /**
     * Update the interface.
     */
    private class InventoryListener(val player: Player) : ContainerListener {
        init {
            createContainers(player)
        }

        /**
         * Called when the inventory container updates.
         */
        override fun update(c: Container?, event: ContainerEvent?) {
            createContainers(player)
        }

        /**
         * Called to refresh the inventory container.
         */
        override fun refresh(c: Container?) {
            createContainers(player)
        }

        /**
         * Generates and sets interface containers for the exchange based on player inventory and container set.
         *
         * @param player The player.
         */
        private fun createContainers(player: Player) {
            setAttribute(player, "container-key", player.generateItems(ItemSet.getItemList(), Components.EXCHANGE_SETS_SIDE_644, 0, listOf("Exchange", "Components"), 7, 4))
            player.generateItems(
                ItemSet.getItemList(),
                Components.EXCHANGE_ITEMSETS_645,
                16,
                listOf("Exchange", "Components"),
                14,
                10
            )
        }
    }
}
