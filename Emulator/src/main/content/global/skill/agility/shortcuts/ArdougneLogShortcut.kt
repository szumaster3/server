package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Scenery
import org.rs.consts.Sounds
import kotlin.random.Random

class ArdougneLogShortcut : InteractionListener {
    private val ardougneLog = intArrayOf(Scenery.LOG_BALANCE_35997, Scenery.LOG_BALANCE_35999)
    private val logBalanceAnimation = Animation(Animations.BALANCE_WALK_ACROSS_LOG_9908)
    private val swimmingAnimation = Animation(Animations.SWIMMING_6988)
    private val swimmingLoopAnimation = Animation(Animations.SWIMMING_LOOP_6989)
    private val failLocation = Location(2600, 3335, 0)
    private val splashGraphics = Graphics.create(org.rs.consts.Graphics.WATER_SPLASH_68)

    override fun defineListeners() {
        on(ardougneLog, IntType.SCENERY, "walk-across") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 33)) {
                sendMessage(player, "You need an agility level of 33 to negotiate this obstacle.")
                return@on true
            }

            val start = player.location
            val (failAnim, failLand, fromWest) = determineFailParameters(node)

            lock(player, 10)
            face(player, node)

            player.logoutListeners["balance-log"] = { p: Player -> p.location = start }

            if (AgilityHandler.hasFailed(player, 33, 0.1)) {
                handleFailure(player, start, failLand, failAnim, fromWest)
            } else {
                val end = start.transform(if (fromWest) 4 else -4, 0, 0)
                AgilityHandler.forceWalk(player, -1, start, end, logBalanceAnimation, 10, 0.0, null, 0).endAnimation =
                    Animation.RESET
                player.logoutListeners.remove("balance-log")
            }
            return@on true
        }
    }

    private fun determineFailParameters(node: Node): Triple<Animation, Location, Boolean> {
        return when (node.id) {
            Scenery.LOG_BALANCE_35997 -> Triple(Animation(Animations.FALL_LOG_2582), Location(2603, 3330, 0), true)
            else -> Triple(Animation(Animations.FALL_LOG_2581), Location(2598, 3333, 0), false)
        }
    }

    private fun handleFailure(
        player: Player,
        start: Location,
        failLand: Location,
        failAnim: Animation,
        fromWest: Boolean,
    ) {
        AgilityHandler.forceWalk(player, -1, start, failLocation, failAnim, 10, 0.0, null, 0)
        AgilityHandler
            .forceWalk(
                player,
                -1,
                failLocation,
                failLand,
                swimmingLoopAnimation,
                15,
                0.0,
                null,
                1,
            ).endAnimation = Animation.RESET

        submitIndividualPulse(
            player,
            object : Pulse(2) {
                private var counter = 0

                override fun pulse(): Boolean {
                    return when (counter++) {
                        0 -> {
                            visualize(player, -1, splashGraphics)
                            playAudio(player, Sounds.WATERSPLASH_2496)
                            teleport(player, failLocation)
                            animate(player, swimmingLoopAnimation)
                            false
                        }

                        1 -> {
                            AgilityHandler.fail(
                                player,
                                if (fromWest) 4 else 0,
                                failLand,
                                swimmingAnimation,
                                Random.nextInt(1, 7),
                                null,
                            )
                            player.logoutListeners.remove("balance-log")
                            true
                        }

                        else -> false
                    }
                }
            },
        )
    }
}
