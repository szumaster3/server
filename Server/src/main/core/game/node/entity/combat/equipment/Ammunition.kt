package core.game.node.entity.combat.equipment

import core.game.node.entity.impl.Projectile
import core.game.world.update.flag.context.Graphics

/**
 * Represents range ammunition types.
 *
 * @author Emperor
 */
class Ammunition(
    val itemId: Int,
    val startGraphics: Graphics?,
    val darkBowGraphics: Graphics?,
    val projectile: Projectile?,
    val poisonDamage: Int,
    val projectileStartHeight: Int = 0,
    val projectileType: Int = 0,
    val projectileAngle: Int = 0,
    val projectileBaseSpeed: Int = 0,
    val projectileId: Int = 0,
    var effect: BoltEffect? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ammunition

        if (itemId != other.itemId) return false
        if (startGraphics != other.startGraphics) return false
        if (darkBowGraphics != other.darkBowGraphics) return false
        if (projectile != other.projectile) return false
        if (poisonDamage != other.poisonDamage) return false
        if (projectileStartHeight != other.projectileStartHeight) return false
        if (projectileType != other.projectileType) return false
        if (projectileAngle != other.projectileAngle) return false
        if (projectileBaseSpeed != other.projectileBaseSpeed) return false
        if (projectileId != other.projectileId) return false
        if (effect != other.effect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId
        result = 31 * result + (startGraphics?.hashCode() ?: 0)
        result = 31 * result + (darkBowGraphics?.hashCode() ?: 0)
        result = 31 * result + (projectile?.hashCode() ?: 0)
        result = 31 * result + poisonDamage
        result = 31 * result + projectileStartHeight
        result = 31 * result + projectileType
        result = 31 * result + projectileAngle
        result = 31 * result + projectileBaseSpeed
        result = 31 * result + projectileId
        result = 31 * result + (effect?.hashCode() ?: 0)
        return result
    }

    companion object {
        private val AMMUNITION = mutableMapOf<Int, Ammunition>()

        /**
         * Gets the ammunition instance for a given item id.
         *
         * @param id The item id.
         * @return The Ammunition instance or null if not found.
         */
        @JvmStatic
        fun get(id: Int): Ammunition? = AMMUNITION[id]

        /**
         * Stores the ammunition instance by its item id.
         *
         * @param id The item id.
         * @param ammo The ammunition instance.
         */
        fun putAmmunition(id: Int, ammo: Ammunition) {
            AMMUNITION[id] = ammo
        }

        /**
         * Gets all ammunition as a collection.
         */
        @JvmStatic
        fun getAll(): Collection<Ammunition> = AMMUNITION.values
    }

    /**
     * Gets a string data of the ammunition.
     *
     * @return the string data
     */
    override fun toString(): String {
        val projectileConfiguration = projectile?.toString() ?: "null"
        return "Ammunition(itemId=$itemId, startGraphics=$startGraphics, darkBowGraphics=$darkBowGraphics, projectile=$projectileConfiguration, poisonDamage=$poisonDamage, projectileId=$projectileId, projectileStartHeight=$projectileStartHeight, projectileType=$projectileType, projectileAngle=$projectileAngle, projectileBaseSpeed=$projectileBaseSpeed, effect=$effect)"
    }
}
