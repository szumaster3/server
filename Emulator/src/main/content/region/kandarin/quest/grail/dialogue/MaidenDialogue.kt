package content.region.kandarin.quest.grail.dialogue

import core.api.sendDialogue
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class MaidenDialogue(
    var forced: Boolean,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GRAIL_MAIDEN_210)

        if (forced) {
            when (stage) {
                0 ->
                    sendDialogue(player!!, "Ting-a-ling-a-ling!").also {
                        var center = Location.create(2764, 4687, 0)
                        var dist = center.location.getDistance(player!!.location)

                        if (dist <= 12) {
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    }
                1 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Welcome to the Grail castle. You should come inside, it's cold out here.",
                    ).also { stage++ }
                2 -> {
                    sendDialogue(player!!, "Somehow you are now inside the castle.")
                    moveInsideCastle()
                    stage = END_DIALOGUE
                }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.NEUTRAL, "Welcome to the Grail castle.").also { stage = END_DIALOGUE }
            }
        }
    }

    private fun moveInsideCastle() {
        val dest = Location.create(2761, 4692, 0)

        if (player!!.location.getDistance(dest) <= 3) {
            player!!.locks.lockMovement(3)
            player!!.walkingQueue.reset()
            player!!.walkingQueue.addPath(dest.x, dest.y)
        } else {
            teleport(player!!, dest)
        }
    }
}
