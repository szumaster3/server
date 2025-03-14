package content.global.ame.surpriseexam

import content.data.GameAttributes
import core.api.getAttribute
import core.api.openDialogue
import core.api.openInterface
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class MordautDialogue(
    val examComplete: Boolean,
    val questionCorrect: Boolean = false,
    val fromInterface: Boolean = false,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MR_MORDAUT_6117)
        if (examComplete) {
            if (getAttribute(player!!, GameAttributes.RE_PATTERN_OBJ, -1) == -1) {
                SurpriseExamUtils.pickRandomDoor(player!!)
            }
            val door = getAttribute(player!!, GameAttributes.RE_PATTERN_OBJ, -1)
            val doortext =
                when (door) {
                    Scenery.DOOR_2188 -> "red cross door in the south west"
                    Scenery.DOOR_2189 -> "blue star in the north west"
                    Scenery.DOOR_2192 -> "purple circle on the north"
                    Scenery.DOOR_2193 -> "green square door on the south east"
                    else -> ""
                }
            when (stage++) {
                0 ->
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "WELL DONE! You've proven your aptitude for pattern recognition. You'll receive a prize when you leave the classroom.",
                    )
                1 ->
                    npcl(FaceAnim.OLD_NORMAL, "To exit, use the $doortext of the school room.").also {
                        stage =
                            END_DIALOGUE
                    }
            }
        } else if (fromInterface) {
            if (questionCorrect) {
                if (getAttribute(player!!, GameAttributes.RE_PATTERN_CORRECT, 0) == 3) {
                    openDialogue(player!!, MordautDialogue(true), npc!!)
                } else {
                    when (stage++) {
                        0 ->
                            if (getAttribute(player!!, GameAttributes.RE_PATTERN_CORRECT, -1) == 1) {
                                npcl(
                                    FaceAnim.OLD_NORMAL,
                                    "Finally, a pupil using his brains rather than trying to eat them. Next question.",
                                )
                            } else {
                                val dialogue =
                                    arrayOf(
                                        "Wonderful! Keep up the good work. Next question.",
                                        "That's correct! Next question.",
                                    )
                                npcl(FaceAnim.OLD_NORMAL, dialogue.random())
                            }

                        1 -> {
                            end()
                            openInterface(player!!, SurpriseExamUtils.INTERFACE)
                        }
                    }
                }
            } else {
                when (stage++) {
                    0 ->
                        if (getAttribute(player!!, GameAttributes.RE_PATTERN_CORRECT, -1) == 0) {
                            npcl(
                                FaceAnim.OLD_NORMAL,
                                "Remember, the goal here is to find the pattern. Look for objects that would have a connection to each other.",
                            )
                        } else {
                            val dialogue =
                                arrayOf(
                                    "No. No, that's not right at all. Okay, next question.",
                                    "That's WRONG. Take your time and think about the next question.",
                                    "No, no, no... That's WRONG! Okay, next question.",
                                )
                            npcl(FaceAnim.OLD_NORMAL, dialogue.random())
                        }

                    1 -> {
                        end()
                        openInterface(player!!, SurpriseExamUtils.INTERFACE)
                    }
                }
            }
        } else {
            when (stage++) {
                0 ->
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "Ah, It's you, ${player!!.username}. You've been slacking in your studies, so it's time for an exam.",
                    )
                1 ->
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "There are two types of question. The first type requires you to find the next object in a pattern.",
                    )
                2 ->
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "In the second type, I will present you with fifteen cards and a hint. Discover three cards related to the hint, select them and confirm.",
                    )
                3 -> npcl(FaceAnim.OLD_NORMAL, "Pick the object that should come next in the pattern.")
                4 -> {
                    end()
                    openInterface(player!!, SurpriseExamUtils.INTERFACE)
                }
            }
        }
    }
}
