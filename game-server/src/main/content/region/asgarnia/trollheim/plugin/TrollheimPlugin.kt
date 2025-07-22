package content.region.asgarnia.trollheim.plugin

import content.global.plugin.iface.warning.WarningManager
import content.global.plugin.iface.warning.Warnings
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
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.CameraViewPacket
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Sounds

@Initializable
class TrollheimPlugin : OptionHandler() {
    private val rockScenery =
        intArrayOf(
            Scenery.ROCKS_3748,
            Scenery.ROCKS_3723,
            Scenery.ROCKS_3722,
            Scenery.ROCKS_3804,
            Scenery.ROCKS_3803,
            Scenery.ROCKS_3791,
            Scenery.ROCKS_3790,
            Scenery.ROCKS_9327,
            Scenery.ROCKS_9306,
            Scenery.ROCKS_9305,
            Scenery.ROCKS_9304,
            Scenery.ROCKS_9303,
        )
    private val arenaScenery = intArrayOf(3786, 3785, 3783, 3782)

    override fun newInstance(arg: Any?): Plugin<Any?> {
        NPCDefinition.forId(1069).handlers["option:talk-to"] = this
        SceneryDefinition.forId(3742).handlers["option:read"] = this
        SceneryDefinition.forId(3774).handlers["option:leave"] = this
        SceneryDefinition.forId(3723).handlers["option:climb"] = this
        SceneryDefinition.forId(3722).handlers["option:climb"] = this
        SceneryDefinition.forId(3748).handlers["option:climb"] = this
        SceneryDefinition.forId(3790).handlers["option:climb"] = this
        SceneryDefinition.forId(3791).handlers["option:climb"] = this
        SceneryDefinition.forId(3782).handlers["option:open"] = this
        SceneryDefinition.forId(3783).handlers["option:open"] = this

        SceneryDefinition.forId(4499).handlers["option:enter"] = this
        SceneryDefinition.forId(4500).handlers["option:enter"] = this
        SceneryDefinition.forId(9303).handlers["option:climb"] = this
        SceneryDefinition.forId(3782).handlers["option:open"] = this
        SceneryDefinition.forId(3783).handlers["option:open"] = this
        SceneryDefinition.forId(3785).handlers["option:open"] = this
        SceneryDefinition.forId(3786).handlers["option:open"] = this
        SceneryDefinition.forId(3757).handlers["option:enter"] = this
        SceneryDefinition.forId(3758).handlers["option:exit"] = this
        SceneryDefinition.forId(9327).handlers["option:climb"] = this
        SceneryDefinition.forId(9304).handlers["option:climb"] = this
        SceneryDefinition.forId(3803).handlers["option:climb"] = this
        SceneryDefinition.forId(3804).handlers["option:climb"] = this
        SceneryDefinition.forId(9306).handlers["option:climb"] = this
        SceneryDefinition.forId(9305).handlers["option:climb"] = this
        SceneryDefinition.forId(3771).handlers["option:enter"] = this
        SceneryDefinition.forId(18834).handlers["option:climb-up"] = this
        SceneryDefinition.forId(18833).handlers["option:climb-down"] = this
        definePlugin(WarningZone())
        ActivityManager.register(WarningCutscene())
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val id = if (node is core.game.node.scenery.Scenery) node.id else (node as NPC).id
        val loc = node.location
        var xOffset = 0
        val yOffset = 0
        when (option) {
            "enter" ->
                when (id) {
                    3735 -> player.properties.teleportLocation = LOCATIONS[0]
                    4499 -> player.properties.teleportLocation = LOCATIONS[2]
                    4500 -> player.properties.teleportLocation = LOCATIONS[3]
                    3723 -> player.properties.teleportLocation = LOCATIONS[4]
                    3757 ->
                        player.properties.teleportLocation =
                            if (loc == Location(2907, 3652, 0)) LOCATIONS[7] else LOCATIONS[4]

                    3771 -> player.teleport(Location(2837, 10090, 2))
                }

            "leave" -> player.teleport(Location(2840, 3690))
            "exit" ->
                when (id) {
                    3758 ->
                        player.properties.teleportLocation =
                            if (loc == Location(2906, 10036, 0)) LOCATIONS[6] else LOCATIONS[5]
                }

            "talk-to" ->
                when (id) {
                    1069 -> player.dialogueInterpreter.open(id, node)
                }

            "read" ->
                when (id) {
                    3742 -> ActivityManager.start(player, "trollheim-warning", false)
                }

            "open" ->
                when (id) {
                    3785, 3786, 3782, 3783 -> {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        return true
                    }

                    3672 -> sendMessage(player, "You don't know how to open the secret door.")
                }

            "climb-up" ->
                when (id) {
                    18834 ->
                        ClimbActionHandler.climb(
                            player,
                            ClimbActionHandler.CLIMB_UP,
                            Location(2828, 3678),
                            "You clamber onto the windswept roof of the Troll Stronghold.",
                        )
                }

            "climb-down" ->
                when (id) {
                    18833 ->
                        ClimbActionHandler.climb(
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
                lock(player, 3)
                lockInteractions(player, 3)
                sendMessage(player, "You climb onto the rock...")
                sendMessageWithDelay(player, "...and step down the other side.", 3)
                xOffset = if (player.location.x < loc.x) 2 else -2
                val scenery = (node as core.game.node.scenery.Scenery)
                when (id) {
                    3722 ->
                        ForceMovement
                            .run(
                                player,
                                player.location,
                                Location.create(2880, 3592, 0),
                                Animation.create(CLIMB_DOWN),
                                Animation.create(CLIMB_DOWN),
                                Direction.NORTH,
                                13,
                            ).endAnimation =
                            Animation.RESET

                    3723 ->
                        ForceMovement
                            .run(
                                player,
                                player.location,
                                Location.create(2881, 3596, 0),
                                Animation.create(CLIMB_UP),
                                Animation.create(CLIMB_UP),
                                Direction.NORTH,
                                13,
                            ).endAnimation =
                            Animation.RESET

                    3790 -> {
                        if (player.location.x > 2877) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location.transform(xOffset, yOffset, 0),
                                    scenery.location.transform(xOffset, yOffset, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.EAST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location.transform(xOffset, yOffset, 0),
                                    scenery.location.transform(xOffset, yOffset, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.WEST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                        if (player.location.x < 2877 || player.location.equals(2878, 3622, 0)) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location.transform(xOffset, yOffset, 0),
                                    scenery.location.transform(xOffset, yOffset, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.WEST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location.transform(xOffset, yOffset, 0),
                                    scenery.location.transform(xOffset, yOffset, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.EAST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                    }

                    3791 -> {
                        lock(player, 3)
                        lockInteractions(player, 3)
                        xOffset = if (player.location.x < loc.x) 2 else -2
                        if (player.location.x < 2877 || player.location.equals(2878, 3622, 0)) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location.transform(xOffset, yOffset, 0),
                                    scenery.location.transform(xOffset, yOffset, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.WEST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location.transform(xOffset, yOffset, 0),
                                    scenery.location.transform(xOffset, yOffset, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.EAST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                    }

                    3748 -> {
                        when (loc) {
                            Location(2821, 3635, 0) ->
                                ForceMovement.run(
                                    player,
                                    player.location,
                                    loc.transform(
                                        if (player.location.x >
                                            loc.x
                                        ) {
                                            -1
                                        } else {
                                            1
                                        },
                                        0,
                                        0,
                                    ),
                                    JUMP,
                                )

                            Location(2910, 3687, 0), Location(2910, 3686, 0) -> {
                                if (getStatLevel(player, Skills.AGILITY) < 43) {
                                    sendMessage(
                                        player,
                                        "You need an agility level of 43 in order to climb this mountain side.",
                                    )
                                    return true
                                }
                                when (player.location) {
                                    Location.create(
                                        2911,
                                        3687,
                                        0,
                                    ),
                                    ->
                                        ForceMovement.run(
                                            player,
                                            player.location,
                                            Location.create(2909, 3687, 0),
                                            JUMP,
                                        )

                                    Location(
                                        2909,
                                        3687,
                                        0,
                                    ),
                                    ->
                                        ForceMovement.run(
                                            player,
                                            player.location,
                                            Location.create(2911, 3687, 0),
                                            JUMP,
                                        )

                                    Location.create(
                                        2911,
                                        3686,
                                        0,
                                    ),
                                    ->
                                        ForceMovement.run(
                                            player,
                                            player.location,
                                            Location.create(2909, 3686, 0),
                                            JUMP,
                                        )

                                    else ->
                                        ForceMovement.run(
                                            player,
                                            player.location,
                                            Location.create(2911, 3686, 0),
                                            JUMP,
                                        )
                                }
                            }

                            else ->
                                ForceMovement.run(
                                    player,
                                    player.location,
                                    if (player.location.y <
                                        scenery.location.y
                                    ) {
                                        player.location.transform(0, 2, 0)
                                    } else {
                                        player.location.transform(0, -2, 0)
                                    },
                                    JUMP,
                                )
                        }
                    }

                    3803, 3804 -> {
                        if (getStatLevel(player, Skills.AGILITY) < 43) {
                            sendMessage(player, "You need an agility level of 43 in order to climb this mountain side.")
                            return true
                        }
                        lock(player, 3)
                        lockInteractions(player, 3)
                        sendMessage(player, "You climb onto the rock...")
                        sendMessageWithDelay(player, "...and step down the other side.", 3)
                        when (player.location) {
                            Location.create(2884, 3684, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2886, 3684, 0),
                                        Animation.create(CLIMB_UP),
                                        Animation.create(CLIMB_UP),
                                        Direction.WEST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location.create(2884, 3683, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2886, 3683, 0),
                                        Animation.create(CLIMB_UP),
                                        Animation.create(CLIMB_UP),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location.create(2886, 3683, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2884, 3683, 0),
                                        Animation.create(CLIMB_DOWN),
                                        Animation.create(CLIMB_DOWN),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location.create(2888, 3660, 0),
                            Location.create(2887, 3660, 0),
                            ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        player.location.transform(0, 2, 0),
                                        Animation.create(CLIMB_UP),
                                        Animation.create(CLIMB_UP),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location.create(2888, 3662, 0),
                            Location.create(2887, 3662, 0),
                            ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        player.location.transform(0, -2, 0),
                                        Animation.create(CLIMB_DOWN),
                                        Animation.create(CLIMB_DOWN),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            else ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2884, 3684, 0),
                                        Animation.create(CLIMB_DOWN),
                                        Animation.create(CLIMB_DOWN),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET
                        }
                    }

                    9306 -> {
                        if (getStatLevel(player, Skills.AGILITY) < 47) {
                            sendMessage(player, "You need an agility level of 47 in order to climb this mountain side.")
                            return true
                        }
                        lock(player, 3)
                        lockInteractions(player, 3)
                        if (player.location == Location.create(2903, 3680, 0)) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    Location.create(2900, 3680, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.WEST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    Location.create(2903, 3680, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.WEST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                    }

                    9303 -> {
                        if (getStatLevel(player, Skills.AGILITY) < 41) {
                            sendMessage(player, "You need an agility level of 41 in order to climb this mountain side.")
                            return true
                        }
                        lock(player, 3)
                        lockInteractions(player, 3)
                        if (player.location.x > loc.x) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    scenery.location.transform(-2, 0, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.EAST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    scenery.location.transform(2, 0, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.EAST,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                    }

                    9304 -> {
                        if (getStatLevel(player, Skills.AGILITY) < 47) {
                            sendMessage(player, "You need an agility level of 47 in order to climb this mountain side.")
                            return true
                        }
                        lock(player, 3)
                        lockInteractions(player, 3)
                        if (player.location == Location.create(2878, 3665, 0)) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    Location.create(2878, 3668, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.NORTH,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    Location.create(2878, 3665, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.NORTH,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                    }

                    9305 -> {
                        if (getStatLevel(player, Skills.AGILITY) < 44) {
                            sendMessage(player, "You need an agility level of 44 in order to climb this mountain side.")
                            return true
                        }
                        lock(player, 3)
                        lockInteractions(player, 3)
                        if (player.location == Location(2909, 3684, 0)) {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    Location.create(2907, 3682, 0),
                                    Animation.create(CLIMB_UP),
                                    Animation.create(CLIMB_UP),
                                    Direction.SOUTH,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        } else {
                            ForceMovement
                                .run(
                                    player,
                                    player.location,
                                    Location.create(2909, 3684, 0),
                                    Animation.create(CLIMB_DOWN),
                                    Animation.create(CLIMB_DOWN),
                                    Direction.SOUTH,
                                    13,
                                ).endAnimation =
                                Animation.RESET
                        }
                    }

                    9327 -> {
                        if (getStatLevel(player, Skills.AGILITY) < 64) {
                            sendMessage(player, "You need an agility level of 64 in order to climb this mountain side.")
                            return true
                        }
                        lock(player, 3)
                        lockInteractions(player, 3)
                        sendMessage(player, "You climb onto the rock...")
                        sendMessageWithDelay(player, "...and step down the other side.", 3)
                        when (scenery.location) {
                            Location(2916, 3672, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2918, 3672, 0),
                                        Animation.create(CLIMB_UP),
                                        Animation.create(CLIMB_UP),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location(2917, 3672, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2915, 3672, 0),
                                        Animation.create(CLIMB_DOWN),
                                        Animation.create(CLIMB_DOWN),
                                        Direction.EAST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location(2923, 3673, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2921, 3672, 0),
                                        Animation.create(CLIMB_UP),
                                        Animation.create(CLIMB_UP),
                                        Direction.WEST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location(2922, 3672, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2924, 3673, 0),
                                        Animation.create(CLIMB_DOWN),
                                        Animation.create(CLIMB_DOWN),
                                        Direction.WEST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location(2947, 3678, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2950, 3681, 0),
                                        Animation.create(CLIMB_DOWN),
                                        Animation.create(CLIMB_DOWN),
                                        Direction.WEST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET

                            Location(2949, 3680, 0) ->
                                ForceMovement
                                    .run(
                                        player,
                                        player.location,
                                        Location.create(2946, 3678, 0),
                                        Animation.create(CLIMB_UP),
                                        Animation.create(CLIMB_UP),
                                        Direction.WEST,
                                        13,
                                    ).endAnimation =
                                    Animation.RESET
                        }
                    }
                }
            }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
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

    private fun getOpposite(dir: Direction): Direction? {
        when (dir) {
            Direction.EAST -> return Direction.WEST
            Direction.NORTH -> return Direction.SOUTH
            Direction.SOUTH -> return Direction.NORTH
            Direction.WEST -> return Direction.EAST
            else -> {}
        }
        return null
    }

    class WarningZone :
        MapZone("trollheim-warning", true),
        Plugin<Any?> {
        override fun enter(entity: Entity): Boolean {
            if (entity is Player) {
                val player = entity.asPlayer()
                if (player.walkingQueue.footPrint.y < 3592 && !WarningManager.isDisabled(player, Warnings.DEATH_PLATEAU)) {
                    player.walkingQueue.reset()
                    player.pulseManager.clear()
                    WarningManager.openWarning(player, Warnings.DEATH_PLATEAU)
                } else {
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

        override fun fireEvent(
            identifier: String,
            vararg args: Any?,
        ): Any? = null
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
                OutgoingContext.Camera(
                    player,
                    OutgoingContext.CameraType.POSITION,
                    loc.x - 2,
                    loc.y,
                    1300,
                    1,
                    30,
                ),
            )
            PacketRepository.send(
                CameraViewPacket::class.java,
                OutgoingContext.Camera(
                    player,
                    OutgoingContext.CameraType.ROTATION,
                    loc.x + 22,
                    loc.y + 10,
                    1300,
                    1,
                    30,
                ),
            )
            Pulser.submit(
                object : Pulse(1, player) {
                    var count: Int = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            4 ->
                                if (npc != null) {
                                    npc.faceTemporary(player, 3)
                                    npc.animate(THROW)
                                    sendProjectile(npc)
                                }

                            6 -> {
                                this@WarningCutscene.stop(false)
                                PacketRepository.send(
                                    CameraViewPacket::class.java,
                                    OutgoingContext.Camera(player, OutgoingContext.CameraType.RESET, 0, 0, 1300, 1, 30),
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
            private val THROW = Animation(Animations.MOUNTAIN_TROLL_THROW_ROCK_1142)
            private val TROLL_LOCATION = Location(2851, 3598, 0)
        }
    }

    companion object {
        private val LOCATIONS =
            arrayOf(
                Location(2269, 4752, 0),
                Location(2858, 3577, 0),
                Location.create(2808, 10002, 0),
                Location.create(2796, 3615, 0),
                Location.create(2907, 10019, 0),
                Location.create(2904, 3643, 0),
                Location(2908, 3654, 0),
                Location.create(2907, 10035, 0),
                Location(2893, 10074, 0),
                Location(2893, 3671, 0),
            )
        private val CLIMBING_BOOTS = Item(Items.CLIMBING_BOOTS_3105)
        private const val CLIMB_DOWN = Animations.WALK_BACKWARDS_CLIMB_1148
        private const val CLIMB_UP = Animations.CLIMB_DOWN_B_740
        val CLIMB_FAIL = Animations.CLIMB_DOWN_A_739
        private val JUMP = Animation(Animations.CLIMB_OBJECT_839)
    }
}
