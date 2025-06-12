package content.region.misthalin.draynor_village.quest.vampire.dialogue

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class MorganDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MORGAN_755)
        val questStage = getQuestStage(player!!, Quests.VAMPIRE_SLAYER)
        when (stage) {
            0 ->
                if (!isQuestComplete(
                        player!!,
                        Quests.VAMPIRE_SLAYER,
                    ) ||
                    questStage == 10 ||
                    questStage == 20 ||
                    questStage == 30
                ) {
                    npc(FaceAnim.HALF_GUILTY, "How are you doing with the quest?").also { stage = 10 }
                } else {
                    player(FaceAnim.HALF_GUILTY, "I have slain the foul creature!").also { stage = 101 }
                }

            10 -> player(FaceAnim.HALF_GUILTY, "I'm still working on it.").also { stage++ }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Please hurry! Every day we live in fear that we",
                    "the vampire's next victim!",
                ).also { stage = END_DIALOGUE }

            101 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Thank you, thank you! You will always be a hero in",
                    "our village!",
                ).also { stage = END_DIALOGUE }
        }
    }
}
