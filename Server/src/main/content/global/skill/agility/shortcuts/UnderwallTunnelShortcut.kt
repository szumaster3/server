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
import shared.consts.Quests
import shared.consts.Scenery

/**
 * Handles the underwall tunnel shortcuts.
 */
class UnderwallTunnelShortcut : InteractionListener {

    companion object {
        private val START_LOCATION: Location = Location(3144, 3514, 0)
        private val EXIT_LOCATION:  Location = Location(3138, 3516, 0)
        private val OBJECTS = intArrayOf(Scenery.UNDERWALL_TUNNEL_9311, Scenery.UNDERWALL_TUNNEL_9312)

        private val LOW_WEST = Location(2575, 3110, 0)
        private val LOW_EAST = Location(2577, 3110, 0)

        // LOW LEVEL TUNNEL
        private val LOW_OBJECTS = intArrayOf(
            Scenery.HOLE_9302,
            Scenery.CASTLE_WALL_9301
        )

        // SWANSONG SPECIAL
        private const val SWAN_OBJECT = Scenery.HOLE_14922

        private val ANIMATIONS = intArrayOf(
            Animations.CRAWL_UNDER_WALL_A_2589,
            Animations.CRAWL_UNDER_WALL_B_2590,
            Animations.CRAWL_UNDER_WALL_C_2591
        )
    }

    override fun defineListeners() {
        on(LOW_OBJECTS, IntType.SCENERY, "climb-under") { player, node ->
            if (getStatLevel(player, Skills.AGILITY) < 16) {
                sendMessage(player, "You need an Agility level of at least 16 to climb under this wall.")
                return@on true
            }

            player.animate(Animation(ANIMATIONS[0]))
            val (positions, direction) = if (node.id == Scenery.HOLE_9302) (LOW_WEST to LOW_EAST) to Direction.EAST else (LOW_EAST to LOW_WEST) to Direction.WEST
            val (startPos, endPos) = positions

            queueScript(player, 0, QueueStrength.NORMAL) {
                forceMove(player, startPos, endPos, 25, 120, direction, ANIMATIONS[1]) {
                    player.animate(Animation(ANIMATIONS[2]))
                }
                return@queueScript stopExecuting(player)
            }

            return@on true
        }

        on(SWAN_OBJECT, IntType.SCENERY, "enter") { player, node ->
            if (!hasRequirement(player, Quests.SWAN_SONG)) {
                sendMessage(player, "You need to start the Swan Song quest to enter this tunnel.")
                return@on true
            }

            val start = player.location
            val direction = Direction.getLogicalDirection(start, node.location)

            player.animate(Animation(ANIMATIONS[0]))
            queueScript(player, 0, QueueStrength.NORMAL) {
                forceMove(player, start, node.location, 20, 120, direction, ANIMATIONS[1]) {
                    player.animate(Animation(ANIMATIONS[2]))
                }
                return@queueScript stopExecuting(player)
            }

            return@on true
        }

        on(OBJECTS, IntType.SCENERY, "climb-into") { player, node ->
            if (getStatLevel(player, Skills.AGILITY) < 21) {
                sendMessage(player, "You need an Agility level of at least 21 to climb under this wall.")
                return@on true
            }

            queueScript(player, 0, QueueStrength.STRONG) {
                if(!finishedMoving(player)) restartScript(player)

                player.animate(Animation(ANIMATIONS[0]))

                val (destination, direction) = when (node.id)
                {
                    OBJECTS[0] -> START_LOCATION to Direction.EAST
                    OBJECTS[1] -> EXIT_LOCATION  to Direction.WEST
                    else -> return@queueScript true
                }

                player.animate(Animation(ANIMATIONS[1]), 2)
                forceMove(player, node.location, destination, 30, 120, direction, ANIMATIONS[1])
                {
                    player.animate(Animation(ANIMATIONS[2]))
                    finishDiaryTask(player, DiaryType.VARROCK, 1, 8)
                }

                return@queueScript stopExecuting(player)
            }

            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, LOW_OBJECTS, "climb-under") { _, node ->
            if (node.id == Scenery.HOLE_9302) LOW_EAST else LOW_WEST
        }

        setDest(IntType.SCENERY, intArrayOf(SWAN_OBJECT), "enter") { _, node ->
            node.location.transform(Direction.getLogicalDirection(node.location, node.location), 1)
        }

        setDest(IntType.SCENERY, OBJECTS, "climb-into") { _, node ->
            return@setDest when (node.id) {
                Scenery.UNDERWALL_TUNNEL_9312 -> Location(3144, 3514, 0)
                else -> Location(3138, 3516, 0)
            }
        }
    }
}