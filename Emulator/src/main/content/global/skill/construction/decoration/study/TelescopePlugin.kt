package content.global.skill.construction.decoration.study

import content.global.activity.shootingstar.ShootingStarPlugin
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Components
import java.util.concurrent.TimeUnit

@Initializable
class TelescopePlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        SceneryDefinition.forId(org.rs.consts.Scenery.TELESCOPE_13656).handlers["option:observe"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.TELESCOPE_13657).handlers["option:observe"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.TELESCOPE_13658).handlers["option:observe"] = this
        return this
    }

    override fun handle(
        player: Player?,
        node: Node?,
        option: String?,
    ): Boolean {
        val obj = node?.asScenery() as Scenery
        val star = ShootingStarPlugin.getStar()
        val delay: Int = 25000 + (25000 / 3)
        val timeLeft = delay - star.ticks
        val window =
            when (obj.id) {
                13657 -> 9
                13658 -> 2
                else -> 24
            }
        val time = RandomFunction.random(-window, window + 1) + TimeUnit.MILLISECONDS.toMinutes(timeLeft * 600L)
        lock(player!!, 3)
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
            return true
        }
    }
}
