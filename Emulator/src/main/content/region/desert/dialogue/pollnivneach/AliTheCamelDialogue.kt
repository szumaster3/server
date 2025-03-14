package content.region.desert.dialogue.pollnivneach

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class AliTheCamelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val phrase = RandomFunction.random(0, 2)
        when (phrase) {
            0 -> player(FaceAnim.AFRAID, "That beast would probably bite my fingers off", "if I tried to pet it")
            1 ->
                player(
                    FaceAnim.DISGUSTED,
                    "I'm not going to pet that! I might get fleas",
                    "or something else that nasty creature",
                    "might have.",
                )
            2 -> player(FaceAnim.THINKING, "Mmmm... Won't you make the nicest kebab?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                player.lock()
                player.animate(Animation(7299))
                player.impactHandler.disabledTicks = 3
                Pulser.submit(
                    object : Pulse(4, player) {
                        override fun pulse(): Boolean {
                            player.unlock()
                            player.animator.reset()
                            return true
                        }
                    },
                )
                sendDialogue(player, "The camel tries to kick you for insulting it.")
                stage = 1
            }

            1 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AliTheCamelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_CAMEL_1873)
    }
}
