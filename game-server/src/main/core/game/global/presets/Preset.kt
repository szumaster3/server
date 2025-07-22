package core.game.global.presets

import core.game.node.item.Item

class Preset(
    equipment: ArrayList<Item>,
    inventory: ArrayList<Item>,
) {
    var equipment: ArrayList<Item>? = null
    var inventory: ArrayList<Item>? = null

    init {
        this.equipment = equipment
        this.inventory = inventory
    }
}
