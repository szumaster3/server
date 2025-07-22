package core.game.node.entity.combat.equipment

import core.game.node.item.Item

/**
 * Represents a weapon used in combat.
 *
 * @property item The weapon item.
 * @property ammunitionSlot The slot in which ammunition is stored (-1 if not applicable).
 * @property ammunition The ammunition item used with the weapon, if applicable.
 * @property type The type of weapon.
 *
 * @author Emperor
 */
class Weapon
@JvmOverloads
constructor(
    val item: Item?,
    val ammunitionSlot: Int = -1,
    val ammunition: Item? = null,
    type: WeaponType? = WeaponType.DEFAULT,
) {
    /**
     * The ID of the weapon item, or -1 if no item is assigned.
     */
    val id: Int = item?.id ?: -1

    /**
     * The name of the weapon, or `"null"` if no item is assigned.
     */
    val name: String = item?.name ?: "null"

    /**
     * The type of weapon.
     */
    var type: WeaponType? = null

    /**
     * Represents different types of weapons in the game.
     */
    enum class WeaponType {
        /**
         * A standard weapon with no special properties.
         */
        DEFAULT,

        /**
         * A crossbow weapon that requires bolts as ammunition.
         */
        CROSSBOW,

        /**
         * A weapon capable of firing two shots in a single attack.
         */
        DOUBLE_SHOT,

        /**
         * A throwable weapon such as knives or javelins.
         */
        THROWN,

        /**
         * A degrading weapon that loses durability over time.
         */
        DEGRADING,

        /**
         * A magical staff used for casting spells.
         */
        STAFF,

        /**
         * A chinchompa, an explosive ranged weapon.
         */
        CHINCHOMPA,
    }

    /**
     * Returns a string representation of the weapon.
     *
     * @return A string containing the weapon's ID, name, type, ammunition slot, and ammunition.
     */
    override fun toString(): String =
        "Weapon(id=$id, name=$name, type=$type, ammunitionSlot=$ammunitionSlot, ammunition=${ammunition?.name ?: "none"})"
}
