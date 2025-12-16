package content.global.travel.balloon

import content.global.travel.balloon.routes.BalloonRoutes
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.Items

class BalloonFlightInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        onClose(Components.ZEP_INTERFACE_470) { player, _ ->
            openInterface(player, Components.FADE_FROM_BLACK_170)
            closeSingleTab(player)
            return@onClose true
        }

        onOpen(Components.ZEP_INTERFACE_470) { player, _ ->
            openSingleTab(player, Components.ZEP_INTERFACE_SIDE_471)

            val routeId = getAttribute(player, "zep_current_route", 1)
            setAttribute(player, "zep_current_route", routeId)

            val currentId = "zep_current_step_$routeId"
            val step = getAttribute(player, currentId, 1)
            setAttribute(player, currentId, step)

            BalloonHelper.drawBaseBalloon(player, routeId, step)
            BalloonRoutes.routes[routeId]
                ?.firstOverlay
                ?.invoke(player, Components.ZEP_INTERFACE_470)

            return@onOpen true
        }

        on(Components.ZEP_INTERFACE_SIDE_471) { player: Player, _, _, buttonID: Int, _, _ ->
            val routeId = getAttribute(player, "zep_current_route", -1)
            if (routeId == -1) return@on true

            val routeData = BalloonRoutes.routes[routeId] ?: return@on true

            val stepAttribute = "zep_current_step_$routeId"
            val step = getAttribute(player, stepAttribute, 1)

            val sequenceProgressAttribute = "zep_sequence_progress_${routeId}_$step"
            val index = getAttribute(player, sequenceProgressAttribute, 0)

            setVarbit(player, 2880, amountInInventory(player, Items.SANDBAG_9943))
            setVarbit(player, 2881, amountInInventory(player, Items.LOGS_1511))

            val sequence = when (step) {
                1 -> routeData.firstSequence
                2 -> routeData.secondSequence
                3 -> routeData.thirdSequence
                else -> emptyList()
            }

            val delta = when (buttonID) {
                4 -> 41   // sandbag  -> moves NE +2
                9 -> 20   // logs     -> moves NE +1
                5 -> 1    // relax    -> moves E  +1
                6 -> -19  // rope     -> moves SE +1
                10 -> -38 // red rope -> moves SE +2
                else -> 8 // Close interface.
            }

            if (buttonID == 8 || buttonID != sequence.getOrNull(index))
            {
                BalloonHelper.clearBalloonState(player, routeId, step)
                closeInterface(player)
                closeSingleTab(player)
                return@on true
            }

            BalloonHelper.drawBalloon(player, delta, routeId, step)

            val newIndex = index + 1
            setAttribute(player, sequenceProgressAttribute, newIndex)

            if (newIndex >= sequence.size)
            {
                removeAttribute(player, sequenceProgressAttribute)
                openInterface(player, Components.ZEP_INTERFACE_470)
                BalloonHelper.updateScreen(player, routeId, step, routeData)
            }

            return@on true
        }
    }
}
