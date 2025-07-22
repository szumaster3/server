package content.global.skill.construction.decoration.study

import content.global.activity.shootingstar.ShootingStarPlugin
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Scenery
import java.util.concurrent.TimeUnit

class TelescopePlugin : InteractionListener {
    private val telescopeSceneryIDs =
        intArrayOf(Scenery.TELESCOPE_13656, Scenery.TELESCOPE_13657, Scenery.TELESCOPE_13658)

    override fun defineListeners() {
        on(telescopeSceneryIDs, IntType.SCENERY, "observe") { player, node ->
            val obj = node?.asScenery() as core.game.node.scenery.Scenery
            val star = ShootingStarPlugin.getStar()
            val delay: Int = 25000 + (25000 / 3)
            val timeLeft = delay - star.ticks
            val window =
                when (obj.id) {
                    Scenery.TELESCOPE_13657 -> 9
                    Scenery.TELESCOPE_13658 -> 2
                    else -> 24
                }
            val time = RandomFunction.random(-window, window + 1) + TimeUnit.MILLISECONDS.toMinutes(timeLeft * 600L)
            lock(player, 3)
            animate(player, Animations.TELESCOPE_POH_3649)
            openInterface(player, Components.TELESCOPE_782).also {
                unlock(player)
                Pulser.submit(
                    object : Pulse(2, player) {
                        override fun pulse(): Boolean {
                            if (obj.isActive) {
                                sendDialogueLines(
                                    player,
                                    "You see a shooting star! The star looks like it will land",
                                    "in about $time minutes!",
                                )
                                return true
                            }
                            return true
                        }
                    },
                )
            }
            return@on true
        }
    }
}
