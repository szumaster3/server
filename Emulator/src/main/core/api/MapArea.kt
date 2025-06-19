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

    companion object {
        /**
         * A map storing zone data for all map areas.
         */
        val zoneMaps = mutableMapOf<String, MapZone>()
    }
}