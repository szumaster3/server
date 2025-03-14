package content.region.desert.dialogue.pollnivneach

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AliTheMayorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.FRIENDLY,
            "Welcome adventurer to the town of Pollnivneach,",
            "the gateway to Menaphos and Al-Kharid. My name is Ali",
            "and I'm the mayor of this town.",
            "I hope you enjoy your stay here.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                player(
                    FaceAnim.FRIENDLY,
                    "Thank you.",
                    "That is the warmest welcome I've had anywhere",
                    "for a while at least. People generally treat",
                    "travelling adventurers with suspicion.",
                ).also {
                    stage++
                }
            1 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "All are welcome here, such is the way",
                    "of things in border regions.",
                    "Now more than ever I suppose.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.FRIENDLY, "What do you mean by that?").also { stage++ }
            3 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "There's trouble in town, and a lot of ",
                    "villagers have left as a result.",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.FRIENDLY, "I'm looking for someone called Ali.").also { stage++ }
            5 -> npc(FaceAnim.FRIENDLY, "I doubt that's easy in Pollnivneach.").also { stage++ }
            6 -> player(FaceAnim.FRIENDLY, "Well can you help me?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "I'm more than a little busy at the moment, ",
                    "I'm sure there are plenty of people ",
                    "in town who could help you.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheMayorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_MAYOR_1870)
    }
}
