package content.global.travel.balloon


import content.data.GameAttributes
import content.global.travel.balloon.BalloonDefinition.ENTRANA
import content.global.travel.balloon.routes.BalloonRoutes
import core.game.node.entity.player.Player
import shared.consts.Components
import content.global.travel.balloon.routes.RouteData
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import shared.consts.NPCs
import shared.consts.Quests

object BalloonHelper {

    /**
     * Draws the base balloon at the beginning of a stage on the interface.
     */
    fun drawBaseBalloon(player: Player, routeId: Int, step: Int) {
        val routeData = BalloonRoutes.routes[routeId] ?: return
        val base = routeData.startPosition[step - 1]

        setAttribute(player, "zep_balloon_top_${routeId}_$step", base.top)
        setAttribute(player, "zep_balloon_bottom_${routeId}_$step", base.bottom)

        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, base.top, 19517)
        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, base.bottom, 19518)
    }

    /**
     * Moves the balloon on the interface according to the delta.
     *
     * @param player The player whose interface to update.
     * @param delta The movement offset (interface child id) for the balloon.
     * @param routeId The id of the balloon route.
     * @param step The current step/stage in the route.
     */
    fun drawBalloon(player: Player, delta: Int, routeId: Int, step: Int) {
        val routeData = BalloonRoutes.routes[routeId] ?: return
        val base = routeData.startPosition[step - 1]

        val lastTop = getAttribute(player, "zep_balloon_top_${routeId}_$step", base.top)
        val lastBottom = getAttribute(player, "zep_balloon_bottom_${routeId}_$step", base.bottom)

        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, lastTop, -1)
        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, lastBottom, -1)

        val offsetTop = if (lastTop == base.top && lastBottom == base.bottom) 1 else 0

        val newTop = lastTop + delta + offsetTop
        val newBottom = lastBottom + delta

        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, newTop, 19517)
        sendModelOnInterface(player, Components.ZEP_INTERFACE_470, newBottom, 19518)

        setAttribute(player, "zep_balloon_top_${routeId}_$step", newTop)
        setAttribute(player, "zep_balloon_bottom_${routeId}_$step", newBottom)
    }

    /**
     * Update the screen route interface to the next stage or completes the flight.
     *
     * @param player The player to update.
     * @param routeId The id of the current balloon route.
     * @param step The current step of the route.
     * @param routeData The data for the current balloon route.
     */
    fun updateScreen(player: Player, routeId: Int, step: Int, routeData: RouteData) {
        when (step) {
            1 -> {
                val next = 2
                setAttribute(player, "zep_current_step_$routeId", next)
                drawBaseBalloon(player, routeId, next)
                routeData.secondOverlay(player, Components.ZEP_INTERFACE_470)
            }

            2 -> {
                val next = 3
                setAttribute(player, "zep_current_step_$routeId", next)
                drawBaseBalloon(player, routeId, next)
                routeData.thirdOverlay(player, Components.ZEP_INTERFACE_470)
            }

            3 -> {
                val balloonDestination = when (routeId) {
                    1 -> BalloonDefinition.TAVERLEY
                    2 -> BalloonDefinition.CRAFT_GUILD
                    3 -> BalloonDefinition.VARROCK
                    4 -> BalloonDefinition.CASTLE_WARS
                    5 -> BalloonDefinition.GRAND_TREE
                    else -> BalloonDefinition.TAVERLEY
                }

                val destination = balloonDestination.destination

                removeAttribute(player, "zep_current_step_$routeId")
                closeInterface(player)
                teleport(player, destination)
                if(!isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY))
                    FinishEnligtenedJourneyQuestDialogue()
            }
        }
    }

    /**
     * Opens the balloon map interface.
     */
    fun openBalloonFlightMap(player: Player, location: BalloonDefinition) {
        player.setAttribute(GameAttributes.BALLOON_ORIGIN, location)
        openInterface(player, Components.ZEP_BALLOON_MAP_469)
        setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, location.componentId, false)
    }

    /**
     * Handles balloon flight.
     */
    fun startFlight(player: Player, destination: BalloonDefinition) {
        val origin = player.getAttribute<BalloonDefinition>(GameAttributes.BALLOON_ORIGIN)
        if (origin == null) {
            player.debug("null location.")
            return
        }

        val animationId = BalloonDefinition.getAnimationId(origin, destination)
        val animationDelay = animationDuration(Animation(animationId))

        registerLogoutListener(player, "balloon-flight") { p ->
            p.location = player.location
        }

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

    /**
     * Unlocks a new balloon destination.
     */
    fun unlockDestination(player: Player, destination: BalloonDefinition) {
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

    /**
     * Clear.
     */
    fun clearBalloonState(player: Player, routeId: Int, step: Int) {
        removeAttributes(
            player,
            "zep_current_route",
            "zep_current_step_$routeId",
            "zep_sequence_progress_${routeId}_$step",
            "zep_balloon_top_${routeId}_$step",
            "zep_balloon_bottom_${routeId}_$step",
        )
    }

    /**
     * Represents the finishing the Enlightened Journey quest dialogue.
     */
    private class FinishEnligtenedJourneyQuestDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            npc = NPC(NPCs.AUGUSTE_5049)
            when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "So what are you going to do now?").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "I am considering starting a balloon enterprise. People all over ${GameWorld.settings?.name} will be able to travel in a new, exciting way.").also { stage++ }
                2 -> npcl(FaceAnim.FRIENDLY, "As my first assistant, you will always be welcome to use a balloon. You'll have to bring your own fuel, though.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
                4 -> npcl(FaceAnim.FRIENDLY, "I will base my operations in Entrana. If you'd like to travel to new places, come see me there.").also { stage++ }
                5 -> {
                    end()
                    finishQuest(player!!, Quests.ENLIGHTENED_JOURNEY)
                }
            }
        }
    }

}