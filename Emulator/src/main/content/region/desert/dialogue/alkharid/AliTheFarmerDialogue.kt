package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AliTheFarmerDialogue(
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
            0 -> npcl(FaceAnim.FRIENDLY, "Oh, er, hello. What do you want?").also { stage++ }
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
                    2 -> playerl(FaceAnim.ASKING, "So, what do you do here?").also { stage = 19 }
                    3 -> playerl(FaceAnim.ASKING, "I hear you work for Ali Morrisane...").also { stage = 30 }
                    4 ->
                        playerl(
                            FaceAnim.ASKING,
                            "I hear you've been threatening the other shopkeepers.",
                        ).also { stage = 40 }
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There's not much farming land around here. Only that little patch outside.",
                ).also {
                    stage++
                }
            11 -> playerl(FaceAnim.ASKING, "Can you give me any advice on farming here in the desert?").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "Like I said, I only know about cactuses...").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "Just tell me about cactuses, then.").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "First you have to weed the patch using a rake.").also { stage++ }
            15 -> playerl(FaceAnim.ASKING, "Can you give me any other advice?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "Not really. I've not done much farming recently.").also { stage++ }
            17 ->
                playerl(
                    FaceAnim.ASKING,
                    "Well, can you at least sell me any gardening tools or seeds?",
                ).also { stage++ }
            18 -> npcl(FaceAnim.GUILTY, "Sorry. They haven't been delivered yet.").also { stage = 1 }
            19 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm going to set up a shop selling farming implements. That patch out there may be small, but it's all we've got.",
                ).also {
                    stage =
                        11
                }
            30 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, he bought these tents and had them put up for us. He says he'll also get our goods in, so we can start selling them soon.",
                ).also {
                    stage++
                }
            31 -> npcl(FaceAnim.FRIENDLY, "I only know how to farm cactuses, so this spot is perfect.").also { stage++ }
            32 -> playerl(FaceAnim.THINKING, "Maybe I should talk to him...").also { stage++ }
            33 ->
                npcl(FaceAnim.FRIENDLY, "Of course. He's always happy to talk to possible business partners.").also {
                    stage =
                        1
                }
            40 -> npcl(FaceAnim.FRIENDLY, "Now why would you think that?").also { stage++ }
            41 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "One of the shopkeepers told they were threatened by a man with a rake...",
                ).also {
                    stage++
                }
            42 ->
                npcl(FaceAnim.FRIENDLY, "Those people just don't want us to succeed. Don't listen to them!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheFarmerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_FARMER_2821)
    }
}
