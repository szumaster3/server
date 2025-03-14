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
class OllieTheCamelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "I wonder if that camel has fleas...")
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
                sendMessage(player, "The camel tries to stamp on your foot, but you pull it back quickly.")
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OllieTheCamelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OLLIE_THE_CAMEL_2811)
    }
}
