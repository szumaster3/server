package content.region.desert.dialogue.alkharid

import core.api.playAudio
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class CamTheCamelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "If I go near that camel, it'll probably bite my hand off.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                end()
                playAudio(player, Sounds.CAMEL_DISGRUNTLED_327)
                sendMessage(player, "The camel spits at you, and you jump back hurriedly.")
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CamTheCamelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAM_THE_CAMEL_2813)
    }
}
