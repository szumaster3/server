package content.region.kandarin.ardougne.west.dialogue

import core.api.openDialogue
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
 * Represents the Nurse Sarah dialogue.
 */
@Initializable
class NurseSarahDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player, Quests.BIOHAZARD, 6, 7)) {
            end()
            openDialogue(player, content.region.kandarin.ardougne.east.quest.biohazard.dialogue.NurseSarahDialogue())
            return true
        }
        playerl(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> if (isQuestComplete(player, Quests.PLAGUE_CITY)) {
                npcl(FaceAnim.NEUTRAL, "The plague itself was a fake, but the image of West Ardougne as a plague city wasn't too far from the truth. Malnutrition, poor sanitation... it's no wonder these people aren't healthy.").also { stage = 4 }
            } else {
                npcl(FaceAnim.FRIENDLY, "Hello my dear, how are you feeling?").also { stage++ }
            }
            1 -> player("I'm ok thanks.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Well in that case I'd better get back to work. Take care.").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "You too.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.NEUTRAL, "Now that we can get in and out again, people are getting healthier, but it'll be a long time before West Ardougne is really a nice place to live.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.NURSE_SARAH_373)
}
