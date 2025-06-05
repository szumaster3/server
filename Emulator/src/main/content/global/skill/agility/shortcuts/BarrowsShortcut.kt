package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.quest.hasRequirement
import core.api.sendDialogue
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Quests

/**
 * Represents the barrows shortcut.
 */
@Initializable
class BarrowsShortcut : AgilityShortcut(intArrayOf(18411), 1, 0.0, "climb-over") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        if (!hasRequirement(player, Quests.IN_AID_OF_THE_MYREQUE)) {
            sendDialogue(player, "Um... those vampyres don't look very nice. I'm not going through here.")
            return
        }

        val direction = if (player.location.y < 3264) Direction.NORTH else Direction.SOUTH
        val destination = player.location.transform(direction, 1)

        ForceMovement.run(
            player,
            player.location,
            destination,
            Animation(Animations.WALK_OVER_STILE_10980),
            10
        )
    }
}
