package custom.guild.warehouse.slot

import core.game.node.item.Item

/**
 * Single guild warehouse slot.
 */
data class WarehouseSlot(
    var item: Item? = null,
    var permission: WarehouseSlotPermission = WarehouseSlotPermission.VETERAN,
    var locked: Boolean = false
){}
