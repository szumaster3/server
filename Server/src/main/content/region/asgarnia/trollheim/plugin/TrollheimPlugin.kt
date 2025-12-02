package content.region.asgarnia.trollheim.plugin

import core.api.*
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.activity.CutscenePlugin
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.WarningManager
import core.game.node.entity.player.link.Warnings
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.*

@Initializable
class TrollheimPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        SceneryDefinition.forId(Scenery.DANGER_SIGN_3742).handlers["option:read"] = this
        SceneryDefinition.forId(Scenery.EXIT_3774).handlers["option:leave"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3723).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3722).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3748).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3790).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3791).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ARENA_ENTRANCE_3782).handlers["option:open"] = this
        SceneryDefinition.forId(Scenery.ARENA_ENTRANCE_3783).handlers["option:open"] = this
        SceneryDefinition.forId(Scenery.CAVE_ENTRANCE_4499).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.TUNNEL_4500).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.ROCKS_9303).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ARENA_ENTRANCE_3782).handlers["option:open"] = this
        SceneryDefinition.forId(Scenery.ARENA_ENTRANCE_3783).handlers["option:open"] = this
        SceneryDefinition.forId(Scenery.ARENA_EXIT_3785).handlers["option:open"] = this
        SceneryDefinition.forId(Scenery.ARENA_EXIT_3786).handlers["option:open"] = this
        SceneryDefinition.forId(Scenery.CAVE_ENTRANCE_3757).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.CAVE_EXIT_3758).handlers["option:exit"] = this
        SceneryDefinition.forId(Scenery.ROCKS_9327).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_9304).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3803).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_3804).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_9306).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.ROCKS_9305).handlers["option:climb"] = this
        SceneryDefinition.forId(Scenery.STRONGHOLD_3771).handlers["option:enter"] = this
        SceneryDefinition.forId(Scenery.TROLL_LADDER_18834).handlers["option:climb-up"] = this
        SceneryDefinition.forId(Scenery.TROLL_LADDER_18833).handlers["option:climb-down"] = this
        definePlugin(WarningZone())
        ActivityManager.register(WarningCutscene())
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val id = if (node is core.game.node.scenery.Scenery) node.id else (node as NPC).id
        val loc = node.location
        when (option) {
            "enter" -> when (id) {
                Scenery.CAVE_ENTRANCE_3735 -> player.properties.teleportLocation = LOCATIONS[0]
                Scenery.CAVE_ENTRANCE_4499 -> player.properties.teleportLocation = LOCATIONS[2]
                Scenery.TUNNEL_4500 -> player.properties.teleportLocation = LOCATIONS[3]
                Scenery.ROCKS_3723 -> player.properties.teleportLocation = LOCATIONS[4]
                Scenery.CAVE_ENTRANCE_3757 -> player.properties.teleportLocation =
                    if (loc == Location(2907, 3652, 0)) LOCATIONS[7] else LOCATIONS[4]

                Scenery.STRONGHOLD_3771 -> player.teleport(Location(2837, 10090, 2))
            }

            "leave" -> player.teleport(Location(2840, 3690))
            "exit" -> when (id) {
                Scenery.CAVE_EXIT_3758 -> player.properties.teleportLocation =
                    if (loc == Location(2906, 10036, 0)) LOCATIONS[6] else LOCATIONS[5]
            }

            "read" -> when (id) {
                Scenery.DANGER_SIGN_3742 -> ActivityManager.start(player, "trollheim-warning", false)
            }

            "open" -> when (id) {
                Scenery.ARENA_EXIT_3785,
                Scenery.ARENA_EXIT_3786,
                Scenery.ARENA_ENTRANCE_3782,
                Scenery.ARENA_ENTRANCE_3783 -> {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    return true
                }

                3672 -> sendMessage(player, "You don't know how to open the secret door.")
            }

            "climb-up" -> when (id) {
                Scenery.TROLL_LADDER_18834 -> ClimbActionHandler.climb(
                    player,
                    ClimbActionHandler.CLIMB_UP,
                    Location(2828, 3678),
                    "You clamber onto the windswept roof of the Troll Stronghold.",
                )
            }

            "climb-down" -> when (id) {
                Scenery.TROLL_LADDER_18833 -> ClimbActionHandler.climb(
                    player,
                    ClimbActionHandler.CLIMB_DOWN,
                    Location(2831, 10076, 2),
                    "You clamber back inside the Troll Stronghold.",
                )
            }

            "climb" -> {
                if (!player.equipment.containsItem(CLIMBING_BOOTS)) {
                    sendMessage(player, "You need Climbing boots to negotiate these rocks.")
                    return true
                }

                val scenery = node as core.game.node.scenery.Scenery

                fun checkRequirements(requiredLevel: Int): Boolean {
                    if (getStatLevel(player, Skills.AGILITY) < requiredLevel) {
                        sendMessage(
                            player, "You need an agility level of $requiredLevel in order to climb this mountain side."
                        )
                        return true
                    }
                    return false
                }

                val xOffset = if (player.location.x < loc.x) 2 else -2
                val yOffset = 0

                player.faceLocation(node.location)

                when (id) {
                    Scenery.ROCKS_3722 -> runClimb(player, Location.create(2880, 3592, 0), CLIMB_DOWN)
                    Scenery.ROCKS_3723 -> runClimb(player, Location.create(2881, 3596, 0), CLIMB_UP)

                    Scenery.ROCKS_3790,
                    Scenery.ROCKS_3791 -> {
                        val anim = if (player.location.x > 2877) CLIMB_DOWN else CLIMB_UP
                        runClimb(player, scenery.location.transform(xOffset, yOffset, 0), anim)
                    }

                    Scenery.ROCKS_3748 -> {
                        when (loc) {
                            Location(2821, 3635, 0) -> runClimb(
                                player, loc.transform(if (player.location.x > loc.x) -1 else 1, 0, 0), JUMP.id
                            )

                            Location(2910, 3687, 0), Location(2910, 3686, 0) -> {
                                if (checkRequirements(43)) return true
                                val target = when (player.location) {
                                    Location(2911, 3687, 0) -> Location(2909, 3687, 0)
                                    Location(2909, 3687, 0) -> Location(2911, 3687, 0)
                                    Location(2911, 3686, 0) -> Location(2909, 3686, 0)
                                    else -> Location(2911, 3686, 0)
                                }
                                runClimb(player, target, JUMP.id)
                            }

                            else -> {
                                val deltaY = if (player.location.y < scenery.location.y) 2 else -2
                                runClimb(player, player.location.transform(0, deltaY, 0), JUMP.id)
                            }
                        }
                    }

                    Scenery.ROCKS_3803,
                    Scenery.ROCKS_3804 -> {
                        if (checkRequirements(43)) return true
                        val target = when (player.location) {
                            Location(2884, 3684, 0) -> Location(2886, 3684, 0)
                            Location(2884, 3683, 0) -> Location(2886, 3683, 0)
                            Location(2886, 3683, 0) -> Location(2884, 3683, 0)
                            Location(2888, 3660, 0), Location(2887, 3660, 0) -> player.location.transform(0, 2, 0)
                            Location(2888, 3662, 0), Location(2887, 3662, 0) -> player.location.transform(0, -2, 0)
                            else -> Location(2884, 3684, 0)
                        }
                        val anim = if (target.y > player.location.y) CLIMB_UP else CLIMB_DOWN
                        runClimb(player, target, anim)
                    }

                    Scenery.ROCKS_9303,
                    Scenery.ROCKS_9304,
                    Scenery.ROCKS_9305,
                    Scenery.ROCKS_9306,
                    Scenery.ROCKS_9327 -> {
                        val requiredLevel = when (id) {
                            9303 -> 41
                            9304 -> 47
                            9305 -> 44
                            9306 -> 47
                            9327 -> 64
                            else -> 0
                        }
                        if (checkRequirements(requiredLevel)) return true
                        val target = when (id) {
                            Scenery.ROCKS_9303 -> if (player.location.x > loc.x) scenery.location.transform(
                                -2, 0, 0
                            ) else scenery.location.transform(2, 0, 0)

                            Scenery.ROCKS_9304 -> if (player.location == Location.create(2878, 3665, 0)) Location.create(
                                2878, 3668, 0
                            ) else Location.create(2878, 3665, 0)

                            Scenery.ROCKS_9305 -> if (player.location == Location.create(2909, 3684, 0)) Location.create(
                                2907, 3682, 0
                            ) else Location.create(2909, 3684, 0)

                            Scenery.ROCKS_9306 -> if (player.location == Location.create(2903, 3680, 0)) Location.create(
                                2900, 3680, 0
                            ) else Location.create(2903, 3680, 0)

                            Scenery.ROCKS_9327 -> when (scenery.location) {
                                Location(2916, 3672, 0) -> Location.create(2918, 3672, 0)
                                Location(2917, 3672, 0) -> Location.create(2915, 3672, 0)
                                Location(2923, 3673, 0) -> Location.create(2921, 3672, 0)
                                Location(2922, 3672, 0) -> Location.create(2924, 3673, 0)
                                Location(2947, 3678, 0) -> Location.create(2950, 3681, 0)
                                Location(2949, 3680, 0) -> Location.create(2946, 3678, 0)
                                else -> player.location
                            }

                            else -> player.location
                        }
                        val anim =
                            if (target.y > player.location.y || target.x > player.location.x) CLIMB_UP else CLIMB_DOWN
                        runClimb(player, target, anim)
                    }
                }
            }
        }
        return true
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is core.game.node.scenery.Scenery) {
            if (n.id == 3782) {
                if (node.location.x >= 2897) {
                    return Location.create(2897, 3618, 0)
                }
            } else if (n.id == 3804) {
                if (n.location == Location(2885, 3684, 0) && node.location.x >= 2885) {
                    return n.location.transform(1, 0, 0)
                }
            } else if (n.id == 9306 && node.location.x >= 2902) {
                return Location.create(2903, 3680, 0)
            } else if (n.id == 9327 && node.asPlayer().location.y >= 3680) {
                return Location.create(2950, 3681, 0)
            }
        }
        return null
    }

    class WarningZone : MapZone("trollheim-warning", true), Plugin<Any?> {
        override fun enter(entity: Entity): Boolean {
            if (entity is Player) {
                val player = entity.asPlayer()
                if (player.walkingQueue.footPrint.y < 3592 &&
                    !WarningManager.isWarningDisabled(player, Warnings.DEATH_PLATEAU))
                {
                    player.walkingQueue.reset()
                    player.pulseManager.clear()
                    WarningManager.openWarningInterface(player, Warnings.DEATH_PLATEAU)
                }
                else
                {
                    return false
                }
            }
            return super.enter(entity)
        }

        override fun configure() {
            register(ZoneBorders(2837, 3592, 2838, 3593))
        }

        override fun newInstance(arg: Any?): Plugin<Any?> {
            ZoneBuilder.configure(this)
            return this
        }

        override fun fireEvent(identifier: String, vararg args: Any?): Any? = null
    }

    class WarningCutscene : CutscenePlugin {
        constructor() : super("trollheim-warning")
        constructor(p: Player?) : super("trollheim-warning", false) {
            this.player = p
        }

        override fun newInstance(p: Player): ActivityPlugin = WarningCutscene(p)

        private fun sendProjectile(npc: NPC) {
            val projectile = Projectile.create(npc, player, 276)
            projectile.speed = 50
            projectile.startHeight = 26
            projectile.endHeight = 1
            projectile.send()
            playAudio(player, Sounds.TROLL_THROW_ROCK_870)
        }

        override fun open() {
            val npc = findNPC(TROLL_LOCATION)
            val loc = Location.create(2849, 3597, 0)
            PacketRepository.send(
                CameraViewPacket::class.java,
                CameraContext(player, CameraContext.CameraType.POSITION, loc.x - 2, loc.y, 1300, 1, 30),
            )
            PacketRepository.send(
                CameraViewPacket::class.java,
                CameraContext(player, CameraContext.CameraType.ROTATION, loc.x + 22, loc.y + 10, 1300, 1, 30),
            )
            Pulser.submit(
                object : Pulse(1, player) {
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            4 -> if (npc != null) {
                                npc.faceTemporary(player, 3)
                                npc.animate(THROW)
                                sendProjectile(npc)
                            }

                            6 -> {
                                this@WarningCutscene.stop(false)
                                PacketRepository.send(
                                    CameraViewPacket::class.java,
                                    CameraContext(player, CameraContext.CameraType.RESET, 0, 0, 1300, 1, 30),
                                )
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        }

        override fun getMapState(): Int = 0

        override fun getSpawnLocation(): Location? = null

        override fun configure() {
            ActivityManager.register(this)
        }

        companion object {
            private val THROW = Animation(Animations.IDLE_1142)
            private val TROLL_LOCATION = Location(2851, 3598, 0)
        }
    }

    companion object {
        private val LOCATIONS = arrayOf(
            Location(2269, 4752, 0),
            Location(2858, 3577, 0),
            Location(2808, 10002, 0),
            Location(2796, 3615, 0),
            Location(2907, 10019, 0),
            Location(2904, 3643, 0),
            Location(2908, 3654, 0),
            Location(2907, 10035, 0),
            Location(2893, 10074, 0),
            Location(2893, 3671, 0)
        )
        private val CLIMBING_BOOTS = Item(Items.CLIMBING_BOOTS_3105)
        private const val CLIMB_DOWN = Animations.WALK_BACKWARDS_CLIMB_1148
        private const val CLIMB_UP = Animations.CLIMB_DOWN_B_740
        private val JUMP = Animation(Animations.CLIMB_OBJECT_839)
    }

    /**
     * Executes a climbing movement.
     * @param player The player who climb.
     * @param to The destination location to move the player to.
     * @param anim The animation id (default is CLIMB_DOWN_B_740).
     * @param direction The direction the player faces during the climb (default is NORTH).
     */
    private fun runClimb(player: Player, to: Location, anim: Int = CLIMB_UP) {
        lock(player, 3)
        lockInteractions(player, 3)
        sendMessage(player, "You climb onto the rock...")
        sendMessage(player, "...and step down the other side.", 3)
        val direction = Direction.getDirection(player.location, to.location)
        ForceMovement.run(player, player.location, to, Animation.create(anim), Animation.create(anim), direction, 13).endAnimation = Animation.RESET
    }
}
