package content.global.skill.crafting.spinning

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import org.rs.consts.Components

class SpinningInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.CRAFTING_SPINNING_459) { player, _ ->
            PacketRepository.send(
                RepositionChild::class.java,
                ChildPositionContext(player, Components.CRAFTING_SPINNING_459, 14, 243, 86),
            )
            PacketRepository.send(
                RepositionChild::class.java,
                ChildPositionContext(player, Components.CRAFTING_SPINNING_459, 15, 237, 136),
            )
            PacketRepository.send(
                RepositionChild::class.java,
                ChildPositionContext(player, Components.CRAFTING_SPINNING_459, 20, 113, 86),
            )
            PacketRepository.send(
                RepositionChild::class.java,
                ChildPositionContext(player, Components.CRAFTING_SPINNING_459, 21, 103, 134),
            )
            PacketRepository.send(
                RepositionChild::class.java,
                ChildPositionContext(player, Components.CRAFTING_SPINNING_459, 25, 361, 130),
            )
            PacketRepository.send(
                RepositionChild::class.java,
                ChildPositionContext(player, Components.CRAFTING_SPINNING_459, 33, 103, 252),
            )
            return@onOpen true
        }

        on(Components.CRAFTING_SPINNING_459) { player, _, opcode, buttonID, _, _ ->
            val spin = Spinning.forId(buttonID) ?: return@on true
            if (!inInventory(player, spin.need, 1)) {
                sendMessage(player, "You need " + getItemName(spin.need).lowercase() + " to make this.")
                return@on true
            }
            var amt = -1
            when (opcode) {
                155 -> amt = 1
                196 -> amt = 5
                124 -> amt = player.inventory.getAmount(Item(spin.need))
                199 ->
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        if (value is String) {
                            submitIndividualPulse(
                                entity = player,
                                pulse = SpinningPulse(player, Item(spin.need, 1), value.toInt(), spin),
                            )
                        } else {
                            submitIndividualPulse(
                                entity = player,
                                pulse = SpinningPulse(player, Item(spin.need, 1), value as Int, spin),
                            )
                        }
                    }
            }
            if (opcode == 199) {
                return@on true
            }
            submitIndividualPulse(
                entity = player,
                pulse = SpinningPulse(player, Item(spin.need, 1), amt, spin),
            )
            return@on true
        }
    }
}
