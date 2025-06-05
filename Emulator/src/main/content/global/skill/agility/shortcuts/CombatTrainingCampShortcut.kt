package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import content.global.skill.agility.AgilityHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Scenery.LOOSE_RAILING_19171

@Initializable
class CombatTrainingCampShortcut : AgilityShortcut(
    intArrayOf(LOOSE_RAILING_19171),
    1,
    1.0,
    "squeeze-through"
) {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val start = if (player.location.x <= 2522) scenery.location else scenery.location.transform(1, 0, 0)
        val end = start.transform(if (player.location.x <= 2522) 1 else -1, 0, 0)

        AgilityHandler.forceWalk(
            player,
            -1,
            start,
            end,
            Animation.create(Animations.DUCK_UNDER_2240),
            5,
            experience,
            null,
            1
        )
    }

    override fun getDestination(node: Node, n: Node): Location? {
        return if (node.location.x <= 2522) node.location.transform(1, 0, 0)
        else node.location.transform(-1, 0, 0)
    }
}
