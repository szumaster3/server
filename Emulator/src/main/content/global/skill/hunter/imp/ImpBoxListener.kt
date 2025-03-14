package content.global.skill.hunter.imp

import core.api.closeAllInterfaces
import core.api.getUsedOption
import core.api.openDialogue
import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

val impBoxIDs = intArrayOf(Items.IMP_IN_A_BOX1_10028, Items.IMP_IN_A_BOX2_10027)

class ImpBoxListener : InteractionListener {
    override fun defineListeners() {
        on(impBoxIDs, IntType.ITEM, "talk-to", "bank") { player, node ->
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
}
