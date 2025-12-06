package content.region.desert.pollniveach.dialogue

import core.api.getVarbit
import core.api.setVarbit
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Vars

/**
 * Represents the Street Urchin dialogue.
 */
@Initializable
class StreetUrchinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val theFeud = getVarbit(player, Vars.VARBIT_THE_FEUD_PROGRESS_334)
        if (theFeud >= 2) {
            player("Hello there!").also { stage = 17 }
        } else {
            player("Hello there little fellow.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.CHILD_FRIENDLY, "I'm not little and if you say that again I'll gut you.").also { stage++ }
            1 -> player("Easy there, I didn't mean to offend.").also { stage++ }
            2 -> npcl(FaceAnim.CHILD_FRIENDLY,"I'll let you off this once. Well what do you want? Information?").also { stage++ }
            3 -> player("Yes, tell me about...").also { stage++ }
            4 -> options("The Menaphites", "The bandits", "The Mayor", "The town", "No thanks.").also { stage++ }
            5 -> when (buttonId) {
                1 -> player("Tell me about the Menaphites.").also { stage++ }
                2 -> player("Tell me about the bandits.").also { stage = 8 }
                3 -> player("Tell me about the mayor.").also { stage = 10 }
                4 -> player("Tell me about the town.").also { stage = 13 }
                5 -> player("No thanks.").also { stage = 15 }
            }
            // Tell me about the Menaphites.
            6 -> npc(FaceAnim.CHILD_FRIENDLY,"They're a bad bunch, always starting fights down in the Asp and Snake.", "Word on the street is that they're led by some deranged priest.", "Though nobody gets to see him as he deals through Rashid the Operator.", "They are planning something but they're such a close knit group that little ever slips out.").also { stage++ }
            7 -> npc(FaceAnim.CHILD_FRIENDLY,"Well do you need any other information then?").also { stage = 4 }
            // Tell me about the bandits.
            8 -> npc(FaceAnim.CHILD_FRIENDLY,"There's not much really to say about them, they're just a group of local thugs", "led by the largest amongst them. They don't harbour the ambitions of the Menaphites.").also { stage++ }
            9 -> npc(FaceAnim.CHILD_FRIENDLY,"Well do you need any other information then?").also { stage = 4 }
            // Tell me about the mayor.
            10 -> npcl(FaceAnim.CHILD_FRIENDLY, "The mayor is a spineless coward. The current state of the town is all his fault. He didn't stand up to the Menaphites when they first came to town. When he finally realised his mistake he hired a group of thugs to try get rid of them.").also { stage++ }
            11 -> npcl(FaceAnim.CHILD_FRIENDLY,"When they discovered him to be weak they turned against him too. Thus causing an even larger mess.").also { stage++ }
            12 -> npc(FaceAnim.CHILD_FRIENDLY,"Well do you need any other information then?").also { stage = 4 }
            // Tell me about the town.
            13 -> npcl(FaceAnim.CHILD_FRIENDLY,"Not much to say about it. Pollivneach is a small town located between Al Kharid and Menaphos " + "and has a particularly bad crime problem. Besides that nothing much happens here.").also { stage++ }
            14 -> npcl(FaceAnim.CHILD_FRIENDLY,"Well do you need any other information then?").also { stage = 4 }
            // No, thanks.
            15 -> npc(FaceAnim.CHILD_FRIENDLY,"Come back if you need any info about anything in the town.").also { stage++ }
            16 -> player("I will do, thanks.").also { stage = END_DIALOGUE }
            17 -> npcl(FaceAnim.CHILD_FRIENDLY, "What do you want? Information?").also { stage++ }
            18 -> player("What would you know?").also { stage++ }
            19 -> npc(FaceAnim.CHILD_FRIENDLY,"No one knows what's going on in the streets better", "than me. I live, sleep, eat and drink here. The fact that", "I'm so small means that people tend to ignore me which", "helps even more.").also { stage++ }
            20 -> npcl(FaceAnim.CHILD_FRIENDLY,"Well do you need any other information then?").also { stage++ }
            21 -> options("Yes, tell me about...", "No thanks.").also { stage++ }
            22 -> when (buttonId) {
                1 -> player("Yes, tell me about...").also { stage = 25 }
                2 -> player("No thanks.").also { stage++ }
            }
            23 -> npcl(FaceAnim.CHILD_FRIENDLY, "Come back if you need any info about anything in the town.").also { stage++ }
            24 -> player(FaceAnim.FRIENDLY, "I will do thanks.").also { stage = END_DIALOGUE }
            25 -> options(
                "The Menaphites",
                "The bandits",
                "The Mayor",
                "Ali Morrisane's nephew",
                "The town"
            ).also { stage++ }
            26 -> when (buttonId) {
                1 -> player("Tell me about the Menaphites.").also { stage++ }
                2 -> player("Tell me about the bandits.").also { stage = 28 }
                3 -> player("Tell me about the mayor.").also { stage = 29 }
                4 -> player("Tell me about Ali Morrisane's nephew.").also { stage = 31 }
                5 -> player("Tell me about the town.").also { stage = 35 }
            }
            // Tell me about the Menaphites.
            27 -> npc(FaceAnim.CHILD_FRIENDLY,"They're a bad bunch, always starting fights down in the Asp and Snake.", "Word on the street is that they're led by some deranged priest.", "Though nobody gets to see him as he deals through Rashid the Operator.", "They are planning something but they're such a close knit group that little ever slips out.").also { stage = 21 }
            // Tell me about the bandits.
            28 -> npc(FaceAnim.CHILD_FRIENDLY,"There's not much really to say about them, they're just a group of local thugs", "led by the largest amongst them. They don't harbour the ambitions of the Menaphites.").also { stage = 21 }
            // Tell me about the mayor.
            29 -> npcl(FaceAnim.CHILD_NORMAL, "The mayor is a spineless coward. The current state of the town is all his fault. He didn't stand up to the Menaphites when they first came to town. When he finally realised his mistake he hired a group of thugs to try get rid of them.").also { stage++ }
            30 -> npcl(FaceAnim.CHILD_FRIENDLY,"When they discovered him to be weak they turned against him too. Thus causing an even larger mess.").also { stage = 21 }
            // Tell me about nephew.
            31 -> npc(FaceAnim.CHILD_FRIENDLY,"He was ok, although he used to kick us when he caught", "any of us stealing from his stall.").also { stage++ }
            32 -> player("Do you know where he is now then?").also { stage++ }
            33 -> npc(FaceAnim.CHILD_FRIENDLY,"He disappeared around a week ago, he was involved in", "some dodgy business with both the gangs. I think he", "scammed them. Whether he got away with it or they", "got him I don't know, but I reckon if you gained either").also { stage++ }
            34 -> {
                npc(FaceAnim.CHILD_FRIENDLY, "of the gangs' trust they may let you in on some more", "info.")
                setVarbit(player!!, Vars.VARBIT_THE_FEUD_PROGRESS_334, 3, true)
                stage = 21
            }
            // Tell me about the town.
            35 -> npcl(FaceAnim.CHILD_FRIENDLY,"Not much to say about it. Pollivneach is a small town located between Al Kharid and Menaphos " + "and has a particularly bad crime problem. Besides that nothing much happens here.").also { stage = 21 }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = StreetUrchinDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.STREET_URCHIN_1868)
}