package content.region.asgarnia.dialogue.burthope

import core.api.getStatLevel
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Ghommal dialogue.
 */
@Initializable
class GhommalDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getStatLevel(player, Skills.ATTACK) + getStatLevel(player, Skills.STRENGTH) >= 130 ||
            getStatLevel(player, Skills.ATTACK) == 99 ||
            getStatLevel(player, Skills.STRENGTH) == 99
        ) {
            npc(FaceAnim.HALF_GUILTY, "Ghommal welcome you to Warrior Guild!").also { stage = 10 }
        } else {
            npc(FaceAnim.HALF_GUILTY, "You not pass. You too weedy.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "What? But I'm a warrior!").also { stage++ }
            1 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Heehee... he say he warrior... I not heard that one",
                    "for... at leas' 5 minutes!",
                ).also {
                    stage++
                }
            2 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Go on, let me in, you know you want to. I could...",
                    "make it worth your while...",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "No! You is not a strong warrior, you not enter till you",
                    "bigger, Ghommal does not takes bribes.",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Ghommal stick to Warrior's Code of Honour. When",
                    "you a bigger, stronger warriror, you come back.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            10 -> player(FaceAnim.HALF_GUILTY, "Umm...thank you, I think.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GHOMMAL_4285)
    }
}
