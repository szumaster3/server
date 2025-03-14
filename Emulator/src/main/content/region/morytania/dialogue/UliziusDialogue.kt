package content.region.morytania.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class UliziusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "What... Oh, don't creep up on me like that... I thought",
                    "you were a Ghast!",
                ).also {
                    stage++
                }
            1 -> player(FaceAnim.HALF_GUILTY, "Can I go through the gate please?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Absolutely not. I've been given strict instructions not to",
                    "let anyone through. It's just too dangerous. No one",
                    "gets in without Drezels say so!",
                ).also {
                    stage++
                }
            3 -> player(FaceAnim.HALF_GUILTY, "Where is Drezel?").also { stage++ }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh, he's in the temple, just go back over the bridge,",
                    "down the ladder and along the hallway, you can't miss",
                    "him.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return UliziusDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ULIZIUS_1054)
    }
}
