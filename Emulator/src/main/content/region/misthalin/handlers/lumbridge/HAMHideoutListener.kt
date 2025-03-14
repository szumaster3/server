package content.region.misthalin.handlers.lumbridge

import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Scenery

class HAMHideoutListener : InteractionListener {
    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "pick-lock", "open", "climb-up", "climb-down") { player, node ->
            val id = node.id
            val option = getUsedOption(player)
            when (id) {
                Scenery.LADDER_5493 -> {
                    if (withinDistance(player, Location.create(3149, 9652, 0))) {
                        ClimbActionHandler.climb(
                            player,
                            Animation(Animations.HUMAN_CLIMB_STAIRS_828),
                            Location(3165, 3251, 0),
                        )
                        return@on true
                    }
                    ClimbActionHandler.climbLadder(player, node.asScenery(), option)
                    sendMessage(player, "You leave the HAM Fanatics' Camp.")
                    return@on true
                }

                Scenery.TRAPDOOR_5490, Scenery.TRAPDOOR_5491 ->
                    when (option) {
                        "open" -> {
                            if (getVarp(player, LumbridgeUtils.hamHideoutEntranceVarp) == 0) {
                                sendMessage(player, "This trapdoor seems totally locked.")
                            } else {
                                setVarp(player, 346, 272731282)
                                ClimbActionHandler.climb(
                                    player,
                                    Animation(Animations.MULTI_BEND_OVER_827),
                                    Location(3149, 9652, 0),
                                )
                                submitIndividualPulse(
                                    player,
                                    object : Pulse(2, player) {
                                        override fun pulse(): Boolean {
                                            setVarp(player, LumbridgeUtils.hamHideoutEntranceVarp, 0)
                                            return true
                                        }
                                    },
                                )
                            }
                        }

                        "close" -> {
                            setVarp(player, LumbridgeUtils.hamHideoutEntranceVarp, 0)
                        }

                        "climb-down" ->
                            when (id) {
                                Scenery.TRAPDOOR_5491 -> {
                                    player.properties.teleportLocation = Location.create(3149, 9652, 0)
                                    sendMessage(player, "You climb down through the trapdoor...")
                                    sendMessage(player, "...and enter a dimly lit cavern area.")
                                }
                            }

                        "pick-lock" -> {
                            lock(player, 3)
                            animate(player, Animations.MULTI_BEND_OVER_827)
                            sendMessage(player, "You attempt to pick the lock on the trap door.")
                            submitIndividualPulse(
                                player,
                                object : Pulse(2) {
                                    override fun pulse(): Boolean {
                                        animate(player, Animations.MULTI_BEND_OVER_827)
                                        sendMessage(player, "You attempt to pick the lock on the trap door.")
                                        val success = RandomFunction.random(3) == 1
                                        sendMessage(
                                            player,
                                            if (success) "You pick the lock on the trap door." else "You fail to pick the lock - your fingers get numb from fumbling with the lock.",
                                        )
                                        unlock(player)
                                        if (success) {
                                            setVarp(player, LumbridgeUtils.hamHideoutEntranceVarp, 1 shl 14)
                                            submitWorldPulse(
                                                object : Pulse(40, player) {
                                                    override fun pulse(): Boolean {
                                                        setVarp(player, LumbridgeUtils.hamHideoutEntranceVarp, 0)
                                                        return true
                                                    }
                                                },
                                            )
                                        }
                                        return true
                                    }
                                },
                            )
                        }
                    }
            }
            return@on true
        }
    }

    companion object {
        val sceneryIDs = intArrayOf(Scenery.TRAPDOOR_5490, Scenery.TRAPDOOR_5491, Scenery.LADDER_5493)
    }
}
