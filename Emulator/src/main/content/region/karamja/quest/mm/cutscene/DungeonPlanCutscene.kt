package content.region.karamja.quest.mm.cutscene

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

class DungeonPlanCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(Location.create(2804, 9144, 0)))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10895)
        addNPC(NPCs.GLO_CARANOCK_1427, 48, 15, Direction.WEST)
        addNPC(NPCs.WAYDAR_1408, 46, 15, Direction.EAST)
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
                dialogueUpdate(NPCs.GLO_CARANOCK_1427, FaceAnim.NEUTRAL, "It is good of you to meet me, Waydar.")
                timedUpdate(6)
            }

            2 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "It is good to see you again, Caranock. It is a strange island these monkeys inhabit.",
                )
                timedUpdate(6)
            }

            3 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Well observed. How have you been keeping yourself occupied?",
                )
                timedUpdate(6)
            }

            4 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "I am now a Flight Commander. My duties include testing Glough's prototype military glider.",
                )
                timedUpdate(6)
            }

            5 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "My my. How things have changed somewhat since Glough's time... Now, what of the human?",
                )
                timedUpdate(6)
            }

            6 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "The human? Just somebody Narnode appears to have taken a fancy to. It is hard to tell you why. I suspect the human was involved with Glough's fall from grace.",
                )
                timedUpdate(6)
            }

            7 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "You may be right. Never mind - There are greater matters afoot. With Glough's gone, it falls to us to continue with his plans.",
                )
                timedUpdate(6)
            }

            8 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Also, the shipyard workers are becoming restless.",
                )
                timedUpdate(6)
            }

            9 -> {
                dialogueUpdate(NPCs.WAYDAR_1408, FaceAnim.NEUTRAL, "I see. What do you have in mind?")
                timedUpdate(6)
            }

            10 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Money for me, Waydar, and promotion for you. As you know, the 10th squad of the Royal Guard are slightly worse for wear on this island.",
                )
                timedUpdate(6)
            }

            11 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "This i know. But i don't see how it leads to money or promotion.",
                )
                timedUpdate(6)
            }

            12 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "What if they were to die? An entire squad of the Royal Guard goes missing in the jungle of Karamja ... We could blame it on the humans.",
                )
                timedUpdate(6)
            }

            13 -> {
                dialogueUpdate(NPCs.WAYDAR_1408, FaceAnim.NEUTRAL, "Narnode would be furious!")
                timedUpdate(6)
            }

            14 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Precisely. He might even order an invasion. at the very last he'll step up the defence. More order for me, promotion for you.",
                )
                timedUpdate(6)
            }

            15 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "Very clever. It might also serve us well to remind Narnode of Bolren's situation.",
                )
                timedUpdate(6)
            }

            16 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "Ah yes - All that trouble with the Khazard. Last I heard, Bolren had retrieved the orbs of protection. Apparently some human lent their assistance.",
                )
                timedUpdate(6)
            }

            17 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "Really? Typically meddling human behaviour. Nevertheless, it will stoke fires of worry. After all, the battle still continues.",
                )
                timedUpdate(6)
            }

            18 -> {
                dialogueUpdate(
                    NPCs.GLO_CARANOCK_1427,
                    FaceAnim.NEUTRAL,
                    "I agree. Anyhow, we don't want your human wondering as to your whereabouts. When the time is right, don't hesitate to ... dispose of it.",
                )
                timedUpdate(6)
            }

            19 -> {
                dialogueUpdate(
                    NPCs.WAYDAR_1408,
                    FaceAnim.NEUTRAL,
                    "Understood. Military gliders are after all an untested form of transport...",
                )
                timedUpdate(6)
                end()
                exitLocation = Location.create(2804, 9144, 0)
            }
        }
    }
}
