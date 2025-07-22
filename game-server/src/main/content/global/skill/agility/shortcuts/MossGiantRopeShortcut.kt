package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.animateScenery
import core.api.finishDiaryTask
import core.api.playAudio
import core.api.sendMessage
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Handles the rope swing shortcut.
 */
@Initializable
class MossGiantRopeShortcut : AgilityShortcut(intArrayOf(2322, 2323), 10, 0.0, "swing-on") {

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        if (!player.location.withinDistance(obj.location, 4)) {
            sendMessage(player, "I can't reach that.")
            return
        }

        val end = if (obj.id == 2322) Location(2704, 3209, 0) else Location(2709, 3205, 0)

        playAudio(player, Sounds.SWING_ACROSS_2494)
        animateScenery(player, obj, 497, true)

        player.lock(1)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    0,
                    player.location,
                    end,
                    Animation(Animations.SWING_ACROSS_OBSTACLE_3130),
                    50,
                    30.0,
                    "You skillfully swing across.",
                )
                finishDiaryTask(player, DiaryType.KARAMJA, 0, 1)
                return true
            }
        })
    }

    override fun getDestination(node: Node?, n: Node): Location {
        return if (n.id == 2322) Location(2709, 3209, 0) else Location(2705, 3205, 0)
    }
}
