package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.playAudio
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Handles the squeeze under lumberyard wall shortcut.
 */
@Initializable
class LumberyardShortcut : AgilityShortcut(intArrayOf(31149), 0, 0.0, "squeeze-under") {

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310)
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    Location(if (player.location.x > 3295) 3296 else 3295, 3498, 0),
                    Animation(Animations.LUMBER_YARD_ENTER_9221),
                    5,
                    0.0,
                    null
                ).endAnimation = Animation.RESET
                return true
            }
        })
    }
}
