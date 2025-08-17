package core.game.world.map.zone

import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.music.MusicZone
import core.game.node.entity.player.link.request.RequestType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.Region
import shared.consts.Items

/**
 * Handles the zones for an entity.
 *
 * @param entity The entity whose zones are being monitored.
 *
 * @author Emperor
 */
class ZoneMonitor(val entity: Entity) {

    /**
     * List of region zones the entity is currently in.
     */
    private val zones: MutableList<RegionZone> = ArrayList(20)

    /**
     * List of music zones the entity is currently in.
     */
    private val musicZones: MutableList<MusicZone> = ArrayList(20)

    /**
     * Returns the type of the first non-zero zone type the entity is in.
     * @return The zone type, or 0 if no zones have a type.
     */
    fun getType(): Int {
        for (zone in zones) {
            if (zone.zone.getZoneType() != 0) return zone.zone.getZoneType()
        }
        return 0
    }

    /**
     * Checks if the entity (player) can log out.
     * @return True if logout is allowed in all zones.
     */
    fun canLogout(): Boolean {
        return zones.all { it.zone.canLogout(entity as? Player ?: return true) }
    }

    /**
     * Checks if an any restriction is active for this entity.
     * @param restriction The restriction to check.
     * @return True if restricted.
     */
    fun isRestricted(restriction: ZoneRestriction): Boolean = isRestricted(restriction.flag)

    /**
     * Checks if an any restriction flag is active for this entity.
     * @param flag The restriction flag.
     * @return True if restricted.
     */
    fun isRestricted(flag: Int): Boolean {
        return zones.any { it.zone.isRestricted(flag) }
    }

    /**
     * Handles death logic in zones.
     * @param killer The entity that killed this entity.
     * @return True if death was handled by any zone.
     */
    fun handleDeath(killer: Entity): Boolean {
        return zones.any { it.zone.death(entity, killer) }
    }

    /**
     * Checks if the entity can continue attacking a target.
     * @param target The target node.
     * @param style The combat style being used.
     * @param message Whether to send a failure message to the player.
     * @return True if attack can continue.
     */
    fun continueAttack(target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (target is Entity) {
            if (!entity.continueAttack(target, style, message)) return false
        }
        if (zones.any { !it.zone.continueAttack(entity, target, style, message) }) return false
        if (entity is Player && target is Player) {
            if (!entity.skullManager.isWilderness || !target.skullManager.isWilderness) {
                if (message) entity.packetDispatch.sendMessage("You can only attack other players in the wilderness.")
                return false
            }
        }
        if (target is Entity && !MapZone.checkMulti(entity, target, message)) return false
        return true
    }

    /**
     * Checks if the entity can interact with a target using a specific option.
     * @param target The target node.
     * @param option The interaction option.
     * @return True if handled by any zone.
     */
    fun interact(target: Node, option: Option): Boolean {
        return zones.any { it.zone.interact(entity, target, option) }
    }

    /**
     * Handles useWith interaction for items and nodes.
     * @param used The item being used.
     * @param with The node it is used with.
     * @return True if any zone handled it.
     */
    fun useWith(used: Item, with: Node): Boolean {
        return zones.any { it.zone.handleUseWith(entity.asPlayer(), used, with) }
    }

    /**
     * Handles interface button clicks in zones.
     * @param interfaceId The interface ID.
     * @param buttonId The button ID.
     * @param slot The slot ID.
     * @param itemId The item ID.
     * @param opcode The packet opcode.
     * @return True if any zone handled the button.
     */
    fun clickButton(interfaceId: Int, buttonId: Int, slot: Int, itemId: Int, opcode: Int): Boolean {
        return zones.any { it.zone.actionButton(entity as Player, interfaceId, buttonId, slot, itemId, opcode) }
    }

    /**
     * Checks if multi-combat boundaries should be ignored for a victim entity.
     * @param victim The entity to check against.
     * @return True if multiway restrictions are ignored.
     */
    fun isIgnoreMultiBoundaries(victim: Entity): Boolean {
        return zones.any { it.zone.ignoreMultiBoundaries(entity, victim) }
    }

    /**
     * Checks if the entity can teleport.
     * @param type Teleport type (0=spell, 1=item, 2=object, 3=npc, -1=force).
     * @param node Optional node involved in teleportation.
     * @return True if teleportation is allowed.
     */
    fun teleport(type: Int, node: Node): Boolean {
        if (type != -1 && entity.isTeleBlocked && !canTeleportByJewellery(type, node)) {
            if (entity.isPlayer()) entity.asPlayer().sendMessage("A magical force has stopped you from teleporting.")
            return false
        }
        return zones.all { it.zone.teleport(entity, type, node) }
    }

    /**
     * Checks if a player can teleport using jewellery in low-level wilderness.
     */
    private fun canTeleportByJewellery(type: Int, node: Node): Boolean {
        if (type != 1 || !WILDERNESS_LEVEL_30_TELEPORT_ITEMS.contains(node.asItem().id)) return false
        if (entity.timers.getTimer("teleblock") != null) return false
        if (entity.zoneMonitor.isRestricted(ZoneRestriction.TELEPORT)) return false
        if (entity.locks.isTeleportLocked()) {
            val p = entity.asPlayer()
            return p.skullManager.level in 1..30
        }
        return false
    }

