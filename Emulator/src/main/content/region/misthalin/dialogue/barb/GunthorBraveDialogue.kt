package content.region.misthalin.dialogue.barb

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class GunthorBraveDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((0..5).random()) {
            0 -> npcl(FaceAnim.HAPPY, forceChat[0]).also { stage = 0 }
            1 -> npcl(FaceAnim.LAUGH, forceChat[1]).also { stage = 0 }
            2 -> npcl(FaceAnim.ASKING, forceChat[2]).also { stage = 0 }
            3 -> npcl(FaceAnim.ANNOYED, forceChat[3]).also { stage = 0 }
            4 -> npcl(FaceAnim.ANGRY, forceChat[4]).also { stage = 0 }
            else -> npcl(FaceAnim.HALF_ASKING, "What do you want?").also { stage = 1 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> end().also { npc.attack(player) }
            1 -> player(FaceAnim.HALF_ASKING, "What are you offering?").also { stage++ }
            2 -> npcl(FaceAnim.NEUTRAL, "A fight!").also { stage = 0 }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GunthorBraveDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUNTHOR_THE_BRAVE_199)
    }

    companion object {
        val forceChat = arrayOf("Ah, you've come for fight!", "You look funny!", "Wanna fight?", "Grrr!", "Go away!")
    }
}
