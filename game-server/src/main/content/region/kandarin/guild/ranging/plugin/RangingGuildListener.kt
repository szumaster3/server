package content.region.kandarin.guild.ranging.plugin

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class RangingGuildListener : InteractionListener {
    override fun defineListeners() {

        on(NPCs.RANGING_GUILD_DOORMAN_679, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello there.")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogueLines(
                    player,
                    node.id,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Greetings. If you are an experienced archer, you may",
                    "want to visit the guild here...",
                )

            }
            return@on true
        }

        on(NPCs.GUARD_678, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello there.")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogueLines(
                    player,
                    node.id,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Greetings, traveller. Enjoy the time at the Ranging", "Guild.",
                )

            }
            return@on true
        }

        on(Scenery.TARGET_2513, IntType.SCENERY, "fire-at") { player, node ->
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
                player.equipment[EquipmentContainer.SLOT_WEAPON] == null || (
                        !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase()
                            .contains("shortbow", true) &&
                                !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase()
                                    .contains("longbow", true))
            ) {
                sendMessage(player, "You must have bronze arrows and a bow equipped.")
                return@on true
            }
            submitIndividualPulse(player, ArcheryCompetitionPulse(player, (node.asScenery())))
            return@on true
        }
    }
}
