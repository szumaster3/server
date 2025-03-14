package content.minigame.puropuro.handlers

import core.api.addItemOrDrop
import core.api.openInterface
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

class ElnockInquisitorListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ELNOCK_INQUISITOR_6070, IntType.NPC, "trade") { player, _ ->
            openInterface(player, Components.ELNOCK_EXCHANGE_540)
            return@on true
        }

        on(NPCs.ELNOCK_INQUISITOR_6070, IntType.NPC, "quick-start") { player, _ ->
            if (!player.savedData.activityData.isElnockSupplies) {
                player.savedData.activityData.isElnockSupplies = true
                addItemOrDrop(player, Items.BUTTERFLY_NET_10010, 1)
                addItemOrDrop(player, Items.IMP_REPELLENT_11262, 1)
                addItemOrDrop(player, Items.IMPLING_JAR_11260, 6)
                sendNPCDialogue(player, NPCs.ELNOCK_INQUISITOR_6070, "Here you go!")
            } else {
                sendNPCDialogue(
                    player,
                    NPCs.ELNOCK_INQUISITOR_6070,
                    "Since I have already given you some equipment for free, I'll be willing to sell you some now.",
                )
            }
            return@on true
        }
    }
}
