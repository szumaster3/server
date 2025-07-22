package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.animate
import core.api.playAudio
import core.api.runTask
import core.api.sendMessage
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
 * Handles the shortcut for squeezing through the crevice.
 */
@Initializable
class FaladorMineCreviceShortcut : AgilityShortcut(intArrayOf(30868), 42, 0.0, "squeeze-through") {

    private val westExit = Location.create(3028, 9806, 0)
    private val eastExit = Location.create(3035, 9806, 0)

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val destination = if (player.location == eastExit) westExit else eastExit

        playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310)
        animate(player, Animations.BALANCING_LEDGE_DISAPPEAR_A_2594)
        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    destination,
                    Animation.create(Animations.HUMAN_TURNS_INVISIBLE_2590),
                    10,
                    0.0,
                    null,
                    1
                )
                runTask(player, 8) {
                    animate(player, Animations.BALANCING_LEDGE_DISAPPEAR_B_2595)
                    sendMessage(player, "You climb your way through the narrow crevice.")
                }
                return true
            }
        })
    }
}