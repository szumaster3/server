package content.minigame.pestcontrol.dialogue

import content.global.travel.charter.Charter
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SquireDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args.size == 3) {
            val type = args[1] as Int
            when (type) {
                0 -> {
                    interpreter
                        .sendDialogues(
                            3781,
                            FaceAnim.HALF_GUILTY,
                            "The Void Knight was killed, another of our Order has",
                            "fallen and that Island is lost.",
                        ).also { stage = 110 }
                }

                1 -> {
                    val points = args[2] as String
                    interpreter
                        .sendDialogues(
                            3781,
                            FaceAnim.HAPPY,
                            "Congratulations! You managed to destroy all the portals!",
                            "We've awarded you $points Void Knight Commendation",
                            "points. Please also accept these coins as a reward.",
                        ).also { stage = 100 }
                }

                else -> {
                    interpreter
                        .sendDialogues(
                            3781,
                            FaceAnim.HALF_GUILTY,
                            "Congratulations! You managed to destroy all the portals!",
                            "However, you did not succeed in reaching the required",
                            "amount of damage dealt we cannot grant you a reward.",
                        ).also { stage = 101 }
                }
            }
            return true
        }
        npc = args[0] as NPC
        if (args.size == 2) {
            npc("Be quick, we're under attack!")
            stage = 699
            return true
        }
        interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "Hi, how can I help you?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (npc.id == 3781) {
                    interpreter.sendOptions(
                        "Select an Option",
                        "I'd like to go back to Port Sarim please.",
                        "I'm fine thanks.",
                    )
                    stage = 500
                    return true
                }
                options(
                    "Who are you?",
                    "Where does this ship go?",
                    "I'd like to go to your outpost.",
                    "I'm fine thanks.",
                )
                stage = 1
            }

            500 ->
                when (buttonId) {
                    1 -> {
                        interpreter.sendDialogues(player, null, "I'd like to go back to Port Sarim please.")
                        stage = 502
                    }

                    2 -> {
                        interpreter.sendDialogues(player, null, "I'm fine thanks.")
                        stage = 501
                    }
                }

            501 -> end()
            502 -> {
                interpreter.sendDialogues(npc, null, "Ok, but please come back soon and help us.")
                stage = 503
            }

            503 -> {
                end()
                Charter.PEST_TO_PORT_SARIM.sail(player)
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "Who are you?")
                        stage = 10
                    }

                    2 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "Where does this ship go?")
                        stage = 20
                    }

                    3 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "I'd like to go to your outpost.")
                        stage = 30
                    }

                    4 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "I'm fine thanks.")
                        stage = 40
                    }
                }

            10 -> {
                interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "I'm a Squire for the Void Knights.")
                stage = 11
            }

            11 -> {
                interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "The who?")
                stage = 12
            }

            12 -> {
                interpreter.sendDialogues(
                    npc,
                    FaceAnim.HALF_GUILTY,
                    "The Void Knights, they are great warriors of balance",
                    "who do Guthix's work here in " + settings!!.name + ".",
                )
                stage = 13
            }

            13 -> end()
            20 -> {
                interpreter.sendDialogues(
                    npc,
                    FaceAnim.HALF_GUILTY,
                    "To the Void Knight outpost. It's a small island just off",
                    "Karamja.",
                )
                stage = 21
            }

            21 -> {
                interpreter.sendOptions("Select an Option", "I'd like to go to your outpost.", "That's nice.")
                stage = 22
            }

            22 ->
                when (buttonId) {
                    1 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "I'd like to go to your outpost.")
                        stage = 23
                    }

                    2 -> {
                        interpreter.sendDialogues(player, FaceAnim.HALF_GUILTY, "That's nice.")
                        stage = 200
                    }
                }

            23 -> {
                interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "Certainly, right this way.")
                stage = 24
            }

            24 -> {
                end()
                Charter.PORT_SARIM_TO_PEST_CONTROL.sail(player)
            }

            200 -> end()
            30 -> {
                interpreter.sendDialogues(npc, FaceAnim.HALF_GUILTY, "Certainly, right this way.")
                stage = 24
            }

            40 -> end()
            699 -> {
                options(
                    "What's going on?",
                    "How do I repair things?",
                    "I want to leave.",
                    "I'd better get back to it then.",
                )
                stage = 700
            }

            700 ->
                when (buttonId) {
                    1 -> {
                        player("What's going on?")
                        stage = 710
                    }

                    2 -> {
                        player("How do I repair things?")
                        stage = 720
                    }

                    3 -> {
                        player("I want to leave.")
                        stage = 730
                    }

                    4 -> {
                        player("I'd better get back to it then.")
                        stage = 740
                    }
                }

            710 -> {
                npc(
                    "This island is being invaded by outsiders and the Void",
                    "Knight over there is using a ritual to unsummon their",
                    "portals. We must defend the Void Knight at all costs.",
                    "however if you get an opening you can destroy the portals.",
                )
                stage = 711
            }

            711 -> end()
            720 -> {
                npc(
                    "There are trees on the island. You'll need to chop them",
                    "down for logs and use a hammer to repair the defences.",
                    "Be careful tough, the trees here don't grow back very",
                    "fast so your consts are limited!",
                )
                stage = 721
            }

            721 -> end()
            730 -> end()
            740 -> end()
            100 -> {
                interpreter.sendDialogue(
                    BLUE + "You now have " + RED + "" + player.getSavedData().activityData.pestPoints + "" + BLUE +
                        " Void Knight Commendation points!</col>",
                    "You can speak to a Void Knight to exchange your points for",
                    "rewards.",
                )
                stage = 101
            }

            101 -> end()
            110 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SQUIRE_3781, NPCs.SQUIRE_3790)
    }
}
