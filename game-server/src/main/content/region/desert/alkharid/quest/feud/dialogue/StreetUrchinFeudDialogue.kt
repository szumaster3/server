package content.region.desert.alkharid.quest.feud.dialogue

import core.api.setVarbit
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Represents the Street Urchin dialogue (The Feud quest).
 */
class StreetUrchinFeudDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.STREET_URCHIN_1868)
        when (stage) {
            0 -> player("Hello there!").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "What do you want? Information?").also { stage++ }
            2 -> player("What would you know?").also { stage++ }
            3 -> npc("No one knows what's going on in the streets better", "than me. I live, sleep, eat and drink here. The fact that", "I'm so small means that people tend to ignore me which", "helps even more.").also { stage++ }
            4 -> npc("Well do you need any other information then?").also { stage++ }
            5 -> options("Yes, tell me about...", "No thanks.").also { stage++ }
            6 -> when (buttonID) {
                1 -> player("Yes, tell me about...").also { stage = 9 }
                2 -> player("No thanks.").also { stage++ }
            }

            7 -> npcl(FaceAnim.FRIENDLY, "Come back if you need any info about anything in the town.").also { stage++ }
            8 -> player(FaceAnim.FRIENDLY, "I will do thanks.").also { stage = END_DIALOGUE }
            9 -> options(
                "The Menaphites",
                "The bandits",
                "The Mayor",
                "Ali Morrisane's nephew",
                "The town"
            ).also { stage++ }

            10 -> when (buttonID) {
                1 -> player("Tell me about the Menaphites.").also { stage = 11 }
                2 -> player("Tell me about the bandits.").also { stage = 20 }
                3 -> player("Tell me about the mayor.").also { stage = 30 }
                4 -> player("Tell me about Ali Morrisane's nephew.").also { stage = 40 }
                5 -> player("Tell me about the town.").also { stage = 50 }
            }

            // Tell me about the Menaphites.
            11 -> npc("They're a bad bunch, always starting fights down in the Asp and Snake.", "Word on the street is that they're led by some deranged priest.", "Though nobody gets to see him as he deals through Rashid the Operator.", "They are planning something but they're such a close knit group that little ever slips out.").also { stage = 4 }
            // Tell me about the bandits.
            20 -> npc("There's not much really to say about them, they're just a group of local thugs", "led by the largest amongst them. They don't harbour the ambitions of the Menaphites.").also { stage = 4 }
            // Tell me about the mayor.
            30 -> npcl(FaceAnim.NEUTRAL, "The mayor is a spineless coward. The current state of the town is all his fault. He didn't stand up to the Menaphites when they first came to town. When he finally realised his mistake he hired a group of thugs to try get rid of them.").also { stage++ }
            31 -> npc("When they discovered him to be weak they turned against him too. Thus causing an even larger mess.").also {
                stage = 4
            }
            // Tell me about nephew.
            40 -> npc("He was ok, although he used to kick us when he caught", "any of us stealing from his stall.").also { stage++ }
            41 -> player("Do you know where he is now then?").also { stage++ }
            42 -> npc("He disappeared around a week ago, he was involved in", "some dodgy business with both the gangs. I think he", "scammed them. Whether he got away with it or they", "got him I don't know, but I reckon if you gained either").also { stage++ }
            43 -> {
                npc("of the gangs' trust they may let you in on some more", "info.")
                setVarbit(player!!, Vars.VARBIT_THE_FEUD_PROGRESS_334, 3, true)
                stage = 4
            }
            // Tell me about the town.
            50 -> npc("Not much to say about it. Pollivneach is a small town located between Al Kharid and Menaphos " + "and has a particularly bad crime problem. Besides that nothing much happens here.").also { stage = 4 }
        }
    }
}
