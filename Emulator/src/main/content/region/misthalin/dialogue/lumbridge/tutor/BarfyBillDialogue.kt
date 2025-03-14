package content.region.misthalin.dialogue.lumbridge.tutor

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class BarfyBillDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                npc("Oh! Hello there.")
                stage = 1
            }

            1 -> {
                interpreter.sendOptions("Select an Option", "Who are you?", "Can you teach me about Canoeing?")
                stage = 2
            }

            2 ->
                when (buttonId) {
                    1 -> {
                        player("Who are you?")
                        stage = 1000
                    }

                    2 -> {
                        npc(
                            "It's really quite simple. Just walk down to that tree on",
                            "the water bank and chop it down.",
                        )
                        stage = 24
                    }
                }

            24 -> {
                npc("Then take your axe to it and shape it how you like!")
                stage = 26
            }

            26 -> {
                npc("You look like you know your way around a tree,", "you can make a canoe.")
                stage = 27
            }

            27 -> end()
            1000 -> {
                npc("My name is Ex Sea Captain Barfy Bill.")
                stage = 1001
            }

            1001 -> {
                player("Ex sea captain?")
                stage = 1002
            }

            1002 -> {
                npc(
                    "Yeah, I bought a lovely ship and was planning to make",
                    "a fortune running her as a merchant vessel.",
                )
                stage = 1003
            }

            1003 -> {
                player("Why are you not still sailing?")
                stage = 1004
            }

            1004 -> {
                npc("Chronic sea sickness. My first, and only, voyage was", "spent dry heaving over the rails.")
                stage = 1005
            }

            1005 -> {
                npc("If I had known about the sea sickness I could have", "saved myself a lot of money.")
                stage = 1006
            }

            1006 -> {
                player("What are you up to now then?")
                stage = 1007
            }

            1007 -> {
                npc("Well my ship had a little fire related problem.", "Fortunately it was well insured.")
                stage = 1008
            }

            1008 -> {
                npc("Anyways, I don't have to work anymore so I've taken to", "canoeing on the river.")
                stage = 1009
            }

            1009 -> {
                npc("I don't get river sick!")
                stage = 69
            }

            69 -> {
                npc("Would you like to know how to make a canoe?")
                stage = 6000
            }

            6000 -> {
                interpreter.sendOptions("Select an Option", "Yes", "No")
                stage = 504
            }

            504 ->
                when (buttonId) {
                    1 -> {
                        player("Could you teach me about canoes?")
                        stage = 560
                    }

                    2 -> end()
                }

            560 -> {
                npc("It's really quite simple. Just walk down to that tree on", "the water bank and chop it down.")
                stage = 24
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BarfyBillDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARFY_BILL_3331)
    }
}
