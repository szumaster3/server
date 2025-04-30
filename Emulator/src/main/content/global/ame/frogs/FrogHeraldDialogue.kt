package content.global.ame.frogs

import content.data.RandomEvent
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

class FrogHeraldDialogue(
    val isStarted: Boolean = false,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FROG_2471)
        if (isStarted) {
            when (stage) {
                0 -> {
                    lock(player!!, 1000)
                    npcl(FaceAnim.OLD_NORMAL, "Welcome to the Land of the Frogs.").also { stage++ }
                }
                1 -> playerl("What am I doing here?").also {
                    face(player!!, findLocalNPC(player!!, NPCs.FROG_2471)!!, 3)
                    stage++
                }
                2 -> npcl(FaceAnim.OLD_NORMAL, "The Frog " + (if (player!!.isMale) "Princess" else "Prince") + " sent for you.").also { stage++ }
                3 -> playerl("Who is the Frog " + (if (player!!.isMale) "Princess" else "Prince") + "?").also { stage++ }
                4 -> npcl(FaceAnim.OLD_NORMAL, "" + (if (player!!.isMale) "She" else "He") + " is the frog with the crown. Make sure you speak to " + (if (player!!.isMale) "Her" else "Him") + ", not the other frogs, or " + (if (player!!.isMale) "She" else "He") + "'ll be offended.").also { stage++ }
                5 -> {
                    end()
                    unlock(player!!)
                    stage = END_DIALOGUE
                }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> npcl(FaceAnim.OLD_NORMAL, "Hey, ${player!!.username}, The Frog " + (if (player!!.isMale) "Princess" else "Prince") + " needs your help!").also { stage++ }
                1 -> {
                    queueScript(player!!, 1, QueueStrength.SOFT) {
                        setAttribute(player!!, RandomEvent.save(), player!!.location)
                        teleport(player!!, Location.create(2463, 4781, 0), TeleportManager.TeleportType.RANDOM_EVENT_OLD)
                        removeTabs(player!!, 0, 1, 2, 3, 4, 5, 6, 7, 14)
                        openDialogue(player!!, FrogHeraldDialogue(true))
                        AntiMacro.terminateEventNpc(player!!)
                        return@queueScript stopExecuting(player!!)
                    }
                }
            }
        }
    }
}
