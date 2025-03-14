package content.region.kandarin.quest.fishingcompo.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SinisterStrangerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("...")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options("...?", "Who are you?", "So... you like fishing?")
                stage++
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("...?")
                        stage = 10
                    }

                    2 -> {
                        player("Who are you?")
                        stage = 20
                    }

                    3 -> {
                        player("So... you like fishing?")
                        stage = 30
                    }
                }

            2 -> {
                options(
                    "You're a vampire aren't you?",
                    "Is it nice there?",
                    "So you like fishing?",
                )
                stage++
            }

            3 ->
                when (buttonId) {
                    1 -> {
                        player("You're a vampire aren't you?")
                        stage = 21
                    }

                    2 -> {
                        player("Is it nice there?")
                        stage = 50
                    }

                    3 -> {
                        player("So you like fishing?")
                        stage = 30
                    }
                }

            4 -> {
                options(
                    "You're a vampire aren't you?",
                    "So you like fishing?",
                    "Well, good luck with the fishing.",
                )
                stage++
            }

            5 ->
                when (buttonId) {
                    1 -> {
                        player("You're a vampire aren't you?")
                        stage = 21
                    }

                    2 -> {
                        player("So you like fishing?")
                        stage = 30
                    }

                    3 -> {
                        player("Well, good luck with the fishing.")
                        stage = 70
                    }
                }

            6 -> {
                interpreter.sendOptions(
                    "Choose an option:",
                    "You're a vampire aren't you?",
                    "If you get thirsty you should drink something.",
                    "Well, good luck with the fishing.",
                )
                stage++
            }

            7 ->
                when (buttonId) {
                    1 -> {
                        player("You're a vampire aren't you?")
                        stage = 21
                    }

                    2 -> {
                        player("If you get thirsty you should drink something.")
                        stage = 40
                    }

                    3 -> {
                        player("Well, good luck with the fishing.")
                        stage = 70
                    }
                }

            9 -> end()
            10 -> {
                npc("...")
                stage++
            }

            11 -> {
                player("......?")
                stage++
            }

            12 -> {
                npc("......")
                stage = 9
            }

            20 -> {
                npc("My name is Vlad.", "I come from far avay,", "vere the sun iz not so bright.")
                stage = 2
            }

            21 -> {
                npc(
                    "Just because I can't stand ze smell ov garlic",
                    "and I don't like bright sunlight doesn't",
                    "necessarily mean I'm ein vampire!",
                )
                stage = 9
            }

            30 -> {
                npc("My doctor told me to take up ein velaxing hobby.", "Vhen I am stressed I tend to get ein little")
                stage++
            }

            31 -> {
                npc("... thirsty.")
                stage = 6
            }

            40 -> {
                npc("I tsink I may do zat soon...")
                stage = 9
            }

            50 -> {
                npc("It is vonderful!", "Zev omen are beautiful und ze nights are long!")
                stage = 4
            }

            70 -> {
                npc("Luck haz notsing to do vith it.", "It is all in ze technique.")
                stage = 9
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SINISTER_STRANGER_3677)
    }
}
