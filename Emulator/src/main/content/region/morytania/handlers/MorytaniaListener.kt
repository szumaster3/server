package content.region.morytania.handlers

import core.api.*
import core.api.quest.hasRequirement
import core.api.ui.setMinimapState
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.*
import kotlin.random.Random

class MorytaniaListener : InteractionListener {
    override fun defineListeners() {
        on(SWAMP_BOAT, IntType.SCENERY, "board", "Board ( Pay 10 )") { player, node ->
            if (!hasRequirement(player, Quests.NATURE_SPIRIT)) return@on true
            lock(player, 13)
            openOverlay(player, Components.FADE_TO_BLACK_120)
            queueScript(player, 3, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        openOverlay(player, Components.SWAMP_BOATJOURNEY_321)
                        setMinimapState(player, 2)
                        return@queueScript delayScript(player, 7)
                    }

                    1 -> {
                        teleport(player, if (node.id == 6970) Location(3522, 3285, 0) else Location(3498, 3380, 0))
                        setMinimapState(player, 0)
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        return@queueScript keepRunning(player)
                    }

                    2 -> {
                        closeOverlay(player)
                        sendDialogue(player, "You arrive at ${if (node.id == 6970) "Mort'ton." else "the swamp"}.")
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(GROTTO_BRIDGE, IntType.SCENERY, "jump") { player, node ->
            val start = node.location
            var failLand = Location(3438, 3331)
            var failAnim = Animation(770)
            var fromGrotto = false

            lock(player, 10)
            if (start.y == 3331) {
                fromGrotto = true
                failAnim = Animation(771)
                failLand = Location(3438, 3328)
            }
            if (content.global.skill.agility.AgilityHandler
                    .hasFailed(player, 1, 0.1)
            ) {
                val end = if (fromGrotto) FAIL_LOCATION else start
                content.global.skill.agility.AgilityHandler
                    .forceWalk(
                        player,
                        -1,
                        start,
                        end,
                        failAnim,
                        15,
                        0.0,
                        null,
                        0,
                    ).endAnimation =
                    SWIMMING_ANIMATION
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    -1,
                    FAIL_LOCATION,
                    failLand,
                    SWIMMING_ANIMATION,
                    15,
                    2.0,
                    null,
                    3,
                )
                submitIndividualPulse(
                    player,
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            sendMessage(player, "You nearly drown in the disgusting swamp.")
                            visualize(player, failAnim, SPLASH_GFX)
                            teleport(player, FAIL_LOCATION)
                            content.global.skill.agility.AgilityHandler.fail(
                                player,
                                0,
                                failLand,
                                SWIMMING_ANIMATION,
                                Random.nextInt(1, 7),
                                "You nearly drown in the disgusting swamp.",
                            )
                            return true
                        }
                    },
                )
            } else {
                val end = if (fromGrotto) start.transform(0, -3, 0) else start.transform(0, 3, 0)
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    -1,
                    start,
                    end,
                    JUMP_ANIMATION,
                    15,
                    15.0,
                    null,
                    0,
                )
            }
            return@on true
        }

        on(Scenery.TREE_5005, IntType.SCENERY, "climb up", "climb down") { player, node ->
            if (node.location == Location(3502, 3431)) {
                when (getUsedOption(player)) {
                    "climb up" -> ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(3502, 3430, 0))
                    "climb down" ->
                        ClimbActionHandler.climb(
                            player,
                            ClimbActionHandler.CLIMB_DOWN,
                            Location(3503, 3431, 0),
                        )
                }
            } else {
                when (getUsedOption(player)) {
                    "climb up" -> ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(3502, 3427, 0))
                    "climb down" ->
                        ClimbActionHandler.climb(
                            player,
                            ClimbActionHandler.CLIMB_DOWN,
                            Location(3502, 3425, 0),
                        )
                }
            }
            return@on true
        }

        on(Scenery.ROPE_BRIDGE_5002, IntType.SCENERY, "walk-here") { player, node ->
            if (node.location == Location(3502, 3428)) {
                teleport(player, Location(3502, 3430, 0))
            } else {
                teleport(player, Location(3502, 3427, 0))
            }
            return@on true
        }
    }

    companion object {
        private val SWAMP_BOAT = intArrayOf(Scenery.SWAMP_BOATY_6970, Scenery.SWAMP_BOATY_6969)
        private const val GROTTO_BRIDGE = Scenery.BRIDGE_3522
        private val SWIMMING_ANIMATION = Animation(Animations.SWIMMING_6988)
        private val JUMP_ANIMATION = Animation(Animations.JUMP_WEREWOLF_1603)
        private val FAIL_LOCATION = Location(3439, 3330)
        private val SPLASH_GFX = Graphics(org.rs.consts.Graphics.WATER_SPLASH_68)
    }
}
