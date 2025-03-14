package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AliTheSmithDialogue(
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
            0 -> npcl(FaceAnim.FRIENDLY, "You seem rather cheerful. Is there anything you're after?").also { stage++ }
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
                        playerl(FaceAnim.HALF_ASKING, "I hear you've been threatening the other shopkeepers.").also {
                            stage =
                                40
                        }
                }
            10 -> npcl(FaceAnim.FRIENDLY, "Well, it's hot and full of sand.").also { stage++ }
            11 -> playerl(FaceAnim.ASKING, "Anything else?").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "Don't ask me, I'm not from around here.").also { stage++ }
            13 -> playerl(FaceAnim.HALF_ASKING, "So where are you from?").also { stage++ }
            14 ->
                npcl(FaceAnim.HALF_GUILTY, "A town to the south of the Shantay Pass, called Pollnivneach.").also {
                    stage =
                        1
                }
            20 ->
                npcl(
                    FaceAnim.SAD,
                    "Not very much, at the moment. I came from further south to set up a smithy here...",
                ).also {
                    stage++
                }
            21 ->
                npcl(FaceAnim.SAD, "...but we still haven't set up the shops, so we have no customers yet.").also {
                    stage =
                        1
                }
            30 -> npcl(FaceAnim.FRIENDLY, "Yes, he's the one who persuaded us to come here.").also { stage++ }
            31 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "He said we'd have lots of customers once we set up our shops, and all he asks for is a cut of the profits.",
                ).also {
                    stage++
                }
            32 -> playerl(FaceAnim.ASKING, "Maybe I should talk to him...").also { stage++ }
            33 ->
                npcl(FaceAnim.FRIENDLY, "Of course. He's always happy to talk to possible business partners.").also {
                    stage =
                        1
                }
            40 -> npcl(FaceAnim.SUSPICIOUS, "What? Who's spreading that rumour?").also { stage++ }
            41 ->
                playerl(
                    FaceAnim.THINKING,
                    "Well, one of the shopkeepers told me a man with a large hammer came to threaten them.",
                ).also {
                    stage++
                }
            42 -> npcl(FaceAnim.SUSPICIOUS, "Don't pay any attention to those people.").also { stage++ }
            43 ->
                npcl(FaceAnim.LAUGH, "They're just worried that they'll lose money when we open our shops.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheSmithDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_SMITH_2820)
    }
}
