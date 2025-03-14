package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AliTheTailorDialogue(
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
            0 -> npcl(FaceAnim.ANNOYED, "I'm a little busy at the moment. What is it?").also { stage++ }
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
                    2 -> playerl(FaceAnim.ASKING, "So, what do you do here?").also { stage = 15 }
                    3 -> playerl(FaceAnim.ASKING, "I hear you work for Ali Morrisane...").also { stage = 19 }
                    4 ->
                        playerl(
                            FaceAnim.ASKING,
                            "I hear you've been threatening the other shopkeepers.",
                        ).also { stage = 24 }
                }
            10 -> npcl(FaceAnim.FRIENDLY, "Oh, it has wonderful weather.").also { stage++ }
            11 -> playerl(FaceAnim.HALF_ASKING, "Wonderful? It's hot and dry all the time!").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "Not quite as hot as back home.").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "Where's home, then?").also { stage++ }
            14 ->
                npcl(FaceAnim.FRIENDLY, "Oh, it's a town to the south of the pass, called Pollnivneach.").also {
                    stage =
                        END_DIALOGUE
                }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If I had cloth, patterns and customers, I'd be a tailor. As it is, I'm a tailor with nothing to do.",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The silk merchant won't even sell me any silks, because he doesn't trust me!",
                ).also {
                    stage++
                }
            17 -> player(FaceAnim.HALF_ASKING, "Anything I can do to help?").also { stage++ }
            18 -> npcl(FaceAnim.FRIENDLY, "No, no, it's all being dealt with.").also { stage = END_DIALOGUE }
            19 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course, he's the one who's going to obtain cloth and clothes patterns so I can set up shop here.",
                ).also {
                    stage++
                }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "And in such a good location, too. Not out of the way like back home.",
                ).also { stage++ }
            21 -> npcl(FaceAnim.FRIENDLY, "The customers will soon pour in!").also { stage++ }
            22 -> player("Maybe I should talk to him...").also { stage++ }
            23 ->
                npcl(FaceAnim.FRIENDLY, "Why not? He's always happy to talk to potential business partners.").also {
                    stage =
                        END_DIALOGUE
                }
            24 -> npc("Me, threaten people?").also { stage++ }
            25 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "One of the shopkeepers did say they were threatened by a man with large scissors...",
                ).also {
                    stage++
                }
            26 ->
                npcl(
                    FaceAnim.FURIOUS,
                    "Oh, them. Don't mind them. I think they're worried about what effect our shops will have.",
                ).also {
                    stage++
                }
            27 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "For all they know, when we open our shops their wares will look cheap and shabby by comparison!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheTailorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_TAILOR_2822)
    }
}
