package content.region.morytania.phas.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Captain Braindeath dialogue on Braindeath Island.
 */
@Initializable
class CaptainBraindeathDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // val quest = getQuestStage(player, Quests.RUM_DEAL)
        // if (hasRequirement(player, Quests.RUM_DEAL)) {
        //     openDialogue(player, CaptainBraindeathDialogue(quest))
        // } else {
            sendDialogue(player!!, "The Captain Braindeath is too busy to talk.").also { stage = END_DIALOGUE }
        // }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CAPTAIN_BRAINDEATH_2827)
}
