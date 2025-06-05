package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations

@Initializable
class WatchtowerShortcut : AgilityShortcut(intArrayOf(20056), 18, 31.0, "climb-up") {

    private val end = Location.create(2548, 3117, 1)
    private val animation = Animation.create(Animations.HUMAN_CLIMB_STAIRS_828)

    override fun run(player: Player, scenery: Scenery, option: String?, failed: Boolean) {
        if (!hasLevelDyn(player, Skills.AGILITY, 18)) {
            sendDialogue(player, "You need an agility level of at least 18 to negotiate this obstacle.")
            return
        }

        lock(player, 2)
        sendMessage(player, "You climb up the wall...")
        sendMessageWithDelay(player, "...and squeeze in through the window.", 1)
        animate(player, animation)

        queueScript(player, 2) {
            teleport(player, end)
            rewardXP(player, Skills.AGILITY, 31.0)
            stopExecuting(player)
        }
    }

    override fun getDestination(node: Node, n: Node): Location {
        return end
    }
}
