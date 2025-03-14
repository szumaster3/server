package content.global.ame.rickturpentine

import core.api.addItemOrDrop
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.system.timer.impl.AntiMacro

class RickTurpentineDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Today is your lucky day, " + (if (player!!.isMale) "sirrah!" else "madam!") +
                        " I am donating to the victims of crime to atone for my past actions!",
                ).also { stage++ }
            1 -> {
                AntiMacro.rollEventLoot(player!!).forEach { addItemOrDrop(player!!, it.id, it.amount) }
                AntiMacro.terminateEventNpc(player!!)
                end()
            }
        }
    }
}
