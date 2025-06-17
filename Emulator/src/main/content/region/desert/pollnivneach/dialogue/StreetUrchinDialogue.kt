package content.region.desert.pollnivneach.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class StreetUrchinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello there little fellow.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npc("I'm not little and if you say that again I'll gut you.")
                stage = 1
            }
            1 -> {
                player("Easy there, I didn't mean to offend.")
                stage = 2
            }
            2 -> {
                npc("I'll let you off this once. Well what do you want? Information?")
                stage = 3
            }
            3 -> {
                player("Yes, tell me about...")
                stage = 4
            }
            4 -> {
                options("The Menaphites", "The bandits", "The Mayor", "The town", "No thanks.")
                stage = 5
            }
            5 -> {
                when (buttonId) {
                    1 -> {
                        player("Tell me about the Menaphites.")
                        stage = 10
                    }
                    2 -> {
                        player("Tell me about the bandits.")
                        stage = 20
                    }
                    3 -> {
                        player("Tell me about the mayor.")
                        stage = 30
                    }
                    4 -> {
                        player("Tell me about the town.")
                        stage = 40
                    }
                    5 -> {
                        player("No thanks.")
                        stage = 50
                    }
                }
            }
            // Tell me about the Menaphites.
            10 -> {
                npc("They're a bad bunch, always starting fights down in the Asp and Snake.",
                        "Word on the street is that they're led by some deranged priest.",
                        "Though nobody gets to see him as he deals through Rashid the Operator.",
                        "They are planning something but they're such a close knit group that little ever slips out.")
                stage = 11
            }
            11 -> {
                npc("Well do you need any other information then?")
                stage = 4
            }
            // Tell me about the bandits.
            20 -> {
                npc("There's not much really to say about them, they're just a group of local thugs",
                        "led by the largest amongst them. They don't harbour the ambitions of the Menaphites.")
                stage = 21
            }
            21 -> {
                npc("Well do you need any other information then?")
                stage = 4
            }
            // Tell me about the mayor.
            30 -> {
                npcl(FaceAnim.NEUTRAL, "The mayor is a spineless coward. The current state of the town is all his fault. He didn't stand up to the Menaphites when they first came to town. When he finally realised his mistake he hired a group of thugs to try get rid of them.")
                stage = 31
            }
            31 -> {
                npc("When they discovered him to be weak they turned against him too. Thus causing an even larger mess.")
                stage = 32
            }
            32 -> {
                npc("Well do you need any other information then?")
                stage = 4
            }
            // Tell me about the town.
            40 -> {
                npc("Not much to say about it. Pollivneach is a small town located between Al Kharid and Menaphos " +
                        "and has a particularly bad crime problem. Besides that nothing much happens here.")
                stage = 41
            }
            41 -> {
                npc("Well do you need any other information then?")
                stage = 4
            }
            // No, thanks.
            50 -> {
                npc("Come back if you need any info about anything in the town.")
                stage = 51
            }
            51 -> {
                player("I will do, thanks.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = StreetUrchinDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.STREET_URCHIN_6357)
}