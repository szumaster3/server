package content.region.morytania.dialogue.phasmatys

import content.region.morytania.quest.deal.dialogue.CaptainBrainDeathDialogueFile
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CaptainBraindeathDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = getQuestStage(player, Quests.RUM_DEAL)
        if (getQuestStage(player, Quests.RUM_DEAL) >= 1) {
            openDialogue(player, CaptainBrainDeathDialogueFile(quest))
        } else {
            sendDialogue(player!!, "The Captain Braindeath is too busy to talk.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_BRAINDEATH_2827)
    }
}
