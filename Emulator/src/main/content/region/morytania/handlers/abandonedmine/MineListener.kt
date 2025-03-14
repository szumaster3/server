package content.region.morytania.handlers.abandonedmine

import core.api.*
import core.api.utils.Vector
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class MineListener : InteractionListener {
    companion object {
        val STAIRS = intArrayOf(Scenery.STAIRS_4919, Scenery.STAIRS_4923, Scenery.STAIRS_4973)
        val LIFT = intArrayOf(Scenery.LIFT_4937, Scenery.LIFT_4938, Scenery.LIFT_4940, Scenery.LIFT_4942)
        val MINE = intArrayOf(Scenery.CART_TUNNEL_4913, Scenery.CART_TUNNEL_4914, Scenery.CART_TUNNEL_4915)
        val EXIT = intArrayOf(Scenery.CART_TUNNEL_4920, Scenery.CART_TUNNEL_4921, Scenery.CART_TUNNEL_20524)
        val FUNGUS = intArrayOf(Scenery.GLOWING_FUNGUS_4932, Scenery.GLOWING_FUNGUS_4933)
        val CRYSTALS = intArrayOf(Scenery.CRYSTAL_OUTCROP_4926, Scenery.CRYSTAL_OUTCROP_4927, Scenery.CRYSTAL_OUTCROP_4928)
        const val CLIMB_OVER_ANIMATION = Animations.CLIMB_OBJECT_839
        val CLIMB_OVER_DURATION = animationCycles(Animations.CLIMB_OBJECT_839)
    }

    override fun defineListeners() {
        on(Scenery.MINE_CART_4918, IntType.SCENERY, "climb-over") { player, node ->

            val playerLocation = player.location
            val direction = Vector.betweenLocs(player.location, node.location).toDirection()
            val destination = player.location.transform(direction, 2)

            stopWalk(player)
            queueScript(player, 1, QueueStrength.SOFT) { _ ->
                forceMove(
                    player = player,
                    start = playerLocation,
                    dest = destination,
                    startArrive = 0,
                    destArrive = CLIMB_OVER_DURATION,
                    dir = direction,
                    anim = CLIMB_OVER_ANIMATION,
                )
                rewardXP(player, Skills.AGILITY, 1.0)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(FUNGUS, IntType.ITEM, "drop") { player, node ->
            replaceSlot(player, node.asItem().slot, Item(Items.ASHES_592))
            sendMessage(player, "When you drop the fungus it crumbles mysteriously into dust.")
            return@on true
        }

        on(Scenery.MINE_CART_4974, IntType.SCENERY) { player, _ ->
            sendDialogue(player, "The minecart is empty, apart from a few spiders. Yuk!")
            return@on true
        }

        on(CRYSTALS, IntType.SCENERY, "Cut") { player, _ ->
            if (!inInventory(player, Items.CHISEL_1755)) {
                sendMessage(player, "You don't have anything suitable for cutting the crystals.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space to hold the crystal.")
                return@on true
            }
            sendMessage(player, "You cut a shard from the crystal.")
            addItem(player, Items.SALVE_SHARD_4082)
            return@on true
        }

        on(STAIRS, IntType.SCENERY, "walk-up", "walk-down") { player, _ ->
            val option = getUsedOption(player)
            when (option) {
                "walk-up" -> teleport(player, Location.create(3453, 3242, 0))
                else -> teleport(player, Location.create(2791, 4592, 0))
            }
        }

        on(LIFT, IntType.SCENERY, "go-up", "go-down") { player, _ ->
            val option = getUsedOption(player)
            if (option == "go-down") {
                submitWorldPulse(
                    object : Pulse(1, player) {
                        var count = 0

                        override fun pulse(): Boolean {
                            when (count++) {
                                0 -> {
                                    sendMessage(player, "The lift descends further into the mines...")
                                    faceLocation(player, Location(player.location.x, player.location.y - 4, 0))
                                }

                                1 -> {
                                    teleport(player, Location(2725, 4456, 0))
                                    animate(player, Animations.STAND_LOW_WATER_777)
                                }

                                2 -> {
                                    sendMessage(
                                        player,
                                        "...plunging you straight into the middle of a chamber flooded with water.",
                                    )
                                    faceLocation(player, Location(player.location.x, player.location.y - 4, -1))
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            } else {
                submitWorldPulse(
                    object : Pulse(1, player) {
                        var count = 0

                        override fun pulse(): Boolean {
                            when (count++) {
                                0 -> forceWalk(player, player.location.transform(0, 4, 0), "dumb")
                                4 -> {
                                    animate(player, Animations.CROSS_WATER_776)
                                    teleport(player, Location(2807, 4493, 0))
                                    faceLocation(player, Location(player.location.x, player.location.y - 4, -1))
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@on true
        }

        on(MINE, IntType.SCENERY, "crawl-down") { player, node ->
            lock(player, 3)
            val entrance =
                when (node.id) {
                    Scenery.CART_TUNNEL_4913 -> Location.create(3436, 9637, 0)
                    Scenery.CART_TUNNEL_4914 -> Location.create(3405, 9631, 0)
                    Scenery.CART_TUNNEL_4915 -> Location.create(3409, 9623, 0)
                    else -> null
                }

            animate(player, Animations.CRAWLING_2796)

            if (node.id == 4915 &&
                player.location != Location.create(3428, 3225, 0) ||
                (
                    node.id == 4914 &&
                        player.location != Location.create(3429, 3233, 0) ||
                        (node.id == 4913 && player.location != Location.create(3441, 3232, 0))
                )
            ) {
                return@on false
            }

            stopWalk(player)
            queueScript(player, 3, QueueStrength.SOFT) {
                entrance?.let {
                    teleport(player, it)
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(EXIT, IntType.SCENERY, "crawl-through") { player, node ->
            val exit =
                when (node.id) {
                    Scenery.CART_TUNNEL_4920 -> Location.create(3441, 3232, 0)
                    Scenery.CART_TUNNEL_4921 -> Location.create(3429, 3233, 0)
                    Scenery.CART_TUNNEL_20524 -> Location.create(3428, 3225, 0)
                    else -> null
                }
            stopWalk(player)
            animate(player, Animations.CRAWLING_2796)
            queueScript(player, 3, QueueStrength.SOFT) {
                exit?.let {
                    teleport(player, it)
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }
}
