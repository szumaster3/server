package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AliTheGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello there!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.ANNOYED, "I'm working. What do you have to say that's so urgent?").also { stage++ }
            1 ->
                options(
                    "What can you tell me about Al Kharid?",
                    "So, what do you do here?",
                    "I hear you work for Ali Morrisane...",
                    "I hear you've been threatening the other shopkeepers.",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "What can you tell me about Al Kharid?").also { stage = 10 }
                    2 -> playerl(FaceAnim.ASKING, "So, what do you do here?").also { stage = 20 }
                    3 -> playerl(FaceAnim.ASKING, "I hear you work for Ali Morrisane...").also { stage = 30 }
                    4 ->
                        playerl(FaceAnim.ASKING, "I hear you've been threatening the other shopkeepers.").also {
                            stage = 40
                        }
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There's a lot of space here. More open space than back home.",
                ).also { stage++ }
            11 -> playerl(FaceAnim.ASKING, "So where is back home?").also { stage++ }
            12 ->
                npcl(FaceAnim.FRIENDLY, "Pollnivneach. It's a town to the south of the Shantay Pass.").also {
                    stage =
                        1
                }
            20 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "I'm on guard duty. Making sure nobody tries to steal anything from the house and tents in the middle of town.",
                ).also {
                    stage++
                }
            21 -> playerl(FaceAnim.ASKING, "Why are you only guarding those buildings?").also { stage++ }
            22 -> npcl(FaceAnim.SUSPICIOUS, "That's all I've been hired to guard.").also { stage = 1 }
            30 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yeah, he hired me. He owns this house and these two tents, too.",
                ).also { stage++ }
            31 -> playerl(FaceAnim.HALF_ASKING, "Is the work good?").also { stage++ }
            32 -> npcl(FaceAnim.FRIENDLY, "It pays better than back home.").also { stage++ }
            33 -> playerl(FaceAnim.ASKING, "Why, what did you do back home?").also { stage++ }
            34 -> npcl(FaceAnim.SUSPICIOUS, "Never you mind.").also { stage++ }
            35 -> npcl(FaceAnim.FRIENDLY, "But Ali Morrisane pays us well, at least.").also { stage++ }
            36 -> playerl(FaceAnim.ASKING, "Maybe I should talk to him...").also { stage++ }
            37 ->
                npcl(FaceAnim.FRIENDLY, "Why not? He always likes to meet potential business partners.").also {
                    stage =
                        1
                }
            40 -> npcl(FaceAnim.ANNOYED, "So? They talk too much.").also { stage++ }
            41 -> playerl(FaceAnim.HALF_ASKING, "You're not going to deny it?").also { stage++ }
            42 ->
                npcl(FaceAnim.LOUDLY_LAUGHING, "Why bother? None of them can fight back, after all.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_GUARD_2823)
    }
}
