package content.global.ame.drilldemon

import content.data.GameAttributes
import core.api.getAttribute
import core.api.sendItemDialogue
import core.api.unlock
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.DARK_RED
import org.rs.consts.Items
import org.rs.consts.NPCs

class SergeantDamienDialogue(
    var isCorrect: Boolean = false,
    var eventStart: Boolean = false,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SERGEANT_DAMIEN_2790)
        val correctCounter = player!!.getAttribute(GameAttributes.DRILL_COUNTER, 0)
        when (stage) {
            0 -> {
                if (correctCounter >= 4) {
                    npc(
                        FaceAnim.OLD_NORMAL,
                        "Well I'll be, you actually did it, private.",
                        "Now take a reward and get out of my sight.",
                    )
                    stage = 100
                } else if (eventStart) {
                    npc(
                        FaceAnim.OLD_NORMAL,
                        "You can get back to your business in a minute, private.",
                        "Now, listen up...",
                    ).also {
                        stage++
                    }
                    DrillDemonUtils.changeSignsAndAssignTask(player!!)
                    stage = 0
                    eventStart = false
                } else {
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        when (getAttribute(player!!, GameAttributes.DRILL_TASK, -1)) {
                            DrillDemonUtils.DD_SIGN_RUN -> if (!isCorrect) "Wrong exercise, worm! I want to see you jogging on the spot!" else "I want to see you jogging on the spot!"
                            DrillDemonUtils.DD_SIGN_JUMP -> if (!isCorrect) "Wrong exercise, worm! I want to see some star jumps, private!" else "I want to see some star jumps, private!"
                            DrillDemonUtils.DD_SIGN_PUSHUP -> if (!isCorrect) "Wrong exercise, worm! Drop and give me push ups, private!" else "Drop and give me push ups, private!"
                            DrillDemonUtils.DD_SIGN_SITUP -> if (!isCorrect) "Wrong exercise, worm! Get down and give me sit ups, private!" else "Get down and give me sit ups, private!"
                            else -> ""
                        },
                    )
                    stage = 1
                    unlock(player!!)
                }
            }

            1 -> {
                end()
                val prefix = "Go to$DARK_RED this mat</col>"
                when (getAttribute(player!!, GameAttributes.DRILL_TASK, -1)) {
                    DrillDemonUtils.DD_SIGN_RUN ->
                        sendItemDialogue(
                            player!!,
                            Items.RUN_10947,
                            "$prefix and jog on the spot!",
                        )
                    DrillDemonUtils.DD_SIGN_JUMP ->
                        sendItemDialogue(
                            player!!,
                            Items.STARJUMP_10949,
                            "$prefix and do some starjumps!",
                        )
                    DrillDemonUtils.DD_SIGN_PUSHUP ->
                        sendItemDialogue(
                            player!!,
                            Items.PUSHUP_10946,
                            "$prefix and do some pushups!",
                        )
                    DrillDemonUtils.DD_SIGN_SITUP ->
                        sendItemDialogue(
                            player!!,
                            Items.SITUP_10948,
                            "$prefix and do some sit ups!",
                        )
                }
            }

            100 -> {
                end()
                DrillDemonUtils.cleanup(player!!)
                DrillDemonUtils.reward(player!!)
            }
        }
    }
}
