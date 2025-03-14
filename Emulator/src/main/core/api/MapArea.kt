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

    fun defineAreaBorders(): Array<ZoneBorders>

    fun getRestrictions(): Array<ZoneRestriction> = emptyArray()

    fun areaEnter(entity: Entity) {}

    fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {}

    fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {}

    fun interactBehavior(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean = false

    fun useWithBehavior(
        player: Player?,
        used: Item?,
        with: Node?,
    ): Boolean = false

    companion object {
        val zoneMaps = mutableMapOf<String, MapZone>()
    }
}
