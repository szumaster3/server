package core.game.world.map.zone

import content.global.skill.summoning.familiar.Familiar
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.Region
import core.game.world.map.RegionManager
import java.util.Objects

/**
 * Represents a map zone.
 * @author Emperor
 */
abstract class MapZone(
    /**
     * The name of this map zone.
     */
    var name: String,

    /**
     * If the map zone can be overlapped by another zone.
     */
    var overlappable: Boolean,

    /**
     * Optional restrictions to apply to this zone.
     */
    vararg restrictions: ZoneRestriction
) : Zone {

    /**
     * The map zone UID.
     */
    private var uid: Int = 0

    /**
     * If random events should be fired in this zone.
     */
    protected var fireRandomEvents: Boolean = true

    /**
     * Restriction flags.
     */
    private var restriction: Int = 0

    /**
     * Zone type (used for items kept on death).
     */
    private var zoneType: Int = 0

    init {
        restrictions.forEach { addRestriction(it.flag) }
    }

    /**
     * Called when an entity enters this zone.
     * @param e The entity entering.
     * @return True if entry was successful.
     */
    override fun enter(e: Entity): Boolean {
        when (e) {
            is Player -> {}
            is NPC -> if (e is Familiar && isRestricted(ZoneRestriction.FOLLOWERS.flag)) {
                e.isInvisible = true
            }
        }
        return true
    }

    /**
     * Called when an entity leaves this zone.
     * @param e The entity leaving.
     * @param logout If the entity is logging out.
     * @return True if leaving is allowed.
     */
    override fun leave(e: Entity, logout: Boolean): Boolean = true

    /**
     * Checks if the player can log out in this zone.
     */
    open fun canLogout(p: Player): Boolean = true

    /**
     * Called when an entity dies in this zone.
     */
    open fun death(e: Entity, killer: Entity): Boolean = false

    /**
     * Handles interaction with a target node.
     */
    open fun interact(e: Entity, target: Node, option: Option): Boolean = false

    /**
     * Handles using an item with a node.
     */
    open fun handleUseWith(player: Player, used: Item, with: Node): Boolean = false

    /**
     * Handles an action button click.
     */
    open fun actionButton(player: Player, interfaceId: Int, buttonId: Int, slot: Int, itemId: Int, opcode: Int): Boolean = false

    /**
     * Checks if the entity can continue attacking a target.
     */
    open fun continueAttack(e: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean = true

    /**
     * Checks if multi-zone boundaries should be ignored for combat.
     */
    open fun ignoreMultiBoundaries(attacker: Entity, victim: Entity): Boolean = false

    companion object {
        /**
         * Checks if an entity can attack another entity respecting multi-zone rules.
         * @param e Attacker entity.
         * @param t Target entity.
         * @param message Whether to send messages.
         * @return True if attack can continue.
         */
        fun checkMulti(e: Entity, t: Entity, message: Boolean): Boolean {
            val time = System.currentTimeMillis()
            val multi = t.properties.isMultiZone && e.properties.isMultiZone
            if (multi || e.isIgnoreMultiBoundaries(t) || e.zoneMonitor.isIgnoreMultiBoundaries(t)) return true

            var target = t.getAttribute("combat-attacker", e)
            if (t.getAttribute("combat-time", -1L) > time && target != e && target.isActive) {
                if (message && e is Player) e.packetDispatch.sendMessage(
                    "Someone else is already fighting this${if (t is Player) " player." else "."}"
                )
                return false
            }

            val target2 = e.getAttribute("combat-attacker", t)
            if (e.getAttribute("combat-time", -1L) > time && target2 != t && target2.isActive) {
                if (t.id == 1614 || t.id == 1613) return true
                if (message && e is Player) e.packetDispatch.sendMessage("You're already under attack!")
                return false
            }
            return true
        }
    }

    /**
     * Checks if an entity can teleport in this zone.
     */
    open fun teleport(e: Entity, type: Int, node: Node?): Boolean = true

    /**
     * Called to start death sequence.
     */
    open fun startDeath(e: Entity, killer: Entity): Boolean = true

    /**
     * Checks if a request can be made in this zone.
     */
    open fun canRequest(type: RequestType, player: Player, target: Player): Boolean = true

    /**
     * Checks if an entity can move from one location to another.
     */
    open fun move(e: Entity, from: Location, to: Location): Boolean = true

    /**
     * Parses a command in this zone.
     */
    open fun parseCommand(player: Player, name: String, arguments: Array<String>): Boolean = false

    /**
     * Called when an entity changes location.
     */
    open fun locationUpdate(e: Entity, last: Location) {}

    /**
     * Configures this zone.
     */
    open fun configure() {}

    /**
     * Removes specified items from player inventory, equipment, and bank.
     * @param player The player.
     * @param items The items to remove.
     */
    fun cleanItems(player: Player?, items: Array<Item?>) {
        player ?: return
        items.forEach { item ->
            item ?: return@forEach
            if (player.inventory.containsItem(item)) player.inventory.remove(
                Item(
                    item.id, player.inventory.getAmount(item)
                )
            )
            if (player.equipment.containsItem(item)) player.equipment.remove(
                Item(
                    item.id, player.equipment.getAmount(item)
                )
            )
            if (player.bank.containsItem(item)) player.bank.remove(Item(item.id, player.bank.getAmount(item)))
        }
    }

    /**
     * Sends a message to a player entity.
     */
    protected fun message(e: Entity, message: String) {
        if (e !is Player) return
        e.packetDispatch.sendMessage(message)
    }

    /**
     * Registers this zone in the regions for the given borders.
     */
    fun register(borders: ZoneBorders) {
        borders.regionIds.forEach { id ->
            RegionManager.forId(id)?.add(RegionZone(this, borders))
        }
    }

    /**
     * Unregisters this zone from the regions for the given borders.
     */
    fun unregister(borders: ZoneBorders) {
        borders.regionIds.forEach { id ->
            RegionManager.forId(id)?.remove(RegionZone(this, borders))
        }
    }

    /**
     * Registers this zone in a region by id.
     */
    fun registerRegion(regionId: Int) = register(ZoneBorders.forRegion(regionId))

    /**
     * Registers this zone in a region with specific borders.
     */
    fun registerRegion(regionId: Int, borders: ZoneBorders) {
        RegionManager.forId(regionId)?.add(RegionZone(this, borders))
    }

    /**
     * Unregisters this zone from a region by id.
     */
    fun unregisterRegion(regionId: Int) {
        RegionManager.forId(regionId)?.regionZones?.removeIf { it.zone == this }
    }

    /**
     * Disables firing of random events in this zone.
     */
    fun disableRandomEvents() {
        fireRandomEvents = false
    }

    /**
     * Checks if random events are active.
     */
    fun isFireRandoms(): Boolean = fireRandomEvents

    /**
     * Checks if this is a dynamic zone (non-location based zones typically return true).
     */
    fun isDynamicZone(): Boolean = false

    /**
     * Adds a restriction by enum.
     */
    fun addRestriction(restriction: ZoneRestriction) {
        addRestriction(restriction.flag)
    }

    /**
     * Adds a restriction by flag.
     */
    fun addRestriction(flag: Int) {
        restriction = restriction or flag
    }

    /**
     * Checks if a restriction flag is active.
     */
    fun isRestricted(flag: Int): Boolean = restriction and flag != 0

    /**
     * Gets restriction flags.
     */
    fun getRestriction(): Int = restriction

    /**
     * Gets the zone type.
     */
    fun getZoneType(): Int = zoneType

    /**
     * Sets the zone type.
     */
    fun setZoneType(type: Int) {
        zoneType = type
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as MapZone
        return uid == other.uid
    }

    override fun hashCode(): Int = Objects.hash(uid, name, overlappable, fireRandomEvents, restriction, zoneType)

    /**
     * Computes the UID based on the zone name
     */
    fun getUid(): Int = name.hashCode()
}
