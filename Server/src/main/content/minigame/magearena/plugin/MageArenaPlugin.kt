package content.minigame.magearena.plugin

import content.data.GodType
import core.api.*
import core.game.global.action.PickupHandler.take
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class MageArenaPlugin: InteractionListener {
    private val godCapes = intArrayOf(Items.SARADOMIN_CAPE_2412, Items.GUTHIX_CAPE_2413, Items.ZAMORAK_CAPE_2414)
    private val godStatue = intArrayOf(Scenery.STATUE_OF_SARADOMIN_2873, Scenery.STATUE_OF_ZAMORAK_2874, Scenery.STATUE_OF_GUTHIX_2875)
    private val sparklingPool = intArrayOf(Scenery.SPARKLING_POOL_2878, Scenery.SPARKLING_POOL_2879)

    override fun defineListeners() {
        on(godCapes, IntType.ITEM, "take", "drop") { player, node ->
            val type = GodType.forCape(node.asItem())
            if (getUsedOption(player) == "take") {
                val capeOnGround = node as GroundItem
                if (GodType.hasAny(player)) {
                    GroundItemManager.destroy(capeOnGround)
                    sendMessage(player, "You may only possess one sacred cape at a time. The conflicting powers of the capes drive them apart.")
                } else {
                    take(player, capeOnGround)
                }
                return@on true
            } else {
                if (type != null) {
                    sendMessage(player, type.dropMessage)
                    removeItem(player, type.cape)
                }
            }
            return@on true
        }

        on(godStatue, IntType.SCENERY, "pray at") { player, node ->
            player.lock(5)
            queueScript(player, 1, QueueStrength.STRONG) { stage: Int ->
                when (stage) {
                    0 -> {
                        forceWalk(player, player.location.transform(0, -1, 0), "DUMB")
                        return@queueScript delayScript(player, 2)
                    }

                    1 -> {
                        player.faceLocation(node.location)
                        return@queueScript delayScript(player, 1)
                    }

                    2 -> {
                        GodType.forScenery(node.id)?.pray(player, node.asScenery())
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(sparklingPool, IntType.SCENERY, "step-into") { player, node ->
            if (node.id != Scenery.SPARKLING_POOL_2879) {
                if (!player.getSavedData().activityData.hasKilledKolodion()) {
                    sendMessage(player, "You step into the pool.")
                    sendMessage(player, "Your boots get wet.", 1)
                } else {
                    sendDialogueLines(
                        player,
                        "You step into the pool of sparkling water. You feel energy rush",
                        "through your veins.",
                    )
                    addDialogueAction(player) { _, _ ->
                        player.lock(3)
                        queueScript(player, 1, QueueStrength.STRONG) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    forceMove(
                                        player,
                                        player.location,
                                        Location(2542, 4720, 0),
                                        0,
                                        30,
                                        Direction.NORTH,
                                        Animations.JUMP_INTO_WATER_7269,
                                    )
                                    return@queueScript keepRunning(player)
                                }

                                1 -> {
                                    sendGraphics(
                                        Graphics(
                                            68,
                                            10,
                                        ),
                                        player.location,
                                    )
                                    return@queueScript keepRunning(player)
                                }

                                2 -> {
                                    teleport(player, Location(2509, 4689, 0))
                                    resetAnimator(player)
                                    return@queueScript stopExecuting(player)
                                }

                                else -> return@queueScript stopExecuting(player)
                            }
                        }
                        return@addDialogueAction
                    }
                }
                return@on true
            } else {
                player.lock(3)
                queueScript(player, 1, QueueStrength.STRONG) { stage: Int ->
                    when (stage) {
                        0 -> {
                            forceMove(
                                player,
                                player.location,
                                Location(2509, 4687, 0),
                                0,
                                30,
                                Direction.SOUTH,
                                Animations.JUMP_INTO_WATER_7269,
                            )
                            return@queueScript keepRunning(player)
                        }

                        1 -> {
                            sendGraphics(Graphics(68, 10), player.location)
                            return@queueScript keepRunning(player)
                        }

                        2 -> {
                            teleport(player, Location(2542, 4718, 0))
                            resetAnimator(player)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
                return@on true
            }
        }
    }


    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.SPARKLING_POOL_2878) { _, _ ->
            return@setDest Location.create(2542, 4718, 0)
        }
    }
}
