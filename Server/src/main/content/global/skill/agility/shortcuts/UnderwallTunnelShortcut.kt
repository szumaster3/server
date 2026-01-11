package content.global.skill.agility.shortcuts

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Scenery as Objects

/**
 * Handles the Grand Exchange underwall shortcut.
 */
class UnderwallTunnelShortcut : InteractionListener {

    companion object {
        private val WEST_SIDE = Location(3138, 3516, 0)
        private val EAST_SIDE = Location(3144, 3514, 0)

        private val OBJECTS = intArrayOf(
            Objects.UNDERWALL_TUNNEL_9311,
            Objects.UNDERWALL_TUNNEL_9312
        )

        private val ANIMATIONS = intArrayOf(
            Animations.CRAWL_UNDER_WALL_A_2589,
            Animations.CRAWL_UNDER_WALL_B_2590,
            Animations.CRAWL_UNDER_WALL_C_2591
        )
    }

    override fun defineListeners() {
        on(OBJECTS, IntType.SCENERY, "climb-into") { player, node ->

            if (getStatLevel(player, Skills.AGILITY) < 21) {
                sendMessage(player, "You need an Agility level of at least 21 to climb under this wall.")
                return@on true
            }

            player.animate(Animation(ANIMATIONS[0]))
            player.animate(Animation(ANIMATIONS[1]), 2)
            queueScript(player, 0, QueueStrength.STRONG) {

                val (destination, direction) = when (node.id) {
                    Objects.UNDERWALL_TUNNEL_9311 -> EAST_SIDE to Direction.EAST
                    Objects.UNDERWALL_TUNNEL_9312 -> WEST_SIDE to Direction.WEST
                    else -> return@queueScript true
                }
                forceMove(
                    player,
                    node.location,
                    destination,
                    30,
                    120,
                    direction,
                    ANIMATIONS[1]
                ) {
                    player.animate(Animation(ANIMATIONS[2]))
                    if (!hasDiaryTaskComplete(player, DiaryType.VARROCK, 1, 8)) {
                        finishDiaryTask(player, DiaryType.VARROCK, 1, 8)
                    }
                }

                return@queueScript stopExecuting(player)
            }

            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, OBJECTS, "climb-into") { _, node ->
            when (node.id) {
                Objects.UNDERWALL_TUNNEL_9311 -> WEST_SIDE
                Objects.UNDERWALL_TUNNEL_9312 -> EAST_SIDE
                else -> node.location
            }
        }
    }
}
