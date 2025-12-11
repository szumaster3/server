package content.region.other.keldagrim.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class FactoryWorkerDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (npc.id) {
            NPCs.FACTORY_WORKER_2172 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "I'm sorry, I'm looking for the blast furnace?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_ANGRY1, "Do I look like a guide to you?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "No, you look like a hard-working dwarf, but can you please tell me where the blast furnace is?").also { stage++ }
                3 -> npcl(FaceAnim.OLD_DEFAULT, "Alright, just head down the stairs, it's easy to find.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Thanks.").also { stage = END_DIALOGUE }
            }

            NPCs.FACTORY_WORKER_2173 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Are you okay?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_ANGRY1, "Don't I look okay?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "If you were any shorter you wouldn't exist.").also { stage++ }
                3 -> npcl(FaceAnim.OLD_ANGRY1, "Very funny, human.").also { stage = END_DIALOGUE }
            }

            NPCs.FACTORY_WORKER_2174 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "What are you dwarves doing in this factory?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_ANGRY1, "Working of course, can't you see that?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "But working on what?").also { stage++ }
                3 -> npcl(FaceAnim.OLD_DEFAULT, "Refining the ore that is being brought into the factory, of course.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "And what does that mean?").also { stage++ }
                5 -> npcl(FaceAnim.OLD_ANGRY1, "It means you should stop asking so many questions and get back to work!").also { stage = END_DIALOGUE }
            }

            NPCs.FACTORY_WORKER_2175 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Who owns this factory?").also { stage++ }
                1 -> npcl(FaceAnim.OLD_DEFAULT, "The Consortium does and that's all you need to know.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "But what company? I thought there were all these different companies?").also { stage++ }
                3 -> npcl(FaceAnim.OLD_DEFAULT, "Oh yes, all the major companies own this plant. It's too vital to be in the hands of one company alone.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "And what exactly are you doing here?").also { stage++ }
                5 -> npcl(FaceAnim.OLD_ANGRY1, "I tire of these questions. Let me get back to work!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.FACTORY_WORKER_2172,
        NPCs.FACTORY_WORKER_2173,
        NPCs.FACTORY_WORKER_2174,
        NPCs.FACTORY_WORKER_2175
    )
}
