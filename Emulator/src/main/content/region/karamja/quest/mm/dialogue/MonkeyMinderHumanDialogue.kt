package content.region.karamja.quest.mm.dialogue

import core.api.openOverlay
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Components

class MonkeyMinderHumanDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> npcl("My word - what are you doing in there?").also { stage++ }
            1 ->
                playerl(
                    "I ... er ... I don't know! One minute I was asleep, and the next minute I was here surrounded by monkeys!",
                ).also {
                    stage++
                }
            2 -> npcl("Well, don't worry. We'll have you out of there shortly.").also { stage++ }
            3 -> {
                openOverlay(player!!, Components.FADE_TO_BLACK_120)
                GameWorld.Pulser
                    .submit(
                        object : Pulse(8) {
                            override fun pulse(): Boolean {
                                openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                                teleport(player!!, Location.create(2605, 3274, 0))
                                return true
                            }
                        },
                    ).also { stage++ }
            }

            4 -> playerl("Thank you.").also { stage++ }
            5 ->
                npcl("No problem.").also {
                    stage = END_DIALOGUE
                    end()
                }
        }
    }
}
