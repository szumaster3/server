package content.global.skill.slayer

import core.api.*
import core.api.quest.hasRequirement
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class SlayerListener : InteractionListener {
    companion object {
        val NULL = -1
        private const val FADE_START = Components.FADE_TO_BLACK_115
        private const val FADE_END = Components.FADE_FROM_BLACK_170
        private const val TRAPDOOR = Scenery.TRAPDOOR_8783
        private const val LADDER = Scenery.LADDER_8785
        private const val STAIRS = Scenery.STAIRS_96
        private const val STAIRS_2 = Scenery.STAIRS_35121
        private const val CAVE_ENTRANCE = Scenery.CAVE_ENTRANCE_15767
        private val CAVE_EXIT =
            intArrayOf(Scenery.CAVE_15811, Scenery.CAVE_15812, Scenery.CAVE_23157, Scenery.CAVE_23158)
        private val SVENS_DIG_LOCATIONS =
            arrayOf(
                Location(2749, 3733, 0),
                Location(2748, 3733, 0),
                Location(2747, 3733, 0),
                Location(2747, 3734, 0),
                Location(2747, 3735, 0),
                Location(2747, 3736, 0),
                Location(2748, 3736, 0),
                Location(2749, 3736, 0),
            )
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.CAVE_23157, Scenery.CAVE_23158), "exit") { _, _ ->
            return@setDest Location(2690, 10124, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(STAIRS), "climb-up") { _, _ ->
            return@setDest Location(2641, 9763, 0)
        }
    }

    private fun enterCavern(player: Player) {
        queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    sendMessage(player, "You dig a hole...")
                    openOverlay(player, FADE_START)
                    return@queueScript delayScript(player, 3)
                }

                1 -> {
                    closeOverlay(player)
                    openInterface(player, FADE_END)
                    teleport(player, Location(2697, 10119, 0), TeleportManager.TeleportType.INSTANT)
                    return@queueScript keepRunning(player)
                }

                2 -> {
                    playAudio(player, Sounds.STUNNED_2727)
                    visualize(
                        player,
                        NULL,
                        Graphics(
                            org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80,
                            96,
                        ),
                    )
                    sendMessage(player, "...And fall into a dark and slimy pit!")
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun defineListeners() {
        for (location in SVENS_DIG_LOCATIONS) {
            onDig(location) { player: Player ->
                enterCavern(player)
            }
        }

        on(TRAPDOOR, IntType.SCENERY, "open") { player, _ ->
            teleport(player, Location(2044, 4649, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(LADDER, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(2543, 3327, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(STAIRS, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(2649, 9804, 0))
            return@on true
        }

        on(STAIRS_2, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(2641, 9763, 0))
            return@on true
        }

        on(CAVE_ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            if (!hasRequirement(player, Quests.CABIN_FEVER)) return@on true
            teleport(player, Location(3748, 9373, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        on(CAVE_EXIT, IntType.SCENERY, "exit") { player, node ->
            when (node.id) {
                Scenery.CAVE_23157, Scenery.CAVE_23158 ->
                    teleport(
                        player,
                        Location(2729, 3733, 0),
                        TeleportManager.TeleportType.INSTANT,
                    )
                else ->
                    teleport(
                        player,
                        Location(3749, 2973, 0),
                        TeleportManager.TeleportType.INSTANT,
                    )
            }
            return@on true
        }
    }
}