    /**
     * Starts death sequence for an entity.
     * @param entity The dying entity.
     * @param killer The killer.
     * @return True if all zones allowed the death to start.
     */
    fun startDeath(entity: Entity, killer: Entity): Boolean {
        return zones.all { it.zone.startDeath(entity, killer) }
    }

    /**
     * Checks if the entity can trigger random events.
     * @return True if allowed in all zones.
     */
    fun canFireRandomEvent(): Boolean {
        return zones.all { it.zone.isFireRandoms() }
    }

    /**
     * Clears all zones the entity is in.
     * @return True if successfully left all zones.
     */
    fun clear(): Boolean {
        if (!zones.all { it.zone.leave(entity, true) }) return false
        musicZones.forEach { it.leave(entity, true) }
        zones.clear()
        musicZones.clear()
        return true
    }

    /**
     * Checks if entity can move from one location to another.
     * @param location Current location.
     * @param destination Destination location.
     * @return True if movement allowed.
     */
    fun move(location: Location, destination: Location): Boolean {
        return zones.all { it.zone.move(entity, location, destination) }
    }

    /**
     * Updates entity location and manages entering/exiting zones.
     * @param last Last known location.
     * @return True if location update succeeded in all zones.
     */
    fun updateLocation(last: Location): Boolean {
        if (entity is Player && !entity.isArtificial) checkMusicZones()
        entity.updateLocation(last)

        zones.removeIf { zone ->
            if (!zone.borders.insideBorder(entity)) {
                if (zone.zone.isDynamicZone()) return@removeIf false
                if (!zone.zone.leave(entity, false)) return@removeIf false
                return@removeIf true
            }
            false
        }

        for (zone in entity.viewport.region?.regionZones!!) {
            if (!zone.borders.insideBorder(entity)) continue
            if (zones.any { it.zone == zone.zone }) {
                zone.zone.locationUpdate(entity, last)
                continue
            }
            if (!zone.zone.enter(entity)) return false
            zones.add(zone)
            zone.zone.locationUpdate(entity, last)
        }

        return true
    }

    /**
     * Checks and updates music zones for a player.
     */
    fun checkMusicZones() {
        if (entity !is Player) return
        val player = entity
        val l = player.location

        musicZones.removeIf { zone ->
            if (!zone.borders.insideBorder(l.x, l.y)) {
                if (zone.leave(player, false)) return@removeIf true
            }
            false
        }

        val r = player.viewport.region
        if (r != null) {
            for (zone in r.musicZones) {
                if (zone.borders.insideBorder(l.x, l.y)) {
                    zone.enter(player)
                    return
                }
            }
        }

        val music = r?.music
        if (music == -1) {
            if (!player.musicPlayer.isPlaying) player.musicPlayer.playDefault()
        } else {
            if (music != null) player.musicPlayer.unlock(music, true)
        }
    }

    /**
     * Parses commands in all zones.
     * @param player The player executing the command.
     * @param name Command name.
     * @param arguments Command arguments.
     * @return True if any zone handled the command.
     */
    fun parseCommand(player: Player, name: String, arguments: Array<String>): Boolean {
        return zones.any { it.zone.parseCommand(player, name, arguments) }
    }

    /**
     * Checks if requests (like trade or duel) are allowed in current zones.
     * @param type Type of request.
     * @param target Target player.
     * @return True if allowed in all zones.
     */
    fun canRequest(type: RequestType, target: Player): Boolean {
        return zones.all { it.zone.canRequest(type, entity.asPlayer(), target) }
    }

    /**
     * Checks if the entity is in a zone by name.
     * @param name Zone name.
     * @return True if entity is in the zone.
     */
    fun isInZone(name: String): Boolean {
        val uid = name.hashCode()
        return zones.any { it.zone.getUid() == uid }
    }

    /**
     * Removes a zone from the entity's list of zones.
     * @param zone The zone to remove.
     */
    fun remove(zone: MapZone) {
        zones.removeIf { it.zone == zone }
    }

    /**
     * Returns a list of current region zones.
     */
    fun getZones(): List<RegionZone> = zones

    /**
     * Returns a list of current music zones.
     */
    fun getMusicZones(): List<MusicZone> = musicZones

    companion object {
        /**
         * Jewellery items that allow teleporting out from wilderness levels 1–30.
         */
        val WILDERNESS_LEVEL_30_TELEPORT_ITEMS: Set<Int> = java.util.Set.of(
            Items.AMULET_OF_GLORY_1704,
            Items.AMULET_OF_GLORY1_1706,
            Items.AMULET_OF_GLORY2_1708,
            Items.AMULET_OF_GLORY3_1710,
            Items.AMULET_OF_GLORYT_10362,
            Items.AMULET_OF_GLORYT1_10360,
            Items.AMULET_OF_GLORYT2_10358,
            Items.AMULET_OF_GLORYT3_10356,
            Items.SKILLS_NECKLACE_11113,
            Items.SKILLS_NECKLACE1_11111,
            Items.SKILLS_NECKLACE2_11109,
            Items.SKILLS_NECKLACE3_11107,
            Items.COMBAT_BRACELET_11126,
            Items.COMBAT_BRACELET1_11124,
            Items.COMBAT_BRACELET2_11122,
            Items.COMBAT_BRACELET3_11120,
            Items.PHARAOHS_SCEPTRE_9044,
            Items.PHARAOHS_SCEPTRE_9046,
            Items.PHARAOHS_SCEPTRE_9048
        )
    }
}
