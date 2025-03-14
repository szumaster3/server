package content.region.morytania.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Graphics

class AbidorCrankDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.WORRIED,
                    "Oh this is no good, you surely will not survive here. Let me take you back.",
                ).also { stage++ }

            1 -> {
                end()
                visualize(
                    npc!!,
                    Animations.CLAP_AND_RAISE_FIST_IN_AIR_1818,
                    Graphics.TELEOTHER_CAST_343,
                )
                sendGraphics(Graphics.TELEOTHER_ACCEPT_342, player!!.location)
                setAttribute(player!!, "teleporting-away", true)
                queueScript(player!!, 3, QueueStrength.SOFT) {
                    poofClear(npc!!)
                    teleport(player!!, Location.create(3402, 3485, 0))
                    unlock(player!!)
                    removeAttribute(player!!, "teleporting-away")
                    return@queueScript stopExecuting(player!!)
                }
            }
        }
    }
}
