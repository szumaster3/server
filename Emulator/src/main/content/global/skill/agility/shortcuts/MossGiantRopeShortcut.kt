package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.api.animateScenery
import core.api.finishDiaryTask
import core.api.playAudio
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Represents the Rope swing shortcut.
 */
@Initializable
class MossGiantRopeShortcut : AgilityShortcut(intArrayOf(2322, 2323), 10, 0.0, "swing-on") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        if (!player.location.withinDistance(scenery.location, 4)) {
            player.sendMessage("I can't reach that.")
            return
        }

        playAudio(player, Sounds.SWING_ACROSS_2494)

        val end = when (scenery.id) {
            2322 -> Location(2704, 3209)
            else -> Location(2709, 3205)
        }

        animateScenery(player, scenery, 497, true)

        AgilityHandler.forceWalk(
            player,
            0,
            player.location,
            end,
            Animation(Animations.SWING_ACROSS_OBSTACLE_3130),
            50,
            30.0,
            "You skillfully swing across.",
            1
        )

        finishDiaryTask(player, DiaryType.KARAMJA, 0, 1)
    }
}
