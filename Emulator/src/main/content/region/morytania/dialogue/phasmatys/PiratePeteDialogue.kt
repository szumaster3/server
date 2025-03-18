package content.region.morytania.dialogue.phasmatys

import content.region.morytania.quest.deal.dialogue.PiratePeteDialogueFile
import core.api.hasRequirement
import core.api.openDialogue
import core.api.quest.hasRequirement
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PiratePeteDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!hasRequirement(player!!, Quests.RUM_DEAL)) {
            sendDialogue(player, "The Pirate Pete is too busy to talk.").also { stage = END_DIALOGUE }
        } else {
            openDialogue(player, PiratePeteDialogueFile())
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PIRATE_PETE_2825)
}
