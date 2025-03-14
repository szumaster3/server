package content.region.kandarin.quest.biohazard.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs
import org.rs.consts.Quests

class NurseSarahDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.NURSE_SARAH_373)
        when (stage) {
            0 -> player("Hello nurse.").also { stage++ }
            1 ->
                if (getQuestStage(player!!, Quests.BIOHAZARD) <= 6) {
                    npc("I don't know how much longer I can cope here.").also { stage = 4 }
                } else {
                    npc(
                        "Oh hello there. I'm afraid I can't stop and talk, a",
                        "group of mourners have become ill with food poisoning. I need",
                        "to go over and see what I can do.",
                    ).also { stage++ }
                }

            2 -> npc("Hmmm, strange that!").also { stage++ }
            3 -> end()
            4 -> player("What? Is the plague getting to you?").also { stage++ }
            5 -> npc("No, strangely enough the people here don't seem to", "be affected.").also { stage++ }
            6 -> npc(" It's just the awful living conditions that is making people", "ill.").also { stage++ }
            7 -> player("I was under the impression that everyone here", "was affected.").also { stage++ }
            8 -> npc("Me too, but that doesn't seem to be the case.").also { stage++ }
            9 -> end()
        }
    }
}
