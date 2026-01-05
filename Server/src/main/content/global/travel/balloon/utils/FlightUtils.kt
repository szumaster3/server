package content.global.travel.balloon.utils

import content.data.GameAttributes
import content.global.travel.balloon.BalloonDefinition
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.colorize
import shared.consts.Components
import shared.consts.Regions

object FlightUtils {
    private fun isUnlocked(player: Player, destination: BalloonDefinition): Boolean {
        return getVarbit(player, destination.varbitId) == 1
    }

    fun openFlightMap(player: Player, location: BalloonDefinition) {
        player.setAttribute(GameAttributes.BALLOON_ORIGIN, location)
        openInterface(player, Components.ZEP_BALLOON_MAP_469)
        setComponentVisibility(player, Components.ZEP_BALLOON_MAP_469, location.componentId, false)
    }

    fun payForFlight(
        player: Player,
        origin: BalloonDefinition?,
        destination: BalloonDefinition,
        onSuccess: () -> Unit
    ) {
        val unlocked = isUnlocked(player, destination)

        if (unlocked) {
            if (ChargeUtils.consumeCharges(player, destination)) {
                onSuccess()
                return
            }

            if (ChargeUtils.consumeLogs(player, destination)) {
                onSuccess()
                return
            }

            sendMessage(player, "You don't have enough charges or logs.")
            return
        }

        if (ChargeUtils.consumeLogs(player, destination)) {
            onSuccess()
        } else {
            sendMessage(player, "You don't have the required logs.")
        }
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

    fun unlockDestination(player: Player, destination: BalloonDefinition) {
        if (getVarbit(player, destination.varbitId) != 1) {
            setVarbit(player, destination.varbitId, 1, true)
            val xp = 2000
            if (destination != BalloonDefinition.ENTRANA)
            {
                rewardXP(player, Skills.FIREMAKING, xp.toDouble())
            }
            sendMessage(player, colorize("%RYou have unlocked the balloon route to ${destination.destName}!"))
        } else
        {
            sendDialogue(player, "You can open new locations from Entrana.")
        }
    }

    fun canFly(
        player: Player,
        origin: BalloonDefinition?,
        destination: BalloonDefinition
    ): Boolean {

        if (!hasLevelStat(player, Skills.FIREMAKING, destination.requiredLevel)) {
            sendDialogue(player, "You require a Firemaking level of ${destination.requiredLevel} to travel to ${destination.destName}.")
            return false
        }

        if (origin == destination) {
            sendDialogue(player, "You can't fly to the same location.")
            return false
        }

        if (player.familiarManager.hasFamiliar() || player.familiarManager.hasPet()) {
            sendMessage(player, "You can't take a follower or pet on a ride.")
            return false
        }

        if (player.settings.weight > 40.0) {
            sendDialogue(player, "You're carrying too much weight to fly. Try reducing your weight below 40 kg.")
            return false
        }

        if (destination == BalloonDefinition.ENTRANA) {
            if (!ItemDefinition.canEnterEntrana(player)) {
                sendDialogue(player, "You can't take flight with weapons and armour to Entrana.")
                return false
            }
            sendMessage(player, "You are quickly searched.")
        }

        return true
    }

    fun unlockNewLocation(
        player: Player,
        destination: BalloonDefinition
    ): Boolean {
        if (isUnlocked(player, destination) ||
            destination == BalloonDefinition.ENTRANA ||
            destination == BalloonDefinition.TAVERLEY
        ) {
            return false
        }

        if (!inBorders(player, getRegionBorders(Regions.ENTRANA_11060))) {
            sendDialogue(player, "You can open new locations from Entrana.")
            return true
        }

        if (!ChargeUtils.consumeLogs(player, destination)) {
            val logName = getItemName(destination.logId)
                .lowercase()
                .removeSuffix("s")
                .trim()

            sendDialogue(
                player,
                "You need ${destination.logCost} $logName to start."
            )
            return true
        }

        closeInterface(player)
        setAttribute(player, "zep_current_route", destination.ordinal)
        setAttribute(player, "zep_current_step_${destination.ordinal}", 1)
        openInterface(player, Components.ZEP_INTERFACE_470)

        return true
    }
}