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
class AlTheCamelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val rand = RandomFunction.random(0, 1)
        when (rand) {
            0 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Mmm... Looks like that camel would make a nice kebab.",
                ).also { stage = 0 }
            1 ->
                playerl(FaceAnim.HALF_GUILTY, "If I go near that camel, it'll probably bite my hand off.").also {
                    stage =
                        0
                }
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
        return AlTheCamelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AL_THE_CAMEL_2809)
    }
}
