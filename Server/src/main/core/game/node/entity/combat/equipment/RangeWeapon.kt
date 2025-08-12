package core.game.node.entity.combat.equipment

import core.game.world.update.flag.context.Animation

/**
 * A class holding all the range weapon definitions.
 *
 * @author Emperor
 */
class RangeWeapon(
    val itemId: Int,
    val animation: Animation,
    val attackSpeed: Int,
    val ammunitionSlot: Int,
    val type: Int,
    val dropAmmo: Boolean,
    val ammunition: List<Int>
) {

    companion object {
        private val RANGE_WEAPONS = mutableMapOf<Int, RangeWeapon>()

        /**
         * Gets a ranged weapon by its item id.
         * @param id The item id.
         * @return The [RangeWeapon] instance or null if not found.
         */
        @JvmStatic
        fun get(id: Int): RangeWeapon? = RANGE_WEAPONS[id]

        /**
         * Adds a ranged weapon to the collection if absent.
         * @param weapon The [RangeWeapon] to add.
         */
        fun putRangeWeapon(weapon: RangeWeapon) {
            RANGE_WEAPONS.putIfAbsent(weapon.itemId, weapon)
        }

        /**
         * Gets all ranged weapons as a collection.
         */
        fun getAll(): Collection<RangeWeapon> = RANGE_WEAPONS.values
    }

    /**
     * The weapon type enum value linked to [type].
     */
    val weaponType: Weapon.WeaponType
        get() = Weapon.WeaponType.values()[type]

    /**
     * Gets a string data of the range weapon.
     *
     * @return the string data
     */
    override fun toString(): String {
        return "RangeWeapon(itemId=$itemId, animationId=${animation.id}, attackSpeed=$attackSpeed, ammunitionSlot=$ammunitionSlot, type=$type, dropAmmo=$dropAmmo, ammunition=$ammunition)"
    }
}
