package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class MossGiantRopeShortcut : InteractionListener {
    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, ROPESWING, "swing-on") { _, node ->
            return@setDest if (node.id == Scenery.ROPESWING_2322) {
                Location.create(2709, 3209, 0)
            } else {
                Location.create(
                    2705,
                    3205,
                    0,
                )
            }
        }
    }

    override fun defineListeners() {
        on(ROPESWING, IntType.SCENERY, "swing-on") { player, node ->
            if (!withinDistance(player, node.location, 4)) {
                player.sendMessage("I can't reach that.")
                return@on true
            }
            if (getStatLevel(player, Skills.AGILITY) < 10) {
                sendMessage(player, "You need an agility level of at least 10 in order to do that.")
                return@on true
            }

            playAudio(player, Sounds.SWING_ACROSS_2494, 1)
            ropeDelay = GameWorld.ticks + animationDuration(Animation.create(497))
            val end = if (node.id == Scenery.ROPESWING_2322) Location(2704, 3209, 0) else Location(2709, 3205, 0)
            animateScenery(player, node.asScenery(), 497, true)
            AgilityHandler.forceWalk(
                player,
                0,
                player.location,
                end,
                Animation(751),
                50,
                22.0,
                "You skillfully swing across.",
                1,
            )
            finishDiaryTask(player, DiaryType.KARAMJA, 0, 1)
            return@on true
        }
    }

    companion object {
        val ROPESWING = intArrayOf(Scenery.ROPESWING_2322, Scenery.ROPESWING_2323)
        var ropeDelay = 0
    }
}
