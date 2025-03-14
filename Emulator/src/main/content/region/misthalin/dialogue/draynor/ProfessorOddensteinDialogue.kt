package content.region.misthalin.dialogue.draynor

import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.*

@Initializable
class ProfessorOddensteinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val quest = player.getQuestRepository().getQuest(Quests.ERNEST_THE_CHICKEN)
        when (quest.getStage(player)) {
            0, 10 -> {
                npc(FaceAnim.HALF_GUILTY, "Be careful in here, there's lots of dangerous equipment.")
                stage = 0
            }

            20 -> {
                npc(FaceAnim.HALF_GUILTY, "Have you found everything yet?")
                stage = 0
            }

            100 -> {
                options("What does this machine do?", "Is this your house?")
                stage = 1
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ERNEST_THE_CHICKEN)
        when (quest.getStage(player)) {
            0, 100 ->
                when (stage) {
                    0 -> {
                        options("What does this machine do?", "Is this your house?")
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player(FaceAnim.HALF_GUILTY, "What does this machine do?")
                                stage = 11
                            }

                            2 -> {
                                player(FaceAnim.HALF_GUILTY, "Is this your house?")
                                stage = 20
                            }
                        }

                    11 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Nothing at the moment... It's broken. It's meant to be",
                            "a transmutation machine.",
                        )
                        stage = 12
                    }

                    12 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It as also spent time as a time travel machine, and a",
                            "dramatic light generator, and a thing for generating",
                            "monsters.",
                        )
                        stage = 13
                    }

                    13 -> end()
                    20 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "No, I'm just one of the tenants. It belongs to the count",
                            "who lives in the basement.",
                        )
                        stage = 21
                    }

                    21 -> end()
                }

            10 ->
                when (stage) {
                    0 -> {
                        options(
                            "I'm looking for a guy called Ernest.",
                            "What does this machine do?",
                            "Is this your house?",
                        )
                        stage = 1
                    }

                    1 ->
                        when (buttonId) {
                            1 -> {
                                player(FaceAnim.HALF_GUILTY, "I'm looking for a guy called Ernest.")
                                stage = 2
                            }

                            2 -> {
                                player(FaceAnim.HALF_GUILTY, "What does this machine do?")
                                stage = 11
                            }

                            3 -> {
                                player(FaceAnim.HALF_GUILTY, "Is this your house?")
                                stage = 20
                            }
                        }

                    2 -> {
                        npc(FaceAnim.HALF_GUILTY, "Ah Ernest, top notch bloke. He's helping me with my", "experiments.")
                        stage = 3
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "So you know where he is then?")
                        stage = 4
                    }

                    4 -> {
                        npc(FaceAnim.HALF_GUILTY, "He's that chicken over there.")
                        stage = 5
                    }

                    5 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Ernest is a chicken..? Are you sure..?",
                        )
                        stage = 6
                    }

                    6 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "Oh, he isn't normally a chicken, or at least he wasn't",
                            "until he helped me test my pouletmorph machine.",
                        )
                        stage = 7
                    }

                    7 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It was originally going to be called a transmutation",
                            "machine. But after testing pouletmorph seems more",
                            "appropriate.",
                        )
                        stage = 8
                    }

                    8 -> {
                        player(FaceAnim.HALF_GUILTY, "I'm glad Veronica didn't actually get engaged to a", "chicken.")
                        stage = 9
                    }

                    9 -> {
                        npc(FaceAnim.HALF_GUILTY, "Who's Veronica?")
                        stage = 10
                    }

                    10 -> {
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Ernest's fiancee. She probably doesn't want to marry a",
                            "chicken.",
                        )
                        stage = 11
                    }

                    11 -> {
                        npc(FaceAnim.HALF_GUILTY, "Ooh I dunno. She could have free eggs for breakfast.")
                        stage = 12
                    }

                    12 -> {
                        player(FaceAnim.HALF_GUILTY, "I think you'd better change him back.")
                        stage = 13
                    }

                    13 -> {
                        npc(FaceAnim.HALF_GUILTY, "Umm... It's not so easy...")
                        stage = 14
                    }

                    14 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "My machine is broken, and the house gremlins have",
                            "run off with some vital bits.",
                        )
                        stage = 15
                    }

                    15 -> {
                        player(FaceAnim.HALF_GUILTY, "Well I can look for them.")
                        stage = 16
                    }

                    16 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "That would be a help. They'll be somewhere in the",
                            "manor house or its grounds, the gremlins never get",
                            "further than the entrance gate.",
                        )
                        stage = 17
                    }

                    17 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I'm missing the pressure guage and a rubber tube.",
                            "They've also taken my oil can, which I'm going to need",
                            "to get this thing started again.",
                        )
                        stage = 18
                    }

                    18 -> {
                        quest.setStage(player, 20)
                        end()
                    }
                }

            20 ->
                when (stage) {
                    0 ->
                        stage =
                            if (player.inventory.containsItem(OIL_CAN) &&
                                player.inventory.containsItem(
                                    PRESSURE_GAUGE,
                                ) &&
                                player.inventory.containsItem(RUBBER_TUBE)
                            ) {
                                player(FaceAnim.HALF_GUILTY, "I have everything!")
                                3
                            } else {
                                player(FaceAnim.HALF_GUILTY, "I'm afraid I'm still looking for them!")
                                1
                            }

                    1 -> {
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "I need a rubber tube, a pressure gauge and a can of",
                            "oil. Then your friend can stop being a chicken.",
                        )
                        stage = 2
                    }

                    2 -> end()
                    3 -> {
                        npc(FaceAnim.HALF_GUILTY, "Give 'em here then.")
                        stage = 4
                    }

                    4 -> {
                        end()
                        player.lock()
                        player.packetDispatch.sendMessage("You give a rubber tube, a pressure gauge,")
                        player.packetDispatch.sendMessage("and a can of oil to the professor.")
                        player.packetDispatch.sendMessage("Oddenstein starts up the machine.")
                        val chicken = findNPC(288)
                        Pulser.submit(
                            object : Pulse(1, player) {
                                var counter = 0

                                override fun pulse(): Boolean {
                                    when (counter++) {
                                        1 -> {
                                            npc.animate(ANIMATION)
                                            npc.faceLocation(FACE_LOCATION)
                                        }

                                        2 -> {
                                            player.packetDispatch.sendMessage("The machine hums and shakes.")
                                            npc.graphics(WEAKEN_START)
                                            Projectile.create(npc, chicken, 106, 40, 36, 52, 75, 15, 11)
                                        }

                                        4 -> {
                                            val ernest = NPC.create(NPCs.ERNEST_287, chicken!!.location)
                                            ernest.setAttribute("target", player)
                                            ernest.init()
                                            setAttribute(player, "ernest-hide", true)
                                            if (player.inventory.remove(OIL_CAN, PRESSURE_GAUGE, RUBBER_TUBE)) {
                                                player.dialogueInterpreter.open(NPCs.ERNEST_287, ernest)
                                            }
                                        }

                                        5 -> {
                                            npc.graphics(WEAKEN_END)
                                            return true
                                        }
                                    }
                                    return false
                                }
                            },
                        )
                    }
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ProfessorOddensteinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PROFESSOR_ODDENSTEIN_286)
    }

    companion object {
        private val OIL_CAN = Item(Items.OIL_CAN_277)
        private val PRESSURE_GAUGE = Item(Items.PRESSURE_GAUGE_271)
        private val RUBBER_TUBE = Item(Items.RUBBER_TUBE_276)
        private val ANIMATION = Animation(Animations.HUMAN_MULTI_USE_832)
        private val FACE_LOCATION = Location.create(3112, 3367, 2)
        private val WEAKEN_START = Graphics(org.rs.consts.Graphics.WEAKEN_CAST_105, 96)
        private val WEAKEN_END = Graphics(org.rs.consts.Graphics.WEAKEN_IMPACT_107, 96)
    }
}
