package core.game.node.entity.impl

import core.game.container.Container
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatPulse
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.ArmourSet
import core.game.node.entity.combat.equipment.WeaponInterface.AttackStyle
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.config.ItemConfigParser
import core.game.system.config.NPCConfigParser
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations

/**
 * A class that holds various properties related to an entity.
 *
 * @author Emperor
 */
class Properties(val entity: Entity) {

    var combatPulse: CombatPulse = CombatPulse(entity)

    /**
     * Checks if the entity is retaliating.
     *
     * @return `true` if the entity is retaliating, `false` otherwise.
     */
    var isRetaliating: Boolean = true

    /**
     * Checks if the entity is currently teleporting.
     *
     * @return `true` if the entity is teleporting, `false` otherwise.
     */
    var isTeleporting: Boolean = false

    /**
     * Gets the combat level of the entity.
     *
     * @return The combat level of the entity.
     */
    @get:Deprecated("Use {@link #getCurrentCombatLevel()} instead.")
    var combatLevel: Int = 0

    /**
     * Gets the attack style used by the entity in combat.
     *
     * @return The attack style used by the entity.
     */
    var attackStyle: AttackStyle? = null

    /**
     * Gets the teleportation location for the entity.
     *
     * @return The location to which the entity teleports.
     */
    var teleportLocation: Location? = null

    /**
     * Gets the spawn location of the entity.
     *
     * @return The spawn location of the entity.
     */
    var spawnLocation: Location? = null

    /**
     * Gets the array of bonuses for the entity.
     *
     * @return The array of bonuses for the entity.
     */
    var bonuses: IntArray = IntArray(15)

    /**
     * Gets the attack speed for the entity.
     *
     * @return The attack speed of the entity.
     */
    var attackSpeed: Int = 4

    /**
     * Gets the timestamp of the last animation end for the entity.
     *
     * @return The timestamp of the last animation end.
     */
    val lastAnimationEnd: Long = 0

    /**
     * Gets the attack animation for the entity.
     *
     * @return The attack animation for the entity.
     */
    var attackAnimation: Animation = Animation(422, Animator.Priority.HIGH)

    /**
     * Gets the defence animation for the entity.
     *
     * @return The defence animation for the entity.
     */
    var defenceAnimation: Animation = Animation(404)

    /**
     * Gets the death animation for the entity.
     *
     * @return The death animation of the entity.
     */
    var deathAnimation: Animation = Animation(9055, Animator.Priority.HIGH)

    /**
     * The Death gfx.
     */
    var deathGfx: Graphics = Graphics(-1)

    /**
     * Gets the range animation for the entity.
     *
     * @return The range animation of the entity.
     */
    var rangeAnimation: Animation? = null

    /**
     * Gets the magic animation for the entity.
     *
     * @return The magic animation of the entity.
     */
    var magicAnimation: Animation? = null

    /**
     * Gets the combat spell currently set for the entity.
     *
     * @return The combat spell of the entity.
     */
    var spell: CombatSpell? = null
        /**
         * Sets the combat spell for the entity.
         *
         * @param spell The combat spell to set.
         */
        set(spell) {
            field = spell
            if (spell != null) {
                combatPulse.updateStyle()
            }
        }

    /**
     * Gets the autocast spell for the entity.
     *
     * @return The autocast spell of the entity.
     */
    var autocastSpell: CombatSpell? = null

    /**
     * Gets the armour set equipped by the entity.
     *
     * @return The armour set of the entity.
     */
    var armourSet: ArmourSet? = null

    /**
     * Checks if the entity is in a multi-zone.
     *
     * @return `true` if the entity is in a multi-zone, `false` otherwise.
     */
    var isMultiZone: Boolean = false

    /**
     * Checks if the entity is in a safe zone.
     *
     * @return `true` if the entity is in a safe zone, `false` otherwise.
     */
    var isSafeZone: Boolean = false

    /**
     * The Safe respawn.
     */
    var safeRespawn: Location? = null

    /**
     * Gets the combat timeout for the entity.
     *
     * @return The combat timeout for the entity.
     */
    var combatTimeOut: Int = 10

    /**
     * Checks if the entity is walkable as an NPC.
     *
     * @return `true` if the entity is walkable as an NPC, `false` otherwise.
     */
    var isNPCWalkable: Boolean = false

    /**
     * Gets the protection combat style for the entity.
     *
     * @return The protection style of the entity.
     */
    var protectStyle: CombatStyle? = null

    /**
     * Updates the defence animation based on the entity's equipment.
     */
    fun updateDefenceAnimation() {
        if (entity is NPC) {
            val animation = entity.definition.getConfiguration<Animation>(NPCConfigParser.DEFENCE_ANIMATION)
            if (animation != null) {
                defenceAnimation = animation
            }
            return
        }
        val c: Container = (entity as Player).equipment
        var item = c[EquipmentContainer.SLOT_SHIELD]
        if (item != null) {
            defenceAnimation = item.definition.getConfiguration<Animation>(
                ItemConfigParser.DEFENCE_ANIMATION, Animation.create(Animations.DEFEND_1156)
            )
        } else if ((c[EquipmentContainer.SLOT_WEAPON].also { item = it }) != null) {
            defenceAnimation = item!!.definition.getConfiguration<Animation>(
                ItemConfigParser.DEFENCE_ANIMATION, Animation.create(Animations.DEFEND_UNARMED_424)
            )
        } else {
            defenceAnimation = Animation.create(Animations.CLAW_DEFENCE_397)
        }
    }

    /**
     * Gets the combat animation for a given index.
     *
     * @param index The index of the animation to get (0 for attack, 1 for magic, 2 for range, 3 for defence, or 4 for death).
     * @return The corresponding animation.
     */
    fun getCombatAnimation(index: Int): Animation {
        return if (index == 0) attackAnimation else (if (index == 1) magicAnimation else if (index == 2) rangeAnimation else if (index == 3) defenceAnimation else deathAnimation)!!
    }

    val currentCombatLevel: Int
        /**
         * Gets the current combat level of the entity.
         *
         * @return The current combat level of the entity.
         */
        get() {
            if (entity is Player) {
                val player = entity
                return if ((settings!!.isPvp || (settings!!.wild_pvp_enabled && player.skullManager.isWilderness)) && !player.familiarManager.isUsingSummoning) {
                    combatLevel
                } else {
                    combatLevel + player.familiarManager.summoningCombatLevel
                }
            }
            return combatLevel
        }
}
