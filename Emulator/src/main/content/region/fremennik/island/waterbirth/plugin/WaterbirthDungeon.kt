package content.region.fremennik.island.waterbirth.plugin

import content.global.plugin.iface.warning.WarningManager
import content.global.plugin.iface.warning.Warnings
import core.api.getRegionBorders
import core.api.inBorders
import core.api.quest.isQuestComplete
import core.api.sendMessage
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
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class WaterbirthDungeon : MapZone("waterbirth dungeon", true, ZoneRestriction.RANDOM_EVENTS), Plugin<Any> {

    companion object {
        private val DOOR_SUPPORTS = arrayOf(
            Location.create(2545, 10145, 0), Location.create(2543, 10143, 0), Location.create(2545, 10141, 0)
        )
    }

    init {
        ClassScanner.definePlugin(DagannothKingNPC())
        ClassScanner.definePlugin(DoorSupportNPC())
        ClassScanner.definePlugin(DungeonOptionHandler())
        ClassScanner.definePlugin(SpinolypNPC())
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any?): Any? = null

    override fun move(e: Entity, from: Location, to: Location): Boolean {
        DOOR_SUPPORTS.forEach { loc ->
            if (loc == to) {
                val npcs = RegionManager.getLocalNpcs(loc, 0)
                val npc = npcs.firstOrNull()
                return npc is DoorSupportNPC && npc.id != npc.originalId
            }
        }

        if (e is Player) {
            val p = e
            if (p.location == Location(2545, 10144, 0) || p.location == Location(2545, 10142, 0)) {
                val eggs = mutableListOf<NPC>()
                listOf(
                    Location(2546, 10142, 0), Location(2546, 10144, 0)
                ).forEach {
                    Repository.findNPC(it)?.takeIf { npc -> npc.id == 2449 }?.let { npc -> eggs.add(npc) }
                }
                if (eggs.isEmpty()) return true

                val currentTicks = GameWorld.ticks
                eggs.forEach { npc ->
                    if (npc.getAttribute("transforming", 0) > currentTicks) return true
                    npc.setAttribute("transforming", currentTicks + 3)
                }

                GameWorld.Pulser.submit(object : Pulse(1) {
                    var counter = 0
                    override fun pulse(): Boolean {
                        counter++
                        when (counter) {
                            1 -> eggs.forEach { it.transform(it.id + 1) }
                            3 -> {
                                val spawns = mutableListOf<NPC>()
                                eggs.forEach { npc ->
                                    npc.transform(npc.id + 1)
                                    val spawn = NPC.create(2454, npc.location.transform(-1, 0, 0))
                                    spawn.init()
                                    spawn.setWalks(true)
                                    spawn.setAggressive(true)
                                    spawn.setRespawn(false)
                                    spawn.attack(p)
                                    spawns.add(spawn)
                                }
                                GameWorld.Pulser.submit(object : Pulse(if (GameWorld.settings!!.isDevMode) 10 else 45) {
                                    override fun pulse(): Boolean {
                                        eggs.forEach { it.reTransform() }
                                        spawns.filter { it.isActive && !it.inCombat() }.forEach { it.clear() }
                                        return true
                                    }
                                })
                            }
                        }
                        return counter == 3
                    }
                })
            }
        }

        return super.move(e, from, to)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e !is Player) return super.interact(e, target, option)
        val player = e
        when (target.id) {
            8966 -> ClimbActionHandler.climb(player, null, Location(2523, 3740, 0))
            10193 -> ClimbActionHandler.climb(player, null, Location(2545, 10143, 0))
            10177 -> when (option.name) {
                "Climb" -> {
                    player.dialogueInterpreter.sendOptions("Select an Option", "Climb Up.", "Climb Down.")
                    player.dialogueInterpreter.addAction { player, buttonId ->
                        when (buttonId) {
                            1 -> ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(2544, 3741, 0))
                            2 -> ClimbActionHandler.climb(
                                player, ClimbActionHandler.CLIMB_DOWN, Location(1799, 4406, 3)
                            )
                        }
                    }
                }
                "Climb-down" -> ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, Location(1799, 4406, 3))
                "Climb-up"   -> ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(2544, 3741, 0))
            }

            10217 -> {
                if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                    ClimbActionHandler.climb(player, null, Location.create(1957, 4373, 1))
                } else {
                    player.sendMessage("You need to have completed Horror from the Deep in order to do this.")
                }
            }

            10230 -> {
                if (!WarningManager.isDisabled(player, Warnings.DAGANNOTH_KINGS_LADDER)) {
                    WarningManager.openWarning(player, Warnings.DAGANNOTH_KINGS_LADDER)
                } else {
                    ClimbActionHandler.climb(player, null, Location.create(2899, 4449, 0))
                }
            }

            10229 -> ClimbActionHandler.climb(player, null, Location.create(1912, 4367, 0))
            else -> return super.interact(e, target, option)
        }
        return true
    }

    override fun configure() {
        register(ZoneBorders(2423, 10090, 2585, 10195))
        registerRegion(7236)
        registerRegion(7492)
        registerRegion(7748)
        registerRegion(11589)
    }

    class DungeonOptionHandler : OptionHandler() {

        override fun newInstance(arg: Any?): Plugin<Any> {
            SceneryDefinition.forId(8958).handlers["option:open"]
            SceneryDefinition.forId(8959).handlers["option:open"]
            SceneryDefinition.forId(8960).handlers["option:open"]

            NPCDefinition.forId(2440).handlers["option:destroy"]
            NPCDefinition.forId(2443).handlers["option:destroy"]
            NPCDefinition.forId(2446).handlers["option:destroy"]

            DOOR_SUPPORTS.forEach { SceneryBuilder.remove(RegionManager.getObject(it)) }
            return this
        }

        private fun pressurePadActivated(player: Player, location: Location): Boolean {
            if (RegionManager.getLocalPlayers(location, 0).isNotEmpty()) return true
            if (RegionManager.getRegionPlane(location).getItem(Items.PET_ROCK_3695, location, player) != null) return true
            return false
        }

        override fun handle(player: Player, node: Node, option: String): Boolean {
            when (node.id) {
                8929 -> {
                    if (inBorders(player, getRegionBorders(10042))) {
                        player.teleporter.send(Location.create(2442, 10147, 0))
                        ClimbActionHandler.climb(player, null, Location.create(2442, 10147, 0))
                    } else {
                        sendMessage(player, "You venture into the icy cavern.")
                        player.teleporter.send(Location.create(3056, 9555, 0))
                    }
                }
                8930 -> player.teleporter.send(Location.create(2545, 10143, 0))
                8958, 8959, 8960 -> {
                    val behind = player.location.x >= 2492
                    if (!behind) {
                        if (!pressurePadActivated(player, node.location.transform(-1, 0, 0)) || !pressurePadActivated(player, node.location.transform(-1, 2, 0))) {
                            player.sendMessage("You cannot see a way to open this door...")
                            return true
                        }
                    }
                    if (!node.isActive) return true
                    SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(8962), 30)
                }

                2440, 2443, 2446 -> {
                    val x = node.location.x
                    val y = node.location.y
                    var canAttack = true
                    when {
                        x == 2545 && y == 10145 && player.location.y > y -> canAttack = false
                        x == 2543 && y == 10143 && player.location.x < x -> canAttack = false
                        x == 2545 && y == 10141 && player.location.y < y -> canAttack = false
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

        override fun getDestination(node: Node, n: Node): Location? {
            if (n.name == "Door-support") {
                val player = node.asPlayer()
                if (player.properties.combatPulse.style != CombatStyle.MELEE) {
                    return node.location
                }
            }
            return null
        }
    }

    class DoorSupportNPC : AbstractNPC {

        private var deathSpawn = -1L

        constructor() : super(-1, null)
        constructor(id: Int, location: Location) : super(id, location)

        override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC =
            DoorSupportNPC(id, location)

        override fun init() {
            lock()
            super.init()
            skills.setStaticLevel(Skills.HITPOINTS, 1)
            skills.setLevel(Skills.HITPOINTS, 1)
            skills.lifepoints = 1
            properties.deathAnimation = Animation(-1)
        }

        override fun handleTickActions() {
            if (deathSpawn != -1L && deathSpawn < GameWorld.ticks) {
                deathSpawn = -1
                transform(originalId)
                skills.setStaticLevel(Skills.HITPOINTS, 1)
                skills.setLevel(Skills.HITPOINTS, 1)
                skills.lifepoints = 1
                fullRestore()
                skills.heal(100)
            }
        }

        override fun checkImpact(state: BattleState) {
            skills.setStaticLevel(Skills.HITPOINTS, 1)
            skills.setLevel(Skills.HITPOINTS, 1)
            skills.lifepoints = 1
            state.estimatedHit = 1
            lock()
            walkingQueue.reset()
        }

        override fun canStartCombat(victim: Entity): Boolean = false

        override fun finalizeDeath(killer: Entity) {
            animator.reset()
            transform(originalId + 1)
            deathSpawn = GameWorld.ticks + 55L
            lock()
        }

        override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
            if (id != originalId) return false
            if (entity.location.getDistance(location) <= 3) {
                if (entity is Player && message) {
                    sendMessage(entity, "The door is propped securely shut from this side...")
                }
                return false
            }
            return style != CombatStyle.MELEE
        }

        override fun getIds(): IntArray =
            intArrayOf(NPCs.DOOR_SUPPORT_2440, NPCs.DOOR_SUPPORT_2443, NPCs.DOOR_SUPPORT_2446)
    }
}
