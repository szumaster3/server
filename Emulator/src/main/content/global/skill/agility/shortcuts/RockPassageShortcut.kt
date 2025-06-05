package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Represents the Rock Passage shortcut.
 */
@Initializable
class RockPassageShortcut : AgilityShortcut(intArrayOf(29099), 29, 0.0, "squeeze-through") {

    override fun run(player: Player, scenery: core.game.node.scenery.Scenery, option: String, failed: Boolean) {
        if (failed) {
            sendMessage(player, "You need an agility level of at least 29 to do this.")
            return
        }

        lock(player, 3)
        animate(player, Animation(Animations.GO_INTO_OBSTACLE_PIPE_4855))
        playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310, 1)

        queueScript(player, 2, QueueStrength.SOFT) {
            val destination = when (player.location.y) {
                2871 -> Location(2596, 2869, 0)
                2869 -> Location(2596, 2871, 0)
                else -> null
            }

            if (destination != null) {
                teleport(player, destination)
            }

            stopExecuting(player)
        }
    }
}