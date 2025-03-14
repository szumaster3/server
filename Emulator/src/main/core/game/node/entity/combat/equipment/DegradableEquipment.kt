package core.game.node.entity.combat.equipment

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Plugin

abstract class DegradableEquipment(
    val slot: Int,
    vararg val itemIds: Int,
) : Plugin<Any?> {
    abstract fun degrade(
        player: Player?,
        entity: Entity?,
        item: Item?,
    )

    abstract fun getDropItem(itemId: Int): Int

    override fun newInstance(arg: Any?): DegradableEquipment {
        var equipment = EQUIPMENT[slot]
        if (equipment == null) {
            EQUIPMENT[slot] = ArrayList(20)
            equipment = EQUIPMENT[slot]
        }
        equipment!!.add(this)
        return this
    }

    override fun fireEvent(
        key: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    companion object {
        @JvmStatic private val EQUIPMENT: Array<ArrayList<DegradableEquipment>?> =
            arrayOfNulls(14)

        @JvmStatic fun degrade(
            player: Player?,
            entity: Entity?,
            attack: Boolean,
        ) {
            // TODO
        }

        @JvmStatic fun checkDegrade(
            player: Player?,
            entity: Entity?,
            slot: Int,
        ) {
            // TODO
        }

        @JvmStatic fun getDropReplacement(itemId: Int): Int {
            for (i in EQUIPMENT.indices) {
                if (EQUIPMENT[i] == null) continue
                for (e in EQUIPMENT[i]!!) {
                    for (id in e.itemIds) {
                        if (id == itemId) {
                            return e.getDropItem(id)
                        }
                    }
                }
            }
            return itemId
        }
    }
}
