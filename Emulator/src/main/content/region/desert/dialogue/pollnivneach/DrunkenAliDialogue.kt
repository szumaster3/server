package content.region.desert.dialogue.pollnivneach

import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DrunkenAliDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FEUD)) {
            npcl(
                FaceAnim.DRUNK,
                "Ahh, a kind stranger. Get this old man another drink so that he may wet his throat and talk to you.",
            ).also {
                stage =
                    END_DIALOGUE
            }
        } else {
            npcl(
                FaceAnim.DRUNK,
                "What were we talking about again? Yes yes, when I was a boy..... no that's not it.",
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
        return DrunkenAliDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRUNKEN_ALI_1863)
    }
}
