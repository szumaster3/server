package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.hasLevelDyn
import core.api.sendMessage
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Represents the squeeze through fence shortcut.
 */
@Initializable
class DraynorManorShortcut : AgilityShortcut(intArrayOf(37703), 28, 0.0, "squeeze-through") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        if (!hasLevelDyn(player, Skills.AGILITY, 28)) {
            sendMessage(player, "You need an Agility level of at least 28 to do this.")
            return
        }

        val direction = if (player.location.x >= 3086) Direction.WEST else Direction.EAST
        val destination = player.location.transform(direction, 1)
        val animation = Animation.create(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844)

        ForceMovement.run(
            player,
            player.location,
            destination,
            animation,
            5
        )
    }
}
