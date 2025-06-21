package core.game.node.entity.player.link.request.trade

import core.api.setVarp
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket

/**
 * Handles the closing of a trade interface and ensures trade state consistency.
 */
class TradeCloseEvent : CloseEvent {
    /**
     * Closes the trade interface for the given player and processes trade state cleanup.
     *
     * @param player The player closing the trade interface.
     * @param component The UI component being closed.
     * @return Always returns `true` to indicate the event was handled.
     */
    override fun close(
        player: Player,
        component: Component,
    ): Boolean {
        val module = TradeModule.getExtension(player) ?: return true

        player.packetDispatch.sendRunScript(101, "")

        val otherModule = TradeModule.getExtension(module.target) ?: return true

        if (module.isAccepted && otherModule.isAccepted) {
            return true
        }

        if (module.stage != 2) {
            retainContainer(player)
            retainContainer(module.target!!)
        }

        closeInterfaces(player)
        closeInterfaces(module.target!!)
        module.target!!.interfaceManager.close()
        end(player)
        player.interfaceManager.openDefaultTabs()
        end(module.target!!)
        module.target!!.interfaceManager.openDefaultTabs()

        return true
    }

    /**
     * Closes the trade-related interfaces and clears relevant UI elements.
     *
     * @param player The player whose interfaces are being closed.
     */
    private fun closeInterfaces(player: Player) {
        player.removeExtension(TradeModule::class.java)
        player.interfaceManager.closeSingleTab()
        PacketRepository.send(
            ContainerPacket::class.java,
            OutgoingContext.Container(player, -1, 2, 24, emptyArray<Item>(), 27, false),
        )
        PacketRepository.send(
            ContainerPacket::class.java,
            OutgoingContext.Container(player, -1, 2, 23, emptyArray<Item>(), 27, false),
        )
        player.packetDispatch.sendRunScript(101, "")
    }

    /**
     * Resets trade-related variables for the player.
     *
     * @param player The player whose trade state is being reset.
     */
    private fun end(player: Player) {
        setVarp(player, 1043, 0)
        setVarp(player, 1042, 0)
    }

    /**
     * Retains the trade container items by adding them back to the player's inventory.
     *
     * @param player The player whose trade container is being retained.
     */
    private fun retainContainer(player: Player) {
        val module = TradeModule.getExtension(player) ?: return
        if (module.isRetained) return

        module.isRetained = true
        player.inventory.addAll(module.container)
    }
}
