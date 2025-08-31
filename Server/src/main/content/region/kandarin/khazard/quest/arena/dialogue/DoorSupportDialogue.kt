package content.region.kandarin.khazard.quest.arena.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Handles interaction with Fight Arena doors.
 */
class DoorSupportDialogue(
    private val isWest: Boolean
) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.KHAZARD_GUARD_257)
        val completeOptionalTask = getAttribute(player!!, GameAttributes.FIGHT_ARENA_OPTIONAL_KILL, false)

        val firstFaceLoc = if (isWest) location(2585, 3141, 0) else location(2585, 3141, 0)
        val secondFaceLoc = if (isWest) location(2603, 3155, 0) else location(2603, 3155, 0)
        val doorLoc = if (isWest) location(2584, 3141, 0) else location(2617, 3172, 0)

        when (stage) {
            0 -> {
                if (isQuestComplete(player!!, Quests.FIGHT_ARENA)) {
                    npcl(
                        FaceAnim.ANGRY,
                        "You're " +
                            if (!player!!.isMale) "her"
                            else "him" + "! The one who beat the " +
                                if (!completeOptionalTask) "General's Pet" else "General" + "! Begone, murderer!"
                    )
                    stage = 3
                } else {
                    face(player!!, firstFaceLoc)
                    playerl(FaceAnim.NEUTRAL, "This door appears to be locked.")
                    stage++
                }
            }
            1 -> {
                face(player!!, secondFaceLoc)
                npcl(FaceAnim.NEUTRAL, "Nice observation guard. You could have just asked to be let in like a normal person.")
                stage++
            }
            2 -> {
                end()
                lock(player!!, 2)
                setQuestStage(player!!, Quests.FIGHT_ARENA, 20)
                DoorActionHandler.handleAutowalkDoor(player!!, getScenery(doorLoc)!!)
            }
            3 -> end()
        }
    }
}