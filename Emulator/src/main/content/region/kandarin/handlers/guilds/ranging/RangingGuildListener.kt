package content.region.kandarin.handlers.guilds.ranging

import core.api.inInventory
import core.api.sendMessage
import core.api.sendNPCDialogueLines
import core.api.submitIndividualPulse
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class RangingGuildListener : InteractionListener {
    override fun defineListeners() {
        on(TARGET, IntType.SCENERY, "fire-at") { player, node ->
            if (player.archeryTargets <= 0) {
                sendNPCDialogueLines(
                    player,
                    NPCs.COMPETITION_JUDGE_693,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Sorry, you may only use the targets for the",
                    "competition, not for practicing.",
                )
                return@on true
            }
            if (!inInventory(player, Items.BRONZE_ARROW_882) ||
                player.equipment[EquipmentContainer.SLOT_WEAPON] == null ||
                (
                    !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase().contains(
                        "shortbow",
                        true,
                    ) &&
                        !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase().contains(
                            "longbow",
                            true,
                        )
                )
            ) {
                sendMessage(player, "You must have bronze arrows and a bow equipped.")
                return@on true
            }

            submitIndividualPulse(player, ArcheryCompetitionPulse(player, (node.asScenery())))
            return@on true
        }
    }

    companion object {
        val TARGET = Scenery.TARGET_2513
    }
}
