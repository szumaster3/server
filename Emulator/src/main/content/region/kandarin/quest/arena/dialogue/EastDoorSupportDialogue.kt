package content.region.kandarin.quest.arena.dialogue

import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Quests

class EastDoorSupportDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.KHAZARD_GUARD_257)
        val completeOptionalTask = getAttribute(player!!, "quest:arena-optional-task", false)
        when (stage) {
            0 -> {
                if (isQuestComplete(player!!, Quests.FIGHT_ARENA)) {
                    npcl(
                        FaceAnim.ANGRY,
                        "You're " + if (!player!!.isMale) "her" else "him" + "! The one who beat the " + if (!completeOptionalTask) "General's Pet" else "General" + "! Begone, murderer!"
                    )
                    stage = 3
                } else {
                    face(player!!, location(2585, 3141, 0))
                    playerl(FaceAnim.NEUTRAL, "This door appears to be locked.")
                    stage++
                }
            }
            1 -> {
                face(player!!, location(2603, 3155, 0))
                npcl(
                    FaceAnim.NEUTRAL,
                    "Nice observation guard. You could have just asked to be let in like a normal person.",
                ).also {
                    stage++
                }
            }

            2 -> {
                end()
                lock(player!!, 2)
                setQuestStage(player!!, Quests.FIGHT_ARENA, 20)
                DoorActionHandler.handleAutowalkDoor(player!!, getScenery(2617, 3172, 0)!!)
            }
            3 -> end()
        }
    }
}
