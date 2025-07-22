package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.isQuestComplete
import core.api.sendDialogue
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Quests

/**
 * Handles the various bar squeeze shortcuts.
 */
@Initializable
class BarSqueezeShortcut : AgilityShortcut(intArrayOf(9334, 9337, 2186), 66, 1.0, "squeeze-through") {

    override fun newInstance(arg: Any?): Plugin<Any> = this

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        val direction = when (obj.id) {
            9334 -> Direction.WEST
            9337 -> if (player.location.y < obj.location.y) Direction.NORTH else Direction.SOUTH
            2186 -> if (player.location.y >= 3161) Direction.SOUTH else Direction.getLogicalDirection(
                player.location,
                obj.location
            )

            else -> Direction.getLogicalDirection(player.location, obj.location)
        }

        val start = if (obj.id == 9334) Location(3424, 3476, 0) else player.location
        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    start,
                    start.transform(direction, 1),
                    Animation(Animations.DUCK_UNDER_2240),
                    10,
                    0.0,
                    null
                )
                return true
            }
        })
    }

    override fun checkRequirements(player: Player): Boolean {
        val inTempleTunnel = player.location.y in 3159..3161
        val questComplete = isQuestComplete(player, Quests.PRIEST_IN_PERIL)

        if (!questComplete && !inTempleTunnel) {
            sendDialogue(player, "You need to have completed Priest in Peril in order to do this.")
            return false
        }

        return super.checkRequirements(player)
    }
}