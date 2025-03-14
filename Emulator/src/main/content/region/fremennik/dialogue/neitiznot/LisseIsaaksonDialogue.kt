package content.region.fremennik.dialogue.neitiznot

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LisseIsaaksonDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Hello, visitor!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.ASKING, "Hello. What are you up to?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, I was about to collect some yak's milk to make yak cheese.",
                ).also { stage++ }
            2 -> playerl(FaceAnim.HALF_WORRIED, "Eughr! Though I am curious. Can I try some?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.SAD,
                    "Sorry, no. The last outlander who ate my cheese was ill for a month.",
                ).also { stage++ }
            4 -> playerl(FaceAnim.ASKING, "So why don't you get ill as well?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, we eat yak milk products every day, from when we're born. So I suppose we're used to it. Anyway I should stop yakking - haha - and get on with my work.",
                ).also {
                    stage++
                }
            6 ->
                playerl(
                    FaceAnim.HAPPY,
                    "I'm glad to see that puns are common everywhere in Gielinor; even here.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LisseIsaaksonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LISSE_ISAAKSON_5513)
    }
}
