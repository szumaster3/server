package core.game.node.item

import core.cache.def.impl.ItemDefinition
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Plugin

abstract class ItemPlugin : Plugin<Any> {
    companion object {
        const val DROP = 1
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any?,
    ): Any {
        return this
    }

    fun register(vararg ids: Int) {
        for (id in ids) {
            ItemDefinition.forId(id).itemPlugin = this
        }
    }

    open fun remove(
        player: Player,
        item: Item,
        type: Int,
    ) {
    }

    open fun canPickUp(
        player: Player,
        item: GroundItem,
        type: Int,
    ): Boolean {
        return true
    }

    open fun createDrop(
        item: Item,
        player: Player,
        npc: NPC?,
        location: Location,
    ): Boolean {
        return true
    }

    open fun getItem(
        item: Item,
        npc: NPC?,
    ): Item {
        return item
    }

    open fun getDeathItem(item: Item): Item {
        return item
    }
}
