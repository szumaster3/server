package content.global.travel.balloon

import content.global.travel.balloon.routes.BalloonRoutes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class BalloonFlightInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        onClose(Components.ZEP_INTERFACE_470) { player, _ ->
            closeSingleTab(player)
            return@onClose true
        }

        onOpen(Components.ZEP_INTERFACE_470) { player, _ ->
            openSingleTab(player, Components.ZEP_INTERFACE_SIDE_471)

            val currentRouteId = player.getAttribute<Int>("zep_current_route") ?: 1
            player.setAttribute("zep_current_route", currentRouteId)

            if (player.getAttribute<Int>("zep_current_step_$currentRouteId") == null) {
                player.setAttribute("zep_current_step_$currentRouteId", 1)
            }

            val routeData = BalloonRoutes.routes[currentRouteId] ?: return@onOpen true

            val stage = 1
            val base = routeData.startPosition[stage - 1]

            player.setAttribute("zep_balloon_top_${currentRouteId}_$stage", base.top)
            player.setAttribute("zep_balloon_bottom_${currentRouteId}_$stage", base.bottom)

            sendModelOnInterface(player, Components.ZEP_INTERFACE_470, base.top, 19517)    // top
            sendModelOnInterface(player, Components.ZEP_INTERFACE_470, base.bottom, 19518) // bottom

            routeData.firstOverlay.invoke(player, Components.ZEP_INTERFACE_470)
            return@onOpen true
        }

        on(Components.ZEP_INTERFACE_SIDE_471) { player: Player, _, _, buttonID: Int, _, _ ->
            val currentRouteId = player.getAttribute<Int>("zep_current_route") ?: 1
            val routeData = BalloonRoutes.routes[currentRouteId] ?: return@on true

            val currentStep = player.getAttribute<Int>("zep_current_step_$currentRouteId") ?: 1
            val progressAttr = "zep_sequence_progress_${currentRouteId}_$currentStep"
            val index = player.getAttribute<Int>(progressAttr) ?: 0

            // X // amount of sandbags? // Timer? // it seems to have a life of its own.
            setVarbit(player, 2880, amountInInventory(player, Items.SANDBAG_9943))
            // Y // amount of logs? // Timer? // it seems to have a life of its own.
            setVarbit(player, 2881, amountInInventory(player, Items.LOGS_1511))

            val requiredSequence = when (currentStep) {
                1 -> routeData.firstSequence
                2 -> routeData.secondSequence
                3 -> routeData.thirdSequence
                else -> emptyList()
            }

            val delta = when (buttonID) {
                4 -> 41    // sandbag  -> moves NE +2
                9 -> 20    // logs     -> moves NE +1
                5 -> 1     // relax    -> moves E  +1
                6 -> -19   // rope     -> moves SE +1
                10 -> -38  // red rope -> moves SE +2
                else -> 0
            }

            // If the button does NOT match the sequence -> reset.
            if (buttonID != requiredSequence.getOrNull(index) || /*Bail button*/buttonID == 8)
            {
                closeInterface(player)
                closeSingleTab(player)
                removeAttribute(player,"zep_current_route")
                removeAttribute(player,"zep_current_step_$currentRouteId")
                removeAttribute(player,progressAttr)
                removeAttribute(player,"zep_balloon_top_${currentRouteId}_$currentStep")
                removeAttribute(player,"zep_balloon_bottom_${currentRouteId}_$currentStep")
                return@on true
            }

           drawBalloon(player, delta, currentRouteId, currentStep)

            if (buttonID == requiredSequence.getOrNull(index)) {
                val newIndex = index + 1
                player.setAttribute(progressAttr, newIndex)

                if (newIndex >= requiredSequence.size) {
                    player.removeAttribute(progressAttr)

                    when (currentStep) {
                        1 -> {
                            player.setAttribute("zep_current_step_$currentRouteId", 2)
                            routeData.secondOverlay(player, Components.ZEP_INTERFACE_470)
                        }
                        2 -> {
                            player.setAttribute("zep_current_step_$currentRouteId", 3)
                            routeData.thirdOverlay(player, Components.ZEP_INTERFACE_470)
                        }
                        3 -> {
                            player.removeAttribute("zep_current_step_$currentRouteId")
                            closeInterface(player)
                            teleport(player, Location(2940, 3420, 0))
                            openDialogue(player, object : DialogueFile() {
                                override fun handle(componentID: Int, buttonID: Int) {
                                    npc = core.game.node.entity.npc.NPC(NPCs.AUGUSTE_5049)
                                    when (stage) {
                                        0 -> playerl(FaceAnim.FRIENDLY, "So what are you going to do now?").also { stage++ }
                                        1 -> npcl(FaceAnim.FRIENDLY, "I am considering starting a balloon enterprise. People all over ${GameWorld.settings?.name} will be able to travel in a new, exciting way.").also { stage++ }
                                        2 -> npcl(FaceAnim.FRIENDLY, "As my first assistant, you will always be welcome to use a balloon. You'll have to bring your own fuel, though.").also { stage++ }
                                        3 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
                                        4 -> npcl(FaceAnim.FRIENDLY, "I will base my operations in Entrana. If you'd like to travel to new places, come see me there.").also { stage++ }
                                        5 -> {
                                            end()
                                            finishQuest(player, Quests.ENLIGHTENED_JOURNEY)
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }

            return@on true
        }
    }

    companion object {
        /**
         * Draws the balloon in the new position and removes the old one.
         */
        fun drawBalloon(player: Player, delta: Int, routeId: Int, stage: Int) {
            val routeData = BalloonRoutes.routes[routeId] ?: return
            val base = routeData.startPosition[stage - 1]

            // Get the last positions of the balloon.
            val lastTop = player.getAttribute<Int>("zep_balloon_top_${routeId}_$stage") ?: base.top
            val lastBottom = player.getAttribute<Int>("zep_balloon_bottom_${routeId}_$stage") ?: base.bottom

            // Remove the old balloon.
            sendModelOnInterface(player, Components.ZEP_INTERFACE_470, lastTop, -1)
            sendModelOnInterface(player, Components.ZEP_INTERFACE_470, lastBottom, -1)

            // Apply +1 offset to top only on the first click
            val offsetTop = if (lastTop == base.top) 1 else 0

            // Calculate the new positions.
            val newTop = lastTop + delta + offsetTop
            val newBottom = lastBottom + delta

            // Draw the new balloon.
            sendModelOnInterface(player, Components.ZEP_INTERFACE_470, newTop, 19517)
            sendModelOnInterface(player, Components.ZEP_INTERFACE_470, newBottom, 19518)

            // Save the new positions back to player attributes.
            player.setAttribute("zep_balloon_top_${routeId}_$stage", newTop)
            player.setAttribute("zep_balloon_bottom_${routeId}_$stage", newBottom)

            // player.debug("Top draw at=[$newTop]")
            // player.debug("Bottom draw at=[$newBottom]")
        }
    }
}
