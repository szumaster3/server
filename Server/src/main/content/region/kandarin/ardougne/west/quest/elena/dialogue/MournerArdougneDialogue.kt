package content.region.kandarin.ardougne.west.quest.elena.dialogue

import core.api.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Mourner (West) dialogue.
 *
 * # Relations
 * - [Plague City][content.region.kandarin.ardougne.west.quest.elena.PlagueCity]
 */
class MournerArdougneDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.MOURNER_718)
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            in 0..5 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                1 -> npcl(FaceAnim.NEUTRAL, "What are you up to with old man Edmond?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Nothing, we've just been chatting.").also { stage++ }
                3 -> npcl(FaceAnim.NEUTRAL, "What about his daughter?").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "you know about that then?").also { stage++ }
                5 -> npcl(FaceAnim.NEUTRAL, "We know about everything that goes on in Ardougne. We have to if we are to contain the plague.").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Have you see his daughter recently?").also { stage++ }
                7 -> npcl(FaceAnim.NEUTRAL, "I imagine she's caught the plague. Either way she won't be allowed out of West Ardougne, the risk is too great.").also { stage = END_DIALOGUE }
            }
            6 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FaceAnim.NEUTRAL, "Been digging have we?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What do you mean?").also { stage++ }
                3 -> npcl(FaceAnim.NEUTRAL, "Your hands are covered in mud.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Oh that...").also { stage++ }
                5 -> npcl(FaceAnim.NEUTRAL, "Funny, you don't look like the gardening type.").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Oh no, I love gardening! It's my favorite pastime.").also { stage = END_DIALOGUE }
            }
            7 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FaceAnim.NEUTRAL, "What are you up to?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "What do you mean?").also { stage++ }
                3 -> npcl(FaceAnim.NEUTRAL, "You and that Edmond fella, you're looking very suspicious.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "We're just gardening. Have you heard any news about West Ardougne?").also { stage++ }
                5 -> npcl(FaceAnim.NEUTRAL, "Just the usual, everyone's sick or dying.").also { stage++ }
                6 -> npcl(FaceAnim.HALF_THINKING, "I'm furious at King Tyras for bringing this plague to our lands.").also { stage = END_DIALOGUE }
            }
            8 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FaceAnim.NEUTRAL, "Do you have a problem traveller?").also { stage++ }
                2 -> playerl(FaceAnim.NEUTRAL, "No, I just wondered why you're wearing that outfit... Is it fancy dress?").also { stage++ }
                3 -> npcl(FaceAnim.NEUTRAL, "No! It's for protection.").also { stage++ }
                4 -> playerl(FaceAnim.NEUTRAL, "Protection from what?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "The plague of course...").also { stage = END_DIALOGUE }
            }
            in 9..16 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FaceAnim.NEUTRAL, "Can I help you?").also { stage++ }
                2 -> playerl(FaceAnim.NEUTRAL, "What are you doing?").also { stage++ }
                3 -> npcl(FaceAnim.NEUTRAL, "I'm guarding the border to West Ardougne. No-one except we mourners can pass through.").also { stage++ }
                4 -> playerl(FaceAnim.NEUTRAL, "Why?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "The plague of course. We can't risk cross contamination.").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Ok then, see you around.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "Maybe...").also { stage = END_DIALOGUE }
            }
            in 17..100 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hello there.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "I'd stand away from there. That black cross means that house has been touched by the plague.").also { stage = END_DIALOGUE }
            }
            else -> when(stage){
                0 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
                1 -> npcl(FaceAnim.HALF_ASKING, "What are you up to?").also { stage++ }
                2 -> playerl(FaceAnim.NEUTRAL, "Nothing.").also { stage++ }
                3 -> npcl(FaceAnim.THINKING, "I don't trust you.").also { stage++ }
                4 -> playerl(FaceAnim.NEUTRAL, "You don't have to.").also { stage++ }
                5 -> npcl(FaceAnim.ANGRY, "If I find you attempting to cross the wall I'll make sure you never return.").also { stage = END_DIALOGUE }
            }
        }
    }
}
