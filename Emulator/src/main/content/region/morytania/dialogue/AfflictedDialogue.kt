package content.region.morytania.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class AfflictedDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val forceChat =
        arrayOf(
            "ughugh",
            "knows'is",
            "knows'is",
            "nots",
            "pirsl",
            "wot's",
            "zurgle",
            "gurghl",
            "mee's",
            "seysyi",
            "sfriess",
            "says",
        )

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        forceChat.shuffle()
        npc(
            FaceAnim.ASKING,
            forceChat
                .copyOfRange(
                    0,
                    RandomFunction.random(1, 6),
                ).contentToString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", ""),
        ).also {
            stage =
                END_DIALOGUE
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AFFLICTED_1257, NPCs.AFFLICTED_1258, NPCs.AFFLICTED_1261, NPCs.AFFLICTED_1262)
    }
}
