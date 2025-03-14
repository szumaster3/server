package content.region.misthalin.dialogue.digsite

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler.getEndLocation
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs

class GateGuardDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MUSEUM_GUARD_5941)
        when (stage) {
            0 ->
                if (npc!!.id == NPCs.MUSEUM_GUARD_5942) {
                    npc(
                        FaceAnim.FRIENDLY,
                        "Hello there! Sorry, I can't stop to talk. I'm guarding this",
                        "workman's gate. I'm afraid you can't come through here -",
                        "you'll need to find another way around.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "Welcome! Would you like to go into the Dig Site",
                        "archaeology cleaning area?",
                    ).also {
                        stage++
                    }
                }
            1 -> options("Yes, I'll go in!", "No thanks, I'll take a look around out there.").also { stage++ }
            2 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "Yes, I'll go in!").also { stage++ }
                    2 ->
                        playerl(FaceAnim.HALF_GUILTY, "No thanks, I'll take a look around out there.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            3 -> {
                end()
                val doorLocation = Location(3261, 3446, 0)
                val door = getObject(doorLocation)
                if (getEndLocation(player!!, door!!).y > player!!.location.y) {
                    Animation(Animations.HANDS_BEHIND_BACK_SIDEWAYS_DO_A_MOTION_6391)
                } else {
                    Animation(Animations.HANDS_BEHIND_BACK_SIDEWAYS_DO_A_MOTION_6392)
                }
                handleAutowalkDoor(player!!, door)
            }
        }
    }
}
