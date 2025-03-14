package content.global.skill.agility.shortcuts

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations

class GrandExchangeShortcut : InteractionListener {
    companion object {
        private val SHORTCUTS =
            mapOf(
                9311 to
                    listOf(
                        Location.create(3138, 3516, 0),
                        Location.create(3143, 3514, 0),
                        Location.create(3144, 3514, 0),
                    ),
                9312 to
                    listOf(
                        Location.create(3144, 3514, 0),
                        Location.create(3139, 3516, 0),
                        Location.create(3138, 3516, 0),
                    ),
            )
        private val CLIMB_DOWN = Animation.create(Animations.CRAWL_UNDER_WALL_A_2589)
        private val CRAWL_THROUGH = Animation.create(Animations.CRAWL_UNDER_WALL_B_2590)
        private val CLIMB_UP = Animation.create(Animations.CRAWL_UNDER_WALL_C_2591)
    }

    override fun defineListeners() {
        on(SHORTCUTS.keys.toIntArray(), IntType.SCENERY, "climb-into") { player, node ->
            if (!canUseShortcut(player)) return@on true

            player.locks.lockComponent(4)
            val scenery = node as Scenery
            val path = SHORTCUTS[scenery.id] ?: return@on true

            initiateForceMovement(player, scenery, path)
            handleShortcut(player, path)
            return@on true
        }
    }

    private fun canUseShortcut(player: Player): Boolean {
        if (!hasLevelDyn(player, Skills.AGILITY, 21)) {
            sendMessage(player, "You need an agility level of at least 21 to do this.")
            return false
        }
        return true
    }

    private fun initiateForceMovement(
        player: Player,
        scenery: Scenery,
        path: List<Location>,
    ) {
        ForceMovement.run(
            player,
            path[0],
            scenery.location,
            ForceMovement.WALK_ANIMATION,
            CLIMB_DOWN,
            ForceMovement.direction(path[0], scenery.location),
            ForceMovement.WALKING_SPEED,
            ForceMovement.WALKING_SPEED,
            false,
        )
    }

    private fun handleShortcut(
        player: Player,
        path: List<Location>,
    ) {
        submitIndividualPulse(
            player,
            object : Pulse(1, player) {
                private var count = 0
                private var reachedStart = false

                override fun pulse(): Boolean {
                    if (!reachedStart && player.location != path[0]) {
                        return false
                    }
                    reachedStart = true

                    return when (++count) {
                        2 -> {
                            teleport(player, path[1])
                            visualize(player, CRAWL_THROUGH, -1)
                            false
                        }

                        3 -> {
                            ForceMovement.run(player, path[1], path[2], CLIMB_UP)
                            unlock(player)
                            true
                        }

                        else -> false
                    }
                }
            },
        )
    }
}
