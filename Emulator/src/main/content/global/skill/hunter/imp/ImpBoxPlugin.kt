package content.global.skill.hunter.imp

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.ContainerPacket
import org.rs.consts.Items


class ImpBoxPlugin : InteractionListener, InterfaceListener {

    private val impBoxComponent = 478
    private val impBoxID = intArrayOf(Items.IMP_IN_A_BOX1_10028, Items.IMP_IN_A_BOX2_10027)


    override fun defineListeners() {
        on(impBoxID, IntType.ITEM, "talk-to", "bank") { player, node ->
            val option = getUsedOption(player)

            when (option) {
                "bank" ->
                    if (player.interfaceManager.hasChatbox()) {
                        closeAllInterfaces(player).also {
                            openInterface(player, 478)
                        }
                    }

                else ->
                    if (node.id == Items.IMP_IN_A_BOX1_10028) {
                        openDialogue(player, ImpDialogue())
                    } else {
                        openDialogue(player, ImpDialogueExtension())
                    }
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(impBoxComponent) { player, _ ->
            PacketRepository.send(
                ContainerPacket::class.java,
                OutgoingContext.Container(player, impBoxComponent, 61, 91, player.inventory, true),
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
                        impBoxID[1] -> {
                            replaceSlot(player, boxSlot, Item(boxId[0]).asItem())
                        }

                        impBoxID[0] -> {
                            replaceSlot(player, boxSlot, Item(Items.MAGIC_BOX_10025))
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
