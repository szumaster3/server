package content.global.skill.hunter.imp

import core.api.closeInterface
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Items

class ImpInterface : InterfaceListener {
    private val impBoxComponent = 478

    override fun defineInterfaceListeners() {
        onOpen(impBoxComponent) { player, _ ->
            PacketRepository.send(
                ContainerPacket::class.java,
                ContainerContext(player, impBoxComponent, 61, 91, player.inventory, true),
            )
            return@onOpen true
        }

        on(impBoxComponent) { player, _, _, _, slot, _ ->
            val boxId = intArrayOf(Items.IMP_IN_A_BOX1_10028, Items.IMP_IN_A_BOX2_10027)
            val item = player.inventory.get(slot)
            val boxSlot = player.inventory.getSlot(item)
            if (item != null) {
                if (player.bank.canAdd(item) && item.id != item.id) {
                    player.dialogueInterpreter.close()
                    player.inventory.remove(item)
                    player.bank.add(item)
                    when (item.id) {
                        impBoxIDs[1] -> {
                            replaceSlot(player, boxSlot, Item(boxId[0]).asItem())
                        }

                        impBoxIDs[0] -> {
                            replaceSlot(player, boxSlot, Item(10025))
                            closeInterface(player)
                        }
                    }
                }

                sendMessage(player, "You cannot add this item to your bank.")
                return@on false
            }

            return@on true
        }
    }
}
