package content.region.kandarin.quest.elena.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CarlaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.SAD, "Oh, hello.").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "You seem upset... What's wrong?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "That's awful, I'm sorry.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It would be easier to cope with if I could have spent his last few days with him.",
                ).also { stage = 4 }

            4 -> playerl(FaceAnim.FRIENDLY, "Why didn't you?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.SAD,
                    "Those mourners came and whisked him away. He didn't even seem that ill, I thought it was a common cold. But the mourners said he was infected and had to be taken away. Two days later the mourners returned and told me he had died.",
                ).also { stage++ }

            6 -> playerl(FaceAnim.FRIENDLY, "Again, I'm sorry. Life can be harsh.").also { stage++ }
            7 ->
                options(
                    "Where did the plague come from?",
                    "Have there been many victims of the plague?",
                    "I hope things get easier for you.",
                ).also { stage++ }

            8 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "Where did the plague come from?").also { stage = 9 }
                    2 -> playerl(FaceAnim.ASKING, "Have there been many victims of the plague?").also { stage = 10 }
                    3 -> playerl(FaceAnim.ASKING, "I hope things get easier for you.").also { stage = 13 }
                }

            9 ->
                npcl(
                    FaceAnim.SAD,
                    "It's down to King Tyras. He and his men brought the plague here from the darklands, and then left us to suffer. One day he'll pay for what he's done!",
                ).also { stage = 8 }

            10 ->
                npcl(
                    FaceAnim.SAD,
                    "You could say that... I've heard reports that half of West Ardougne is infected! Many have lost friends and family...",
                ).also { stage++ }

            11 -> playerl(FaceAnim.FRIENDLY, "It sounds an awful way to live.").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "People are very depressed and scared. I've never met anyone fully infected though. I suppose we should be grateful to the mourners for that.",
                ).also { stage = END_DIALOGUE }

            13 -> npcl(FaceAnim.SAD, "Me too...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CARLA_712)
    }
}
