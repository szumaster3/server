package content.region.desert.quest.deserttreasure.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class IceTrollDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_LAUGH1, "Hur hur hur!", "Well look here, a puny fleshy human!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_LAUGH1,
                    "You should beware of the icy wind that runs through",
                    "this valley, it will bring a fleshy like you to a cold end",
                    "indeed!",
                )

            1 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return IceTrollDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ICE_TROLL_1935)
    }
}
