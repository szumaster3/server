package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations

/**
 * Handles the Oo'glog wall rock passage shortcut.
 */
@Initializable
class OoglogRockPassageShortcut : AgilityShortcut(intArrayOf(29099), 29, 0.0, "squeeze-through") {

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        player.lock(1)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    player.location.transform(0, if (player.location.y == 2871) -2 else 2, 0),
                    Animation(Animations.DUCK_UNDER_2240),
                    5,
                    0.0,
                    null
                )
                return true
            }
        })
    }
}