package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class BrokenPierShortcut : InteractionListener {
    override fun defineListeners() {
        on(Scenery.BROKEN_PIER_41531, IntType.SCENERY, "step") { player, _ ->
            val animationDelay = Animation(Animations.JUMP_BRIDGE_769).duration
            if (!isDiaryComplete(player, DiaryType.FREMENNIK, 1)) {
                sendMessage(player, "You need to claim the reward for the medium Fremennik diary to use this shortcut.")
                return@on true
            }
            if (!inEquipment(player, Items.FREMENNIK_SEA_BOOTS_2_14572)) {
                sendMessage(player, "You don't have the required boots in order to do that.")
                return@on true
            }

            queueScript(player, 1, QueueStrength.NORMAL) { stage: Int ->
                when (stage) {
                    0 -> {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            Location(2572, 3862, 0),
                            Location(2573, 3862, 0),
                            Animation(Animations.JUMP_BRIDGE_769),
                            animationCycles(Animations.JUMP_BRIDGE_769),
                            0.0,
                            null,
                        )
                        return@queueScript delayScript(player, animationDelay)
                    }

                    1 -> {
                        AgilityHandler.forceWalk(
                            player,
                            -1,
                            player.location,
                            Location(2576, 3862, 0),
                            Animation(Animations.JUMP_OVER_OBSTACLE_6132),
                            animationCycles(Animations.JUMP_OVER_OBSTACLE_6132),
                            0.0,
                            null,
                        )
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }
    }
}
