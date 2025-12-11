package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.animate
import core.api.forceMove
import core.api.playAudio
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.Sounds

/**
 * Handles the mine crevice shortcut in dwarven mine.
 */
@Initializable
class FaladorMineCreviceShortcut : AgilityShortcut(intArrayOf(30868), 42, 0.0, "squeeze-through") {

    private val westExit = Location.create(3028, 9806, 0)
    private val eastExit = Location.create(3035, 9806, 0)

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val destination = if (player.location == eastExit) westExit else eastExit

        player.lock(9)
        playAudio(player, Sounds.SQUEEZE_THROUGH_ROCKS_1310)
        player.animate(Animation(2594))
        forceMove(player, player.location, destination, 30, 240, null, Animations.CRAWL_UNDER_WALL_B_2590) {
            animate(player, 2595)
            sendMessage(player, "You climb your way through the narrow crevice.")
        }
    }
}