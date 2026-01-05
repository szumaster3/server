package content.global.travel.balloon

import content.global.travel.balloon.routes.BalloonRoutes
import content.global.travel.balloon.utils.DrawUtils
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Components

class BalloonFlightInterface : InterfaceListener {

    override fun defineInterfaceListeners() {

        onClose(Components.ZEP_INTERFACE_470) { player, _ ->
            closeSingleTab(player)
            showMinimap(player)
            return@onClose true
        }

        onOpen(Components.ZEP_INTERFACE_470) { player, _ ->
            openSingleTab(player, Components.ZEP_INTERFACE_SIDE_471)
            hideMinimap(player)
            val routeId = getAttribute(player, "zep_current_route", 1)
            setAttribute(player, "zep_current_route", routeId)

            val currentId = "zep_current_step_$routeId"
            val step = getAttribute(player, currentId, 1)
            setAttribute(player, currentId, step)

            DrawUtils.drawBaseBalloon(player, routeId, step)

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

            registerLogoutListener(player, "balloon-control-panel") {
                DrawUtils.clearBalloonState(player, routeId, step)
            }

            val sequence =
                when (step) {
                    1 -> routeData.firstSequence
                    2 -> routeData.secondSequence
                    3 -> routeData.thirdSequence
                    else -> emptyList()
                }

            val move =
                when (buttonID) {
                    4 -> DrawUtils.BalloonMove.SANDBAG
                    9 -> DrawUtils.BalloonMove.LOGS
                    5 -> DrawUtils.BalloonMove.RELAX
                    6 -> DrawUtils.BalloonMove.TUG
                    10 -> DrawUtils.BalloonMove.EMERGENCY_TUG
                    else -> null
                }

            if (buttonID == 8 || buttonID != sequence.getOrNull(index) || move == null) {
                DrawUtils.clearBalloonState(player, routeId, step)
                closeInterface(player)
                closeSingleTab(player)
                return@on true
            }

            DrawUtils.getSoundForButton(player, buttonID)
            DrawUtils.drawBalloon(player, move, routeId, step)

            setAttribute(player, sequenceProgressAttribute, index + 1)

            if (index + 1 >= sequence.size) {
                DrawUtils.reset(player, Components.ZEP_INTERFACE_470)
                removeAttribute(player, sequenceProgressAttribute)
                DrawUtils.updateScreen(player, routeId, step, routeData)
            }

            return@on true
        }
    }
}
