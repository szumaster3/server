package content.region.karamja.quest.mm.handlers

import core.api.getAttribute
import core.api.setAttribute
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs.consts.Components
import kotlin.math.abs

class ReinitialisationPanelListener : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.TRAIL_PUZZLE_363) { player, _, _, buttonID, slot, _ ->

            val puzzlePieces: Array<Item?> = ((3904..3950 step 2).toList().map { Item(it) }).toTypedArray()
            val currentPiecesPosition: Array<Item?>?

            when (buttonID) {
                6 -> {
                    currentPiecesPosition = getAttribute(player, "mm:puzzle", puzzlePieces?.clone())
                    val targetSlot =
                        listOf(1, -1, 5, -5)
                            .map { findTargetSlot(currentPiecesPosition, slot, it) }
                            .firstOrNull { it != -1 }
                    targetSlot?.let { swapPieces(currentPiecesPosition, slot, it) }
                    PacketRepository.send(
                        ContainerPacket::class.java,
                        ContainerContext(player, -1, -1, 140, currentPiecesPosition, 25, false),
                    )
                    setAttribute(player, "/save:mm:puzzle", currentPiecesPosition)
                    if (isPuzzleSolved(currentPiecesPosition, puzzlePieces)) {
                        setAttribute(player, "/save:mm:puzzle:done", true)
                    }
                }
            }
            return@on true
        }
    }

    private fun swapPieces(
        puzzlePieces: Array<Item?>?,
        slot: Int,
        targetSlot: Int,
    ) {
        puzzlePieces?.set(targetSlot, puzzlePieces[slot])
        puzzlePieces?.set(slot, Item(-1))
    }

    private fun findTargetSlot(
        puzzlePieces: Array<Item?>?,
        slot: Int,
        offset: Int,
    ): Int {
        val targetSlot = slot + offset
        return if ((targetSlot in 0..24) &&
            (abs(slot - targetSlot) == 1 || abs(slot - targetSlot) == 5) &&
            puzzlePieces
                ?.get(
                    targetSlot,
                )?.id == 65535
        ) {
            targetSlot
        } else {
            -1
        }
    }

    private fun isPuzzleSolved(
        currentPieces: Array<Item?>?,
        puzzlePieces: Array<Item?>?,
    ): Boolean = currentPieces.contentEquals(puzzlePieces)
}
