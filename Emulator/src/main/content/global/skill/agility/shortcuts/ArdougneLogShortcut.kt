package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Scenery
import org.rs.consts.Sounds
import kotlin.random.Random

/**
 * Handles Log balance shortcut.
 */
@Initializable
class ArdougneLogShortcut : AgilityShortcut(intArrayOf(Scenery.LOG_BALANCE_35997, Scenery.LOG_BALANCE_35999), 33, 0.0, "walk-across") {

    private val logBalanceAnim = Animation(Animations.BALANCE_WALK_ACROSS_LOG_9908)
    private val swimLoopAnim = Animation(Animations.SWIMMING_LOOP_6989)
    private val splashGfx = Graphics.create(org.rs.consts.Graphics.WATER_SPLASH_68)
    private val failMidLocation = Location(2600, 3335, 0)

    override fun run(player: Player, scenery: core.game.node.scenery.Scenery, option: String, failed: Boolean) {
        val start = player.location
        val fromWest = scenery.id == Scenery.LOG_BALANCE_35997
        val failAnim = Animation(if (fromWest) Animations.FALL_LOG_2582 else Animations.FALL_LOG_2581)
        val failLand = if (fromWest) Location(2603, 3330, 0) else Location(2598, 3333, 0)

        playAudio(player, Sounds.LOG_BALANCE_2470)
        player.logoutListeners["balance-log"] = { it.location = start }

        if (AgilityHandler.hasFailed(player, 33, 0.1)) {
            AgilityHandler.forceWalk(player, -1, start, failMidLocation, failAnim, 10, 0.0, null, 0)
            AgilityHandler.forceWalk(player, -1, failMidLocation, failLand, swimLoopAnim, 15, 0.0, null, 1).endAnimation = swimLoopAnim

            player.lock(3)
            GameWorld.Pulser.submit(object : Pulse(1, player) {
                private var step = 0

                override fun pulse(): Boolean = when (step++) {
                    0 -> {
                        teleport(player, failMidLocation)
                        playAudio(player, Sounds.WATERSPLASH_2496, 1)
                        visualize(player, Animations.DROWN_765, splashGfx)
                        player.animate(swimLoopAnim, 1)
                        false
                    }
                    1 -> {
                        AgilityHandler.fail(
                            player,
                            if (fromWest) 3 else 1,
                            failLand,
                            swimLoopAnim,
                            Random.nextInt(1, 7),
                            null
                        )
                        player.logoutListeners.remove("balance-log")
                        true
                    }
                    else -> false
                }
            })
        } else {
            val end = start.transform(if (fromWest) 4 else -4, 0, 0)
            AgilityHandler.forceWalk(player, -1, start, end, logBalanceAnim, 10, 0.0, null, 1).endAnimation = Animation.RESET
            player.logoutListeners.remove("balance-log")
        }
    }
}