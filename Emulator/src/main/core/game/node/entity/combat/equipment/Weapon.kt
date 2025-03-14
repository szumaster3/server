package core.game.node.entity.combat.equipment

import core.game.node.item.Item

class Weapon
    @JvmOverloads
    constructor(
        val item: Item?,
        val ammunitionSlot: Int = -1,
        val ammunition: Item? = null,
        type: WeaponType? = WeaponType.DEFAULT,
    ) {
        val id: Int = item?.id ?: -1
        val name: String = if (item == null) "null" else item.name
        var type: WeaponType? = null

        enum class WeaponType {
            DEFAULT,
            CROSSBOW,
            DOUBLE_SHOT,
            THROWN,
            DEGRADING,
            STAFF,
            CHINCHOMPA,
        }

        override fun toString(): String {
            return "Weapon(id=$id, name=$name, type=$type, ammunitionSlot=$ammunitionSlot, ammunition=${ammunition?.name ?: "none"})"
        }
    }
