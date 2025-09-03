package content.region.kandarin.ardougne.east.dialogue

import content.region.kandarin.ardougne.east.quest.biohazard.dialogue.ElenaBiohazardDialogue
import core.api.openDialogue
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Elena dialogue.
 */
@Initializable
class ElenaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.PLAGUE_CITY)){
            end()
            openDialogue(player, ElenaBiohazardDialogue())
        } else {
            npc("Hello Elena.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npcl(FaceAnim.HALF_ASKING, "Hey, how are you?").also { stage++ }
            1 -> playerl(FaceAnim.HAPPY, "Good thanks, yourself?").also { stage++ }
            2 -> npcl(FaceAnim.HALF_THINKING, "Not bad, let me know when you hear from King Lathas again.").also { stage++ }
            3 -> playerl(FaceAnim.NOD_YES, "Will do.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ElenaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ELENA_3209)
}
