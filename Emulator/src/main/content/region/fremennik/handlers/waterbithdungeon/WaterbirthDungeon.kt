package content.region.fremennik.handlers.waterbithdungeon

import content.global.handlers.iface.warning.WarningManager
import content.global.handlers.iface.warning.Warnings
import content.region.fremennik.handlers.npc.waterbirth.DagannothKingNPC
import content.region.fremennik.handlers.npc.waterbirth.SpinolypNPC
import core.api.quest.isQuestComplete
import core.api.sendMessage
import core.api.teleport
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.settings
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.map.RegionManager.getObject
import core.game.world.map.RegionManager.getRegionPlane
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.game.world.repository.Repository.findNPC
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class WaterbirthDungeon :
    MapZone("Water birth dungeon", true, ZoneRestriction.RANDOM_EVENTS),
    Plugin<Any?> {
    init {
        definePlugin(DagannothKingNPC())
        definePlugin(DoorSupportNPC())
        definePlugin(DungeonOptionHandler())
        definePlugin(SpinolypNPC())
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?>? {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun move(
        e: Entity,
        from: Location,
        to: Location,
    ): Boolean {
        for (location in DOOR_SUPPORTS) {
            if (location == to) {
                val npcs: List<NPC> = getLocalNpcs(location, 0)
                var npc: NPC? = null
                if (npcs.size != 0) {
                    npc = npcs[0]
                }
                if (npc != null && npc is DoorSupportNPC) {
                    val door = npc
                    return door.id != door.originalId
                }
                return false
            }
        }
        if (e is Player) {
            val p = e.asPlayer()
            if (p.location == Location(2545, 10144, 0) || p.location == Location(2545, 10142, 0)) {
                val eggs: MutableList<NPC> = ArrayList(20)
                var n = findNPC(Location(2546, 10142, 0))
                if (n != null && n.id == 2449) {
                    eggs.add(n)
                }
                n = findNPC(Location(2546, 10144, 0))
                if (n != null && n.id == 2449) {
                    eggs.add(n)
                }
                if (eggs.size == 0) {
                    return true
                }
                for (npc in eggs) {
                    if (npc.getAttribute("transforming", 0) > ticks) {
                        return true
                    }
                    npc.setAttribute("transforming", ticks + 3)
                }
                Pulser.submit(
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (++counter) {
                                1 -> for (n in eggs) {
                                    n.transform(n.id + 1)
                                }

                                3 -> {
                                    val spawns: MutableList<NPC> = ArrayList(20)
                                    for (n in eggs) {
                                        n.transform(n.id + 1)
                                        val spawn =
                                            NPC.create(NPCs.DAGANNOTH_SPAWN_2454, n.location.transform(-1, 0, 0))
                                        spawn.init()
                                        spawn.isWalks = true
                                        spawn.isAggressive = true
                                        spawn.isRespawn = false
                                        spawn.attack(p)
                                        spawns.add(spawn)
                                    }
                                    Pulser.submit(
                                        object : Pulse(if (settings!!.isDevMode) 10 else 45) {
                                            override fun pulse(): Boolean {
                                                for (n in eggs) {
                                                    n.reTransform()
                                                }
                                                for (spawn in spawns) {
                                                    if (spawn.isActive && !spawn.inCombat()) {
                                                        spawn.clear()
                                                    }
                                                }
                                                return true
                                            }
                                        },
                                    )
                                }
                            }
                            return counter == 3
                        }
                    },
                )
            }
        }
        return super.move(e, from, to)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (e is Player) {
            val player = e
            when (target.id) {
                8966 -> {
                    player.properties.teleportLocation = Location.create(2523, 3740, 0)
                    return true
                }

                10193 -> {
                    player.properties.teleportLocation = Location(2545, 10143, 0)
                    return true
                }

                10177 -> {
                    when (option.name) {
                        "Climb" -> {
                            player.dialogueInterpreter.sendOptions("Select an Option", "Climb Up.", "Climb Down.")
                            player.dialogueInterpreter.addAction { player, buttonId ->
                                when (buttonId) {
                                    1 ->
                                        ClimbActionHandler.climb(
                                            player,
                                            ClimbActionHandler.CLIMB_UP,
                                            Location(2544, 3741, 0),
                                        )

                                    2 ->
                                        ClimbActionHandler.climb(
                                            player,
                                            ClimbActionHandler.CLIMB_DOWN,
                                            Location(1799, 4406, 3),
                                        )
                                }
                            }
                        }

                        "Climb-down" ->
                            ClimbActionHandler.climb(
                                player,
                                ClimbActionHandler.CLIMB_DOWN,
                                Location(1799, 4406, 3),
                            )

                        "Climb-up" ->
                            ClimbActionHandler.climb(
                                player,
                                ClimbActionHandler.CLIMB_UP,
                                Location(2544, 3741, 0),
                            )
                    }
                    return true
                }

                10217 -> {
                    if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                        teleport(player, Location.create(1957, 4373, 1))
                    } else {
                        sendMessage(player, "You need to have completed Horror from the Deep in order to do this.")
                    }
                    return true
                }

                10230 -> {
                    if (!Warnings.DAGANNOTH_KINGS_LADDER.isDisabled) {
                        WarningManager.openWarning(
                            player,
                            Warnings.DAGANNOTH_KINGS_LADDER,
                        )
                    } else {
                        teleport(player, Location.create(2899, 4449, 0))
                    }
                    return true
                }

                10229 -> {
                    teleport(player, Location.create(1912, 4367, 0))
                    return true
                }
            }
        }
        return super.interact(e, target, option)
    }

    override fun configure() {
        register(ZoneBorders(2423, 10090, 2585, 10195))
        registerRegion(7236)
        registerRegion(7492)
        registerRegion(7748)
        registerRegion(11589)
    }

    class DungeonOptionHandler : OptionHandler() {
        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any> {
            SceneryDefinition.forId(8958).handlers["option:open"] = this
            SceneryDefinition.forId(8959).handlers["option:open"] = this
            SceneryDefinition.forId(8960).handlers["option:open"] = this
            NPCDefinition.forId(2440).handlers["option:destroy"] = this
            NPCDefinition.forId(2443).handlers["option:destroy"] = this
            NPCDefinition.forId(2446).handlers["option:destroy"] = this
            for (l in DOOR_SUPPORTS) {
                SceneryBuilder.remove(getObject(l!!))
            }
            return this
        }

        private fun pressurePadActivated(
            player: Player,
            location: Location,
        ): Boolean {
            return if (getLocalPlayers(location, 0).size > 0) {
                true
            } else {
                getRegionPlane(location).getItem(Items.PET_ROCK_3695, location, player) != null
            }
        }

        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean {
            when (node.id) {
                8958, 8959, 8960 -> {
                    val behind = player.location.x >= 2492
                    if (!behind) {
                        if (!pressurePadActivated(player, node.location.transform(-1, 0, 0)) ||
                            !pressurePadActivated(
                                player,
                                node.location.transform(-1, 2, 0),
                            )
                        ) {
                            player.sendMessage("You cannot see a way to open this door...")
                            return true
                        }
                    }
                    if (!node.isActive) {
                        return true
                    }
                    SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(8962), 30)
                }

                2440, 2443, 2446 -> {
                    val x = node.location.x
                    val y = node.location.y
                    var canAttack = true
                    if (x == 2545 && y == 10145 && player.location.y > y) {
                        canAttack = false
                    } else if (x == 2543 && y == 10143 && player.location.x < x) {
                        canAttack = false
                    } else if (x == 2545 && y == 10141 && player.location.y < y) {
                        canAttack = false
                    }
                    if (!canAttack) {
                        player.sendMessage("The door is propped securely shut from this side...")
                        return true
                    }
                    player.attack(node)
                }
            }
            return true
        }

        override fun getDestination(
            node: Node,
            n: Node,
        ): Location? {
            if (n.name == "Door-support") {
                val player = node.asPlayer()
                if (player.properties.combatPulse.style !== CombatStyle.MELEE) {
                    return node.location
                }
            }
            return null
        }
    }

    class DoorSupportNPC : AbstractNPC {
        private var deathSpawn: Long = -1

        constructor() : super(-1, null)

        constructor(id: Int, location: Location?) : super(id, location)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return DoorSupportNPC(id, location)
        }

        override fun init() {
            lock()
            super.init()
            getSkills().setStaticLevel(Skills.HITPOINTS, 1)
            getSkills().setLevel(Skills.HITPOINTS, 1)
            getSkills().lifepoints = 1
            properties.deathAnimation = Animation(-1)
        }

        override fun handleTickActions() {
            if (deathSpawn != -1L && deathSpawn < ticks) {
                deathSpawn = -1
                transform(originalId)
                getSkills().setStaticLevel(Skills.HITPOINTS, 1)
                getSkills().setLevel(Skills.HITPOINTS, 1)
                getSkills().lifepoints = 1
                fullRestore()
                getSkills().heal(100)
            }
        }

        override fun face(entity: Entity): Boolean {
            return false
        }

        override fun faceLocation(locatin: Location): Boolean {
            return false
        }

        override fun checkImpact(state: BattleState) {
            getSkills().setStaticLevel(Skills.HITPOINTS, 1)
            getSkills().setLevel(Skills.HITPOINTS, 1)
            getSkills().lifepoints = 1
            getSkills().lifepoints = 1
            state.estimatedHit = 1
            lock()
            walkingQueue.reset()
        }

        override fun canStartCombat(victim: Entity): Boolean {
            return false
        }

        override fun commenceDeath(killer: Entity) {
            animator.reset()
            transform(originalId + 1)
            lock()
        }

        override fun finalizeDeath(killer: Entity) {
            animator.reset()
            transform(originalId + 2)
            deathSpawn = (ticks + 55).toLong()
            lock()
        }

        override fun isAttackable(
            entity: Entity,
            style: CombatStyle,
            message: Boolean,
        ): Boolean {
            if (id != originalId) {
                return false
            }
            if (entity.location.getDistance(getLocation()) <= 3) {
                if (entity is Player) {
                    val player = entity.asPlayer()
                    if (message) {
                        player.sendMessage("The door is propped securely shut from this side...")
                    }
                }
                return false
            }
            return style !== CombatStyle.MELEE
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.DOOR_SUPPORT_2440, NPCs.DOOR_SUPPORT_2443, NPCs.DOOR_SUPPORT_2446)
        }
    }

    companion object {
        private val DOOR_SUPPORTS =
            arrayOf(Location.create(2545, 10145, 0), Location.create(2543, 10143, 0), Location.create(2545, 10141, 0))
    }
}
