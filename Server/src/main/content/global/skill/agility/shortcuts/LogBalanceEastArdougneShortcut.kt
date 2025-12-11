package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityHandler.extinguishLightOnWater
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.cyclesToTicks
import shared.consts.Animations
import shared.consts.Scenery
import shared.consts.Sounds
import shared.consts.Graphics as Gfx

/**
 * Handles the east Ardougne log balance.
 */
class LogBalanceEastArdougneShortcut : InteractionListener {

    companion object {
        private val OBJECTS = intArrayOf(Scenery.LOG_BALANCE_35997, Scenery.LOG_BALANCE_35999)

        private const val BALANCE_ANIMATION = Animations.BALANCE_WALK_ACROSS_LOG_9908
        private const val SWIMMING_ANIMATION = Animations.SWIMMING_LOOP_6989
        private const val FAIL_WEST_ANIMATION = Animations.FALL_LOG_2582
        private const val FAIL_EAST_ANIMATION = Animations.FALL_LOG_2581

        private const val SPLASH_GRAPHICS = Gfx.WATER_SPLASH_68

        private const val LOG_BALANCE_SOUND = Sounds.LOG_BALANCE_2470
        private const val WATERSPLASH_SOUND = Sounds.WATERSPLASH_2496

        private val FAIL_LOCATION = Location(2600, 3335, 0)

        private val WEST_START_LOC = Location(2600, 3332, 0)
        private val WEST_END_LOC = Location(2602, 3330, 0)
        private val WEST_LAND_LOC = Location(2604, 3330, 0)

        private val EAST_START_LOC = Location(2600, 3334, 0)
        private val EAST_END_LOC = Location(2599, 3333, 0)
        private val EAST_LAND_LOC = Location(2597, 3333, 0)

        private const val WEST_SWIM_CYCLE = 90
        private const val EAST_SWIM_CYCLE = 30
    }

    override fun defineListeners() {
        on(OBJECTS, IntType.SCENERY, "walk-across") { player, node ->
            if (getStatLevel(player, Skills.AGILITY) < 33) {
                sendMessage(player, "You need an Agility level of at least 33 to cross this log.")
                return@on true
            }

            val start = player.location
            val fromWest = node.id == OBJECTS[0]

            playAudio(player, LOG_BALANCE_SOUND)
            player.logoutListeners["balance-log"] = { it.location = start }

            queueScript(player, 0, QueueStrength.SOFT) {
                if (AgilityHandler.hasFailed(player, 33, 0.1)) {
                    handleFail(player, start, fromWest)
                } else {
                    handleSuccess(player, start, fromWest)
                }
                return@queueScript true
            }
            return@on true
        }
    }

    private fun handleSuccess(player: Player, start: Location, fromWest: Boolean) {
        val end = start.transform(if (fromWest) 4 else -4, 0, 0)
        forceMove(player, start, end, 0, 90, null, BALANCE_ANIMATION)
        {
            resetAnimator(player)
            clearLogoutListener(player, "balance-log")
        }
    }

    private fun handleFail(player: Player, start: Location, fromWest: Boolean) {
        val failAnim = if (fromWest) FAIL_WEST_ANIMATION else FAIL_EAST_ANIMATION
        val startLoc = if (fromWest) WEST_START_LOC else EAST_START_LOC
        val endLoc = if (fromWest) WEST_END_LOC else EAST_END_LOC
        val exitLoc = if (fromWest) WEST_LAND_LOC else EAST_LAND_LOC
        val swimmingCycle = if (fromWest) WEST_SWIM_CYCLE else EAST_SWIM_CYCLE
        val direction = if (fromWest) Direction.EAST else Direction.WEST

        forceMove(player, start, Location(2600, 3336, 0), 0, 45, null, BALANCE_ANIMATION) {

            player.animate(Animation(failAnim))
            queueScript(player, 1, QueueStrength.STRONG) { stage ->
                when (stage) {
                    0 -> {
                        playAudio(player, WATERSPLASH_SOUND)
                        teleport(player, FAIL_LOCATION)
                        visualize(player, Animations.DROWN_765, SPLASH_GRAPHICS)
                        extinguishLightOnWater(player)
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        val destArrive = 30
                        forceMove(player, player.location, startLoc, 0, destArrive, Direction.SOUTH, SWIMMING_ANIMATION)
                        return@queueScript delayScript(player, cyclesToTicks(destArrive))
                    }
                    2 -> {
                        forceMove(player, startLoc, endLoc, 0, swimmingCycle, direction, SWIMMING_ANIMATION)
                        return@queueScript delayScript(player, cyclesToTicks(swimmingCycle))
                    }
                    3 -> {
                        val destArrive = 45
                        forceMove(player, endLoc, exitLoc, 0, destArrive, direction)
                        return@queueScript delayScript(player, cyclesToTicks(destArrive))
                    }
                    4 -> {
                        resetAnimator(player)
                        impact(player, (2..6).random(), ImpactHandler.HitsplatType.NORMAL)
                        clearLogoutListener(player, "balance-log")
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }
}
