package content.region.kandarin.ardougne.quest.drunkmonk.plugin

import content.region.kandarin.quest.drunkmonk.dialogue.BrotherCedricDialogueFile
import content.region.kandarin.quest.drunkmonk.dialogue.BrotherOmadDialogueFile
import content.region.kandarin.quest.drunkmonk.dialogue.MonasteryMonkDialogueFile
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class MonksFriendListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.BROTHER_CEDRIC_280, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, BrotherCedricDialogueFile(), NPC(NPCs.BROTHER_CEDRIC_280))
            return@on true
        }

        on(NPCs.BROTHER_OMAD_279, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, BrotherOmadDialogueFile(), NPC(NPCs.BROTHER_OMAD_279))
            return@on true
        }

        on(NPCs.MONK_281, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, MonasteryMonkDialogueFile(), NPC(NPCs.MONK_281))
            return@on true
        }
    }
}
