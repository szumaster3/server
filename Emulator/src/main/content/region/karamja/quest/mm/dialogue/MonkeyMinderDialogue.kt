package content.region.karamja.quest.mm.dialogue

import core.api.openOverlay
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Components

class MonkeyMinderDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Ook Ook!").also { stage++ }
            1 ->
                npcl(
                    "Why do you monkeys keep trying to scape? Good thing I've caught you before you got away, you little scoundrel.",
                ).also {
                    stage++
                }
            2 -> playerl("Ook!").also { stage++ }
            3 -> npcl("Let's put you back in you cage where you belong...").also { stage++ }
            4 -> playerl("Ok!").also { stage++ }
            5 -> npcl("What??").also { stage++ }
            6 -> playerl("Err ... Ook?").also { stage++ }
            7 -> npcl("I must be imagining things ... monkeys can't talk.").also { stage++ }
            8 -> {
                openOverlay(player!!, Components.FADE_TO_BLACK_120)
                GameWorld.Pulser.submit(
                    object : Pulse(8) {
                        override fun pulse(): Boolean {
                            openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                            teleport(player!!, Location.create(2603, 3279, 0)).also {
                                stage = END_DIALOGUE
                                end()
                            }
                            return true
                        }
                    },
                )
            }
        }
    }
}
