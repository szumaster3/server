package content.global.travel.balloon

import content.data.GameAttributes
import content.global.travel.balloon.BalloonDefinition.ENTRANA
import content.global.travel.balloon.routes.BalloonRoutes
import content.global.travel.balloon.routes.RouteData
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Sounds

object BalloonUtils {
    private const val SANDBAG_VARBIT = 2880
    private const val LOGS_VARBIT = 2881
    private const val STORAGE_CAPACITY = 15

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

        if (bottom == 98) {
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

    fun openBalloonFlightMap(player: Player, location: BalloonDefinition) {
        player.setAttribute(GameAttributes.BALLOON_ORIGIN, location)
        openInterface(player, Components.ZEP_BALLOON_MAP_469)
        setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, location.componentId, false)
    }

    fun startFlight(player: Player, destination: BalloonDefinition) {
        val origin = player.getAttribute<BalloonDefinition>(GameAttributes.BALLOON_ORIGIN)
        if (origin == null) {
            player.debug("null location.")
            return
        }

        val animationId = BalloonDefinition.getAnimationId(origin, destination)
        val animationDelay = animationDuration(Animation(animationId))

        registerLogoutListener(player, "balloon-flight") { p -> p.location = player.location }

        lock(player, animationDelay)
        hideMinimap(player)
        playJingle(player, 118)
        openOverlay(player, Components.BLACK_OVERLAY_333)
        openInterface(player, Components.ZEP_BALLOON_MAP_469)
        setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, 12, false)
        animateInterface(player, Components.ZEP_BALLOON_MAP_469, 12, animationId)
        sendMessage(player, "You board the balloon and fly to ${destination.destName}.")
        teleport(player, destination.destination, TeleportManager.TeleportType.INSTANT)

        queueScript(player, animationDelay, QueueStrength.SOFT) {
            unlock(player)
            closeInterface(player)
            showMinimap(player)
            openOverlay(player, Components.FADE_FROM_BLACK_170)
            removeAttribute(player, GameAttributes.BALLOON_ORIGIN)
            sendDialogue(player, "You arrive safely ${destination.destName}.")
            if (destination == BalloonDefinition.VARROCK)
                finishDiaryTask(player, DiaryType.VARROCK, 2, 17)
            return@queueScript stopExecuting(player)
        }
    }

    private fun unlockDestination(player: Player, destination: BalloonDefinition) {
        if (getVarbit(player, destination.varbitId) != 1) {
            setVarbit(player, destination.varbitId, 1, true)
            val xp = 2000
            if (destination != ENTRANA) {
                rewardXP(player, Skills.FIREMAKING, xp.toDouble())
            }
            sendMessage(player, "You have unlocked the balloon route to ${destination.destName}!")
        } else {
            sendDialogue(player, "You can open new locations from Entrana.")
        }
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
                unlockDestination(player, balloonDestination)
                if (!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY))
                    openDialogue(player, FinishEnligtenedJourneyQuestDialogue())
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

    fun addToStorage(player: Player, varbit: Int, amount: Int): Boolean {
        if (amount <= 0) return false

        val current = getStorage(player, varbit)
        if (current >= STORAGE_CAPACITY) return false

        val add = minOf(amount, STORAGE_CAPACITY - current)
        setVarbit(player, varbit, current + add, true)
        return true
    }

    fun removeFromStorage(player: Player, varbit: Int, amount: Int): Boolean {
        if (amount <= 0) return false

        val current = getStorage(player, varbit)
        if (current <= 0) return false

        val remove = minOf(amount, current)
        setVarbit(player, varbit, current - remove, true)
        return true
    }

    private fun getStorage(player: Player, varbit: Int): Int =
        getVarbit(player, varbit).coerceIn(0, STORAGE_CAPACITY)

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

    private class FinishEnligtenedJourneyQuestDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            npc = NPC(NPCs.AUGUSTE_5049)
            when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "So what are you going to do now?").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "I am considering starting a balloon enterprise. People all over ${GameWorld.settings?.name} will be able to travel in a new, exciting way.").also { stage++ }
                2 -> npcl(FaceAnim.HAPPY, "As my first assistant, you will always be welcome to use a balloon. You'll have to bring your own fuel, though.").also { stage++ }
                3 -> playerl(FaceAnim.HAPPY, "Thanks!").also { stage++ }
                4 -> npcl(FaceAnim.HAPPY, "I will base my operations in Entrana. If you'd like to travel to new places, come see me there.").also { stage++ }
                5 -> {
                    end()
                    finishQuest(player!!, Quests.ENLIGHTENED_JOURNEY)
                }
            }
        }
    }
}
