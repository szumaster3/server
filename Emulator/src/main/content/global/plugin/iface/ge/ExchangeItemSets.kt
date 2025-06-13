package content.global.plugin.iface.ge

import core.api.ContainerListener
import core.api.getAttribute
import core.api.openInterface
import core.api.setAttribute
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.access.InterfaceContainer
import core.game.ge.ItemSet
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs.consts.Components

/**
 * Handles the Exchange Item Sets interface logic.
 */
class ExchangeItemSets : InterfaceListener {
    override fun defineInterfaceListeners() {
        onClose(Components.EXCHANGE_ITEMSETS_645) { player, _ ->
            val listener = getAttribute<InventoryListener?>(player, "ge-listener", null)
            player.inventory.listeners.remove(listener)
            player.interfaceManager.closeSingleTab()
            player.removeAttribute("container-key")
            player.removeAttribute("ge-listener")
            return@onClose true
        }
    }

    companion object {
        /**
         * Opens the interface for the player.
         *
         * @param player The player.
         */
        @JvmStatic
        fun openFor(player: Player) {
            openInterface(player, Components.EXCHANGE_ITEMSETS_645)
            player.interfaceManager.openSingleTab(Component(Components.EXCHANGE_SETS_SIDE_644))
            val listener: InventoryListener
            setAttribute(player, "ge-listener", InventoryListener(player).also { listener = it })
            player.inventory.listeners.add(listener)
        }
    }

    /**
     * Listens to the inv container events to update the interface accordingly.
     *
     * @property player The player.
     */
    private class InventoryListener(
        val player: Player,
    ) : ContainerListener {
        init {
            createContainers(player)
        }

        /**
         * Called when the inventory container updates.
         */
        override fun update(
            c: Container?,
            event: ContainerEvent?,
        ) {
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
            setAttribute(
                player,
                "container-key",
                InterfaceContainer.generateItems(
                    player,
                    player.inventory.toArray(),
                    arrayOf("Exchange", "Components"),
                    Components.EXCHANGE_SETS_SIDE_644,
                    0,
                    7,
                    4,
                ),
            )

            InterfaceContainer.generateItems(
                player,
                ItemSet.getItemArray(),
                arrayOf("Exchange", "Components"),
                Components.EXCHANGE_ITEMSETS_645,
                16,
                14,
                10,
            )
        }
    }
}
