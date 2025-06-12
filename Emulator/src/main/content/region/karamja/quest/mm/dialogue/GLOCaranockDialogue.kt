package content.region.karamja.quest.mm.dialogue

import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE
import org.rs.consts.Quests

class GLOCaranockDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> npcl("Who are you? Did Glough send you?").also { stage++ }
            1 -> playerl("Glough? No. He has been forced to resign by the king.").also { stage++ }
            2 -> npcl("Forced to resign??").also { stage++ }
            3 -> playerl("He was plotting to start a war between the Gnomes and the humankind.").also { stage++ }
            4 ->
                playerl(
                    "Anyway, I am here on a separate mission. I am investigating the mysterious disappearance of the 10th squad of kings Narnode's Royal Guard. They were to carry out some work in the area.",
                ).also {
                    stage++
                }
            5 -> npcl("Royal Guard? I know nothing about them. Absolutely nothing.").also { stage++ }
            6 -> playerl("You have no idea why they mysteriously disappeared?").also { stage++ }
            7 -> npcl("None whatsoever. What were they here to do?").also { stage++ }
            8 -> playerl("They were to oversee the decommission of the shipyard.").also { stage++ }
            9 ->
                npcl(
                    "Decommission the shipyard ... I see. Well, we have had some seriously strong southerly winds of late. They may have been blown off course during flight.",
                ).also {
                    stage++
                }
            10 ->
                npcl("I shall see personally to the decommission. You should report to the king immediately.").also {
                    setQuestStage(player!!, Quests.MONKEY_MADNESS, 11)
                    stage = END_DIALOGUE
                }
        }
    }
}
