package content.region.misthalin.handlers.lumbridge

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations

@Initializable
class LumbridgeBasementPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(6899).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(6898).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(6905).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(6912).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(5949).handlers["option:jump-across"] = this
        SceneryDefinition.forId(6658).handlers["option:enter"] = this
        SceneryDefinition.forId(32944).handlers["option:enter"] = this
        SceneryDefinition.forId(40261).handlers["option:climb-up"] = this
        SceneryDefinition.forId(40262).handlers["option:climb-up"] = this
        SceneryDefinition.forId(40849).handlers["option:jump-down"] = this
        SceneryDefinition.forId(40260).handlers["option:climb-through"] = this
        SceneryDefinition.forId(41077).handlers["option:crawl-through"] = this
        // definePlugins(LightCreatureNPC(), LightCreatureHandler())
        SceneryBuilder.add(Scenery(40260, Location.create(2526, 5828, 2), 2))
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        when (option) {
            "squeeze-through" -> {
                var dir: Direction? = null
                var to: Location? = null
                when (node.id) {
                    6912 -> {
                        to =
                            if (node.location.y == 9603) {
                                Location.create(3224, 9601, 0)
                            } else {
                                Location.create(
                                    3224,
                                    9603,
                                    0,
                                )
                            }
                        dir = if (node.location.y == 9603) Direction.SOUTH else Direction.NORTH
                    }

                    else -> {
                        to =
                            if (player.location.x >= 3221) {
                                Location.create(3219, 9618, 0)
                            } else {
                                Location.create(
                                    3222,
                                    9618,
                                    0,
                                )
                            }
                        dir = if (player.location.x >= 3221) Direction.WEST else Direction.EAST
                    }
                }
                player.sendMessage("You squeeze through the hole.")
                ForceMovement.run(player, player.location, to, ANIMATION, ANIMATION, dir, 20).endAnimation =
                    Animation.RESET
                return true
            }

            "jump-across" -> {
                var f: Location? = null
                var s: Location? = null
                when (node.id) {
                    5949 -> {
                        f = Location.create(3221, 9554, 0)
                        s =
                            if (player.location.y >= 9556) {
                                Location.create(3221, 9552, 0)
                            } else {
                                Location.create(
                                    3221,
                                    9556,
                                    0,
                                )
                            }
                    }
                }
                val first = f
                val second = s
                player.lock()
                Pulser.submit(
                    object : Pulse(2, player) {
                        var counter: Int = 1

                        override fun pulse(): Boolean {
                            if (counter == 3) {
                                player.unlock()
                                ForceMovement.run(player, player.location, second, JUMP_ANIMATION, 20)
                                player.sendMessage("You leap across with a mighty leap!")
                                return true
                            } else if (counter == 1) {
                                ForceMovement.run(player, player.location, first, JUMP_ANIMATION, 20)
                            }
                            counter++
                            return false
                        }
                    },
                )
            }

            "enter" ->
                when (node.id) {
                    32944 ->

                        player.teleport(Location.create(3219, 9532, 2))

                    6658 ->

                        player.teleport(Location.create(3226, 9542, 0))
                }

            "climb-up" ->
                when (node.id) {
                    40261 -> player.teleport(player.location.transform(0, -1, 1))
                    40262 -> player.teleport(player.location.transform(0, -1, 1))
                }

            "jump-down" ->
                when (node.id) {
                    40849 -> player.teleport(player.location.transform(0, 1, -1))
                }

            "climb-through" ->
                when (node.id) {
                    40260 -> player.teleport(Location.create(2525, 5810, 0))
                }

            "crawl-through" ->
                when (node.id) {
                    41077 -> player.teleport(Location.create(2527, 5830, 2))
                }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is Scenery) {
            if (n.getId() == 5949) {
                return if (node.location.y >= 9555) Location.create(3221, 9556, 0) else Location.create(3221, 9552, 0)
            } else if (n.getId() == 40262) {
                return n.getLocation().transform(0, 1, 0)
            } else if (n.getId() == 40261) {
                return n.getLocation().transform(0, 1, 0)
            }
        }
        return null
    }

    /*
    inner class LightCreatureHandler : UseWithHandler(4700, 4701, 4702) {

        override fun newInstance(arg: Any?): Plugin<Any> {
            addHandler(2021, NPC_TYPE, this)
            return this
        }

        override fun handle(event: NodeUsageEvent): Boolean {
            val player = event.player
            if (!hasRequirement(player, "While Guthix Sleeps")) return true
            player.lock(2)
            player.teleport(Location.create(2538, 5881, 0))
            return true
        }

        override fun getDestination(player: Player, with: Node): Location? {
            if (player.location.withinDistance(with.location)) {
                return player.location
            }
            return null
        }
    }

    inner class LightCreatureNPC : AbstractNPC {
        constructor() : super(0, null) {
            this.isWalks = true
            this.setWalkRadius(10)
        }

        constructor(id: Int, location: Location?) : super(id, location)

        override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
            return LightCreatureNPC(id, location)
        }

        override fun handleTickActions() {
            if (!locks.isMovementLocked) {
                if (isWalks && !pulseManager.hasPulseRunning() && nextWalk < ticks) {
                    setNextWalk()
                    val l = getLocation().transform(
                        -5 + RandomFunction.random(getWalkRadius()),
                        -5 + RandomFunction.random(getWalkRadius()),
                        0
                    )
                    if (canMove(l)) {
                        Pathfinder.find(this, l, true, Pathfinder.PROJECTILE).walk(this)
                    }
                }
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.LIGHT_CREATURE_2021)
        }
    }
     */
    companion object {
        private val ANIMATION = Animation(Animations.DUCK_UNDER_2240)
        private val JUMP_ANIMATION = Animation(Animations.HUMAN_JUMP_SHORT_GAP_741)
    }
}
