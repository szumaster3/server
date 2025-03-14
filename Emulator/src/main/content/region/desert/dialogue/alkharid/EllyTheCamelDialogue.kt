package content.region.desert.dialogue.alkharid

import core.api.playAudio
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class EllyTheCamelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val rand = RandomFunction.random(0, 2)
        when (rand) {
            0 ->
                player(FaceAnim.HALF_GUILTY, "If I go near that camel, it'll probably", "bite my hand off.").also {
                    stage =
                        0
                }
            1, 2 -> player(FaceAnim.HALF_THINKING, "I wonder if that camel has fleas...").also { stage = 0 }
        }
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
                sendMessage(player, "The camel tries to stomp on your foot, but you pull it back quickly.")
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return EllyTheCamelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ELLY_THE_CAMEL_2810, NPCs.CAM_THE_CAMEL_2812)
    }
}
