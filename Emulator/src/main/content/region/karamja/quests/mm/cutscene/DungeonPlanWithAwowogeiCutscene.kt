package content.region.karamja.quests.mm.cutscene

import core.game.activity.Cutscene
import core.game.component.Component
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.NPCs

class DungeonPlanWithAwowogeiCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(Location.create(2805, 2760, 0)))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10895)
        addNPC(NPCs.GLO_CARANOCK_1427, 48, 15, Direction.WEST)
        addNPC(NPCs.WAYDAR_1408, 46, 15, Direction.EAST)
        addNPC(NPCs.AWOWOGEI_1448, 47, 15, Direction.NORTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                player.interfaceManager.open(Component(Components.QUEST_COMPLETE_SCROLL_277))
                for (i in 0..17) {
                    when (i) {
                        9 ->
                            player.packetDispatch.sendString(
                                "Meanwhile, somewhere far below the Ape Atoll...",
                                Components.QUEST_COMPLETE_SCROLL_277,
                                i,
                            )

                        else -> player.packetDispatch.sendString("", Components.QUEST_COMPLETE_SCROLL_277, i)
                    }
                }

                GameWorld.Pulser.submit(
                    object : Pulse(4) {
                        override fun pulse(): Boolean {
                            teleport(player, 47, 25)
                            moveCamera(47, 20)
                            rotateCamera(47, 15, 65)
                            return true
                        }
                    },
                )

                GameWorld.Pulser.submit(
                    object : Pulse(6) {
                        override fun pulse(): Boolean {
                            fadeFromBlack()
                            player.interfaceManager.close(Component(Components.QUEST_COMPLETE_SCROLL_277))
                            timedUpdate(4)
                            return true
                        }
                    },
                )
            }

            1 -> {
                dialogueUpdate(NPCs.GLO_CARANOCK_1427, FaceAnim.NEUTRAL, "Good evening, Awowogei.")
                timedUpdate(6)
            }

            2 -> {
                dialogueUpdate(
                    NPCs.AWOWOGEI_1448,
                    FaceAnim.NEUTRAL,
                    "It is always dark here, Gnome. Why have you asked to see me in private?",
                )
                timedUpdate(4)
            }

            3 -> {
                dialogueUpdate(NPCs.WAYDAR_1408, FaceAnim.NEUTRAL, "Caranock and I have a suggestion to make.")
                timedUpdate(6)
            }

            4 -> {
                dialogueUpdate(NPCs.AWOWOGEI_1448, FaceAnim.NEUTRAL, "Then be quick about it.")
                timedUpdate(6)
            }

            5 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "The foot soldiers of the Royal Guard in your jail...",
                )
                timedUpdate(6)
            }

            6 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Would it not be easier if they were somehow just to ... die?",
                )
                timedUpdate(6)
            }

            7 -> {
                dialogueUpdate(
                    NPCs.AWOWOGEI_1448,
                    FaceAnim.NEUTRAL,
                    "Why would I want to do that? Your king would declare war on my island.",
                )
                timedUpdate(6)
            }

            8 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "I assure you he will not. We will lay the blame at the human's feet.",
                )
                timedUpdate(4)
            }

            9 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Narnode will indeed declare war: not against you, but against humankind.",
                )
                timedUpdate(4)
            }

            10 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "You are of course welcome to your share of the profits.",
                )
                timedUpdate(6)
            }

            11 -> {
                dialogueUpdate(
                    NPCs.AWOWOGEI_3396,
                    FaceAnim.NEUTRAL,
                    "Intriguing. I have recently secured an alliance with the northern monkeys, which may prove useful.",
                )
                timedUpdate(4)
            }

            12 -> {
                dialogueUpdate(NPCs.AWOWOGEI_1448, FaceAnim.NEUTRAL, "What would you have me do?")
                timedUpdate(6)
            }

            13 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Kill the foot soldiers and the rest of the 10th squad. My superior has sent you a few tricks which may prove useful.",
                )
                timedUpdate(4)
            }

            14 -> {
                dialogueUpdate(NPCs.AWOWOGEI_1448, FaceAnim.NEUTRAL, "Such as?")
                timedUpdate(6)
            }

            15 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "High magic: the ability to summon the entire 10th squad to a single location. And - ",
                )
                timedUpdate(4)
            }

            16 -> {
                dialogueUpdate(NPCs.AWOWOGEI_1448, FaceAnim.NEUTRAL, "Even those who escaped?")
                timedUpdate(6)
            }

            17 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Yes. And of course, you will also receive access to one of his 'pets'.",
                )
                timedUpdate(4)
            }

            18 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "You must be careful with these, as you have only one use of each. Ensure you set your trap well - none must survive lest they spread the truth.",
                )
                timedUpdate(6)
            }

            19 -> {
                dialogueUpdate(NPCs.WAYDAR_1408, FaceAnim.NEUTRAL, "What of my human?")
                timedUpdate(6)
            }

            20 -> {
                dialogueUpdate(NPCs.AWOWOGEI_1448, FaceAnim.NEUTRAL, "What human??")
                timedUpdate(6)
            }

            21 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Ignore ${if (player.isMale) "him" else "her"}. My colleague's official mission was to look after a human in the area, but don't worry: it is probably dead already.",
                )
                timedUpdate(6)
            }

            22 -> {
                dialogueUpdate(NPCs.AWOWOGEI_1448, FaceAnim.NEUTRAL, "I should hope so, for both of your sakes.")
                timedUpdate(6)
            }

            23 -> {
                dialogueUpdate(
                    NPCs.AWOWOGEI_1448,
                    FaceAnim.NEUTRAL,
                    "Very well. I shall let you know when I have dealt with the Royal Guard.",
                )
                timedUpdate(6)
            }

            24 -> {
                dialogueUpdate(NPCs.GLO_CARANOCK_1427, FaceAnim.NEUTRAL, "Good luck, Awowogei.")
                timedUpdate(6)
            }

            25 -> {
                dialogueUpdate(
                    NPCs.AWOWOGEI_1448,
                    FaceAnim.NEUTRAL,
                    "With success to one of Glough's 'pets', I don't think I'll need it...",
                )
                timedUpdate(4)
                end()
                exitLocation = Location.create(2805, 2760, 0)
            }
        }
    }
}
