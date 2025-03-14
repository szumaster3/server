package content.region.tirannwn.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MawrthDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.FRIENDLY,
            "Those children are nothing but trouble - if I did not watch them all the time, Seren knows what trouble they would get in to!",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "They look old enough to look after themselves.").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "They are only 34 and 38, far too young to be left unsupervised.",
                ).also { stage++ }
            2 -> player(FaceAnim.FRIENDLY, "Only!?! How old does that make you?").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Has no one told you it is rude to ask a lady her age?").also { stage++ }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Sorry, I wasn't thinking. Anyway... I'd better stop distracting you.",
                ).also { stage++ }
            5 -> npc(FaceAnim.FRIENDLY, "Okay, See you some other time.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return MawrthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAWRTH_2366)
    }
}
