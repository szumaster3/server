package content.global.skill.slayer.dungeon.waterbirth_dungeon

import core.api.playAudio
import core.api.playGlobalAudio
import core.api.replaceScenery
import core.api.sendMessage
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.*
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.minutesToTicks
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

@Initializable
class WaterbirthDungeon : MapZone(
    "Waterbirth dungeon",
    true,
    ZoneRestriction.RANDOM_EVENTS
), Plugin<Any?> {

    init {
        definePlugin(WaterbirthDungeonOptionHandler())
        definePlugin(DoorSupportNPC())
        definePlugin(SpinolypNPC())
        definePlugin(DagannothKingNPC())
        definePlugin(DagannothSpawnEggNPC())
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? = null

    override fun move(e: Entity, from: Location, to: Location): Boolean {
        if (to !in DoorSupportLocation.ALL) {
            return super.move(e, from, to)
        }

        val door = RegionManager.getLocalNpcs(to, 0)
            .firstOrNull { it is DoorSupportNPC } as? DoorSupportNPC
            ?: return false

        return door.id != door.originalId
    }

    override fun configure() {
        register(ZoneBorders(2423, 10090, 2585, 10195))
        registerRegion(7236)
        registerRegion(7492)
        registerRegion(7748)
        registerRegion(11589)
    }

    /**
     * Handles interactions around dungeon.
     */
    class WaterbirthDungeonOptionHandler : OptionHandler() {

        override fun newInstance(arg: Any?): Plugin<Any> {
            listOf(
                Scenery.DOOR_8958,
                Scenery.DOOR_8959,
                Scenery.DOOR_8960
            ).forEach { id ->
                SceneryDefinition.forId(id).handlers["option:open"] = this
            }

            DoorSupportNPC.IDS.forEach { id ->
                NPCDefinition.forId(id).handlers["option:destroy"] = this
            }

            DoorSupportLocation.ALL.forEach { location ->
                SceneryBuilder.remove(RegionManager.getObject(location))
            }

            return this
        }

        override fun handle(player: Player, node: Node, option: String): Boolean {
            when (node.id) {
                Scenery.DOOR_8958,
                Scenery.DOOR_8959,
                Scenery.DOOR_8960 -> handleDungeonDoor(player, node)

                in DoorSupportNPC.IDS -> handleDoorSupport(player, node)
            }
            return true
        }

        private fun handleDungeonDoor(player: Player, door: Node) {
            if (!canOpenDoor(player, door)) {
                sendMessage(player, "You cannot see a way to open this door...")
                return
            }

            if (door.isActive) {
                playGlobalAudio(player.location, 1065)
                replaceScenery(door.asScenery(), Scenery.DOOR_8962, 30)
            }
        }

        private fun handleDoorSupport(player: Player, node: Node) {
            if (!canAttackFromSide(player, node)) {
                sendMessage(player, "This door does not seem to be openable from this side...")
                return
            }
            player.attack(node)
        }

        private fun canOpenDoor(player: Player, door: Node): Boolean {
            if (player.location.x >= 2492) return true

            val leftPad  = door.location.transform(-1, 0, 0)
            val rightPad = door.location.transform(-1, 2, 0)

            return isPressurePadActive(player, leftPad)
                    && isPressurePadActive(player, rightPad)
        }

        private fun isPressurePadActive(player: Player, location: Location): Boolean =
            RegionManager.getLocalPlayers(location, 0).isNotEmpty() ||
                    RegionManager.getRegionPlane(location).getItem(Items.PET_ROCK_3695, location, player) != null

        private fun canAttackFromSide(player: Player, node: Node): Boolean = when (node.location) {
            DoorSupportLocation.NORTH -> player.location.y <= node.location.y
            DoorSupportLocation.WEST -> player.location.x >= node.location.x
            DoorSupportLocation.SOUTH -> player.location.y >= node.location.y
            else -> true
        }

        override fun getDestination(node: Node, target: Node): Location? {
            if (target.name == "Door-support") {
                val player = node.asPlayer()
                if (player.properties.combatPulse.style != CombatStyle.MELEE) {
                    return node.location
                }
            }
            return null
        }
    }

    /**
     * Handles door support NPC logic.
     */
    class DoorSupportNPC : AbstractNPC {

        private var respawnTick: Long? = null

        constructor() : super(-1, null)
        constructor(id: Int, location: Location?) : super(id, location)

        override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
            DoorSupportNPC(id, location)

        override fun init() {
            lock()
            properties.deathAnimation = Animation(-1)
            super.init()
        }

        override fun handleTickActions() {
            respawnTick?.takeIf { it < ticks }?.let {
                respawnTick = null
                transform(originalId)
            }
        }

        override fun face(entity: Entity?): Boolean = false
        override fun faceLocation(location: Location?): Boolean = false
        override fun canStartCombat(victim: Entity): Boolean = false

        override fun checkImpact(state: BattleState) {
            state.estimatedHit = 1
            lock()
            walkingQueue.reset()
        }

        override fun finalizeDeath(killer: Entity) {
            animator.reset()
            playAudio(killer.asPlayer(), 1071)
            transform(originalId + 1)
            respawnTick = ticks + minutesToTicks(1).toLong()
            lock()
        }

        override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
            if (id != originalId) return false

            if (entity.location.getDistance(location) <= 3) {
                if (message && entity is Player) {
                    sendMessage(entity, "This door does not seem to be openable from this side...")
                }
                return false
            }
            return style != CombatStyle.MELEE
        }

        override fun getIds(): IntArray = IDS

        companion object {
            val IDS = intArrayOf(
                NPCs.DOOR_SUPPORT_2440,
                NPCs.DOOR_SUPPORT_2443,
                NPCs.DOOR_SUPPORT_2446
            )
        }
    }

    /**
     * Represents the door support locations.
     */
    object DoorSupportLocation {
        val NORTH: Location  = Location.create(2545, 10145, 0)
        val WEST:  Location  = Location.create(2542, 10143, 0)
        val SOUTH: Location  = Location.create(2545, 10141, 0)

        val ALL = setOf(NORTH, WEST, SOUTH)
    }
}