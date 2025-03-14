package core.game.node.entity.player.link.request.trade

import core.api.setVarp
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket

class TradeCloseEvent : CloseEvent {
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

    private fun closeInterfaces(player: Player) {
        player.removeExtension(TradeModule::class.java)
        player.interfaceManager.closeSingleTab()
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, -1, 2, 24, emptyArray<Item>(), 27, false),
        )
        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, -1, 2, 23, emptyArray<Item>(), 27, false),
        )
        player.packetDispatch.sendRunScript(101, "")
    }

    private fun end(player: Player) {
        setVarp(player, 1043, 0)
        setVarp(player, 1042, 0)
    }

    private fun retainContainer(player: Player) {
        val module = TradeModule.getExtension(player) ?: return
        if (module.isRetained) return

        module.isRetained = true
        player.inventory.addAll(module.container)
    }
}
