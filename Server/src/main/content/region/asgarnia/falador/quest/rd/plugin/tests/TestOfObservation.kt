package content.region.asgarnia.falador.quest.rd.plugin.tests

import content.region.asgarnia.falador.quest.rd.RDUtils
import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.GameWorld
import shared.consts.Components
import shared.consts.NPCs

class TestOfObservation(private val state: Int = 0) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.LADY_TABLE_2283)
        when (state) {
            1 -> handleTaskBegin()
            2 -> handleTaskFail()
        }
    }

    private fun handleTaskBegin() {
        when (stage) {
            0 -> {
                player!!.lock()
                player!!.faceLocation(location(2458, 4980, 0))
                setAttribute(player!!, "rd:statues", (1..12).random())
                setVarbit(player!!, 658, getAttribute(player!!, "rd:statues", 0))
                npcl(FaceAnim.FRIENDLY, "Welcome, @name. This room will test your observation skills.").also { stage++ }
            }
            1 -> npcl(FaceAnim.FRIENDLY, "Study the statues closely. There is one missing statue in this room.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "We will also mix the order up a little, to make things interesting for you!").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "You have 10 seconds to memorise the statues... starting NOW!").also { stage++ }
            4 -> {
                end()
                player!!.unlock()
                startStatueMemorizationPulse()
            }
        }
    }

    private fun startStatueMemorizationPulse() {
        GameWorld.Pulser.submit(
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean =
                    when (counter++) {
                        16 -> {
                            openOverlay(player!!, Components.FADE_TO_BLACK_120)
                            sendNPCDialogueLines(player!!, NPCs.LADY_TABLE_2283, FaceAnim.FRIENDLY, true, "We will now dim the lights and bring the missing statue", "back in.")
                            false
                        }
                        23 -> {
                            setVarbit(player!!, 658, 0)
                            openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                            npc(FaceAnim.FRIENDLY, "Please touch the statue you think has been added.")
                            true
                        }
                        else -> false
                    }
            },
        )
    }

    private fun handleTaskFail() {
        when (stage) {
            0 ->
                if (
                    getAttribute(player!!, RecruitmentDrive.stageFail, false) &&
                    !getAttribute(player!!, RecruitmentDrive.stagePass, false)
                ) {
                    setAttribute(player!!, RecruitmentDrive.stageFail, true)
                    npc(
                        FaceAnim.SAD,
                        "No... I am very sorry.",
                        "Apparently you are not up to the challenge.",
                        "I will return you where you came from, better luck in the",
                        "future."
                    )
                    stage++
                }
            1 -> {
                end()
                lock(player!!, 10)
                removeAttribute(player!!, SirReenItchoodPlugin.ATTRIBUTE_CLUE)
                setAttribute(player!!, RecruitmentDrive.stagePass, false)
                setAttribute(player!!, RecruitmentDrive.stageFail, false)
                RDUtils.failSequence(player!!)
            }
        }
    }
}
