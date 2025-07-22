package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Represents the trellis shortcut behind watchtower.
 */
@Initializable
class WatchtowerShortcut : AgilityShortcut(intArrayOf(20056), 18, 31.0, "climb-up") {

    private val end = Location.create(2548, 3117, 1)
    private val animation = Animation.create(Animations.HUMAN_CLIMB_STAIRS_828)

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        animate(player, animation)
        sendMessage(player, "You climb up the wall...")
        queueScript(player, 2) {
            teleport(player, end)
            rewardXP(player, Skills.AGILITY, 31.0)
            sendMessage(player, "...and squeeze in through the window.")
            return@queueScript stopExecuting(player)
        }
    }
}
