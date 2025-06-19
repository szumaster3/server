package core.api

import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

/**
 * Interface that allows a class to define a map area.
 * Optionally-overridable methods include [getRestrictions], [areaEnter], [areaLeave] and [entityStep]
 */
interface MapArea : ContentInterface {
    var zone: MapZone
        get() =
            zoneMaps[this::class.java.simpleName + "MapArea"]
                ?: throw IllegalStateException("Zone not initialized.")
        set(value) {
            zoneMaps[this::class.java.simpleName + "MapArea"] = value
        }

    /**
     * Defines the borders of this map area.
     *
     * @return An array of [ZoneBorders] representing the area's boundaries.
     */
    fun defineAreaBorders(): Array<ZoneBorders>

    /**
     * Returns restrictions applied within this area.
     *
     * @return An array of [ZoneRestriction]; empty by default.
     */
    fun getRestrictions(): Array<ZoneRestriction> = emptyArray()

    /**
     * Called when an entity enters the area.
     *
     * @param entity The entity that entered.
     */
    fun areaEnter(entity: Entity) {}

    /**
     * Called when an entity leaves the area.
     *
     * @param entity The entity that left.
     * @param logout True if the entity left due to logout.
     */
    fun areaLeave(entity: Entity, logout: Boolean) {}

    /**
     * Called when an entity steps within the area.
     *
     * @param entity The entity that moved.
     * @param location The new location.
     * @param lastLocation The previous location.
     */
    fun entityStep(entity: Entity, location: Location, lastLocation: Location) {}

    /**
     * Called when an entity interacts with a target in the area.
     *
     * @param entity The acting entity.
     * @param node The target node.
     * @param option The selected interaction option.
     * @return True if the interaction was handled.
     */
    fun onInteraction(entity: Entity, node: Node, option: Option): Boolean = false

    /**
     * Called when a player uses an item with another node in the area.
     *
     * @param player The player performing the action.
     * @param used The used item.
     * @param with The target node.
     * @return True if the action was handled.
     */
    fun onUseWith(player: Player?, used: Item?, with: Node?): Boolean = false

    companion object {
        /**
         * A map storing zone data for all map areas.
         */
        val zoneMaps = mutableMapOf<String, MapZone>()
    }
}