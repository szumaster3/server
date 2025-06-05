package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Sounds
import kotlin.random.Random

/**
 * Represents the Strange floor shortcut between the entrance and the Poison spider area.
 */
@Initializable
class TaverleyStrangeFloorShortcut : AgilityShortcut(intArrayOf(9294), 80, 12.5, "jump-over") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val fromLeft = player.location.x < 2880
        val start = Location.create(if (fromLeft) 2877 else 2881, 9813, 0)
        val end = Location.create(if (fromLeft) 2881 else 2877, 9813, 0)

        lock(player, 6)
        AgilityHandler.forceWalk(player, -1, start, end, Animation(Animations.RUNNING_OSRS_STYLE_1995), 13, 0.0, null, 0)

        submitIndividualPulse(player, object : Pulse(1, player) {
            var counter = 0
            var fail = AgilityHandler.hasFailed(player, 1, 0.1)

            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> animate(player, Animations.RUNNING_OSRS_STYLE_1995)
                    1 -> {
                        playAudio(player, Sounds.JUMP_2461)
                        animate(player, Animations.JUMP_WEREWOLF_1603, true)
                        if (fail) {
                            playAudio(player, Sounds.FLOOR_SPIKES_1383)
                            playAudio(player, Sounds.JUMP_BLADES_2464)
                            animateScenery(scenery, 1111)
                            AgilityHandler.fail(
                                player,
                                0,
                                end,
                                Animation(1603),
                                Random.nextInt(1, 7),
                                "You trigger the trap as you jump over it."
                            )
                            return true
                        }
                    }
                }
                return false
            }
        })
    }
}
