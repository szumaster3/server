package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import content.global.skill.agility.AgilityHandler
import core.api.animate
import core.api.faceLocation
import core.api.playAudio
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Scenery.CREVICE_30868
import org.rs.consts.Sounds

@Initializable
class DwarvenMineCreviceShortcut : AgilityShortcut(
    intArrayOf(CREVICE_30868),
    42,
    0.0,
    "squeeze-through"
) {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val destination = if (player.location == Location(3035, 9806, 0)) {
            Location(3028, 9806, 0)
        } else {
            Location(3035, 9806, 0)
        }

        faceLocation(player, destination)
        animate(player, Animations.DUCK_UNDER_2240, true)
        playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310)

        AgilityHandler.forceWalk(
            player,
            -1,
            player.location,
            destination,
            Animation.create(Animations.HUMAN_TURNS_INVISIBLE_2590),
            10,
            experience,
            null,
            1
        ).endAnimation = Animation(Animations.DUCK_UNDER_2240)
    }

    override fun getDestination(node: Node, n: Node): Location? {
        return if (node.location == Location(3035, 9806, 0)) {
            Location(3028, 9806, 0)
        } else {
            Location(3035, 9806, 0)
        }
    }
}
