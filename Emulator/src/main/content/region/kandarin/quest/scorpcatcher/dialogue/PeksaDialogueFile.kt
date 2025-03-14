package content.region.kandarin.quest.scorpcatcher.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class PeksaDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.SCORPION_CATCHER)
        npc = NPC(NPCs.PEKSA_538)
        when {
            (questStage in 10..99) -> {
                when (stage) {
                    0 ->
                        npcl(
                            "Now how could you know about that, I wonder? Mind you, I don't have it anymore.",
                        ).also { stage++ }
                    1 ->
                        npcl(
                            " I gave it as a present to my brother Ivor when I visited our outpost in the west.",
                        ).also { stage++ }
                    2 ->
                        npcl(
                            "Well, actually I hid it in his bed so it would nip him. It was a bit of a surprise gift.",
                        ).also { stage++ }
                    3 -> options("So where's this outpost?", "Thanks for the information").also { stage++ }
                    4 ->
                        when (buttonID) {
                            1 -> playerl("So where's this outpost?").also { stage = 5 }
                            2 -> playerl("Thanks for the information").also { stage = 6 }
                        }

                    5 ->
                        npcl(
                            "Its a fair old trek to the west, across the White Wolf Mountains. Then head west, north-west until you see the axes and horned helmets.",
                        ).also {
                            stage++
                        }
                    6 -> npcl("No problems! Tell Ivor I said hi!").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
