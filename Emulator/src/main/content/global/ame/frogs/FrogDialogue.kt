package content.global.ame.frogs

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FrogDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val failRandom = (player!!.getAttribute(FrogUtils.ATTRIBUTE_FROG_TASK_FAIL, 0))
        if (failRandom == 1) {
            when (stage) {
                0 ->
                    npcl(
                        FaceAnim.OLD_ANGRY1,
                        "Don't talk to me! Speak to the frog " + (if (player!!.isMale) "Princess" else "Prince") + "!",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        } else {
            when (stage) {
                0 -> {
                    lock(player!!, 1000)
                    sendNPCDialogue(
                        player!!,
                        NPCs.FROG_2471,
                        "Well, we'll see how you like being a frog!",
                        FaceAnim.OLD_NORMAL,
                    ).also {
                        stage++
                    }
                }

                1 -> {
                    end()
                    setAttribute(player, FrogUtils.ATTRIBUTE_FROG_TASK_FAIL, 1)
                    animate(player, FrogUtils.TRANSFORMATION_ANIM)
                    queueScript(player, 2, QueueStrength.SOFT) { _ ->
                        player.appearance.transformNPC(FrogUtils.FROG_APPEARANCE_NPC)
                        unlock(player)
                        return@queueScript stopExecuting(player)
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FROG_2472)
    }
}
