package content.global.random.event.frog_princess

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the dialogue plugin used for the Frog NPC.
 * @author szu
 */
@Initializable
class FrogDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val p = player ?: return false
        val gender = if (p.isMale) "Princess" else "Prince"
        val failRandom = getAttribute(player, GameAttributes.KTF_KISS_FAIL, -1)
        if (failRandom == 1) {
            if (stage == 0) {
                npcl(FaceAnim.OLD_ANGRY1, "Don't talk to me! Speak to the frog $gender!")
                stage = END_DIALOGUE
            }
        } else {
            when (stage) {
                0 -> sendNPCDialogue(p, NPCs.FROG_2471, "Well, we'll see how you like being a frog!", FaceAnim.OLD_NORMAL).also { stage++ }
                1 -> transformPlayerIntoFrog(p)
            }
        }
        return true
    }

    private fun transformPlayerIntoFrog(p: Player) {
        end()
        setAttribute(p, GameAttributes.KTF_KISS_FAIL, 1)
        p.animate(Animation(FrogUtils.TRANSFORM_INTO_FROG))
        queueScript(p, 1, QueueStrength.SOFT) {
            p.appearance.transformNPC(FrogUtils.FROG_APPEARANCE_NPC)
            return@queueScript stopExecuting(p)
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FROG_2472)
}