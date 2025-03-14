package content.region.misc.dialogue.keldagrim.politics

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class YellowFortuneDirectorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.isMale) {
            npc(FaceAnim.OLD_NORMAL, "Hello there, ${player.username}. You look a little masculine today!").also {
                stage =
                    END_DIALOGUE
            }
        } else {
            npc(
                FaceAnim.OLD_NORMAL,
                "You are looking more and more like a male these days, ${player.username}... be careful now!",
            ).also {
                stage =
                    END_DIALOGUE
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return YellowFortuneDirectorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.YELLOW_FORTUNE_DIRECTOR_2102)
    }
}
