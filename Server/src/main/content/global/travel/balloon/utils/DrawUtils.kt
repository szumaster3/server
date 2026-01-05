package content.global.travel.balloon.utils

import content.global.travel.balloon.BalloonDefinition
import content.global.travel.balloon.routes.BalloonRoutes
import content.global.travel.balloon.routes.RouteData
import content.region.other.entrana.quest.zep.dialogue.AugusteFirstTalkAfterQuestDialogue
import core.api.*
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.Quests
import shared.consts.Sounds

object DrawUtils {
    fun drawBaseBalloon(player: Player, routeId: Int, step: Int) {
        val base = BalloonRoutes.routes[routeId]!!.startPosition[step - 1]

        setAttribute(player, keyTop(routeId, step), base.top)
        setAttribute(player, keyBottom(routeId, step), base.bottom)

        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, base.top, 19517)
        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, base.bottom, 19518)
    }

    fun drawBalloon(player: Player, move: BalloonMove, routeId: Int, step: Int) {
        val base = BalloonRoutes.routes[routeId]!!.startPosition[step - 1]

        var top = getAttribute(player, keyTop(routeId, step), base.top)
        var bottom = getAttribute(player, keyBottom(routeId, step), base.bottom)

        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, top, -1)
        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, bottom, -1)

        if (bottom == 98 || bottom == 113) {
            bottom += 2
        }

        repeat(move.dx) {
            top = moveEast(top)
            bottom = moveEast(bottom)
        }

        repeat(kotlin.math.abs(move.dy)) {
            if (move.dy < 0) {
                top = moveNorth(top)
                bottom = moveNorth(bottom)
            } else {
                top = moveSouth(top)
                bottom = moveSouth(bottom)
            }
        }

        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, top, 19517)
        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, bottom, 19518)

        setAttribute(player, keyTop(routeId, step), top)
        setAttribute(player, keyBottom(routeId, step), bottom)
    }

    fun updateScreen(player: Player, routeId: Int, step: Int, routeData: RouteData) {
        when (step) {
            1 -> {
                val next = 2
                setAttribute(player, "zep_current_step_$routeId", next)
                routeData.secondOverlay(player, Components.ZEP_INTERFACE_470)
                drawBaseBalloon(player, routeId, next)
            }
            2 -> {
                val next = 3
                setAttribute(player, "zep_current_step_$routeId", next)
                routeData.thirdOverlay(player, Components.ZEP_INTERFACE_470)
                drawBaseBalloon(player, routeId, next)
            }
            3 -> {
                val balloonDestination =
                    when (routeId) {
                        1 -> BalloonDefinition.TAVERLEY
                        2 -> BalloonDefinition.CRAFT_GUILD
                        3 -> BalloonDefinition.VARROCK
                        4 -> BalloonDefinition.CASTLE_WARS
                        5 -> BalloonDefinition.GRAND_TREE
                        else -> BalloonDefinition.TAVERLEY
                    }

                removeAttribute(player, "zep_current_step_$routeId")
                teleport(player, balloonDestination.destination)
                FlightUtils.unlockDestination(player, balloonDestination)

                closeOverlay(player)
                closeInterface(player)

                if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY))
                    openDialogue(player, AugusteFirstTalkAfterQuestDialogue())
            }
        }
    }

    private fun moveEast(child: Int) = child + 1

    private fun moveNorth(child: Int) = child + if (child >= 118) 20 else 19

    private fun moveSouth(child: Int) = child - 19

    enum class BalloonMove(val dx: Int, val dy: Int) {
        SANDBAG(1, -2),
        LOGS(1, -1),
        RELAX(1, 0),
        TUG(0, 1),
        EMERGENCY_TUG(0, 2)
    }

    fun getSoundForButton(player: Player, buttonID: Int) {
        val sound =
            mapOf(
                4 to Sounds.ZEP_DROP_BALLAST_3249,
                9 to Sounds.ZEP_USE_LOGS_3251,
                5 to Sounds.ZEP_BREEZE_3247,
                6 to Sounds.ZEP_HAMMERING_1_3250,
                10 to Sounds.ZEP_CONSTRUCT_3248
            )[buttonID]
        sound?.let { playAudio(player, it) }
    }

    fun clearBalloonState(player: Player, routeId: Int, step: Int) {
        removeAttributes(
            player,
            "zep_current_route",
            "zep_current_step_$routeId",
            "zep_sequence_progress_${routeId}_$step",
            keyTop(routeId, step),
            keyBottom(routeId, step)
        )
    }

    private fun keyTop(routeId: Int, step: Int) = "zep_balloon_top_${routeId}_$step"

    private fun keyBottom(routeId: Int, step: Int) = "zep_balloon_bottom_${routeId}_$step"

    private val allChildren = (78..230).toSet()

    fun reset(player: Player, component: Int) {
        allChildren.forEach { sendModelOnInterface(player, component, it, -1) }
    }
}