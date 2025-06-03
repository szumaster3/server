package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.animateScenery
import core.api.finishDiaryTask
import core.api.getStatLevel
import core.api.playAudio
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class MossGiantRopeShortcut : InteractionListener {

    override fun defineListeners() {
        on(ROPE_SWING_SCENERY, IntType.SCENERY, "swing-on") { player, node ->
            if (!player.location.withinDistance(node.location, 4)) {
                player.sendMessage("I can't reach that.")
                return@on true
            }

            if (getStatLevel(player, Skills.AGILITY) < 10) {
                player.sendMessage("You need an agility level of at least 10 in order to do that.")
                return@on true
            }

            playAudio(player, Sounds.SWING_ACROSS_2494)

            val end = when (node.id) {
                Scenery.ROPESWING_2322 -> Location(2704, 3209)
                else -> Location(2709, 3205)
            }

            animateScenery(player, node.asScenery(), 497, true)

            AgilityHandler.forceWalk(
                player,
                0,
                player.location,
                end,
                Animation(Animations.SWING_ACROSS_OBSTACLE_3130),
                50,
                30.0,
                "You skillfully swing across.",
                1,
            )

            finishDiaryTask(player, DiaryType.KARAMJA, 0, 1)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, ROPE_SWING_SCENERY, "swing-on") { _, node ->
            return@setDest if (node.id == Scenery.ROPESWING_2322) {
                Location.create(2709, 3209, 0)
            } else {
                Location.create(2705, 3205, 0)
            }
        }
    }

    companion object {
        val ROPE_SWING_SCENERY = intArrayOf(Scenery.ROPESWING_2322, Scenery.ROPESWING_2323)
    }
}
